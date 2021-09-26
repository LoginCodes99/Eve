package Player;

import java.util.HashMap;
import java.util.Map;

public class Player {
    private String playerName;
    private String playerId;
    private Map<String, String> playerGuilds;

    public Player(String playerName, String playerId, String playerGuildId, String playerNickname, String tokeepError) {
        this.playerId = playerId;
        this.playerName = playerName;
        playerGuilds = new HashMap<>();
        playerGuilds.put(playerGuildId, playerNickname);
    }

    public String getPlayerId() { return this.playerId; }
    public String getPlayerName() { return this.playerName; }
    public String getPlayerNickname(String guildId) { return this.playerGuilds.get(guildId); }


}
