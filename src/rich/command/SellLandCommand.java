package rich.command;

import com.sun.xml.internal.ws.server.UnsupportedMediaException;
import rich.application.Player;

public class SellLandCommand implements Command {
    private int position;

    public SellLandCommand(int position) {
        this.position = position;
    }

    @Override
    public Player.State execute(Player player) {
        player.sellLand(this.position);
        return Player.State.WAITING_FOR_COMMAND;
    }

    @Override
    public Player.State respondWith(Player player, Response response) {
        throw new UnsupportedMediaException();
    }
}
