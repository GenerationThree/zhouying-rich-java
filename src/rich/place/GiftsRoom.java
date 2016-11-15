package rich.place;

import rich.Player;
import rich.tool.Tool;

import java.util.ArrayList;
import java.util.List;

public class GiftsRoom implements Place {
    private Tool attachedTool;
    private List<Player> playersOn;

    public GiftsRoom() {
        this.attachedTool = null;
        playersOn = new ArrayList<>();
    }

    @Override
    public Player.State actionTo(Player player) {
        return Player.State.WAITING_FOR_RESPONSE;
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

    @Override
    public void clearTool() {
        attachedTool = null;
    }

    @Override
    public Tool attachedToolType() {
        return attachedTool;
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
