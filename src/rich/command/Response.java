package rich.command;

import rich.Player;

public interface Response {
    Player.State execute(Player player);
}
