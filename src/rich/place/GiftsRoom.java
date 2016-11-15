package rich.place;

import rich.application.Player;

public class GiftsRoom extends Place {

    public GiftsRoom() {
        super();
    }

    @Override
    public Player.State actionTo(Player player) {
        return Player.State.WAITING_FOR_RESPONSE;
    }

}
