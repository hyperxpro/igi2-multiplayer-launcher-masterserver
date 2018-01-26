package launcher.server.processor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import launcher.server.DataProcessor;

/**
 *
 * @author Hyper
 */
public class ServerListUpdate extends Thread {

    @Override
    public void run() {

        while (true) {

            FileOutputStream writer_flush = null;
            try {
                // Flush File
                writer_flush = new FileOutputStream(new File("/var/www/igi2_master_server/igi2/masterserver/igi2_masterserver/servers/igi2-masterserver/servers/launcher_9-6/igi2_servers_igi2.LAUNCHER"));
                writer_flush.write(("").getBytes());
                writer_flush.close();

                // Print Log
                DataProcessor.echo_console("Master Server - Server Lists Updated Successfully...", 7);

                // Wait 5 Minutes
                Thread.sleep(300000);
            } catch (IOException ex) {
                Logger.getLogger(ServerListUpdate.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(ServerListUpdate.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    if (writer_flush != null) {
                        writer_flush.close();
                    }
                } catch (IOException ex) {
                    Logger.getLogger(ServerListUpdate.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
