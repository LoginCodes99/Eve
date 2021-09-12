import Commands.CreateCommand;
import Commands.HelpCommand;
import EveEvents.Event;
import EveEvents.EventController;
import Server.Server;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.tools.picocli.CommandLine;

import java.util.Map;
import java.util.regex.Pattern;


public class MessageListener extends ListenerAdapter {
    JDA jda;
    Map<String, Server> serverList;
    Map<String, Server> userToServerList;
    Pattern timeFormat;

    HelpCommand helpCommand = new HelpCommand();
    CreateCommand createCommand = new CreateCommand();

    private static final Logger logger = LogManager.getLogger();

    public MessageListener(Map<String, Server> serverList, Map<String, Server> userToServerList) {
        this.serverList = serverList;
        this.userToServerList = userToServerList;
        timeFormat = Pattern.compile("([2][0-3]|[0-1][0-9]|[1-9]):[0-5][0-9]\\s([AaPp][Mm])\\s(C|c|EST|est)");
    }

    public void setJDA(JDA jda) {
        this.jda = jda;
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        //Return if message is from a bot
        if(event.getAuthor().isBot()) {
            return;
        }

        String message = event.getMessage().getContentRaw();

        //Help command
        if(message.equals("!eve")) {
            logger.info("{} has called the help command", event.getAuthor().getName());
            helpCommand.buildHelpResponse();
            helpCommand.sendResponse(event.getChannel());
            return;
        }

        if(message.startsWith("!eve")) {
            Server server = serverList.get(event.getGuild().getId());
            //Initial check for server in serverList
            if(server == null) {
                logger.error("Server doesn't exist in server list");
                return;
            }

            EventController eventController = server.getEventController();

            //Checking the event controller
            if(eventController == null) {
                logger.error("Event controller for {}:{} is null", event.getGuild().getName(), event.getGuild().getId());
                return;
            }

            String command = message.split(" ")[1];
            String[] commandParams = message.split(" ");
            switch (command) {
                case "create":
                    boolean correctFormat = createCommand.checkCommand(commandParams);
                    if(!correctFormat) {
                        logger.info("{} has called the create help command", event.getAuthor().getName());
                        createCommand.buildCreateHelpResponse();
                        createCommand.sendResponse(event.getChannel());
                        return;
                    }

                    logger.info("{} has started creating an event", event.getAuthor().getName());
                    createCommand.buildCreateResponse();
                    //createCommand.sendResponse(event.getChannel());
                    //eventController.createEvent(event.getAuthor().getId(), event.getGuild().getId());
                    break;
                case "delete":
                    logger.info("{} has tried to delete an event", event.getAuthor().getName());
                    break;
                default:
                    logger.error("{} used an invalid command !eve {}", event.getAuthor().getName(), command);
                    break;
            }

        }
        /*  else
            if(event.getChannel().getName().equals("events")) {
            System.out.println(event.getMessage().getEmbeds().get(0).getFields().get(1).getValue());
            Member m = event.getGuild().getMembersByName(event.getMessage().getEmbeds().get(0).getFields().get(1).getValue(), true).get(0);
            eventController.setEventMessageId(m.getId(), event.getMessage().getId());
        }*/
    }

    @Override
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
        Server server = userToServerList.get(event.getAuthor().getId());
        EventController eventController = server.getEventController();
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
