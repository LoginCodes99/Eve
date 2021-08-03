import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

import java.util.regex.Pattern;


public class MessageListener extends ListenerAdapter {
    JDA jda;
    EventController eventController;
    EventChannel eventChannel;
    Pattern timeFormat;

    public MessageListener(EventController ec, EventChannel ech) {
        eventController = ec;
        eventChannel = ech;
        timeFormat = Pattern.compile("([2][0-3]|[0-1][0-9]|[1-9]):[0-5][0-9]\\s([AaPp][Mm])\\s(C|c|EST|est)");
    }

    public void setJDA(JDA jda) {
        this.jda = jda;
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if(!event.getAuthor().isBot() && event.getMessage().getContentRaw().contains("!eve")) {
            String[] command = event.getMessage().getContentRaw().split(" ");
            int commandLength = command.length;

            //Checking for the command
            if(commandLength == 1) {
                helpGuildCommands(event.getChannel());
            } else if(commandLength > 1) {
                if(eventController == null) {
                    System.out.println("eventController is null");
                } else {
                    if (command[1].equals("create"))
                        eventController.createEvent(event.getAuthor().getId(), event.getGuild().getId());
                }
            }

            //Old code -- needs to be reimplemented eventually
            /*} else if ((msg_argv.length > 1) && msg_argv[1].equals("setchannelname")) {
            List<TextChannel> channel = event.getGuild().getTextChannelsByName(eventChannel.channelName, true);
            if(channel.size() > 0) {
                channel.get(0).getManager().setName(msg_argv[2]).queue();
                eventChannel.channelName = msg_argv[2];
            }
            } else if ((msg_argv.length > 1) && msg_argv[1].equals("setchannelcategory")) {
            List<TextChannel> channel = event.getGuild().getTextChannelsByName(eventChannel.channelName, true);
            if(channel.size() > 0) {
                channel.get(0).getManager().setParent(msg_argv[2]).queue();
                eventChannel.channelName = msg_argv[2];
            }
            }*/

        } else if(event.getAuthor().isBot() && event.getChannel().getName().equals("events")) {
            System.out.println(event.getMessage().getEmbeds().get(0).getFields().get(1).getValue());
            Member m = event.getGuild().getMembersByName(event.getMessage().getEmbeds().get(0).getFields().get(1).getValue(), true).get(0);
            eventController.setEventMessageId(m.getId(), event.getMessage().getId());
        }
    }

    @Override
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
        String[] command = event.getMessage().getContentRaw().split(" ");
        int commandLength = command.length;
        if(!event.getAuthor().isBot() && event.getMessage().getContentRaw().contains("!eve")) {
            //Checking for the command
            if(eventController.isUserEditingEvent(event.getAuthor().getId())) {
                switch (command[1]) {
                    case "amongus":
                    case "valorant":
                    case "gta":
                        eventController.getEventByAuthor(event.getAuthor().getId()).setGame(command[1]);
                        promptEventTime(event.getChannel());
                        break;
                    default:
                        System.out.println("Error while creating event");
                }
            } else {
                helpPrivateCommands(event.getChannel());
            }
        } else if(event.getMessage().getContentRaw().matches(timeFormat.pattern())) {
            eventController.getEventByAuthor(event.getAuthor().getId()).setTime(command[0], command[1], command[2]);
            eventController.finishEventEditing(event.getAuthor().getId());
            sendEventToChannel(eventController.getEventByAuthor(event.getAuthor().getId()));
        }
    }

    @Override
    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
        event.getChannel().retrieveMessageById(event.getMessageId()).queue(message -> {
            //MessageEmbed e = event
        });
    }

    public void helpGuildCommands(MessageChannel channel) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("List of Commands:");
        eb.setDescription("**!eve create** - Creates an event in the guild that the command was called.\n" +
                "**!eve setchannelname new_name** - (WIP) Changes the name of the default 'events' channel\n" +
                "**!eve setchannelcategory category_name** - (WIP) Changes the category location");
        channel.sendMessageEmbeds(eb.build()).queue();
    }

    public void helpPrivateCommands(MessageChannel channel) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("List of Commands:");
        eb.setDescription("**WIP**");
        channel.sendMessageEmbeds(eb.build()).queue();
    }

    public void promptEventTime(PrivateChannel channel) {
        channel.sendMessage("What time is your event? Format: HH:MM am/pm C/EST ie, 06:30 pm est").queue();
    }

    public void sendEventToChannel(Event userCreatedEvent) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(userCreatedEvent.getGame());
        eb.addField("Time", userCreatedEvent.getTime(), false);
        eb.addField("Created By", userCreatedEvent.getCreator(), false);
        eb.addField("Current players 1/" + userCreatedEvent.getLimit(), userCreatedEvent.getPlayers(), false);
        eb.setDescription("React to the message to join the waiting lobby");
        jda.getGuildById(userCreatedEvent.getGuildId()).getTextChannelsByName("events", true).get(0).sendMessageEmbeds(eb.build()).queue();
    }

}
