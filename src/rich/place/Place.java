package rich.place;

import rich.Player;

public interface Place {
    Player.State actionTo(Player player);
}
