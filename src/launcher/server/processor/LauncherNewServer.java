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

/**
 *
 * @author Hyper
 */
public class LauncherNewServer {

    public static void main(String[] args) {
        MultiThreadedServer server = new MultiThreadedServer(50000);
        new Thread(server).start();
        new ServerListUpdate().start();
    }
}
