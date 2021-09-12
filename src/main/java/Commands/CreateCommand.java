package Commands;

import Enums.GamesEnum;
import Enums.TimezonesEnum;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CreateCommand extends Command{
    String formatError = "";

    private static final Logger logger = LogManager.getLogger();


    public void buildCreateHelpResponse() {
        commandEmbed.clear();
        if(!formatError.equals("")) {
            commandEmbed.setDescription(formatError);
            formatError = "";
        }

        commandEmbed.setTitle("Create Command:");
        commandEmbed.addField("Game:", getGameList(), false);
        commandEmbed.addField("Time:", "HH:MM am/pm", false);
        commandEmbed.addField("Timezone:", getTimezoneList(), false);
    }

    public void buildCreateResponse() {

    }

    public boolean checkCommand(String [] commandParams) {
        //!eve create command
        if(commandParams.length == 2) {
            return false;
        }

        //Checking the amount of parameters
        if (commandParams.length != 6) {
            logger.info("Incorrect amount of arguments");
            formatError = "**Number of Arguments**";
            return false;
        }

        //Checking if game exists
        if(!GamesEnum.exists(commandParams[2].toLowerCase())) {
            logger.info("Incorrect game name");
            formatError = "**Incorrect game name**";
            return false;
        }

        //Checking time format
        if (!commandParams[3].matches("([2][0-3]|[0-1][0-9]|[1-9]):[0-5][0-9]")) {
            logger.info("Incorrect numeric time format should be HH:MM");
            formatError = "**Incorrect time format**";
            return false;
        }

        if (!commandParams[4].matches("([AaPp][Mm])")) {
            logger.info("Incorrect am/pm format");
            formatError = "**Incorrect time format**";
            return false;
        }

        //Checking timezone
        if(!TimezonesEnum.exists(commandParams[5].toLowerCase())) {
            logger.info("Incorrect timezone");
            formatError = "**Incorrect timezone format**";
            return false;
        }

        return true;
    }

    public String getGameList() {
        StringBuilder gameList = new StringBuilder();

        for (GamesEnum game : GamesEnum.values()) {
            gameList.append("**");
            gameList.append(game.getProperName());
            gameList.append("** - ");
            gameList.append(game.toString().toLowerCase());
            gameList.append("\n");
        }

        return gameList.toString();
    }

    public String getTimezoneList() {
        StringBuilder timezoneList = new StringBuilder();

        for (TimezonesEnum timezone : TimezonesEnum.values()) {
            timezoneList.append("**");
            timezoneList.append(timezone.getProperName());
            timezoneList.append("** - ");
            timezoneList.append(timezone.toString().toLowerCase());
            timezoneList.append("\n");
        }

        return timezoneList.toString();
    }
}
