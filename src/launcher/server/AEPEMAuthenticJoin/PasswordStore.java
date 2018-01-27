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
package launcher.server.AEPEMAuthenticJoin;

/**
 *
 * @author Hyper
 */
public class PasswordStore {

    private String ServerIP;
    private String ServerPORT;
    private String ServerPASSWORD;
    private boolean TakePassword;
    private String ServerID;

    public PasswordStore(String ServerIP, String ServerPORT, String ServerPASSWORD, boolean TakePassword, String ServerID) {
        this.ServerIP = ServerIP;
        this.ServerPORT = ServerPORT;
        this.ServerPASSWORD = ServerPASSWORD;
        this.TakePassword = TakePassword;
        this.ServerID = ServerID;
    }

    public String getServerID() {
        return ServerID;
    }

    public boolean isTakePassword() {
        return TakePassword;
    }

    public void setTakePassword(boolean TakePassword) {
        this.TakePassword = TakePassword;
    }

    public String getServerIPPORT() {
        return ServerIP + ":" + ServerPORT;
    }

    public String getServerIP() {
        return ServerIP;
    }

    public String getServerPORT() {
        return ServerPORT;
    }

    public String getServerPASSWORD() {
        return ServerPASSWORD;
    }

    public void setServerPASSWORD(String ServerPASSWORD) {
        this.ServerPASSWORD = ServerPASSWORD;
    }

}
