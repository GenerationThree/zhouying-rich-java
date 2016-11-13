package rich;

public interface Command {
    Player.State execute(Player player);

    Player.State respondWith(Player player, Response response);
}
