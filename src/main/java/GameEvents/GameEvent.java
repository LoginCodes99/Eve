package GameEvents;

import Enums.GamesEnum;
import Player.Player;
import EveEventManager.GameTime;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GameEvent {
    private String eventId;
    private Player gameCreator;
    private GamesEnum gameEnum;
    private GameTime gameTime;
    private List<Player> playerList;
    private Integer maxPlayers;

    //Embed for event message
    MessageEmbed gameEventEmbed;

    public GameEvent(Player gameCreator, GamesEnum gameEnum, GameTime gameTime) {
        this.eventId = UUID.randomUUID().toString();
        this.gameCreator = gameCreator;
        this.gameEnum = gameEnum;
        this.gameTime = gameTime;
        this.playerList = new ArrayList<>();
        this.playerList.add(gameCreator);
    }

    public void setMaxPlayers(Integer maxPlayers) { this.maxPlayers = maxPlayers; }
    public void setGameEventEmbed() {
        EmbedBuilder gameEventEmbedBuilder = new EmbedBuilder();
        gameEventEmbedBuilder.setTitle(getGameEnum().getProperName() + " Game");
        gameEventEmbedBuilder.addField("Host:", getGameCreator().getCreatorNickname() + "-" + getGameCreator().getCreatorName(), true);
        gameEventEmbedBuilder.addField("When?", getGameTime().toString(), false);
        gameEventEmbedBuilder.addField("Players " + playerList.size() + "/" + getMaxPlayers(), gamePlayerListToString(), false);
        gameEventEmbed = gameEventEmbedBuilder.build();
    }

    public String getEventId() { return this.eventId; }
    public Player getGameCreator() { return this.gameCreator; }
    public GamesEnum getGameEnum() { return this.gameEnum; }
    public GameTime getGameTime() { return this.gameTime; }
    public List<Player> getGamePlayerList() { return this.playerList; }
    public Integer getMaxPlayers() { return this.maxPlayers; }
    public MessageEmbed getGameEventEmbed() { return this.gameEventEmbed; }

    public boolean canAddPlayer() {
        return getGamePlayerList().size()+1 <= getMaxPlayers();
    }

    public void addPlayer(Player newPlayer) {
        playerList.add(newPlayer);
    }

    public String gamePlayerListToString() {
        StringBuilder gamePlayerListBuilder = new StringBuilder();
        int curPlayer = 0;
        for (Player player : getGamePlayerList()) {
            gamePlayerListBuilder.append(player.getCreatorNickname());
            curPlayer++;

            if(curPlayer != playerList.size()) {
                gamePlayerListBuilder.append(", ");
            }
        }

        return gamePlayerListBuilder.toString();
    }

}
