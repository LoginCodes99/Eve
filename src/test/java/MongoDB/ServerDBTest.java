package MongoDB;

import Server.Server;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ServerDBTest {

    private static final Logger logger = LogManager.getLogger();

    private MongoDatabase eveDatabase;

    ServerDBTest() {
        String connectionString = System.getProperty("mongodb");
        MongoClient mongoClient = MongoClients.create(connectionString);
        eveDatabase = mongoClient.getDatabase("Eve");
        ServerDB.setServers(eveDatabase.getCollection("Servers"));
    }

    @AfterEach
    public void deleteTestObjects() {
        ServerDB.deleteOneServer("69420test");
    }

    @Test
    public void testInsertOneServer() {
        Server testServer = new Server("69420test", "Test Server", "Test", "69420test");

        try {
            ServerDB.insertOneServer(testServer);
            Assertions.assertNotNull(ServerDB.findOneServer("69420test"));
            assertServerData(testServer, ServerDB.findOneServer("69420test"));
        } catch(MongoException e) {
            if(e.getMessage().contains("E11000 duplicate key error")) {
                Assertions.fail("Test object still present in Server collection. Error with deleteTestObjects()");
            }
            Assertions.fail("Failed to insert server entity due to MongoException: " + e.getMessage());
        }
    }

    @Test
    public void testDeleteOneServer() {
        Server testServer = new Server("69420test", "Test Server", "Test", "69420test");

        ServerDB.insertOneServer(testServer);
        ServerDB.deleteOneServer("69420test");
        Assertions.assertNull(ServerDB.findOneServer("69420test"), "Test server was still present when an attempt to delete was made");
    }

    @Test
    public void updateOneServer() {
        Server testServer = new Server("69420test", "Test Server", "Test", "69420test");

        ServerDB.insertOneServer(testServer);
        testServer.setGuildName("Test Server but Updated");
        ServerDB.updateOneServer(testServer);
        assertServerData(testServer, ServerDB.findOneServer("69420test"));
    }

    private void assertServerData(Server expectedServer, Server actualServer) {
        Assertions.assertEquals(expectedServer.getGuildId(), actualServer.getGuildId(), "Guild Id did not match");
        Assertions.assertEquals(expectedServer.getGuildName(), actualServer.getGuildName(), "Guild name did not match");
        Assertions.assertEquals(expectedServer.getEventChannelId(), actualServer.getEventChannelId(), "Guild EventChannelId did not match");
        Assertions.assertEquals(expectedServer.getEventChannelName(), actualServer.getEventChannelName(), "Guild EventChannelName did not match");
    }
}
