package rich.place;

import rich.Player;

public class GiftsRoom implements Place {
    @Override
    public Player.State actionTo(Player player) {
        return Player.State.WAITING_FOR_RESPONSE;
    }
}
