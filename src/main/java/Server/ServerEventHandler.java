package Server;

import MongoDB.ServerDB;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.hooks.EventListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class ServerEventHandler implements EventListener {

    private static final Logger logger = LogManager.getLogger();

    @Override
    public void onEvent(@NotNull GenericEvent event) {
        switch (event.getClass().getName()) {
            case "GuildJoinEvent":
                addServer(((GuildJoinEvent) event).getGuild());
                break;
            case "GuildLeaveEvent":
                deleteServer(((GuildLeaveEvent) event).getGuild());
                break;
        }
    }

    public void addServer(Guild g) {
        Server newServer = new Server(g.getId(), g.getName());

        //Creating events channel
        try {
            g.createTextChannel("events").queue();
            newServer.setEventChannel(g.getTextChannelsByName("events", true).get(0).getId(), "events");
        } catch(InsufficientPermissionException e) {
            logger.error("Eve does not have the correct permissions in {}", g.getName());
            return;
        }

        //Adding server to DB
        ServerDB.insertOneServer(newServer);
    }

    public void deleteServer(Guild g) {
        if(g == null) {
            logger.error("Error removing server");
            return;
        }

        //TODO: Remove all players with only this server in their guild list
        //Removing all players with only this server in their guild list

        //Removing server from DB
        ServerDB.deleteOneServer(g.getId());
    }
}
