package rich.command;

import rich.application.Player;

public interface Response {
    Player.State execute(Player player);
}
