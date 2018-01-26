package launcher.server.processor;

/**
 *
 * @author Hyper
 */
public class PlayerIPRecord {

    private String PlayerName;
    private String PlayerIP;
    private String ServerIP;
    private String ServerPort;

    public PlayerIPRecord(String PlayerName, String PlayerIP, String ServerIP, String ServerPort) {
        this.PlayerName = PlayerName;
        this.PlayerIP = PlayerIP;
        this.ServerIP = ServerIP;
        this.ServerPort = ServerPort;
    }
    
    public String getServerIPPort(){
        return ServerIP + ":" + ServerPort;
    }

    public String getPlayerName() {
        return PlayerName;
    }

    public String getPlayerIP() {
        return PlayerIP;
    }

    public String getServerIP() {
        return ServerIP;
    }

    public String getServerPort() {
        return ServerPort;
    }

}
