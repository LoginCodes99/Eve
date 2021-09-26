package MongoDB;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ConnectionTest {

    private static final Logger logger = LogManager.getLogger();

    private MongoDatabase eveDatabase;

    ConnectionTest() {
        String connectionString = System.getProperty("mongodb");
        MongoClient mongoClient = MongoClients.create(connectionString);
        eveDatabase = mongoClient.getDatabase("Eve");
    }

    @Test
    @Order(1)
    public void testConnectToEveDb() {
        String connectionString = System.getProperty("mongodb");
        assertNotNull(connectionString, "Connection String is null");

        MongoClient mongoClient = null;
        try {
            mongoClient = MongoClients.create(connectionString);
        } catch(IllegalArgumentException e) {
            Assertions.fail("IllegalArgumentException caught");
            return;
        }
        assertNotNull(mongoClient, "MongoClient is null");

        MongoDatabase eveDatabase = mongoClient.getDatabase("Eve");
        assertNotNull(eveDatabase, "Eve Database is null");
    }

    @Test
    public void testCollectionsExists() {
        //Populating collections list
        List<String> collections = new ArrayList<>();
        collections.add("Servers");
        collections.add("Events");

        for(String collectionName : collections) {
            assertTrue(eveDatabase.listCollectionNames().into(new ArrayList<String>()).contains(collectionName), "Missing the collection: " + collectionName);
        }
    }
}
