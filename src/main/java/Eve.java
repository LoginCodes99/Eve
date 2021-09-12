import EveEvents.EventController;
import Server.Server;
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


public class Eve implements EventListener {
    JDA jda;
    Map<String, Server> serverList = new ConcurrentHashMap<>();
    Map<String, Server> userToServerList = new ConcurrentHashMap<>();

    private static final Logger logger = LogManager.getLogger();

    public Eve(String[] args) {
        logger.trace("Starting Eve the event bot");
        MessageListener messageListener = new MessageListener(serverList, userToServerList);
        JDABuilder builder = JDABuilder.createLight(args[0],
                GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES, GatewayIntent.GUILD_MEMBERS)
                .addEventListeners(this)
                .addEventListeners(messageListener)
                .setActivity(Activity.listening("Type !eve"));

        try {
            jda = builder.build();
            messageListener.setJDA(jda);
        } catch (Exception e) {
            logger.error("Failed logging Eve into Discord");
        }
    }

    @Override
    public void onEvent(@NotNull GenericEvent event) {
        if(event instanceof ReadyEvent) {
            //Checking for the "events" channel in every guild that Eve is in
            initializeServers();
            logger.info("Eve is ready and listening");
        } else if(event instanceof GuildJoinEvent) {
            addServer(((GuildJoinEvent) event).getGuild());
        } else if(event instanceof GuildLeaveEvent) {
            removeServer(((GuildLeaveEvent) event).getGuild());
        }
    }

    public void initializeServers() {
        logger.info("Initializing Server Objects");
        for(Guild g : jda.getGuilds()) {
            addServer(g);
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

    public void addServer(Guild g) {
        serverList.put(g.getId(), new Server(g.getId(), "events"));
        serverList.get(g.getId()).setEventController(new EventController(jda));

        logger.info("Adding {}:{} to serverList", g.getName(), g.getId());
        for(Member m : g.getMembers()) {
            userToServerList.put(m.getId(), serverList.get(g.getId()));
        }

        checkGuildForEventChannel(g);
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
