import java.util.ArrayList;
import java.util.List;

public class Event {
    String creatorNickname;
    String creatorId;
    String guildId;
    String messageId;
    String game = "";
    EventTime eventTime;
    List<String> eventPlayers;

    public Event(String nickname, String creatorId, String guildId) {
        this.creatorNickname = nickname;
        this.creatorId = creatorId;
        this.guildId = guildId;
        eventPlayers = new ArrayList<String>();
        eventPlayers.add(creatorNickname);
    }

    public void setGame(String game) {
        System.out.println("Set game to " + game);
        this.game = game;
    }

    public void setTime(String time, String ampm, String timezone) {
        System.out.println("Set time to " + time + " " + ampm + " " + timezone);
        eventTime = new EventTime(time, ampm, timezone);
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getGame() {
        switch (game) {
            case "amongus":
                return "Among us";
            default:
                return "Error";
        }
    }

    public String getTime() {
        return eventTime.time + " " + eventTime.ampm + " " + eventTime.timezone;
    }

    public String getCreator() {
        return creatorNickname;
    }

    public String getLimit() {
        return "10";
    }

    public String getPlayers() {
        String playerString = "";
        for(String player : eventPlayers) {
            playerString = playerString.concat(player);
            if(!eventPlayers.get(eventPlayers.size()-1).equals(player)) {
                playerString = playerString.concat(", ");
            }
        }
        return playerString;
    }

    public String getGuildId() {
        return guildId;
    }

    /*JDA

    @Override
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
        Message msg = event.getMessage();
        jda.getGuildById(guildId);
        if(canEdit && !event.getAuthor().getName().equals("Eve")) {
            eventCreator = event.getAuthor().getName();
            if (msg.getContentRaw().contains("amongus")) {
                promptEventTime(event.getChannel());
                //createAmongUsEvent(jda, member);
            } else if (msg.getContentRaw().contains("valorant")) {
                promptEventTime(event.getChannel());

            } else if (msg.getContentRaw().contains("gta")) {
                promptEventTime(event.getChannel());

            } else if (msg.getContentRaw().matches(timeFormat.pattern()) && canSetTime) {
                System.out.println(eventCreator + "has set the event time to " + msg.getContentRaw());
                setEventTime(msg.getContentRaw());
            } else if (!msg.getContentRaw().matches(timeFormat.pattern()) && canSetTime) {
                System.out.println(eventCreator + "has input the time incorrectly");
                giveMoreTimeExamples(event.getChannel());
            }
        }
    }

    public void giveMoreTimeExamples(MessageChannel eventChannel) {
        eventChannel.sendMessage("Incorrect format. No worries!! :grin:\n" +
                            "Here are a couple of other time examples:\n" +
                            "12:30 am C <- Make sure spacing is correct\n" +
                            "01:03 pm EST <- Start the hour with 0 if it's one digit").queue();
    }

    public void createAmongUsEvent(Guild guildFromMessage, Member memberFromMessage) {
        while(timeSet) {
            try {
                wait(1);
            } catch(Exception e) {
                System.out.println("Bad things have happened while waiting");
            }
        }

        //Create the embed of the current supported games
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Among Us Game");
        eb.setDescription("**Host:** @" + eventCreator + "\n" +
                          "**Time:** ");

        System.out.println(guildFromMessage);
        System.out.println(guildFromMessage.getTextChannelsByName("events", true));
        List<TextChannel> eventChannelList = guildFromMessage.getTextChannelsByName("events", true);

        if ((eventChannelList.size() > 0)) {
            eventChannelList.get(0).sendMessageEmbeds(eb.build()).queue();
        }
    }*/
}
