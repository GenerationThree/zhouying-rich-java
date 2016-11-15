package rich.place;

import rich.GameConstant;
import rich.Player;
import rich.tool.Tool;

import java.util.ArrayList;
import java.util.List;

public class Prison implements Place {
    private Tool attachedTool;
    private List<Player> playersOn;

    public Prison() {
        this.attachedTool = null;
        playersOn = new ArrayList<>();
    }

    @Override
    public Player.State actionTo(Player player) {
        player.pausedBy(GameConstant.PRISON_PAUSED_TIMES);
        return Player.State.END_TURN;
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

    @Override
    public boolean isPlayerOn() {
        return playersOn.size() > 0;
    }

    @Override
    public void setPlayerOn(Player player) {
        playersOn.add(player);
    }

    private boolean canToolBeAttached() {
        return attachedTool == null && playersOn.size() == 0;
    }
}
