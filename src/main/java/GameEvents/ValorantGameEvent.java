package GameEvents;

import Enums.GamesEnum;
import Player.Player;
import EveEventManager.GameTime;

public class ValorantGameEvent extends GameEvent {

    public ValorantGameEvent(Player gameCreator, GamesEnum gameEnum, GameTime gameTime) {
        super(gameCreator, GamesEnum.VALORANT, gameTime);
        setMaxPlayers(5);
    }
}
