package MongoDB;

import Player.Player;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;

public class PlayerDB {

    private static final Logger logger = LogManager.getLogger();

    static MongoCollection<Document> players;

    public static void setPlayers(MongoCollection<Document> playersCollection) {
        players = playersCollection;
    }

    public static void insertOnePlayer(Player playerEntity) {
        try {
            players.insertOne(generateNewPlayer(playerEntity));
        } catch(MongoException e) {
            if(e.getMessage().contains("E11000 duplicate key error")) {
                logger.error("Error while trying to insert player: Duplicate key " + playerEntity.getPlayerId());
            } else {
                logger.error("Error while trying to insert player: " + e.getMessage());
            }
        }
    }

    private static Document generateNewPlayer(Player playerEntity) {
        return new Document("_id", playerEntity.getPlayerId()).append("player_name", playerEntity.getPlayerName())
                .append("eventChannel_id", serverEntity.getEventChannelId())
                .append("eventChannel_name", serverEntity.getEventChannelName());
    }
}
