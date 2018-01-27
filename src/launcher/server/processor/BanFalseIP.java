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

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Hyper
 */
public class BanFalseIP extends Thread {

    private String IP;
    private String Data;

    public BanFalseIP(String IP, String Data) {
        this.IP = IP;
        this.Data = Data;
    }

    @Override
    public void run() {

        try {
            
            Runtime BlockIP = Runtime.getRuntime();
            BlockIP.exec("sudo iptables -t mangle -A Banner -s " + getIP() + " -j DROP");
            
            BufferedWriter bw = null;
            FileWriter fw = null;
            
            try {
                
                fw = new FileWriter("/home/admin/Launcher/Master_Server/Master_Server_Log/Banned_IP.log");
                bw = new BufferedWriter(fw, 51200);
                bw.write(getData() + getIP());
                
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                
                try {
                    
                    if (bw != null) {
                        bw.close();
                    }
                    
                    if (fw != null) {
                        fw.close();
                    }
                    
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            
        } catch (IOException ex) {
            Logger.getLogger(BanFalseIP.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public String getIP() {
        return IP;
    }

    public String getData() {
        return Data;
    }

}
