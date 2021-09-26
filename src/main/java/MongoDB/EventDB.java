package MongoDB;

import GameEvents.GameEvent;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;

public class EventDB {

    private static final Logger logger = LogManager.getLogger();

    static MongoCollection<Document> events;

    public static void setEvents(MongoCollection<Document> eventsCollection) {
        events = eventsCollection;
    }

    public static void insertOneEvent(GameEvent gameEventEntity) {
        try {
            events.insertOne(generateNewEvent(gameEventEntity));
        } catch(MongoException e) {
            if(e.getMessage().contains("E11000 duplicate key error")) {
                logger.error("Error while trying to insert event: Duplicate key " + gameEventEntity.g);
            } else {
                logger.error("Error while trying to insert event: " + e.getMessage());
            }
        }
    }

    private static Document generateNewEvent(GameEvent gameEventEntity) {
        return new Document("_id", gameEventEntity.getEventId()).append("game_name", gameEventEntity.getGameEnum().toString())
                           .append("gameEventCreator_id", gameEventEntity.getGameCreator().getCreatorId())
                           .append("gameEvent", serverEntity.getEventChannelName());
    }
}
