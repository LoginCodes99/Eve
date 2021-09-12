package Commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

public class Command {
    EmbedBuilder commandEmbed = new EmbedBuilder();

    public void sendResponse(TextChannel c) {
        c.sendMessageEmbeds(commandEmbed.build()).queue();
    }
}
