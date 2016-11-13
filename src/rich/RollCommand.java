package rich;

public class RollCommand implements Command {
    private GameMap map;
    private Dice dice;

    public RollCommand(GameMap map, Dice dice) {
        this.map = map;
        this.dice = dice;
    }

    @Override
    public Player.State execute(Player player) {
        Land target = map.move(player.getCurrentLand(), dice.next());
        player.moveTo(target);

        if (target.getOwner() != null && target.getOwner() != player ) {
            return Player.State.END_TURN;
        }
        return Player.State.WAITING_FOR_RESPONSE;
    }

    @Override
    public Player.State respondWith(Player player, Response response) {
        return response.execute(player);
    }

    public static Response YesToBuy = player -> {
        Land current = player.getCurrentLand();
        if (current.getOwner() == null) {
            player.buy();
        }
        return Player.State.END_TURN;
    };

    public static Response NoToBuy = player -> Player.State.END_TURN;

    public static Response YesToUpgrade = player -> {
        Land current = player.getCurrentLand();
        if (current.getOwner() == player) {
            player.upgrade();
        }
        return Player.State.END_TURN;
    };

    public static Response NoToUpgrade = player -> Player.State.END_TURN;

}
