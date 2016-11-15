package rich.tool;

import rich.Player;
import rich.place.Place;

public enum Tool {
    RoadBlock {
        @Override
        public int getPoints() {
            return 50;
        }

        @Override
        public Player.State takeAction(Player player) {
            return Player.State.WAITING_FOR_RESPONSE;
        }
    }, Bomb {
        @Override
        public int getPoints() {
            return 50;
        }

        @Override
        public Player.State takeAction(Player player) {
            return Player.State.END_TURN;
        }
    }, Robot {
        @Override
        public int getPoints() {
            return 30;
        }

        @Override
        public Player.State takeAction(Player player) {
            return Player.State.WAITING_FOR_RESPONSE;
        }
    };

    abstract public int getPoints();

    public boolean attachTo(Place place) {
        return place.tryToAttachTool(this);
    }

    public abstract Player.State takeAction(Player player);
}
