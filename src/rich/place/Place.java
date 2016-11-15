package rich.place;

import rich.Player;
import rich.tool.Tool;

public interface Place {
    Player.State actionTo(Player player);

    boolean tryToAttachTool(Tool tool);

    boolean isToolAttached();

    void clearTool();

    Tool attachedToolType();

    boolean isPlayerOn();

    void setPlayerOn(Player player);

}
