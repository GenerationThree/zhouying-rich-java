package rich.unit;

import org.junit.Before;
import org.junit.Test;
import rich.Player;
import rich.command.Command;
import rich.command.SellToolCommand;
import rich.tool.Tool;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class SellToolCommandTest {

    private static final int START_POINTS = 1000;
    private Player player;

    @Before
    public void before() {
        // First, prepare a player with a tool
        player = Player.createPlayerWithPoints(START_POINTS);
        player.buyTool(Tool.RoadBlock);
        assertThat(player.getState(), is(Player.State.WAITING_FOR_COMMAND));
        assertThat(player.getToolsAmount(), is(1));
    }

    @Test
    public void should_sell_tool_from_player() {
        final int FIRST = 0;
        Command sellTool = new SellToolCommand(FIRST);

        player.execute(sellTool);
        assertThat(player.getState(), is(Player.State.WAITING_FOR_COMMAND));
        assertThat(player.getToolsAmount(), is(0));
    }

    @Test
    public void should_not_sell_tool_when_tool_number_is_not_correct() {
        final int NOT_CORRECT = -1;
        Command sellTool = new SellToolCommand(NOT_CORRECT);

        player.execute(sellTool);
        assertThat(player.getState(), is(Player.State.WAITING_FOR_COMMAND));
        assertThat(player.getToolsAmount(), is(1));
    }

}