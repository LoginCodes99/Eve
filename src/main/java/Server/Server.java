package Server;

import EveEventManager.EventController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Server {
    private String guildId;
    private String eventChannelName;
    private String eventChannelId;
    private EventController eventController;

    private static final Logger logger = LogManager.getLogger();

    public Server(String guildId, String eventChannelName, String eventChannelId) {
        this.guildId = guildId;
        this.eventChannelName = eventChannelName;
        this.eventChannelId = eventChannelId;
        logger.info("Server created with guildID: " + this.guildId + " and eventChannelName: "  + this.eventChannelName + ":" + this.eventChannelId);
    }

    public void setEventController(EventController eventController) {
        this.eventController = eventController;
    }

    public EventController getEventController() {
        return this.eventController;
    }
    public String getGuildId() { return this.guildId; }
    public String getEventChannelName() { return this.eventChannelName; }
    public String getEventChannelId() { return this.eventChannelId; }
}
