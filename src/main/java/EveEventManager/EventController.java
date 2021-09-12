package EveEventManager;

import Enums.GamesEnum;
import Enums.TimezonesEnum;
import GameEvents.GameEvent;
import GameEvents.ValorantGameEvent;
import net.dv8tion.jda.api.JDA;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class EventController {
    JDA jda;
    Map<String, GameEvent> listOfEvents = new HashMap<String, GameEvent>();
    Map<String, String> messageIdToCreator = new HashMap<String, String>();
    Map<String, GameEvent> editingEvents = new HashMap<String, GameEvent>();

    private static final Logger logger = LogManager.getLogger();

    public EventController(JDA jda) {
        this.jda = jda;
    }

    public void startEventCreation(GamePlayer gameCreator, GamesEnum gamesEnum, GameTime gameTime) {
        switch (gamesEnum) {
            case VALORANT:
                startValorantEvent(gameCreator, gamesEnum, gameTime);
                break;
            case GTAV:
                startGtavEvent();
                break;
            default:
                logger.error("GameEnum Error while starting event");
        }
    }

    public void startValorantEvent(GamePlayer gameCreator, GamesEnum game, GameTime gameTime) {
        ValorantGameEvent gameEvent = new ValorantGameEvent(gameCreator, game, gameTime);
        editingEvents.put(gameCreator.getCreatorId(), gameEvent);
    }

    public void startGtavEvent() {

    }

    public GameEvent finishEventCreation(String gameCreatorId) {
        GameEvent confirmedEvent = editingEvents.get(gameCreatorId);
        editingEvents.remove(gameCreatorId);

        listOfEvents.put(gameCreatorId, confirmedEvent);
        confirmedEvent.setGameEventEmbed();

        return confirmedEvent;
    }

    public void mapCreatorToMessage(String messageId, String gameCreatorId) {
        messageIdToCreator.put(messageId, gameCreatorId);
    }

    public void addPlayerToGameEvent(String messageId, GamePlayer gamePlayer) {
        GameEvent gameEvent = listOfEvents.get(messageIdToCreator.get(messageId));

        if(gameEvent.canAddPlayer()) {
            gameEvent.addPlayer(gamePlayer);
        }
    }
}
