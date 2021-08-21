package Server;

import EveEvents.Event;
import EveEvents.EventController;

import java.util.ArrayList;
import java.util.List;

public class Server {
    String guildId;
    String eventChannelName;
    EventController eventController;

    public Server(String guildId, String eventChannelName) {
        this.guildId = guildId;
        this.eventChannelName = eventChannelName;
    }

    public void setEventController(EventController eventController) {
        this.eventController = eventController;
    }

    public EventController getEventController() {
        return eventController;
    }
}
