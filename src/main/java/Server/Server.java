package Server;

import EveEventManager.EventController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Server {
    private String guildId;
    private String guildName;
    private String eventChannelName;
    private String eventChannelId;
    private EventController eventController;

    private static final Logger logger = LogManager.getLogger();

    public Server(String guildId, String guildName) {
        this.guildId = guildId;
        this.guildName = guildName;
        logger.info("Server created: " + this.guildName + ":" + this.guildId);
    }

    public void setEventController(EventController eventController) {
        this.eventController = eventController;
    }
    public void setGuildName(String guildName) { this.guildName = guildName; }

    public EventController getEventController() {
        return this.eventController;
    }
    public String getGuildId() { return this.guildId; }
    public String getGuildName() { return this.guildName; }
    public String getEventChannelName() { return this.eventChannelName; }
    public String getEventChannelId() { return this.eventChannelId; }

    public void setEventChannel(String eventChannelId, String eventChannelName) {
        this.eventChannelId = eventChannelId;
        this.eventChannelName = eventChannelName;
    }
}
