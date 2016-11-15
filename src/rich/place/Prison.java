package rich.place;

import rich.GameConstant;
import rich.Player;

public class Prison extends Place {

    public Prison() {
        super();
    }

    @Override
    public Player.State actionTo(Player player) {
        player.pausedBy(GameConstant.PRISON_PAUSED_TIMES);
        return Player.State.END_TURN;
    }

}
