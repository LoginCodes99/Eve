package GameEvents;

import Enums.GamesEnum;
import Player.Player;
import EveEventManager.GameTime;

public class GtavGameEvent extends GameEvent {

    public GtavGameEvent(Player gameCreator, GamesEnum gameEnum, GameTime gameTime) {
        super(gameCreator, GamesEnum.GTAV, gameTime);
        setMaxPlayers(4);
    }
}
