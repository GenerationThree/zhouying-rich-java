package rich.place;

import rich.Player;
import rich.tool.Tool;

public class MineralLand implements Place {
    private int points;
    private Tool attachedTool;

    public MineralLand(int points) {
        this.points = points;
        attachedTool = null;
    }

    @Override
    public Player.State actionTo(Player player) {
        return null;
    }

    @Override
    public boolean attach(Tool tool) {
        if (canToolBeAttached()) {
            this.attachedTool = tool;
            return true;
        }
        return false;
    }

    private boolean canToolBeAttached() {
        return attachedTool == null;
    }

    @Override
    public void clearTool() {
        attachedTool = null;
    }

    @Override
    public boolean isToolAttached() {
        return attachedTool != null;
    }
}
