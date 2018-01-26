package launcher.server.processor;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Hyper
 */
public class BanFalseIP extends Thread {

    private String IP;
    private String Data;

    public BanFalseIP(String IP, String Data) {
        this.IP = IP;
        this.Data = Data;
    }

    @Override
    public void run() {

        try {
            
            Runtime BlockIP = Runtime.getRuntime();
            BlockIP.exec("sudo iptables -t mangle -A Banner -s " + getIP() + " -j DROP");
            
            BufferedWriter bw = null;
            FileWriter fw = null;
            
            try {
                
                fw = new FileWriter("/home/admin/Launcher/Master_Server/Master_Server_Log/Banned_IP.log");
                bw = new BufferedWriter(fw, 51200);
                bw.write(getData() + getIP());
                
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                
                try {
                    
                    if (bw != null) {
                        bw.close();
                    }
                    
                    if (fw != null) {
                        fw.close();
                    }
                    
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            
        } catch (IOException ex) {
            Logger.getLogger(BanFalseIP.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public String getIP() {
        return IP;
    }

    public String getData() {
        return Data;
    }

}
