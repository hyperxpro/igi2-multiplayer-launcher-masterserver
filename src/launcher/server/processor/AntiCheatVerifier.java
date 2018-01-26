package launcher.server.processor;

/**
 *
 * @author Hyper
 */
public class AntiCheatVerifier {

    private String PlayerName;
    private String ServerIP;
    private String ServerPORT;
    private String JoinerID;
    private boolean Verified;

    public AntiCheatVerifier(String PlayerName, String ServerIP, String ServerPORT, String JoinerID, boolean Verified) {
        this.PlayerName = PlayerName;
        this.ServerIP = ServerIP;
        this.ServerPORT = ServerPORT;
        this.JoinerID = JoinerID;
        this.Verified = Verified;
    }

    public String getServerIPPort() {
        return ServerIP + ":" + ServerPORT;
    }

    public String getPlayerName() {
        return PlayerName;
    }

    public String getServerIP() {
        return ServerIP;
    }

    public String getServerPORT() {
        return ServerPORT;
    }

    public String getJoinerID() {
        return JoinerID;
    }

    public boolean isVerified() {
        return Verified;
    }

    public void setVerified(boolean Verified) {
        this.Verified = Verified;
    }

}
