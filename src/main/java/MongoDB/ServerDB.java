package MongoDB;

import Server.Server;
import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;

public class ServerDB {

    private static final Logger logger = LogManager.getLogger();

    static MongoCollection<Document> servers;

    public static void setServers(MongoCollection<Document> serversCollection) {
        servers = serversCollection;
    }

    public static void insertOneServer(Server serverEntity) {
        try {
            servers.insertOne(generateNewServer(serverEntity));
        } catch(MongoException e) {
            if(e.getMessage().contains("E11000 duplicate key error")) {
                logger.error("Error while trying to insert server: Duplicate key " + serverEntity.getGuildId());
            } else {
                logger.error("Error while trying to insert server: " + e.getMessage());
            }
        }
    }

    public static Server findOneServer(String serverId) {
        return documentToServer(servers.find(new BasicDBObject("_id", serverId)).first());
    }

    public static void deleteOneServer(String serverId) {
        servers.deleteOne(new BasicDBObject("_id", serverId));
    }

    public static void updateOneServer(Server server) {
        servers.updateOne(new BasicDBObject("_id", server.getGuildId()), updateExistingServer(server));
    }

    private static Document generateNewServer(Server serverEntity) {
        return new Document("_id", serverEntity.getGuildId()).append("server_name", serverEntity.getGuildName())
                                                             .append("eventChannel_id", serverEntity.getEventChannelId())
                                                             .append("eventChannel_name", serverEntity.getEventChannelName());
    }

    private static BasicDBObject updateExistingServer(Server serverEntity) {
        BasicDBObject updateObject = new BasicDBObject();
        updateObject.put("$set", new Document("server_name", serverEntity.getGuildName())
                                             .append("eventChannel_id", serverEntity.getEventChannelId())
                                             .append("eventChannel_name", serverEntity.getEventChannelName()));
        return updateObject;
    }

    private static Server documentToServer(Document serverDocument) {
        if(serverDocument == null) { return null; }

        return new Server(serverDocument.getString("_id"),
                          serverDocument.getString("server_name"),
                          serverDocument.getString("eventChannel_name"),
                          serverDocument.getString("eventChannel_id"));
    }
}
