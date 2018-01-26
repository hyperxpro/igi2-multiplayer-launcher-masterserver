package launcher.server.processor;

/**
 *
 * @author Hyper
 */
public class ServerQueue {

    private String ServerIP;
    private String ServerPORT;
    private int QueueNumber;

    public ServerQueue(String ServerIP, String ServerPORT, int QueueNumber) {
        this.ServerIP = ServerIP;
        this.ServerPORT = ServerPORT;
        this.QueueNumber = QueueNumber;
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

    public String getQueueNumber() {
        return String.valueOf(QueueNumber);
    }

    public void addQueueNumber(int Number) {
        this.QueueNumber = this.QueueNumber + Number;
    }

    public void setQueueNumber(int QueueNumber) {
        if (this.QueueNumber - QueueNumber >= 0) {
            this.QueueNumber = this.QueueNumber - QueueNumber;
        }
    }
}
