package rich.helper;

import rich.application.Player;
import rich.place.Land;
import rich.tool.Tool;

public class PlayerFixture {
    public static Player getPlayerWithSomeAssets() {
        Player player = Player.createWithBalanceAndPoints(10000, 1000);

        Land zeroLevel = new Land(200);
        Land oneLevel = new Land(200);
        oneLevel.setLevel(1);
        Land twoLevel = new Land(500);
        twoLevel.setLevel(2);
        Land threeLevel = new Land(300);
        threeLevel.setLevel(3);

        player.buy(zeroLevel);
        player.buy(oneLevel);
        player.buy(twoLevel);
        player.buy(threeLevel);

        player.buyTool(Tool.Bomb);
        player.buyTool(Tool.RoadBlock);
        player.buyTool(Tool.Robot);

        return player;
    }

}
