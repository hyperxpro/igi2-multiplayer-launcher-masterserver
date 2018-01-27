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

package launcher.server.processor;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Hyper
 */
public class NewAccountEmailVerificationController extends Thread {

    public static List<EmailVerification> reset = new ArrayList<>();

    private String Name;
    private String Email;
    private String Code;
    private String IP;
    private String OSName;
    private String PCName;

    public NewAccountEmailVerificationController(String Name, String Email, String Code, String IP, String OSName, String PCName) {
        this.Name = Name;
        this.Email = Email;
        this.Code = Code;
        this.IP = IP;
        this.OSName = OSName;
        this.PCName = PCName;
    }

    @Override
    public void run() {
        new EmailVerification(getUserName(), getEmail(), getCode(), getIP(), getOSName(), getPCName()).NewAccountVerification(); // Send Verification Code For Password Reset
    }

    public String getUserName() {
        return Name;
    }

    public String getEmail() {
        return Email;
    }

    public String getCode() {
        return Code;
    }

    public String getIP() {
        return IP;
    }

    public String getOSName() {
        return OSName;
    }

    public String getPCName() {
        return PCName;
    }

    public static void removeEmail(String Email) {
        EmailVerification email = null;
        for (EmailVerification p : reset) {
            if (p.getEmail().equals(Email)) {
                email = p;
                break;
            }
        }

        if (email != null) {
            reset.remove(email);
        }
    }
    
    public static EmailVerification EmailStatus(String Email) {
        for (EmailVerification p : reset) {
            if (p.getEmail().equals(Email)) {
                return p;
            }
        }
        return null;
    }

}
