package rich.place;

import rich.Player;
import rich.tool.Tool;

public class ToolsRoom implements Place {
    @Override
    public Player.State actionTo(Player player) {
        if (player.canHaveMoreTools() && player.hasEnoughPoints()) {
            return Player.State.WAITING_FOR_RESPONSE;
        }
        return Player.State.END_TURN;
    }

    @Override
    public Player.State actionToResponse(Player player) {
        if (player.pointsEnoughToBuyRoadBlock()) {
            player.buyTool(Tool.RoadBlock);
        }
        return Player.State.END_TURN;
    }
}
