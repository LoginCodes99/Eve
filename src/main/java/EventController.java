import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

public class EventController {
    JDA jda;
    Map<String, Event> listOfEvents = new HashMap<String, Event>();
    Map<String, String> messageIdToCreator = new HashMap<String, String>();
    Map<String, Event> editingEvents = new HashMap<String, Event>();

    public void setJDA(JDA jda) {
        this.jda = jda;
    }

    public void createEvent(String creatorId, String guildId) {
        //String userNickname = Objects.requireNonNull(Objects.requireNonNull(jda.getGuildById(guildId)).getMemberById(creatorId).getNickname());
        Guild g = jda.getGuildById(guildId);
        Member m;
        String userNickname = "";
        String rawNickname = "";

        if(g != null) {
            System.out.println(creatorId);
            try {
                m = g.retrieveMemberById(creatorId).submit().get();
            } catch (Exception e) {
                System.out.println("Error retrieving member");
                return;
            }

        } else {
            System.out.println("Guild is null");
            return;
        }

        if(m != null) {
            rawNickname = m.getNickname();
            userNickname = m.getNickname().replaceAll("[^a-zA-Z0-9]", " ").trim();
            System.out.println(userNickname + " has tried to create an event.");
        } else {
            System.out.println("Member is null");
            return;
        }

        //User cannot already be creating an event
        //TO-DO: Create command to cancel all events being edited by user
        if(editingEvents.containsKey(creatorId)) {
            final String eveCreateResponse = "You are already editing an event!";
            User eventCreator = User.fromId(creatorId);
            eventCreator.openPrivateChannel().flatMap(channel -> channel.sendMessage(eveCreateResponse)).queue();
            return;
        }

        final String eveCreateResponse = "Hello " + userNickname + "\n" + "What event would you like to schedule?";
        User eventCreator;
        try {
            eventCreator = jda.retrieveUserById(creatorId).submit().get();
            eventCreator.openPrivateChannel().flatMap(channel -> channel.sendMessage(eveCreateResponse)).queue();
        } catch (Exception e) {
            System.out.println("Error retrieving user");
            return;
        }

        //Create the embed of the current supported games
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("List of Events:");
        eb.setDescription("Among Us -- !eve amongus \n" +
                          "Valorant -- !eve valorant \n" +
                          "GTA V    -- !eve gta\n" +
                          "Other    -- !eve other\n\n" +
                          "**Type the command listed above to start creating your event**");
        eventCreator.openPrivateChannel().flatMap(channel -> channel.sendMessageEmbeds(eb.build())).queue();

        Event newEvent = new Event(rawNickname, creatorId, guildId);
        editingEvents.put(creatorId, newEvent);
    }

    public boolean isUserEditingEvent(String userId) {
        return editingEvents.containsKey(userId);
    }

    public Event getEventByAuthor(String creatorId){
        return editingEvents.get(creatorId);
    }

    public void finishEventEditing(String creatorId) {
        Event e = editingEvents.get(creatorId);
        editingEvents.remove(creatorId);
        listOfEvents.put(creatorId, e);
    }

    public Event getEventByMessageId(String messageId) {
        return listOfEvents.get(messageIdToCreator.get(messageId));
    }

    public void setEventMessageId(String eventCreator, String messageId) {
        getEventByAuthor(eventCreator).setMessageId(messageId);
        messageIdToCreator.put(messageId, eventCreator);
    }
}
