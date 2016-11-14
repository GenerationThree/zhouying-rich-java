package rich.command;

import com.sun.xml.internal.ws.server.UnsupportedMediaException;
import rich.Player;

public class BlockCommand implements Command {
    private int position;

    public BlockCommand(int position) {
        this.position = position;
    }

    @Override
    public Player.State execute(Player player) {
        return Player.State.WAITING_FOR_COMMAND;
    }

    @Override
    public Player.State respondWith(Player player, Response response) {
        throw new UnsupportedMediaException();
    }
}
