package rich.place;

import rich.Player;

public class MineralLand extends Place {
    private int points;


    public MineralLand(int points) {
        super();
        this.points = points;
    }

    @Override
    public Player.State actionTo(Player player) {
        player.gainPoints_(this.points);
        return Player.State.END_TURN;
    }

}
