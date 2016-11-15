package rich.command;

import com.sun.xml.internal.ws.server.UnsupportedMediaException;
import rich.application.GameConstant;
import rich.application.Player;

public class RobotCommand implements Command {

    @Override
    public Player.State execute(Player player) {
        player.cleanRoad(GameConstant.ROBOT_CLEAR_RANGE);
        return Player.State.WAITING_FOR_COMMAND;
    }

    @Override
    public Player.State respondWith(Player player, Response response) {
        throw new UnsupportedMediaException();
    }
}
