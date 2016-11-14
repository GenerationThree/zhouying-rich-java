package rich.place;

import rich.Player;
import rich.tool.Tool;

public interface Place {
    Player.State actionTo(Player player);

    boolean attachedBy(Tool tool);
}
