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
package launcher.server;

import launcher.server.processor.AntiCheatVerifier;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Hyper
 */
public class AntiCheatVerifierController {

    public static List<AntiCheatVerifier> antiCheat = new ArrayList<AntiCheatVerifier>();

    /**
     * Get For Player's Anti Cheat Status
     *
     * @param IP Server IP
     * @param Port Server PORT
     * @param Name Player Name
     * @param JoinerID Player Joiner ID
     * @return
     */
    public static AntiCheatVerifier getPlayerByServerIPPORTName(String IP, String Port, String Name, String JoinerID) {
        for (AntiCheatVerifier antiCheatVerifier : antiCheat) {
            if (antiCheatVerifier.getServerIPPort().equals(IP + ":" + Port) && antiCheatVerifier.getPlayerName().equals(Name) && antiCheatVerifier.getJoinerID().equals(JoinerID)) {
                return antiCheatVerifier;
            }
        }
        return null;
    }

    /**
     * Add Player To Anti Cheat Verification List
     *
     * @param ServerIP Server IP
     * @param ServerPORT Server Port
     * @param PlayerName Player Name
     * @param JoinerID Player JoinerID
     * @param Verified Verification Status
     */
    public static void addPlayerAntiCheatVerifier(String ServerIP, String ServerPORT, String PlayerName, String JoinerID, boolean Verified) {
        antiCheat.add(new AntiCheatVerifier(PlayerName, ServerIP, ServerPORT, JoinerID, Verified));
    }

    /**
     * Set Verified On Player's Anti Cheat Status
     *
     * @param PlayerName Player Name
     * @param ServerIP Server IP
     * @param ServerPORT Server Port
     * @param JoinerID Player Joiner ID
     */
    public static void setPlayerAntiCheatVerified(String PlayerName, String ServerIP, String ServerPORT, String JoinerID) {
        for (AntiCheatVerifier antiCheatVerifier : antiCheat) {
            if (antiCheatVerifier.getPlayerName().equals(PlayerName) && antiCheatVerifier.getJoinerID().equals(JoinerID) && antiCheatVerifier.getServerIPPort().equals(ServerIP + ":" + ServerPORT)) {
                antiCheatVerifier.setVerified(true);
                System.out.println("Anti cheat verified for " + PlayerName);
                break;
            }
        }
    }

    /**
     * Check If Player Is Anti-Cheat Verified Or Not
     *
     * @param ServerIP Server IP
     * @param ServerPort Server Port
     * @param PlayerName Player Name
     * @param JoinerID Player JoinerID
     * @return Returns Result
     */
    public static boolean isPlayerVerified(String ServerIP, String ServerPort, String PlayerName, String JoinerID) {
        for (AntiCheatVerifier antiCheatVerifier : antiCheat) {
            if (antiCheatVerifier.getPlayerName().equals(PlayerName) && antiCheatVerifier.getServerIPPort().equals(ServerIP + ":" + ServerPort) && antiCheatVerifier.getJoinerID().equals(JoinerID)) {
                if (antiCheatVerifier.isVerified()) {
                    return true;
                }
                break;
            }
        }
        return false;
    }
}
