import Commands.AddPlayerCommand;
import Commands.CreateCommand;
import Commands.HelpCommand;
import EveEventManager.EventController;
import Server.Server;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class MessageListener extends ListenerAdapter {
    JDA jda;
    Map<String, Server> serverList;
    Map<String, Server> userToServerList;
    List<String> waitingToConfirm = new ArrayList<>();

    HelpCommand helpCommand = new HelpCommand();
    CreateCommand createCommand = new CreateCommand();
    AddPlayerCommand addPlayerCommand = new AddPlayerCommand();

    private static final Logger logger = LogManager.getLogger();

    public MessageListener(Map<String, Server> serverList, Map<String, Server> userToServerList) {
        this.serverList = serverList;
        this.userToServerList = userToServerList;
    }

    public void setJDA(JDA jda) {
        this.jda = jda;
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        //Return if message is from a bot
        if(event.getAuthor().isBot()) {
            if(!event.getAuthor().getName().equals("Eve")) {
                return;
            }

            String embedTitle = event.getMessage().getEmbeds().get(0).getTitle();
            if(embedTitle == null) {
                return;
            }

            switch (embedTitle) {
                case "Confirm Event?":
                    waitingToConfirm.add(event.getMessageId());
                    break;
                case "Valorant Game":
                case "GTA V Game":
                    String hostName = event.getMessage().getEmbeds().get(0).getFields().get(0).getValue().split("-")[1].trim();
                    serverList.get(event.getGuild().getId()).getEventController().
                    serverList.get(event.getGuild().getId()).getEventController().mapCreatorToMessage(event.getMessageId(), event.getGuild().getMembersByName(hostName, true).get(0).getId());
                    break;
            }
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
                    createCommand.buildConfirmCreateResponse(eventController,
                                                             event.getMember().getNickname(),
                                                             event.getAuthor().getName(),
                                                             event.getAuthor().getId(),
                                                             event.getGuild().getId(),
                                                             commandParams[2],
                                                             Arrays.copyOfRange(commandParams, 3, 5),
                                                             commandParams[5]);
                    createCommand.sendResponse(event.getChannel());
                    break;
                case "delete":
                    logger.info("{} has tried to delete an event", event.getAuthor().getName());
                    break;
                default:
                    logger.error("{} used an invalid command !eve {}", event.getAuthor().getName(), command);
                    break;
            }

        }
    }

    @Override
    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
        //Return if message is from a bot
        if(event.getUser().isBot()) {
            return;
        }

        Server server = serverList.get(event.getGuild().getId());
        //Initial check for server in serverList
        if(server == null) {
            logger.error("Server doesn't exist in server list");
            return;
        }

        EventController eventController = server.getEventController();

        MessageReaction messageReaction = event.getReaction();
        String messageId = event.getMessageId();

        switch (messageReaction.getReactionEmote().getName()) {
            case "\uD83D\uDC4D":
                if(waitingToConfirm.contains(messageId)) {
                    logger.info("{} has confirmed an event", event.getUser().getName());
                    createCommand.buildCreateResponse(eventController, event.getUserId());
                    createCommand.sendResponse(event.getGuild().getTextChannelById(server.getEventChannelId()));
                }
                break;
            case "\uD83D\uDC4E":
                logger.info("{} has cancelled an event", event.getUser().getName());
                waitingToConfirm.remove(messageId);
            case "✋":
                logger.info("{} has tried to join the lobby of {}", event.getUser().getName(), event.getMessageId());
                addPlayerCommand.addPlayerToGameEvent(eventController, event.getMessageId(), event.getMember().getNickname(), event.getUser().getName(), event.getUserId(), event.getGuild().getId());
        }
    }
}
