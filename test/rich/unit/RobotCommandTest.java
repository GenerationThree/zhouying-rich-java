package rich.unit;

import org.junit.Before;
import org.junit.Test;
import rich.GameMapImp;
import rich.Player;
import rich.command.Command;
import rich.command.RobotCommand;
import rich.place.Place;
import rich.tool.Tool;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class RobotCommandTest {

    private GameMapImp gameMap;
    private Command robot;
    private Player player;

    @Before
    public void before() {
        gameMap = new GameMapImp();
        robot = new RobotCommand();
        player = Player.createPlayerWithBalanceAndAMap(gameMap, 0);
        player.gainPoints();
        player.buyTool(Tool.Bomb);
        player.buyTool(Tool.Bomb);
        player.buyTool(Tool.RoadBlock);
        player.buyTool(Tool.Robot);
    }

    @Test
    public void should_clear_tools_in_range() {

        // Set two bombs 5 steps and 6 steps in front
        player.putBomb(5);
        player.putBomb(6);
        Place placeWithBomb1 = gameMap.findByPosition(5);
        Place placeWithBomb2 = gameMap.findByPosition(6);
        assertThat(placeWithBomb1.isToolAttached(), is(true));
        assertThat(placeWithBomb2.isToolAttached(), is(true));

        player.execute(robot);
        assertThat(placeWithBomb1.isToolAttached(), is(false));
        assertThat(placeWithBomb2.isToolAttached(), is(false));
        assertThat(player.getState(), is(Player.State.WAITING_FOR_COMMAND));
    }

    @Test
    public void should_not_clear_tools_out_of_range() {
        // Set a block in 1 step behind and bomb 11 steps in front
        player.putBlock(-1);
        player.putBomb(11);
        Place placeWithBlock = gameMap.findByPosition(69);
        Place placeWithBomb = gameMap.findByPosition(11);


        assertThat(placeWithBlock.isToolAttached(), is(true));
        assertThat(placeWithBlock.isToolAttached(), is(true));
        assertThat(placeWithBomb.isToolAttached(), is(true));
        assertThat(player.getState(), is(Player.State.WAITING_FOR_COMMAND));
    }


}
