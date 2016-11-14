package rich.place;

import rich.GameConstant;
import rich.Player;
import rich.tool.Tool;

public class Prison implements Place {
    private Tool attachedTool;

    public Prison() {
        this.attachedTool = null;
    }

    @Override
    public Player.State actionTo(Player player) {
        player.pausedBy(GameConstant.PRISON_PAUSED_TIMES);
        return Player.State.END_TURN;
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

    @Override
    public Tool attachedToolType() {
        return attachedTool;
    }

    @Override
    public boolean isToolAttached() {
        return attachedTool != null;
    }

    private boolean canToolBeAttached() {
        return attachedTool == null;
    }
}
