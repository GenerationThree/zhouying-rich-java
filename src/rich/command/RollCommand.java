package rich.command;

import rich.application.Dice;
import rich.application.GameMap;
import rich.application.Player;
import rich.place.Land;
import rich.place.Place;
import rich.place.ToolsRoom;
import rich.tool.Tool;

public class RollCommand implements Command {
    private GameMap map;
    private Dice dice;

    public RollCommand(GameMap map, Dice dice) {
        this.map = map;
        this.dice = dice;
    }

    @Override
    public Player.State execute(Player player) {
        Place target = map.move(player.getCurrentPlace(), dice.next());
        player.moveTo(target);
        return target.actionTo(player);
    }

    @Override
    public Player.State respondWith(Player player, Response response) {
        return response.execute(player);
    }

    public static Response YesToBuy = player -> {
        Land land = (Land) player.getCurrentPlace();
        if (land.getOwner() == null) {
            player.buy(land);
        }
        return Player.State.END_TURN;
    };

    public static Response NoToBuy = player -> Player.State.END_TURN;

    public static Response YesToUpgrade = player -> {
        Land land = (Land) player.getCurrentPlace();
        if (land.getOwner() == player) {
            player.upgrade(land);
        }
        return Player.State.END_TURN;
    };

    public static Response NoToUpgrade = player -> Player.State.END_TURN;

    public static Response BuyRoadBlock = player -> {
        if (player.buyTool(Tool.RoadBlock)) {
            if (player.canBuy(ToolsRoom.CHEAPEST_TOOL))
                return Player.State.WAITING_FOR_RESPONSE;
            else
                return Player.State.END_TURN;
        }
        return Player.State.END_TURN;
    };

    public static Response BuyBomb = player -> {
        if (player.buyTool(Tool.Bomb)) {
            if (player.canBuy(ToolsRoom.CHEAPEST_TOOL))
                return Player.State.WAITING_FOR_RESPONSE;
            else
                return Player.State.END_TURN;
        }
        return Player.State.END_TURN;
    };

    public static Response BuyRobot = player -> {
        if (player.buyTool(Tool.Robot)) {
            if (player.canBuy(ToolsRoom.CHEAPEST_TOOL))
                return Player.State.WAITING_FOR_RESPONSE;
            else
                return Player.State.END_TURN;
        }
        return Player.State.END_TURN;
    };

    public static Response ExitToolsRoom = player -> Player.State.END_TURN;

    public static Response ChooseBonus = player -> {
        player.gainBonus();
        return Player.State.END_TURN;
    };

    public static Response ChoosePoint = player -> {
        player.gainPoints();
        return Player.State.END_TURN;
    };

    public static Response ChooseMascot = player -> {
        player.gainNoPunishTimes();
        return Player.State.END_TURN;
    };

    public static Response ExitGiftsRoom = player -> Player.State.END_TURN;

}
