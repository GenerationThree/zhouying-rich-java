package rich.command;

import com.sun.xml.internal.ws.server.UnsupportedMediaException;
import rich.Player;

public class QueryCommand implements Command {
    @Override
    public Player.State execute(Player player) {
        player.query();
        return Player.State.WAITING_FOR_COMMAND;
    }

    @Override
    public Player.State respondWith(Player player, Response response) {
        throw new UnsupportedMediaException();
    }
}
