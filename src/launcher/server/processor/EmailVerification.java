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

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import static launcher.server.processor.NewAccountEmailVerificationController.removeEmail;

/**
 *
 * @author Hyper
 */
public class EmailVerification extends Thread {

    private long MailInnerTime;
    private String MailTime;
    private String Email;
    private String Code;
    private String IP;
    private String OSName;
    private String PCName;
    private String Name;

    public EmailVerification(String Name, String Email, String Code, String IP, String OSName, String PCName) {
        this.Name = Name;
        this.Email = Email;
        this.Code = Code;
        this.IP = IP;
        this.OSName = OSName;
        this.PCName = PCName;
        start();
    }

    public EmailVerification(String Email, String Code, String IP, String OSName, String PCName) {
        this.Email = Email;
        this.Code = Code;
        this.IP = IP;
        this.OSName = OSName;
        this.PCName = PCName;
        start();
    }
    
    @Override
    public void run() {
        int Time = 600; // 10 Minutes Timeout

        setEmailTime("TIME"); // Set A First Value

        while (true) {
            try {
                setMailTime(Time);

                if (getEmailTime().equalsIgnoreCase("0 min 0 sec")) {
                    removeEmail(getEmail());
                    return;
                }

                setEmailTime(((getInnerMailTime() / 60) % (60)) + " min " + getInnerMailTime() % 60 + " sec");
                Time--;
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(EmailVerification.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void NewAccountVerification() {

        String EmailText = "Hey " + getUserName() + "!" + "\n\n"
                + "Welcome To IGI-2 Multiplayer " + "\n\n"
                + "Thank For Your Signing Up For IGI-2 Multiplayer Launcher Account."
                + " " + "But Before We Proceed Further, We Need To Verify Your Account"
                + "\n" + "Please Enter The Verification Code Given Below In IGI-2 Multiplayer Launcher Email Verification Prompt."
                + "\n\n"
                + "Here Is Your Verification Code: " + "\n"
                + "-------------------------------------------------------------------------------" + "\n"
                + getCode() + "\n"
                + "-------------------------------------------------------------------------------"
                + "\n\nLauncher Account Request Was Generated From: "
                + "\n--------------------------------\n"
                + "IP Address: " + getIP() + "\n"
                + "Opreating System: " + getOSName() + "\n"
                + "Machine Name: " + getPCName() + "\n" + "--------------------------------\n\n"
                + "See You Soon In IGI-2 Multiplayer :)" + "\n"
                + "Have A Nice Day!";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "email-smtp.eu-west-1.amazonaws.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("AKIAIXONK52MDUQPJB3A", "AnmNB/H4hN0v956jQFV8rznZywxWd3E1InoL9RWzQMog");
            }
        });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("no-reply@igi-2.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(getEmail()));
            message.setSubject("IGI-2 Multiplayer Launcher Account Verification");
            message.setText(EmailText);

            Transport.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public void PasswordReset() {

        String EmailText = "Hey There!" + "\n\n"
                + "Looks Like You Forgot Your Password Of IGI-2 Multiplayer Launcher Account."
                + " Well, Don't Worry, You Can Easily Get It Back By Resetting It's Password. Let's Get Started!"
                + "\n" + "Please Enter The Verification Code Given Below In IGI-2 Multiplayer Launcher Password Reset Verification Prompt To Reset Password." + "\n\n"
                + "Here Is Your Verification Code: "
                + "\n"
                + "-------------------------------------------------------------------------------"
                + "\n"
                + getCode()
                + "\n"
                + "-------------------------------------------------------------------------------"
                + "\n\nLauncher Account Request Was Generated From: "
                + "\n--------------------------------\n"
                + "IP Address: " + getIP() + "\n"
                + "Opreating System: " + getOSName() + "\n"
                + "Machine Name: " + getPCName() + "\n" + "--------------------------------\n\n"
                + "See You Soon In IGI-2 Multiplayer :)" + "\n"
                + "Have A Nice Day!";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "email-smtp.eu-west-1.amazonaws.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("AKIAIXONK52MDUQPJB3A", "AnmNB/H4hN0v956jQFV8rznZywxWd3E1InoL9RWzQMog");
            }
        });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("no-reply@igi-2.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(getEmail()));
            message.setSubject("IGI-2 Multiplayer Launcher Account Verification");
            message.setText(EmailText);

            Transport.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public String getUserName() {
        return Name;
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

    public String getEmail() {
        return Email;
    }

    public String getCode() {
        return Code;
    }

    public String getEmailTime() {
        return MailTime;
    }

    private void setEmailTime(String Time) {
        this.MailTime = Time;
    }

    private long getInnerMailTime() {
        return MailInnerTime;
    }

    private void setMailTime(long Time) {
        this.MailInnerTime = Time;
    }
}
