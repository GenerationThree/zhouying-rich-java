package rich;

public interface Command {
    Player.State execute(Player player);
}
