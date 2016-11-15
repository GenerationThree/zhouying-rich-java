package rich.command;

import com.sun.xml.internal.ws.server.UnsupportedMediaException;
import rich.application.Player;

public class SellToolCommand implements Command{
    private int index;

    public SellToolCommand(int index) {
        this.index = index;
    }

    @Override
    public Player.State execute(Player player) {
        player.sellTool(index);
        return Player.State.WAITING_FOR_COMMAND;
    }

    @Override
    public Player.State respondWith(Player player, Response response) {
        throw new UnsupportedMediaException();
    }
}
