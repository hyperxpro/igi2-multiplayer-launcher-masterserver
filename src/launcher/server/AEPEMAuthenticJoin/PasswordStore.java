package launcher.server.AEPEMAuthenticJoin;

/**
 *
 * @author Hyper
 */
public class PasswordStore {

    private String ServerIP;
    private String ServerPORT;
    private String ServerPASSWORD;
    private boolean TakePassword;
    private String ServerID;

    public PasswordStore(String ServerIP, String ServerPORT, String ServerPASSWORD, boolean TakePassword, String ServerID) {
        this.ServerIP = ServerIP;
        this.ServerPORT = ServerPORT;
        this.ServerPASSWORD = ServerPASSWORD;
        this.TakePassword = TakePassword;
        this.ServerID = ServerID;
    }

    public String getServerID() {
        return ServerID;
    }

    public boolean isTakePassword() {
        return TakePassword;
    }

    public void setTakePassword(boolean TakePassword) {
        this.TakePassword = TakePassword;
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

    public String getServerPASSWORD() {
        return ServerPASSWORD;
    }

    public void setServerPASSWORD(String ServerPASSWORD) {
        this.ServerPASSWORD = ServerPASSWORD;
    }

}
