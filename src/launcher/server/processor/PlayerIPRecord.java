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

/**
 *
 * @author Hyper
 */
public class PlayerIPRecord {

    private String PlayerName;
    private String PlayerIP;
    private String ServerIP;
    private String ServerPort;

    public PlayerIPRecord(String PlayerName, String PlayerIP, String ServerIP, String ServerPort) {
        this.PlayerName = PlayerName;
        this.PlayerIP = PlayerIP;
        this.ServerIP = ServerIP;
        this.ServerPort = ServerPort;
    }
    
    public String getServerIPPort(){
        return ServerIP + ":" + ServerPort;
    }

    public String getPlayerName() {
        return PlayerName;
    }

    public String getPlayerIP() {
        return PlayerIP;
    }

    public String getServerIP() {
        return ServerIP;
    }

    public String getServerPort() {
        return ServerPort;
    }

}
