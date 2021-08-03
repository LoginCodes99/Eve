import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;

import java.util.List;

public class EvePrecheck {

    public void checkGuildForEventChannel(JDA jda) {
        //Getting the list of guilds Eve is in
        System.out.println("Checking guilds for the events channel");
        List<Guild> listOfGuilds = jda.getGuilds();

        //Each guild is checked for the "events" channel. If it is not present, it is created
        for (Guild g: listOfGuilds) {
            System.out.println("Checking " + g.getName());
            System.out.println(g.getTextChannelsByName("events", true));
            if(g.getTextChannelsByName("events", true).size() == 0) {
                System.out.println(g.getName() + " didn't have the event channel. Creating one now.... \n");
                try {
                    g.createTextChannel("events").queue();
                } catch(InsufficientPermissionException e) {
                    System.out.println("Eve does not have the correct permissions in " + g.getName());
                }
            }
        }
    }
}
