package GameEvents;

import Enums.GamesEnum;
import Enums.TimezonesEnum;
import EveEventManager.GamePlayer;
import EveEventManager.GameTime;

public class ValorantGameEvent extends GameEvent {

    public ValorantGameEvent(GamePlayer gameCreator, GamesEnum gameEnum, GameTime gameTime) {
        super(gameCreator, GamesEnum.VALORANT, gameTime);
        setMaxPlayers(5);
    }
}
