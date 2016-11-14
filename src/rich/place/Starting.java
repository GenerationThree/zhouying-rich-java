package rich.place;

import rich.Player;
import rich.tool.Tool;

public class Starting implements Place {
    private Tool attachedTool;

    public Starting() {
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

    @Override
    public void clearTool() {
        attachedTool = null;
    }

    private boolean canToolBeAttached() {
        return attachedTool == null;
    }

    @Override
    public boolean isToolAttached() {
        return attachedTool != null;
    }
}
