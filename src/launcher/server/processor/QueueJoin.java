package launcher.server.processor;

import launcher.server.ServerJoinController;

/**
 *
 * @author Hyper
 */
public class QueueJoin extends Thread {

    private String PlayerName;
    private String IP;
    private String ServerIP;
    private String ServerPort;
    private String JoinerID;
    private int ConnectionTimeout;
    private int QueueNumber;

    public QueueJoin(String PlayerName, String IP, String JoinerID, String ServerIP, String ServerPort, int ConnectionTimeout, int QueueNumber) {
        this.PlayerName = PlayerName;
        this.IP = IP;
        this.ServerIP = ServerIP;
        this.ServerPort = ServerPort;
        this.JoinerID = JoinerID;
        this.ConnectionTimeout = ConnectionTimeout;
        this.QueueNumber = QueueNumber;
        start();
    }

    @Override
    public void run() {
        while (true) {

            if (getConnectionTimeout() == 0) {
                ServerJoinController.removePlayerByJoinerID(getJoinerID(), getServerIP(), getServerPort(), 2);
                return;
            }

            setConnectionTimeout();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
            }
        }
    }

    public String getServerIPPORT() {
        return ServerIP + ":" + ServerPort;
    }

    public String getQueueNumber() {
        return String.valueOf(QueueNumber);
    }

    public void setQueueNumber(int QueueNumber) {
        if (this.QueueNumber - QueueNumber >= 0) {
            this.QueueNumber = this.QueueNumber - QueueNumber;
        }
    }

    public String getPlayerName() {
        return PlayerName;
    }

    public String getIP() {
        return IP;
    }

    public String getServerIP() {
        return ServerIP;
    }

    public String getServerPort() {
        return ServerPort;
    }

    public String getJoinerID() {
        return JoinerID;
    }

    public int getConnectionTimeout() {
        return ConnectionTimeout;
    }

    public void setConnectionTimeout() {
        this.ConnectionTimeout--;
    }

    public void resetConnectionTimeout(int Time) {
        this.ConnectionTimeout = Time;
    }
}
