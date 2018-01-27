/*
 * IGI-2 Multiplayer Launcher
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
 * IGI-2 Multiplayer Launcher
 * Owner: Aayush Atharva
 * Email: aayush@igi2.co.in
 */
package launcher.server.processor;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;

public class MultiThreadedServer implements Runnable {

    protected int serverPort;
    protected ServerSocket serverSocket = null;
    protected boolean isStopped = false;
    protected Thread runningThread = null;

    public MultiThreadedServer(int port) {
        this.serverPort = port;
    }

    @Override
    public void run() {
        
        synchronized (this) {
            this.runningThread = Thread.currentThread();
        }
        
        openServerSocket(); // Start Server On Port
    
        // Accepter
        while (!isStopped()) {
            Socket clientSocket = null;
            
            try {
                clientSocket = this.serverSocket.accept();
            } catch (IOException e) {
                
                // Check If Server Is Stopped
                if (isStopped()) {
                    System.out.println("Server Stopped.");
                    return;
                }

                throw new RuntimeException("Error accepting client connection", e); // Throw Exception
            }

            new Thread(new WorkerRunnable(clientSocket)).start(); // Start Client Handler
        }
        
        System.out.println("Server Stopped."); // Server Stopped Message
        
    }

    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    public synchronized void stop() {
        this.isStopped = true;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error Closing Server", e);
        }
    }

    private void openServerSocket() {
        try {
            this.serverSocket = new ServerSocket(this.serverPort);
        } catch (IOException e) {
            throw new RuntimeException("Cannot Start Server At Port: " + this.serverPort, e);
        }
    }
}
