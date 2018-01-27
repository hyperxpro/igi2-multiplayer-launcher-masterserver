/*
 * IGI-2 Multiplayer Launcher Master Server
 *
 * Copyright (c) 2018, Aayush Atharva
 *
 * Permission is hereby granted, free of charge to any person obtaining a copy of this software and associated documentation files 
 * (the "Software"), to deal in the Software with restriction. A person can use, copy the Software without restriction. But if a person 
 * modify the software, the person must push the code to the Software GitHub repository. If a person wants to publish or distribute the 
 * software, the person must put this "Created By: Aayush Atharva" on About Section of the Software And this License must be present with 
 * every file of the Software. And If a person wants to sell the software, the person get permission from the owner of this Software.
 *
 *
 *
 *
 * IGI-2 Multiplayer Launcher Master Server
 * Owner: Aayush Atharva
 * Email: aayush@igi2.co.in
 */
package launcher.server.processor;

import aayush.atharva.TurboCryptography.Decryption;
import aayush.atharva.TurboCryptography.Encryption;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import launcher.server.DataProcessor;

public class WorkerRunnable implements Runnable {

    protected Socket clientSocket = null;

    public WorkerRunnable(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
//        System.out.println("Client Address: " + clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort());

        InputStream input = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        OutputStream output = null;

        try {

            input = clientSocket.getInputStream();
            isr = new InputStreamReader(input);
            br = new BufferedReader(isr);
            String Data = null;

            try {
                Data = new Decryption(AESSecretKey, CBCA, CBCB, KeyAES, Key, IV, br.readLine()).Decrypt(); // Decrypt Data And Load To String
            } catch (Exception ex) {

                if (input != null) {
                    input.close();
                }

                if (isr != null) {
                    isr.close();
                }

                if (br != null) {
                    br.close();
                }

//                new BanFalseIP(clientSocket.getInetAddress().getHostAddress(), "Block IP For Wrong Packet, IP: ").start(); // Block IP
            }

            String Response = null;
            if (Data != null) {
                try {
                    Response = new Encryption(AESSecretKey, CBCA, CBCB, KeyAES, Key, IV, new DataProcessor(Data, clientSocket).Process()).Encrypt();
                } catch (Exception ex) {
                    Logger.getLogger(WorkerRunnable.class.getName()).log(Level.SEVERE, null, ex);
                }

                output = clientSocket.getOutputStream();
                output.write(Response.getBytes());
                output.close();
            } else {
                if (output != null) {
                    try {
                        output.close();
                    } catch (IOException ex) {
                        Logger.getLogger(WorkerRunnable.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException ex) {
                        Logger.getLogger(WorkerRunnable.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

        } catch (SocketException e) {
            //  System.err.println("Client Disconnected, " + clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort());
        } catch (IOException e) {
            Logger.getLogger(WorkerRunnable.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException ex) {
                    Logger.getLogger(WorkerRunnable.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException ex) {
                    Logger.getLogger(WorkerRunnable.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            if (br != null) {
                try {
                    br.close();
                } catch (IOException ex) {
                    Logger.getLogger(WorkerRunnable.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            if (output != null) {
                try {
                    output.close();
                } catch (IOException ex) {
                    Logger.getLogger(WorkerRunnable.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private static final String AESSecretKey = ""; // AES KEY
    private static final String CBCA = ""; // 16 Bit
    private static final String CBCB = ""; // 16 Bit
    private static final String KeyAES = ""; // 128 Bit
    private static final String Key = ""; // 16 Bit Key
    private static final String IV = ""; // 16 Bit IV
}
