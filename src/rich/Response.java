package rich;

public enum Response {

    YES_TO_BUY {
        @Override
        public Player.State execute(Player player, Response response) {
            player.buy();
            return Player.State.END_TURN;
        }
    },
    NO_TO_BUY {
        @Override
        public Player.State execute(Player player, Response response) {
            return Player.State.END_TURN;
        }
    };


    abstract public Player.State execute(Player player, Response response);

}
