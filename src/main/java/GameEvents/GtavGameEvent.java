package GameEvents;

import Enums.GamesEnum;
import Enums.TimezonesEnum;
import EveEventManager.GamePlayer;
import EveEventManager.GameTime;

public class GtavGameEvent extends GameEvent {

    public GtavGameEvent(GamePlayer gameCreator, GamesEnum gameEnum, GameTime gameTime) {
        super(gameCreator, GamesEnum.GTAV, gameTime);
        setMaxPlayers(4);
    }
}
