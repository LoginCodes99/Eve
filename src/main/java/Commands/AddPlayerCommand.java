package Commands;

import EveEventManager.EventController;
import Player.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AddPlayerCommand extends Command{

    private static final Logger logger = LogManager.getLogger();

    public void addPlayerToGameEvent(EventController eventController, String messageId, String playerNickname, String playerName, String playerId, String playerGuildId) {
        eventController.addPlayerToGameEvent(messageId, new Player(playerNickname, playerName, playerId, playerGuildId));
    }
}
