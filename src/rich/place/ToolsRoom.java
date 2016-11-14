package rich.place;

import rich.Player;
import rich.tool.Tool;

public class ToolsRoom implements Place {
    public static Tool CHEAPEST_TOOL = Tool.Robot;
    private Tool attachedTool;

    public ToolsRoom() {
        this.attachedTool = null;
    }

    @Override
    public Player.State actionTo(Player player) {
        if (player.canBuy(CHEAPEST_TOOL)) {
            return Player.State.WAITING_FOR_RESPONSE;
        }
        return Player.State.END_TURN;
    }


    @Override
    public boolean attachedBy(Tool tool) {
        if (canToolBeAttached()) {
            this.attachedTool = tool;
            return true;
        }
        return false;
    }

    private boolean canToolBeAttached() {
        return attachedTool == null;
    }

}
