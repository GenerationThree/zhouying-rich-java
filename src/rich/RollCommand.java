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
}
