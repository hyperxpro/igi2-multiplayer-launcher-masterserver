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
