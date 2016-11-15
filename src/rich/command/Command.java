package rich.command;

import rich.application.Player;

public interface Command {
    Player.State execute(Player player);

    Player.State respondWith(Player player, Response response);
}
