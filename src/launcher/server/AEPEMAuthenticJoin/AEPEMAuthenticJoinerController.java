/*
IGI-2 Multiplayer Launcher Master Server

Copyright (c) 2018, Aayush Atharva

Permission is hereby granted, free of charge to any person obtaining a copy of this software and associated documentation files 
(the "Software"), to deal in the Software with restriction. A person can use, copy the Software without restriction. But if a person modify
the software, the person must push the code to the Software GitHub repository. If a person wants to publish or distribute the software,
the person must put this "Created By: Aayush Atharva" on About Section of the Software. And If a person want's to sell the software,
the person get permission from the owner of this Software. 


IGI-2 Multiplayer Launcher Master Server
Owner: Aayush Atharva
Email: aayush@igi2.co.in
*/

package launcher.server.AEPEMAuthenticJoin;

import java.util.ArrayList;
import java.util.List;
import static launcher.server.DataProcessor.echo_console;

/**
 *
 * @author Hyper
 */
public class AEPEMAuthenticJoinerController {

    public static List<PasswordStore> passwordStore = new ArrayList<PasswordStore>();

    /**
     * Add New Server To AEPEM Server List
     * @param IP Server IP
     * @param PORT Server PORT
     * @param Password Server Password
     * @param ServerID Server ID
     */
    public static void addNewServer(String IP, String PORT, String Password, String ServerID) {
        PasswordStore passwdStore = getServerByAddress(IP, PORT);
        if (passwdStore != null) {
            passwordStore.remove(passwdStore);
            passwordStore.add(new PasswordStore(IP, PORT, Password, false, ServerID));
            echo_console("Server " + IP + ":" + PORT + " Updated with Password: " + Password, 5);
        } else {
            passwordStore.add(new PasswordStore(IP, PORT, Password, false, ServerID));
            echo_console(" New Server, Server IP: " + IP + ":" + PORT + ", Password: " + Password + " [SUCCESS]", 5);
        }
    }

    public static PasswordStore getServerByAddress(String IP, String PORT) {
        for (PasswordStore Data : passwordStore) {
            if (Data.getServerIPPORT().equals(IP + ":" + PORT)) {
                return Data;
            }
        }
        return null;
    }

    public static PasswordStore getServerByAddressAndServerID(String IP, String PORT, String ServerID) {
        for (PasswordStore Data : passwordStore) {
            if (Data.getServerIPPORT().equals(IP + ":" + PORT) && Data.getServerID().equals(ServerID)) {
                return Data;
            }
        }
        return null;
    }

    public static boolean setNewPassword(String IP, String PORT, String Password) {
        for (PasswordStore Data : passwordStore) {
            if (Data.getServerIPPORT().equals(IP + ":" + PORT)) {
                if (Data.isTakePassword()) {
                    Data.setServerPASSWORD(Password);
                    Data.setTakePassword(false);
                    echo_console("[" + IP + ":" + PORT + "] Setting New Password : " + Password, 3);
                    return true;
                }
                break;
            }
        }
        return false;
    }

}
