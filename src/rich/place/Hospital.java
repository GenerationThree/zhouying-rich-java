package rich.place;

import rich.application.Player;

public class Hospital extends Place {

    public Hospital() {
        super();
    }

    @Override
    public Player.State actionTo(Player player) {
        return Player.State.END_TURN;
    }

}
