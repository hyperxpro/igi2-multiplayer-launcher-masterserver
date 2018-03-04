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
package launcher.server;

import launcher.server.processor.PlayerIPRecord;
import launcher.server.processor.QueueJoin;
import launcher.server.processor.AntiCheatVerifier;
import aayush.atharva.TurboCryptography.Decryption;
import aayush.atharva.TurboCryptography.Encryption;
import launcher.server.processor.EmailVerification;
import launcher.server.processor.PasswordHash;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;
import java.util.TimeZone;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import static launcher.server.AEPEMAuthenticJoin.AEPEMAuthenticJoinerController.addNewServer;
import static launcher.server.AEPEMAuthenticJoin.AEPEMAuthenticJoinerController.getServerByAddress;
import static launcher.server.AEPEMAuthenticJoin.AEPEMAuthenticJoinerController.getServerByAddressAndServerID;
import static launcher.server.AEPEMAuthenticJoin.AEPEMAuthenticJoinerController.setNewPassword;
import launcher.server.AEPEMAuthenticJoin.PasswordStore;
import static launcher.server.AntiCheatVerifierController.getPlayerByServerIPPORTName;
import static launcher.server.AntiCheatVerifierController.isPlayerVerified;
import static launcher.server.AntiCheatVerifierController.setPlayerAntiCheatVerified;
import static launcher.server.PlayerIPRecordController.getPlayerIPByServerNameIPandPort;
import static launcher.server.PlayerIPRecordController.removePlayerFromIPRecord;
import static launcher.server.ServerJoinController.addPlayer;
import static launcher.server.ServerJoinController.getPlayerInformationByJoinerID;
import static launcher.server.ServerJoinController.getPlayerInformationByServerIP_PORT_MP_NAME;
import static launcher.server.ServerJoinController.removePlayerByJoinerID;
import org.apache.commons.validator.routines.EmailValidator;
import launcher.server.processor.NewAccountEmailVerificationController;
import static launcher.server.processor.NewAccountEmailVerificationController.EmailStatus;
import static launcher.server.processor.NewAccountEmailVerificationController.removeEmail;
import static launcher.server.processor.NewAccountEmailVerificationController.reset;
import launcher.server.processor.PasswordResetEmailVerificationController;

/**
 *
 * @author Hyper
 */
public class DataProcessor {

    private String PacketData;
    private Socket socket = null;
    private static final File MasterServerList = new File("/var/www/igi2_master_server/igi2/masterserver/igi2_masterserver/servers/igi2-masterserver/servers/launcher_9-6/igi2_servers_igi2.LAUNCHER");

    // MySQL
    private static final String ENDPOINT = "jdbc:mysql://localhost:3306/igi2?useSSL=false";
    private static final String USER = "root";
    private static final String PASS = "123456";
    private Connection connection;
    private Statement statement;
    private ResultSet resultset;

    public DataProcessor(String Data, Socket socket) {
        this.PacketData = Data;
        this.socket = socket;
    }

    private Socket getSocket() {
        return socket;
    }

    private String getPacketData() {
        return PacketData;
    }

    public String Process() {

        try {

            Properties Data = new Properties();
            InputStream input = new ByteArrayInputStream(getPacketData().getBytes(StandardCharsets.UTF_8.name()));
            Data.load(input);

            String Query = Data.getProperty("Query");

//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
            { // Sign Up For New Account

                if (Query.equals("Sign_Up")) {

                    if (Data.getProperty("Email").contains(" ")) {
                        return "Response=Invalid Email Address";
                    }

                    String Encrypt_Email = new Encryption(AESSecretKey, CBCKeyA, CBCKeyB, KeyAES, Key, IV, Data.getProperty("Email").toUpperCase()).Encrypt();
                    String MP_Name_Verifier = new Encryption(AESSecretKey, CBCKeyA, CBCKeyB, KeyAES, Key, IV, FixExtraWhiteSpace(Data.getProperty("MP_Name")).toUpperCase()).Encrypt();
                    String Username_Verifier = new Encryption(AESSecretKey, CBCKeyA, CBCKeyB, KeyAES, Key, IV, FixExtraWhiteSpace(Data.getProperty("Username")).toUpperCase()).Encrypt();

                    // Check Data
                    {
                        { // Check Name

                            String Name = FixExtraWhiteSpace(Data.getProperty("Name"));

                            if (Name.length() > 50) {
                                return "Response=Maximum Name Lenght Is 50 Characters\nPlease use a shoter name";
                            }
                        }

                        {   // Check Username
                            if (isUsernameValid(Username_Verifier)) {
                                return "Response=Username already taken\nPlease use another Username";
                            }

                            String Username = FixExtraWhiteSpace(Data.getProperty("Username"));

                            if (Username.length() > 50) {
                                return "Response=Maximum Username Lenght is 50 Characters\nPlease use a different small Username";
                            }
                        }

                        {  // Check Multiplayer Name
                            if (!isMP_NameValid(MP_Name_Verifier)) {
                                return "Response=Multiplayer name already taken\nPlease use another Multiplayer name";
                            }

                            String MP_Name = FixExtraWhiteSpace(Data.getProperty("MP_Name"));

                            if (MP_Name.length() > 50) {
                                return "Response=Maximum Multiplayer Name Lenght is 50 Characters\nPlease use a different small Multiplayer Name";
                            }
                        }

                        {  // Check Email Address

                            String Email = Data.getProperty("Email");

                            if (Email.length() > 50) {
                                return "Response=Maximum Email Address Lenght is 50 Characters\nPlease use a different small Email Address";
                            }

                            if (!isEmailAddressValid(Email)) {
                                return "Response=Invalid Email Address\nPlease use a valid Email address.";
                            }

                            String CheckEmail = Data.getProperty("Email").toLowerCase();

                            CheckEmail = CheckEmail.substring(CheckEmail.lastIndexOf("@") + 1);

                            if (isEmailFake(CheckEmail)) {
                                return "Response=Email Not Allowed. Please use a valid Email address.\nContact Support At: aayush@igi-2.com for further help.";
                            }

                            if (isEmailValid(Encrypt_Email)) {
                                return "Response=The Email Address which you entered is already associated with another account\nPlease use different unique Email Address";
                            }
                        }
                    }

                    // Verify Email And Add Account
                    {

                        String Email = Data.getProperty("Email").toUpperCase();

                        EmailVerification verify = EmailStatus(Email);

                        if (verify == null) {
                            reset.add(new EmailVerification(Data.getProperty("Name"), Email, generateEmailCode(), socket.getInetAddress().getHostAddress(), Data.getProperty("OSName"), Data.getProperty("PCName"))); // Add Data To Password Reset
                            verify = EmailStatus(Email); // Fetch Email Code
                            new NewAccountEmailVerificationController(Data.getProperty("Name"), Email, verify.getCode(), getSocket().getInetAddress().getHostAddress(), Data.getProperty("OSName"), Data.getProperty("PCName")).start();// Send Verification Code For Password Reset
                            echo("[" + getSocket().getInetAddress().getHostAddress() + "] [SignUp] Email Verification For New Account, Email Sent To: " + Data.getProperty("Email") + " [SUCCESS]", 0);
                            return "Response=Continue_Verification_New_Account";
                        } else if (Data.getProperty("EmailCode") != null) {

                            String Code = Data.getProperty("EmailCode");

                            if (Code.equals(verify.getCode())) {

                                String EncryptUsername = new Encryption(AESSecretKey, CBCKeyA, CBCKeyB, KeyAES, Key, IV, Data.getProperty("Username")).Encrypt();
                                String Encrypt_MP_Name = new Encryption(AESSecretKey, CBCKeyA, CBCKeyB, KeyAES, Key, IV, Data.getProperty("MP_Name")).Encrypt();
                                String Encrypt_Name = new Encryption(AESSecretKey, CBCKeyA, CBCKeyB, KeyAES, Key, IV, Data.getProperty("Name")).Encrypt();
                                String Message = new Encryption(AESSecretKey, CBCKeyA, CBCKeyB, KeyAES, Key, IV, "NONE").Encrypt();
                                String Hash_Password = new PasswordHash().HashPassword(Data.getProperty("Password"));
                                String Account_Status = new Encryption(AESSecretKey, CBCKeyA, CBCKeyB, KeyAES, Key, IV, "OK").Encrypt();
                                String Join_Time = getUTCTime();
                                String Account_Ray_ID = getRayID();

                                while (isRayIDValid(Account_Ray_ID)) {
                                    Account_Ray_ID = getRayID();
                                }

                                // Add New Account
                                {
                                    addAccount(Account_Ray_ID, Encrypt_Name, EncryptUsername, Username_Verifier, Encrypt_MP_Name, MP_Name_Verifier, Encrypt_Email, Hash_Password, Account_Status, Message, Join_Time); // Add New Account To Database
                                }

                                // Process WHOIS Database
                                {
                                    addWhois(Encrypt_Name, EncryptUsername, Encrypt_MP_Name, Account_Ray_ID, Join_Time);
                                }

                                removeEmail(verify.getEmail());

                                echo("[" + getSocket().getInetAddress().getHostAddress() + "] [SignUp] Account Successfully Created, Email: " + Data.getProperty("Email") + ", Multiplayer Name: " + Data.getProperty("MP_Name") + " [SUCCESS]", 0);

                                return "Response=Account Created Successfully";
                            } else {
                                return "Response=Wrong Verification Code";
                            }

                        } else if (verify != null) {
                            return "Response=Verification Code Already Sent To Your Email\nYou Can Request For New Code After " + verify.getEmailTime();
                        }
                    }
                }
            }
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
            { // Reset Password
                if (Query.equals("Reset_Password")) {

                    String Email = Data.getProperty("Email").toUpperCase();

                    if (isEmailValid(new Encryption(AESSecretKey, CBCKeyA, CBCKeyB, KeyAES, Key, IV, Data.getProperty("Email").toUpperCase()).Encrypt())) {

                        EmailVerification passwordReset = EmailStatus(Email);

                        if (passwordReset == null) {
                            reset.add(new EmailVerification(Email, generateEmailCode(), getSocket().getInetAddress().getHostAddress(), Data.getProperty("OSName"), Data.getProperty("PCName"))); // Add Data To Password Reset
                            passwordReset = EmailStatus(Email);  // Fetch Email Code
                            new PasswordResetEmailVerificationController(Email, passwordReset.getCode(), getSocket().getInetAddress().getHostAddress(), Data.getProperty("OSName"), Data.getProperty("PCName")).start();// Send Verification Code For Password Reset
                            echo("[" + getSocket().getInetAddress().getHostAddress() + "] [PasswordReset] Email Verification For Account Password Reset, Email Sent To: " + Data.getProperty("Email") + " [SUCCESS]", 1);
                            return "Response=Continue_Verification_Password_Reset";
                        } else if (Data.getProperty("EmailCode") != null) {

                            String Code = Data.getProperty("EmailCode");

                            if (Code.equals(passwordReset.getCode())) {

                                if (Data.getProperty("Password") != null) {
                                    updatePassword(Data.getProperty("Password"), Email);
                                    removeEmail(Email);
                                    echo("[" + getSocket().getInetAddress().getHostAddress() + "] [PasswordReset] New Password Successfully Updated For Account, Email: " + Data.getProperty("Email") + " [SUCCESS]", 1);
                                    return "Response=Password Updated Successfully. Now You Can Login To Your Account With New Password!";
                                }

                                return "Response=Code_Verified" + "\n" + "ReseterID=" + Data.getProperty("ReseterID");

                            } else {
                                return "Response=Wrong Verification Code";
                            }

                        } else {
                            return "Response=Verification Code Already Sent To Your Email. You Can Request For New Code After " + passwordReset.getEmailTime();
                        }

                    } else {
                        return "Response=No Account Exist With The Email Address You Entered";
                    }
                }
            }
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
            // Sign In            
            {

                if (Query.equals("SignIn")) {

                    String AuthenticationHeader = Data.getProperty("AuthHeader");

                    switch (AuthenticationHeader) {
                        case "Email": {

                            String Email = new Encryption(AESSecretKey, CBCKeyA, CBCKeyB, KeyAES, Key, IV, Data.getProperty("Email").toUpperCase()).Encrypt();
                            String Password = Data.getProperty("Password");

                            if (!isEmailValid(Email)) {
                                return "Response=No Account Exist With The Email Address You Entered";
                            }

                            if (new PasswordHash().isPasswordValid(Password, AuthenticateUserViaEmail(Email))) {

                                String MP_Name = new Decryption(AESSecretKey, CBCKeyA, CBCKeyB, KeyAES, Key, IV, getMultiplayerName(Email)).Decrypt(); // Fetch Multiplayer Name
                                echo("[" + getSocket().getInetAddress().getHostAddress() + "] [SignIn] User: " + Data.getProperty("Email") + " Authenticated Successfully. [SUCCESS]", 2); // Print Information To Console
                                return "Response=Session Authenticated" + "\n" + "SignerID=" + Data.getProperty("SignerID") + "\n" + "MP_Name=" + MP_Name;

                            } else {
                                return "Response=Wrong Password";
                            }

                        }
                        case "Username": {
                            String Username = new Encryption(AESSecretKey, CBCKeyA, CBCKeyB, KeyAES, Key, IV, Data.getProperty("Username").toUpperCase()).Encrypt();
                            String Password = Data.getProperty("Password");

                            if (!isUsernameValid(Username)) {
                                return "Response=No Account Exist With The Username You Entered";
                            }

                            if (new PasswordHash().isPasswordValid(Password, AuthenticateUserViaUsername(Username))) {

                                String MP_Name = new Decryption(AESSecretKey, CBCKeyA, CBCKeyB, KeyAES, Key, IV, getMultiplayerName_Username(Username)).Decrypt(); // Fetch Multiplayer Name
                                echo("[" + getSocket().getInetAddress().getHostAddress() + "] [SignIn] User: " + Data.getProperty("Username") + " Authenticated Successfully. [SUCCESS]", 2); // Print Information To Console
                                return "Response=Session Authenticated" + "\n" + "SignerID=" + Data.getProperty("SignerID") + "\n" + "MP_Name=" + MP_Name;

                            } else {
                                echo("[" + getSocket().getInetAddress().getHostAddress() + "] [SignIn] User: " + Data.getProperty("Username") + " Authenticated Unsuccessfully. [WRONG PASSWORD]", 2);
                                return "Response=Wrong Password";
                            }
                        }
                        default:
                            return "Response=Unknown Sign In Identifier";
                    }
                }
            }
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
            { // WHOIS

                if (Query.equals("WHOIS_INFO_REQUEST")) {

                    String UserIdentity = Data.getProperty("Identity"); // Load User Identity
                    String MP_Name_Verifier = new Encryption(AESSecretKey, CBCKeyA, CBCKeyB, KeyAES, Key, IV, Data.getProperty("MP_Name").toUpperCase()).Encrypt(); // Encrypt Player Name For VFR Table Matching

                    String WHOIS_Information = WHOIS_MP_Name(MP_Name_Verifier);

                    if (WHOIS_Information != null) {
                        echo("[" + getSocket().getInetAddress().getHostAddress() + "] [WHOIS] User: " + UserIdentity + " Requested WHOIS For: " + Data.getProperty("MP_Name") + " [SUCCESS]", 3);
                        return "Response=Request_Completed" + "\n" + WHOIS_Information;
                    }

                    echo("[" + getSocket().getInetAddress().getHostAddress() + "] [WHOIS] User: " + UserIdentity + " Requested WHOIS For: " + Data.getProperty("MP_Name") + " | Player Not Found [FAILED]", 3);
                    return "Response=Player_Not_Found";
                }
            }

//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
            { // Connect Player To Server

                if (Query.equals("Join_Server")) {

                    String JoinerID = Data.getProperty("JoinerID");
                    String MP_Name = Data.getProperty("MP_Name");
                    String ServerIP = Data.getProperty("ServerIP");
                    String ServerPort = Data.getProperty("ServerPORT");
                    String UserIdentity = Data.getProperty("Identity");

                    QueueJoin player = getPlayerInformationByJoinerID(JoinerID);

                    if (player == null && Data.getProperty("Status") == null) {

                        if (VerifyJoinerID(JoinerID)) {
                            echo("[" + getSocket().getInetAddress().getHostAddress() + "] [Join_Server-Register] User: " + UserIdentity + " | Player: " + MP_Name + ",Was Registering For Join Server : " + ServerIP + ":" + ServerPort + " | Used Fake JoinerID [DISCONNECTED]", 4);
                            return "Response=You're Not Allowed To Join Server";
                        } else {

                            addPlayer(MP_Name, JoinerID, getSocket().getInetAddress().getHostAddress(), ServerIP, ServerPort); //  Add To Queue

                            addJoinerID(JoinerID); // Add JoinerID To Database

                            player = getPlayerInformationByJoinerID(JoinerID); //  Assign New Value To QueueJoin

                            echo("[" + getSocket().getInetAddress().getHostAddress() + "] [Join_Server-Register] User: " + UserIdentity + " | Player: " + player.getPlayerName() + ", Registered To Join Server : " + player.getServerIPPORT() + " | Current Position In Queue: " + player.getQueueNumber() + " | " + " JoinerID: " + player.getJoinerID(), 4);

                            return "Response=In_Queue" + "\n" + "QueueNumber=" + player.getQueueNumber();
                        }

                    } else if (player != null && Data.getProperty("Status") != null) {

                        String PlayerStatus = Data.getProperty("Status");

                        if (PlayerStatus.equals("KeepConnectionAlive_JoiningServer")) {
                            player.resetConnectionTimeout(5);
                        }

                        PasswordStore ServerPassword = getServerByAddress(player.getServerIP(), player.getServerPort());

                        echo("[" + getSocket().getInetAddress().getHostAddress() + "] [Join_Server-Joining] User: " + UserIdentity + " | Player: " + player.getPlayerName() + ", Joining Server : " + player.getServerIPPORT() + " | Current Position In Queue: " + player.getQueueNumber() + " | " + " JoinerID: " + player.getJoinerID() + "\n" + "Pass=" + ServerPassword.getServerPASSWORD(), 4);

                        return "That's_Affiramative!";

                    } else if (player != null) {

                        String QueueNumber = String.valueOf(player.getQueueNumber());

                        player.resetConnectionTimeout(5);

                        if (QueueNumber.equals("0")) {

                            PasswordStore ServerPassword = getServerByAddress(player.getServerIP(), player.getServerPort());

                            echo("[" + getSocket().getInetAddress().getHostAddress() + "] [Join_Server-JoinGranted] User: " + UserIdentity + " | Player In Queue: " + QueueNumber + " | Server : " + player.getServerIPPORT() + " | " + "JoinerID : " + player.getJoinerID() + " | Password: " + ServerPassword.getServerPASSWORD(), 4);

                            return "Response=In_Queue" + "\n" + "QueueNumber=" + QueueNumber + "\n" + "ServerPassword=" + ServerPassword.getServerPASSWORD();

                        } else {

                            echo("[" + getSocket().getInetAddress().getHostAddress() + "] [Join_Server-InQueue] User: " + UserIdentity + " | Player In Queue: " + QueueNumber + " | Server : " + player.getServerIPPORT() + " | " + "JoinerID : " + player.getJoinerID(), 4);

                            return "Response=In_Queue" + "\n" + "QueueNumber=" + QueueNumber;
                        }
                    }
                }
            }
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
            { // Add or Update New AEPEM AuthenticJoin Password

                if (Query.equals("AEPEM_AuthenticJoin_Password")) {

                    String ServerIP;
                    String ServerPORT = Data.getProperty("PORT");
                    String ServerPassword = Data.getProperty("PASS");
                    String ServerID = Data.getProperty("ID");

                    if (Data.getProperty("ServerIP") == null) {
                        ServerIP = getSocket().getInetAddress().getHostAddress();
                    } else {
                        ServerIP = Data.getProperty("ServerIP");
                    }

                    PasswordStore passStore = getServerByAddressAndServerID(ServerIP, ServerPORT, ServerID);

                    if (passStore == null) {
                        addNewServer(ServerIP, ServerPORT, ServerPassword, ServerID); // Add New Server
                        return "Response=Change";
                    } else {

                        boolean Action = setNewPassword(ServerIP, ServerPORT, ServerPassword); // Set New Password

                        if (Action) {
                            return "Response=Change";
                        } else {
                            return "Response=Keep";
                        }

                    }
                }
            }

//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
            { // Get Player IP
                if (Query.equals("GetPlayerIP")) {

                    String PlayerName = Data.getProperty("MP_Name");
                    String ServerIP;
                    String ServerPORT = Data.getProperty("PORT");

                    if (Data.getProperty("ServerIP") == null) {
                        ServerIP = getSocket().getInetAddress().getHostAddress();
                    } else {
                        ServerIP = Data.getProperty("ServerIP");
                    }

                    PlayerIPRecord playerIPRecord = getPlayerIPByServerNameIPandPort(ServerIP, ServerPORT, PlayerName);

                    if (playerIPRecord == null) {
                        return "Response=Not_Found";
                    } else {

                        String IP = playerIPRecord.getPlayerIP();

                        QueueJoin player = getPlayerInformationByServerIP_PORT_MP_NAME(ServerIP, ServerPORT, PlayerName);

                        if (player != null) {
                            removePlayerByJoinerID(player.getJoinerID(), ServerIP, ServerPORT, 1);
                        }

                        removePlayerFromIPRecord(playerIPRecord.getServerIP(), playerIPRecord.getServerPort(), playerIPRecord.getPlayerIP(), playerIPRecord.getPlayerName());

                        return "Response=Found" + "\n" + "IP=" + IP;
                    }
                }
            }

            { // Get Player IP
                if (Query.equals("GPLRIP")) {

                    String ServerIP;
                    String ServerPORT = Data.getProperty("PORT");
                    String PlayerName = Data.getProperty("MP_Name");

                    if (Data.getProperty("ServerIP") == null) {
                        ServerIP = getSocket().getInetAddress().getHostAddress();
                    } else {
                        ServerIP = Data.getProperty("ServerIP");
                    }

                    PlayerIPRecord playerIPRecord = getPlayerIPByServerNameIPandPort(ServerIP, ServerPORT, PlayerName);

                    if (playerIPRecord == null) {
                        return "Response=Not_Found";
                    } else {
                        QueueJoin player = getPlayerInformationByServerIP_PORT_MP_NAME(ServerIP, ServerPORT, PlayerName);
                        String PlayerIP = playerIPRecord.getPlayerIP();

                        boolean Action = false;

                        if (player != null) {
                            Action = isPlayerVerified(ServerIP, ServerPORT, PlayerName, player.getJoinerID());
                            System.out.println(player.getPlayerName() + " anti cheat status " + Action);
                            removePlayerByJoinerID(player.getJoinerID(), ServerIP, ServerPORT, 1);
                        }

                        if (Action) {
                            removePlayerFromIPRecord(playerIPRecord.getServerIP(), playerIPRecord.getServerPort(), playerIPRecord.getPlayerIP(), playerIPRecord.getPlayerName());
                            return "Response=Found" + "\n" + "IP=" + PlayerIP;
                        }

                        return "Response=Not_Found";
                    }
                }
            }

//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
            { // Anti Cheat Verification
                if (Query.equals("ACB")) {

                    String ServerIP = Data.getProperty("SIP");
                    String ServerPORT = Data.getProperty("SPORT");
                    String PlayerName = Data.getProperty("PNAME");
                    String JoinerID = Data.getProperty("JID");

                    AntiCheatVerifier antiCheatVerifier = getPlayerByServerIPPORTName(ServerIP, ServerPORT, PlayerName, JoinerID);

                    if (antiCheatVerifier != null) {
                        setPlayerAntiCheatVerified(PlayerName, ServerIP, ServerPORT, JoinerID);
                    }

                    return "Response=Disconnect";
                }
            }

//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
            { // Register New Server

                if (Query.equals("Register_Server")) {

                    String ServerPORT = Data.getProperty("PORT");

                    if (ServerPORT.matches("[0-9]+")) {

                        String ServerIP;

                        if (Data.getProperty("ServerIP") == null) {
                            ServerIP = getSocket().getInetAddress().getHostAddress();
                        } else {
                            ServerIP = Data.getProperty("ServerIP");
                        }

                        int Port = Integer.parseInt(ServerPORT);

                        if (Port >= 0 && Port <= 65535) {

                            {   // Print Server List

                                StringBuilder Content = new StringBuilder();

                                Scanner in = null;

                                in = new Scanner(MasterServerList);
                                while (in.hasNext()) {
                                    String Line = in.nextLine();

                                    if (Line.equals(ServerIP + ":" + ServerPORT + " " + Data.getProperty("Password_Method"))) {
                                        Content.append(Line);
                                    }
                                }

                                if (Content.toString().isEmpty()) {

                                    try {
                                        FileWriter writer = new FileWriter(MasterServerList, true);
                                        writer.write(ServerIP + ":" + ServerPORT + " " + Data.getProperty("Password_Method"));
                                        writer.write("\r\n");
                                        writer.close();
                                    } catch (IOException e) {
                                        System.err.println("Unable To Print Server List: " + e);
                                    }

                                }
                            }

                            { // Print Server Information To Database

                                try {
                                    FileWriter writer = new FileWriter("/root/Desktop/Database/Servers_Info/" + ServerIP + ":" + ServerPORT + ".txt", true);
                                    writer.write(ServerIP + ":" + ServerPORT + " " + Data.getProperty("Password_Method") + "\n\n----------------------" + getPacketData() + "\n\n----------------------");
                                    writer.write("\r\n");
                                    writer.close();
                                } catch (IOException e) {
                                    System.err.println("Unable To Print Information In Database: " + e.getMessage());
                                }

                            }
                        }
                    }
                }
            }

//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return "Response=Request Not Recognized";
    }

    private void addAccount(String Account_Ray_ID, String Name, String Username, String Username_VFR, String MP_Name, String MP_Name_VFR, String Email, String Password, String Account_Status, String Message, String Join_Time) {
        SQLQuery("insert into account (Account_Ray_ID,Name,Username,Username_VFR,MP_Name,MP_Name_VFR,Email,Password,Account_Status,Message,Join_Time) values('" + Account_Ray_ID + "', '" + Name + "','" + Username + "','" + Username_VFR + "','" + MP_Name + "','" + MP_Name_VFR + "','" + Email + "','" + Password + "','" + Account_Status + "','" + Message + "','" + Join_Time + "')");
    }

    private void updatePassword(String Password, String Email) throws Exception {
        SQLQuery("update account set Password= '" + new PasswordHash().HashPassword(Password) + "' where Email= '" + new Encryption(AESSecretKey, CBCKeyA, CBCKeyB, KeyAES, Key, IV, Email.toUpperCase()).Encrypt() + "'");
    }

    private void addWhois(String Name, String Username, String MP_Name, String Account_Ray_ID, String Join_Time) {
        SQLQuery("insert into whois (Name,Username,MP_Name,Account_Ray_ID,Join_Time) values('" + Name + "', '" + Username + "','" + MP_Name + "','" + Account_Ray_ID + "','" + Join_Time + "')");
    }

    private void addJoinerID(String ID) {
        SQLQuery("insert into JoinerID (JoinerIDRecord) values('" + ID + "')");
    }

    private String getRayID() {
        return UUID.randomUUID().toString();
    }

    private boolean VerifyJoinerID(String ID) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(ENDPOINT, USER, PASS);
            statement = connection.createStatement();

            String query = "select * from JoinerID where JoinerIDRecord='" + ID + "'";
            statement = connection.createStatement();
            resultset = statement.executeQuery(query);

            while (resultset.next()) {
                return true;
            }

            connection.close();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private String AuthenticateUserViaEmail(String Email) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(ENDPOINT, USER, PASS);
            statement = connection.createStatement();

            String query = "select * from account where Email='" + Email + "'";
            statement = connection.createStatement();
            resultset = statement.executeQuery(query);

            while (resultset.next()) {
                return resultset.getString("Password");
            }

            connection.close();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String AuthenticateUserViaUsername(String Username) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(ENDPOINT, USER, PASS);
            statement = connection.createStatement();

            String query = "select * from account where Username_VFR='" + Username + "'";
            statement = connection.createStatement();
            resultset = statement.executeQuery(query);

            while (resultset.next()) {
                return resultset.getString("Password");
            }

            connection.close();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean isEmailValid(String Email) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(ENDPOINT, USER, PASS);
            statement = connection.createStatement();

            String query = "select * from account where Email='" + Email + "'";
            statement = connection.createStatement();
            resultset = statement.executeQuery(query);

            while (resultset.next()) {
                return true;
            }

            connection.close();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private String getMultiplayerName(String Email) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(ENDPOINT, USER, PASS);
            statement = connection.createStatement();

            String query = "select * from account where Email='" + Email + "'";
            statement = connection.createStatement();
            resultset = statement.executeQuery(query);

            while (resultset.next()) {
                return resultset.getString("MP_Name");
            }

            connection.close();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getMultiplayerName_Username(String Username) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(ENDPOINT, USER, PASS);
            statement = connection.createStatement();

            String query = "select * from account where Username_VFR='" + Username + "'";
            statement = connection.createStatement();
            resultset = statement.executeQuery(query);

            while (resultset.next()) {
                return resultset.getString("MP_Name");
            }

            connection.close();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean isUsernameValid(String Username) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(ENDPOINT, USER, PASS);
            statement = connection.createStatement();

            String query = "select * from account where Username_VFR='" + Username + "'";
            statement = connection.createStatement();
            resultset = statement.executeQuery(query);

            while (resultset.next()) {
                return true;
            }

            connection.close();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean isMP_NameValid(String MP_Name) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(ENDPOINT, USER, PASS);
            statement = connection.createStatement();

            String query = "select * from account where MP_Name_VFR='" + MP_Name + "'";
            statement = connection.createStatement();
            resultset = statement.executeQuery(query);

            while (resultset.next()) {
                return false;
            }

            connection.close();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    private String WHOIS_MP_Name(String MP_Name) throws Exception {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(ENDPOINT, USER, PASS);
            statement = connection.createStatement();

            String query = "select * from account where MP_Name_VFR='" + MP_Name + "'";
            statement = connection.createStatement();
            resultset = statement.executeQuery(query);

            while (resultset.next()) {
                return getWHOIS(resultset.getString("Account_Ray_ID"));
            }

            connection.close();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getWHOIS(String Account_Ray_ID) throws Exception {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(ENDPOINT, USER, PASS);
            statement = connection.createStatement();

            String query = "select * from whois where Account_Ray_ID='" + Account_Ray_ID + "'";
            statement = connection.createStatement();
            resultset = statement.executeQuery(query);

            while (resultset.next()) {
                String Name = new Decryption(AESSecretKey, CBCKeyA, CBCKeyB, KeyAES, Key, IV, resultset.getString("Name")).Decrypt();
                String Username = new Decryption(AESSecretKey, CBCKeyA, CBCKeyB, KeyAES, Key, IV, resultset.getString("Username")).Decrypt();
                String MP_Name = new Decryption(AESSecretKey, CBCKeyA, CBCKeyB, KeyAES, Key, IV, resultset.getString("MP_Name")).Decrypt();;
                String AccountRayID = resultset.getString("Account_Ray_ID");
                String Join_Time = resultset.getString("Join_Time");
                return "Name=" + Name + "\n" + "Username=" + Username + "\n" + "MP_Name=" + MP_Name + "\n" + "Account_Ray_ID=" + AccountRayID + "\n" + "Join_Time=" + Join_Time;
            }

            connection.close();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean isEmailFake(String Email) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(ENDPOINT, USER, PASS);
            statement = connection.createStatement();

            String query = "select * from fakeemailbanned where Email='" + Email + "'";
            statement = connection.createStatement();
            resultset = statement.executeQuery(query);

            while (resultset.next()) {
                return true;
            }

            connection.close();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean isRayIDValid(String ID) throws SQLException {

        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(ENDPOINT, USER, PASS);
            statement = connection.createStatement();

            String query = "select * from account where Account_Ray_ID='" + ID + "'";
            statement = connection.createStatement();
            resultset = statement.executeQuery(query);

            while (resultset.next()) {
                return true;
            }

            connection.close();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void SQLQuery(String query) {
        try {
            connection = DriverManager.getConnection(ENDPOINT, USER, PASS);
            statement = connection.createStatement();
            statement.executeUpdate(query);
            connection.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String getUTCTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, yyyy hh:mm:ss a z");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        return sdf.format(new Date());
    }

    private String getISTTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, yyyy hh:mm:ss a z");
        sdf.setTimeZone(TimeZone.getTimeZone("IST"));
        return sdf.format(new Date());
    }

    private static String getISTTime(int x) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, yyyy hh:mm:ss a z");
        sdf.setTimeZone(TimeZone.getTimeZone("IST"));
        return sdf.format(new Date());
    }

    private String generateEmailCode() {
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 25; i = i + 1) {
            stringBuilder.append("ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890".charAt(random.nextInt("ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890".length())));
        }
        return stringBuilder.toString();
    }

    private boolean isEmailAddressValid(String Email) {
        return EmailValidator.getInstance().isValid(Email);
    }

    private void echo(String Data, int Opreation) {

        FileWriter writer = null;
        try {
            System.out.println("[" + getISTTime() + "] " + Data);
            String Filename = "/home/admin/Launcher/Master_Server/Master_Server_Log/";
            switch (Opreation) {
                case 0:
                    Filename = Filename + "Account_Creation.log";
                    break;
                case 1:
                    Filename = Filename + "Password_Reset.log";
                    break;
                case 2:
                    Filename = Filename + "Sign_In.log";
                    break;
                case 3:
                    Filename = Filename + "WHOIS_Request.log";
                    break;
                case 4:
                    Filename = Filename + "Join_Server.log";
                    break;
                case 5:
                    Filename = Filename + "AEPEM_Password.log";
                    break;
                case 6:
                    Filename = Filename + "Banned_Player.log";
                    break;
                default:
                    Filename = Filename + "Logs.log";
                    break;
            }
            writer = new FileWriter(Filename, true);
            writer.write("[" + getISTTime() + "] " + Data);
            writer.write("\r\n");
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(DataProcessor.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(DataProcessor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void echo_console(String Data, int Opreation) {

        FileWriter writer = null;
        try {
            System.out.println("[" + getISTTime(Opreation) + "] " + Data);
            String Filename = "/home/admin/Launcher/Master_Server/Master_Server_Log/";
            switch (Opreation) {
                case 0:
                    Filename = Filename + "Server_Queue_Load.log";
                    break;
                case 1:
                    Filename = Filename + "Players_Joined.log";
                    break;
                case 2:
                    Filename = Filename + "Players_Disconnected.log";
                    break;
                case 3:
                    Filename = Filename + "New_Server_Password.log";
                    break;
                case 4:
                    Filename = Filename + "Join_Server.log";
                    break;
                case 5:
                    Filename = Filename + "AEPEM_Password.log";
                    break;
                case 6:
                    Filename = Filename + "Banned_Player.log";
                    break;
                case 7:
                    Filename = Filename + "Master_Server_List_Update.log";
                    break;
                default:
                    Filename = Filename + "Logs.log";
                    break;
            }
            writer = new FileWriter(Filename, true);
            writer.write("{" + getISTTime(0) + "] " + Data);
            writer.write("\r\n");
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(DataProcessor.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(DataProcessor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private String FixExtraWhiteSpace(String Data) {

        while (Data.contains("  ")) {
            Data = Data.replaceAll("  ", " ");
        }

        char First_Char = Data.charAt(0);

        char Last_Char = Data.charAt(Data.length() - 1);

        if (String.valueOf(First_Char).equals(" ")) {
            Data = removeFirstSpace(Data);
        }

        if (String.valueOf(Last_Char).equals(" ")) {
            Data = removeLastSpace(Data);
        }

        return Data.trim();
    }

    private String removeLastSpace(String Data) {
        return Data.substring(0, Data.length() - 1);
    }

    private String removeFirstSpace(String Data) {
        return Data.substring(1);
    }

    private static final String AESSecretKey = ""; // AES KEY
    private static final String CBCKeyA = ""; // 16 Bit
    private static final String CBCKeyB = ""; // 16 Bit
    private static final String KeyAES = ""; // 128 Bit
    private static final String Key = ""; // 16 Bit Key
    private static final String IV = ""; // 16 Bit IV

}
