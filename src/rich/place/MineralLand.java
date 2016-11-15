package rich.place;

import rich.Player;
import rich.tool.Tool;

import java.util.ArrayList;
import java.util.List;

public class MineralLand implements Place {
    private int points;
    private Tool attachedTool;
    private List<Player> playersOn;

    public MineralLand(int points) {
        this.points = points;
        attachedTool = null;
        playersOn = new ArrayList<>();
    }

    @Override
    public Player.State actionTo(Player player) {
        return null;
    }

    @Override
    public boolean tryToAttachTool(Tool tool) {
        if (canToolBeAttached()) {
            this.attachedTool = tool;
            return true;
        }
        return false;
    }

    private boolean canToolBeAttached() {
        return attachedTool == null && playersOn.size() == 0;
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
}
