package rich.place;

import rich.application.Player;

public class MagicRoom extends Place {

    public MagicRoom() {
        super();
    }

    @Override
    public Player.State actionTo(Player player) {
        return Player.State.END_TURN;
    }

}
