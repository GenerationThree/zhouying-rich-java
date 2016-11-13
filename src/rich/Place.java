package rich;

public interface Place {
    Player.State actionTo(Player player);

    Player.State actionToResponse(Player player);
}
