package MongoDB;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class Connection {
    MongoDatabase eveDatabase;
    MongoCollection<Document> serverCollection;

    private static final Logger logger = LogManager.getLogger();

    public Connection(String connectionString) {
        logger.info("Creating a new connection");

        MongoClient mongoClient = MongoClients.create(connectionString);

        eveDatabase = mongoClient.getDatabase("Eve");

        serverCollection = eveDatabase.getCollection("Servers");
    }

    public MongoCollection<Document> getServerCollection() {
        logger.info("Getting server collection");
        return serverCollection;
    }
}
