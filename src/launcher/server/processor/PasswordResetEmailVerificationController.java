package launcher.server.processor;

/**
 *
 * @author Hyper
 */
public class PasswordResetEmailVerificationController extends Thread {

    private String Email;
    private String Code;
    private String IP;
    private String OSName;
    private String PCName;

    public PasswordResetEmailVerificationController(String Email, String Code, String IP, String OSName, String PCName) {
        this.Email = Email;
        this.Code = Code;
        this.IP = IP;
        this.OSName = OSName;
        this.PCName = PCName;
    }

    @Override
    public void run() {
        new EmailVerification(getEmail(), getCode(), getIP(), getOSName(), getPCName()).PasswordReset();  // Send Verification Code For Password Reset
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

}
