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



package launcher.server;

import launcher.server.processor.QueueJoin;
import launcher.server.processor.ServerQueue;
import launcher.server.processor.AntiCheatVerifier;
import java.util.Queue;
import java.util.LinkedList;
import launcher.server.AEPEMAuthenticJoin.AEPEMAuthenticJoinerController;
import launcher.server.AEPEMAuthenticJoin.PasswordStore;
import static launcher.server.AntiCheatVerifierController.addPlayerAntiCheatVerifier;
import static launcher.server.AntiCheatVerifierController.antiCheat;
import static launcher.server.DataProcessor.echo_console;
import static launcher.server.PlayerIPRecordController.addPlayerToIPRecord;

/**
 *
 * @author Hyper
 */
public class ServerJoinController {

    public static Queue<QueueJoin> PlayerQueueList = new LinkedList<QueueJoin>();
    public static Queue<ServerQueue> ServerQueueList = new LinkedList<ServerQueue>();

    /**
     * Add New To Queue On Specific Server
     *
     * @param PlayerName = Player Name To Add In Queue
     * @param JoinerID = Player Joiner ID
     * @param PlayerIP = Player IP Address
     * @param ServerIP = Server IP Address
     * @param ServerPORT = Server Port
     */
    public static void addPlayer(String PlayerName, String JoinerID, String PlayerIP, String ServerIP, String ServerPORT) {

        String ServerQueueNumber = getServerQueueNumber(ServerIP, ServerPORT);

        if (ServerQueueNumber == null) {
            ServerQueueList.add(new ServerQueue(ServerIP, ServerPORT, 0));
            ServerQueueNumber = "0";
        }

        echo_console("Current Load At Server: " + ServerIP + ":" + ServerPORT + " is " + ServerQueueNumber + " Players", 0);

        PlayerQueueList.add(new QueueJoin(PlayerName, PlayerIP, JoinerID, ServerIP, ServerPORT, 2, Integer.parseInt(ServerQueueNumber)));

        addServerQueue(ServerIP, ServerPORT);
        addPlayerToIPRecord(PlayerIP, ServerIP, ServerPORT, PlayerName);
        addPlayerAntiCheatVerifier(ServerIP, ServerPORT, PlayerName, JoinerID, false);

    }

    /**
     * Add New Server To Database
     *
     * @param ServerIP = Add Server IP To Add To Database
     * @param ServerPORT = Add Server PORT To Add To Database
     */
    public static void addServerQueue(String ServerIP, String ServerPORT) {
        for (ServerQueue Data : ServerQueueList) {
            if (Data.getServerIPPORT().equals(ServerIP + ":" + ServerPORT)) {
                Data.addQueueNumber(1);
                break;
            }
        }
    }

    /**
     * Get Current Number Of Players In Queue For Specific Server
     *
     * @param IP = Server IP
     * @param PORT = Server Port
     * @return
     */
    public static String getServerQueueNumber(String IP, String PORT) {
        for (ServerQueue Data : ServerQueueList) {
            if (Data.getServerIPPORT().equals(IP + ":" + PORT)) {
                return Data.getQueueNumber();
            }
        }
        return null;
    }

    /**
     * Get Player By It's Joiner ID
     *
     * @param JoinerID = Joiner ID
     * @return
     */
    public static QueueJoin getPlayerInformationByJoinerID(String JoinerID) {
        for (QueueJoin Data : PlayerQueueList) {
            if (Data.getJoinerID().equals(JoinerID)) {
                return Data;
            }
        }
        return null;
    }

    /**
     * Print All Data From Player Queue List
     */
    public static void PrintAllPlayerQueueData() {
        for (QueueJoin a : PlayerQueueList) {
            System.out.println(a.getIP());
        }
    }

    /**
     * Remove Player From Queue, This Removes Player From Server Queue And Well
     * As Server And Player's Queue Of Server, And Allow New Password Change
     *
     * @param JoinerID JoinerID Of Player
     * @param ServerIP Server IP
     * @param ServerPORT Server Port
     * @param Reason Removing Reason
     */
    public static void removePlayerByJoinerID(String JoinerID, String ServerIP, String ServerPORT, int Reason) {

        QueueJoin Player = null;
        for (QueueJoin Data : PlayerQueueList) {
            if (Data.getJoinerID().equals(JoinerID)) {
                Player = Data;
                break;
            }
        }

        if (Player != null) {

            // Allow New Password To Be Stored
            PasswordStore passwordStore = AEPEMAuthenticJoinerController.getServerByAddress(Player.getServerIP(), Player.getServerPort());
            passwordStore.setTakePassword(true);

            try {
                Thread.sleep(200); // Idle For 0.2 Seconds For New Password To Settle
            } catch (InterruptedException ex) {
            }

            reducePlayerQueue(ServerIP, ServerPORT); // Reduce Player From Queue
            PlayerQueueList.remove(Player); // Remove Player From Queue

            // Removing Reason
            if (Reason == 1) {
                DataProcessor.echo_console("Removing Player From Queue, Player: " + Player.getPlayerName() + " Successfully Joined Server " + Player.getServerIPPORT() + " | JoinerID: " + Player.getJoinerID(), 1);
            } else if (Reason == 2) {
                DataProcessor.echo_console("Removing Player From Queue, Player: " + Player.getPlayerName() + " Disconnected, Was Joining Server " + Player.getServerIPPORT() + " | JoinerID: " + Player.getJoinerID(), 2);
            }
        }

        for (ServerQueue Data : ServerQueueList) {
            if (Data.getServerIPPORT().equals(ServerIP + ":" + ServerPORT)) {
                Data.setQueueNumber(1);
                break;
            }
        }
        
        AntiCheatVerifier antiCheatVrf = null;
        for (AntiCheatVerifier antiCheatVerf : antiCheat) {
            if (antiCheatVerf.getServerIPPort().equals(ServerIP + ":" + ServerPORT) && antiCheatVerf.getJoinerID().equals(JoinerID)) {
                antiCheatVrf = antiCheatVerf;
                break;
            }
        }
        
        if (antiCheatVrf != null) {
            antiCheat.remove(antiCheatVrf);
        }
    }

    /**
     * Fetch Player Information By His Requested Server IP, Server PORT and
     * Multiplayer Name
     *
     * @param IP Server IP
     * @param Port Server Port
     * @param MP_Name Multiplayer Name
     * @return
     */
    public static QueueJoin getPlayerInformationByServerIP_PORT_MP_NAME(String IP, String Port, String MP_Name) {
        for (QueueJoin Data : PlayerQueueList) {
            if (Data.getServerIPPORT().equals(IP + ":" + Port) && Data.getPlayerName().equals(MP_Name)) {
                return Data;
            }
        }
        return null;
    }

    /**
     * Reduce Player Queue From Specific Server [Reduced By 1]
     *
     * @param ServerIP
     * @param ServerPORT
     */
    public static void reducePlayerQueue(String ServerIP, String ServerPORT) {
        for (QueueJoin Data : PlayerQueueList) {
            if (Data.getServerIPPORT().equals(ServerIP + ":" + ServerPORT)) {
                Data.setQueueNumber(1);
            }
        }
    }
}
