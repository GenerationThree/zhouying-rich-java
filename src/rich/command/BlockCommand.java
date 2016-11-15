package rich.command;

import com.sun.xml.internal.ws.server.UnsupportedMediaException;
import rich.application.Player;

public class BlockCommand implements Command {
    private int step;

    public BlockCommand(int step) {
        this.step = step;
    }

    @Override
    public Player.State execute(Player player) {
        player.putBomb(step);
        return Player.State.WAITING_FOR_COMMAND;
    }

    @Override
    public Player.State respondWith(Player player, Response response) {
        throw new UnsupportedMediaException();
    }
}
