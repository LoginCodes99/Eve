package Server;

import EveEvents.Event;
import EveEvents.EventController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class Server {
    String guildId;
    String eventChannelName;
    EventController eventController;

    private static final Logger logger = LogManager.getLogger();

    public Server(String guildId, String eventChannelName) {
        this.guildId = guildId;
        this.eventChannelName = eventChannelName;
        logger.info("Server created with guildID: " + this.guildId + " and eventChannelName: "  + this.eventChannelName);
    }

    public void setEventController(EventController eventController) {
        this.eventController = eventController;
    }

    public EventController getEventController() {
        return eventController;
    }
}
