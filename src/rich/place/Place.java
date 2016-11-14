package rich.place;

import rich.Player;
import rich.tool.Tool;

public interface Place {
    Player.State actionTo(Player player);

    boolean attach(Tool tool);

    boolean isToolAttached();

    void clearTool();
}
