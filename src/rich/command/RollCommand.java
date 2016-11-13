package rich.command;

import rich.*;
import rich.place.Place;

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
        Place current = player.getCurrentPlace();
        return current.actionToResponse(player);
    };

    public static Response NoToBuy = player -> Player.State.END_TURN;

    public static Response YesToUpgrade = player -> {
        Place current = player.getCurrentPlace();
        return current.actionToResponse(player);
    };

    public static Response NoToUpgrade = player -> Player.State.END_TURN;

    public static Response BuyRoadBlock = player -> {
        Place current = player.getCurrentPlace();
        return current.actionToResponse(player);
    };

}
