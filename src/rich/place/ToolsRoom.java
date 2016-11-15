package rich.place;

import rich.Player;
import rich.tool.Tool;

public class ToolsRoom extends Place {
    public static Tool CHEAPEST_TOOL = Tool.Robot;

    public ToolsRoom() {
        super();
    }

    @Override
    public Player.State actionTo(Player player) {
        if (player.canBuy(CHEAPEST_TOOL)) {
            return Player.State.WAITING_FOR_RESPONSE;
        }
        return Player.State.END_TURN;
    }

}
