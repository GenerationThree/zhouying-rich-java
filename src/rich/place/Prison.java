package rich.place;

import rich.application.GameConstant;
import rich.application.Player;

public class Prison extends Place {

    public Prison() {
        super();
    }

    @Override
    public Player.State actionTo(Player player) {
        player.setPauseTime(GameConstant.PRISON_PAUSED_TIMES);
        return Player.State.END_TURN;
    }

}
