package rich.place;

import rich.GameConstant;
import rich.Player;
import rich.tool.Tool;

import java.util.ArrayList;
import java.util.List;

public class Land implements Place {
    private static final int TOP_LEVEL = 3;
    private Player owner;
    private int currentLevel;
    private int price;
    private int roadToll;
    private Tool attachedTool;
    private List<Player> playersOn;

    public Land() {
        currentLevel = 0;
        owner = null;
        attachedTool = null;
        playersOn = new ArrayList<>();
    }

    public Land(int price) {
        currentLevel = 0;
        owner = null;
        attachedTool = null;
        this.price = price;
        playersOn = new ArrayList<>();
    }

    public Player getOwner() {
        return owner;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public int getPrice() {
        return price;
    }

    public static Land createLandWithPrice(int price) {
        Land land = new Land();
        land.price = price;
        land.playersOn = new ArrayList<>();
        return land;
    }

    public int roadToll() {
        int ret = 0;
        if (owner != null) {
            switch (currentLevel) {
                case 0:
                    ret = price / 2;
                    break;
                case 1:
                    ret = price;
                    break;
                case 2:
                    ret = price * 2;
                    break;
                case 3:
                    ret = price * 4;
                    break;
            }
        }
        return ret;
    }

    public void upgrade() {
        currentLevel += 1;
    }

    public boolean canUpgrade() {
        return currentLevel < TOP_LEVEL;
    }

    // For test, clean it!!!
    public void setPrice(int price) {
        this.price = price;
    }

    // For test, clean it!!!
    public void setLevel(int level) {
        this.currentLevel = level;
    }

    @Override
    public Player.State actionTo(Player player) {
        if (attachedTool != null && attachedTool.ordinal() == Tool.Bomb.ordinal()) {
            player.pausedBy(GameConstant.BOMB_PAUSED_TIMES);
            return Player.State.END_TURN;
        }

        if (owner != null && owner != player) {
            if (player.canAfford(this.price)) {
                player.pay(this.price);
                return Player.State.END_TURN;
            }
            else {
                return Player.State.GAME_OVER;
            }
        }
        if (owner == player) {
            return Player.State.WAITING_FOR_RESPONSE;
        }
        else {
            return Player.State.WAITING_FOR_RESPONSE;
        }
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

    public void setOwner(Player owner) {
        this.owner = owner;
    }
}
