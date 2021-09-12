package Commands;

public class HelpCommand extends Command {

    public void buildHelpResponse() {
        commandEmbed.clear();
        commandEmbed.setTitle("List of Commands:");
        commandEmbed.setDescription("**!eve create** - Shows list of games and command format\n" +
                                    "**!eve create [GAME] [TIME] [TIMEZONE]** - Creates a new event");
    }
}
