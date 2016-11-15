package rich.place;

import rich.Player;
import rich.tool.Tool;

import java.util.ArrayList;
import java.util.List;

public class ToolsRoom implements Place {
    public static Tool CHEAPEST_TOOL = Tool.Robot;
    private Tool attachedTool;
    private List<Player> playersOn;

    public ToolsRoom() {
        this.attachedTool = null;
        playersOn = new ArrayList<>();
    }

    @Override
    public Player.State actionTo(Player player) {
        if (player.canBuy(CHEAPEST_TOOL)) {
            return Player.State.WAITING_FOR_RESPONSE;
        }
        return Player.State.END_TURN;
    }

    @Override
    public void clearTool() {
        attachedTool = null;
    }

    @Override
    public Tool attachedToolType() {
        return attachedTool;
    }


    @Override
    public boolean tryToAttachTool(Tool tool) {
        if (canToolBeAttached()) {
            this.attachedTool = tool;
            return true;
        }
        return false;
    }

    @Override
    public boolean isToolAttached() {
        return attachedTool != null;
    }

    private boolean canToolBeAttached() {
        return attachedTool == null && playersOn.size() == 0;
    }

    @Override
    public boolean isPlayerOn() {
        return playersOn.size() > 0;
    }

    @Override
    public void setPlayerOn(Player player) {
        playersOn.add(player);
    }

}
