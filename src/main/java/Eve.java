import EveEvents.EventController;
import Server.Server;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Eve implements EventListener {
    JDA jda;
    Map<String, Server> serverList = new HashMap<>();
    Map<String, Server> userToServerList = new HashMap<>();

    private static final Logger logger = LogManager.getLogger();

    public Eve(String[] args) {
        MessageListener messageListener = new MessageListener(serverList, userToServerList);
        JDABuilder builder = JDABuilder.createLight(args[0],
                GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES, GatewayIntent.GUILD_MEMBERS)
                .addEventListeners(this)
                .addEventListeners(messageListener)
                .setActivity(Activity.listening("Type !eve"));

        try {
            jda = builder.build();
            initializeServers();
            messageListener.setJDA(jda);
        } catch (Exception e) {
            logger.error("Failed logging Eve into Discord");
        }
    }

    @Override
    public void onEvent(@NotNull GenericEvent event) {
        if(event instanceof ReadyEvent) {
            //Checking for the "events" channel in every guild that Eve is in
            checkGuildForEventChannel();
        }
    }

    public void initializeServers() {
        for(Guild g : jda.getGuilds()) {
            //Setting up the serverList
            serverList.put(g.getId(), new Server(g.getId(), "events"));
            serverList.get(g.getId()).setEventController(new EventController(jda));

            //Setting up the userToServerList
            for(Member m : g.getMembers()) {
                userToServerList.put(m.getId(), serverList.get(g.getId()));
            }
        }
    }

    public void checkGuildForEventChannel() {
        //Getting the list of guilds Eve is in
        //System.out.println("Checking guilds for the events channel");
        logger.debug("Checking guild for the event channel");
        List<Guild> listOfGuilds = jda.getGuilds();

        //Each guild is checked for the "events" channel. If it is not present, it is created
        for (Guild g: listOfGuilds) {
            //System.out.println("Checking " + g.getName());
            logger.debug("Checking {}", g.getName());
            if(g.getTextChannelsByName("events", true).size() == 0) {
                //System.out.println(g.getName() + " didn't have the event channel. Creating one now.... \n");
                logger.debug("{} didn't have the event channel. Creating one now... \n", g.getName());
                try {
                    g.createTextChannel("events").queue();
                } catch(InsufficientPermissionException e) {
                    //System.out.println("Eve does not have the correct permissions in " + g.getName());
                    logger.error("Eve does not have the correct permissions in {}", g.getName());
                }
            }
        }
    }

    public static void main(String[] args) {
        new Eve(args);
    }
}
