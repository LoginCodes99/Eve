import EveEventManager.EventController;
import MongoDB.Connection;
import MongoDB.ServerDB;
import Server.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//TODO: initialize all players and servers making sure they exist in the DB, if not create them
//TODO: move event handling out of this class

public class Eve implements EventListener {
    JDA jda;
    Connection connection;
    Map<String, Server> serverList = new ConcurrentHashMap<>();
    Map<String, Server> userToServerList = new ConcurrentHashMap<>();

    private static final Logger logger = LogManager.getLogger();

    public Eve(String[] args) {
        logger.trace("Starting Eve the event bot");
        MessageListener messageListener = new MessageListener(serverList, userToServerList);
        ServerEventHandler serverEventHandler = new ServerEventHandler();
        JDABuilder builder = JDABuilder.createLight(System.getProperty("token"),
                GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGE_REACTIONS)
                .addEventListeners(this)
                .addEventListeners(serverEventHandler)
                .addEventListeners(messageListener)
                .setActivity(Activity.listening("Type !eve"));

        try {
            jda = builder.build();
            messageListener.setJDA(jda);
        } catch (Exception e) {
            logger.error("Failed logging Eve into Discord");
        }

        //MongoDB Connection
        connection = new Connection(System.getProperty("mongodb"));
        ServerDB.setServers(connection.getServerCollection());
        initializeServers();
    }

    @Override
    public void onEvent(@NotNull GenericEvent event) {
        if(event instanceof ReadyEvent) {
            //Checking for the "events" channel in every guild that Eve is in
            //initializeServers();
            logger.info("Eve is ready and listening");
        }
    }

    public void initializeServers() {
        logger.info("Initializing Server Objects");
        for(Guild g : jda.getGuilds()) {
            addServer(g);
        }

        for (Server server : serverList.values()) {
            ServerDB.insertOneServer(server);
        }
    }

    public void checkGuildForEventChannel(Guild g) {
        //Each guild is checked for the "events" channel. If it is not present, it is created
        logger.info("Checking '{}' for event channel", g.getName());
        if(g.getTextChannelsByName("events", true).size() == 0) {
            logger.info("{} didn't have the event channel. Creating one now... \n", g.getName());
            try {
                g.createTextChannel("events").queue();
            } catch(InsufficientPermissionException e) {
                logger.error("Eve does not have the correct permissions in {}", g.getName());
            }
        } else {
            logger.info("{} has the event channel", g.getName());
        }
    }


    public void removeServer(Guild g) {
        if(g == null) {
            logger.error("Error removing server");
            return;
        }

        for(Member m : g.getMembers()) {
            userToServerList.remove(m.getId());
        }

        logger.info("Removing {} from serverList", g.getName());
        serverList.remove(g.getId());
    }

    public static void main(String[] args) {
        new Eve(args);
    }
}
