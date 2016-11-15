package rich.place;

import rich.Player;
import rich.tool.Tool;

import java.util.ArrayList;
import java.util.List;

public abstract class Place {

    protected Tool attachedTool;
    protected List<Player> playersOn;

    public Place() {
        attachedTool = null;
        playersOn = new ArrayList<>();
    }

    public abstract Player.State actionTo(Player player);

    public boolean tryToAttachTool(Tool tool) {
        if (canToolBeAttached()) {
            this.attachedTool = tool;
            return true;
        }
        return false;
    }

    public boolean isToolAttached() {
        return attachedTool != null;
    }

    public void clearTool() {
        attachedTool = null;
    }

    public Tool attachedToolType() {
        return attachedTool;
    }

    public boolean isPlayerOn() {
        return playersOn.size() > 0;
    }

    public void setPlayerOn(Player player) {
        playersOn.add(player);
    }

    private boolean canToolBeAttached() {
        return attachedTool == null && playersOn.size() == 0;
    }

}
