package launcher.server;

import launcher.server.processor.PlayerIPRecord;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Hyper
 */
public class PlayerIPRecordController {

    public static List<PlayerIPRecord> playerIPRecords = new ArrayList<PlayerIPRecord>();

    /**
     *
     * @param PlayerIP Player IP
     * @param ServerIP Server IP
     * @param ServerPort Server Port
     * @param PlayerName Player Name
     */
    public static void addPlayerToIPRecord(String PlayerIP, String ServerIP, String ServerPort, String PlayerName) {
        playerIPRecords.add(new PlayerIPRecord(PlayerName, PlayerIP, ServerIP, ServerPort));
    }

    /**
     *
     * @param IP Server IP
     * @param PORT Server Port
     * @param PlayerName Player Name
     * @return
     */
    public static PlayerIPRecord getPlayerIPByServerNameIPandPort(String IP, String PORT, String PlayerName) {
        for (PlayerIPRecord Data : playerIPRecords) {
            if (Data.getServerIPPort().equals(IP + ":" + PORT) && Data.getPlayerName().equals(PlayerName)) {
                return Data;
            }
        }
        return null;
    }

    /**
     * Remove Player from IP Record
     * @param ServerIP Server IP
     * @param ServerPort Server Port
     * @param PlayerIP Player IP
     * @param PlayerName Player Name
     */
    public static void removePlayerFromIPRecord(String ServerIP, String ServerPort, String PlayerIP, String PlayerName) {
        PlayerIPRecord Player = null;

        for (PlayerIPRecord Data : playerIPRecords) {
            if (Data.getServerIPPort().equals(ServerIP + ":" + ServerPort) && Data.getPlayerName().equals(PlayerName) && Data.getPlayerIP().equals(PlayerIP)) {
                Player = Data;
                break;
            }
        }

        if (Player != null) {
            playerIPRecords.remove(Player);
        }
    }
}
