package GameEvents;

import Enums.GamesEnum;
import Enums.TimezonesEnum;
import EveEventManager.GamePlayer;
import EveEventManager.GameTime;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.util.ArrayList;
import java.util.List;

public class GameEvent {
    private GamePlayer gameCreator;
    private GamesEnum gameEnum;
    private GameTime gameTime;
    private List<GamePlayer> gamePlayerList;
    private Integer maxPlayers;

    //Embed for event message
    MessageEmbed gameEventEmbed;

    public GameEvent(GamePlayer gameCreator, GamesEnum gameEnum, GameTime gameTime) {
        this.gameCreator = gameCreator;
        this.gameEnum = gameEnum;
        this.gameTime = gameTime;
        gamePlayerList = new ArrayList<>();
        gamePlayerList.add(gameCreator);
    }

    public void setMaxPlayers(Integer maxPlayers) { this.maxPlayers = maxPlayers; }
    public void setGameEventEmbed() {
        EmbedBuilder gameEventEmbedBuilder = new EmbedBuilder();
        gameEventEmbedBuilder.setTitle(getGameEnum().getProperName() + " Game");
        gameEventEmbedBuilder.addField("Host:", getGameCreator().getCreatorNickname() + "-" + getGameCreator().getCreatorName(), true);
        gameEventEmbedBuilder.addField("When?", getGameTime().toString(), false);
        gameEventEmbedBuilder.addField("Players " + gamePlayerList.size() + "/" + getMaxPlayers(), gamePlayerListToString(), false);
        gameEventEmbed = gameEventEmbedBuilder.build();
    }

    public GamePlayer getGameCreator() { return this.gameCreator; }
    public GamesEnum getGameEnum() { return this.gameEnum; }
    public GameTime getGameTime() { return this.gameTime; }
    public List<GamePlayer> getGamePlayerList() { return this.gamePlayerList; }
    public Integer getMaxPlayers() { return this.maxPlayers; }
    public MessageEmbed getGameEventEmbed() { return this.gameEventEmbed; }

    public boolean canAddPlayer() {
        return getGamePlayerList().size()+1 <= getMaxPlayers();
    }

    public void addPlayer(GamePlayer newPlayer) {
        gamePlayerList.add(newPlayer);
    }

    public String gamePlayerListToString() {
        StringBuilder gamePlayerListBuilder = new StringBuilder();
        int curPlayer = 0;
        for (GamePlayer player : getGamePlayerList()) {
            gamePlayerListBuilder.append(player.getCreatorNickname());
            curPlayer++;

            if(curPlayer != gamePlayerList.size()) {
                gamePlayerListBuilder.append(", ");
            }
        }

        return gamePlayerListBuilder.toString();
    }

}
