package launcher.server.processor;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;

public class MultiThreadedServer implements Runnable {

    protected int serverPort;
    protected ServerSocket serverSocket = null;
    protected boolean isStopped = false;
    protected Thread runningThread = null;

    public MultiThreadedServer(int port) {
        this.serverPort = port;
    }

    @Override
    public void run() {
        
        synchronized (this) {
            this.runningThread = Thread.currentThread();
        }
        
        openServerSocket(); // Start Server On Port
    
        // Accepter
        while (!isStopped()) {
            Socket clientSocket = null;
            
            try {
                clientSocket = this.serverSocket.accept();
            } catch (IOException e) {
                
                // Check If Server Is Stopped
                if (isStopped()) {
                    System.out.println("Server Stopped.");
                    return;
                }

                throw new RuntimeException("Error accepting client connection", e); // Throw Exception
            }

            new Thread(new WorkerRunnable(clientSocket)).start(); // Start Client Handler
        }
        
        System.out.println("Server Stopped."); // Server Stopped Message
        
    }

    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    public synchronized void stop() {
        this.isStopped = true;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error Closing Server", e);
        }
    }

    private void openServerSocket() {
        try {
            this.serverSocket = new ServerSocket(this.serverPort);
        } catch (IOException e) {
            throw new RuntimeException("Cannot Start Server At Port: " + this.serverPort, e);
        }
    }
}
