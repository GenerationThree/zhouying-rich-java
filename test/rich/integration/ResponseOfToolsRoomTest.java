package rich.integration;

import org.junit.Before;
import org.junit.Test;
import rich.Dice;
import rich.GameMap;
import rich.Player;
import rich.command.Command;
import rich.command.RollCommand;
import rich.place.Place;
import rich.place.ToolsRoom;
import rich.tool.Tool;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ResponseOfToolsRoomTest {

    private static final int ZERO_POINTS = 0;
    private static final int POINTS_CAN_BUY_ONE = 60;
    private static final int ENOUGH_POINTS = 3000;
    private static final int TOOLS_AMOUNT_LIMIT = 10;

    private GameMap map;
    private Dice dice;

    private Place toolsRoom;
    private Place starting;

    private Player player;
    private Command roll;

    @Before
    public void before() {
        map = mock(GameMap.class);
        dice = mock(Dice.class);
        starting = mock(Place.class);
        toolsRoom = new ToolsRoom();

        roll = new RollCommand(map, dice);
        when(dice.next()).thenReturn(1);
        when(map.move(eq(starting), eq(1))).thenReturn(toolsRoom);
    }

    @Test
    public void should_end_turn_if_player_has_not_enough_points() {
        player = Player.createPlayerWithStartingAndPoints(starting, ZERO_POINTS);
        player.execute(roll);
        assertThat(player.getState(), is(Player.State.END_TURN));
    }

    @Test
    public void should_buy_a_road_block() {
        player = Player.createPlayerWithStartingAndPoints(starting, POINTS_CAN_BUY_ONE);
        player.execute(roll);
        assertThat(player.getState(), is(Player.State.WAITING_FOR_RESPONSE));
        player.respond(RollCommand.BuyRoadBlock);
        assertThat(player.getPoints(), is(POINTS_CAN_BUY_ONE - Tool.RoadBlock.getPoints()));
        assertThat(player.getToolsAmount(), is(1));
    }

    @Test
    public void should_buy_a_bomb() {
        player = Player.createPlayerWithStartingAndPoints(starting, POINTS_CAN_BUY_ONE);
        player.execute(roll);
        assertThat(player.getState(), is(Player.State.WAITING_FOR_RESPONSE));
        player.respond(RollCommand.BuyBomb);
        assertThat(player.getPoints(), is(POINTS_CAN_BUY_ONE - Tool.Bomb.getPoints()));
        assertThat(player.getToolsAmount(), is(1));
    }

    @Test
    public void should_buy_a_robot() {
        player = Player.createPlayerWithStartingAndPoints(starting, POINTS_CAN_BUY_ONE);
        player.execute(roll);
        assertThat(player.getState(), is(Player.State.WAITING_FOR_RESPONSE));
        player.respond(RollCommand.BuyRobot);
        assertThat(player.getPoints(), is(POINTS_CAN_BUY_ONE - Tool.Robot.getPoints()));
        assertThat(player.getToolsAmount(), is(1));
    }

    @Test
    public void should_buy_tools_until_points_is_not_enough() {
        player = Player.createPlayerWithStartingAndPoints(starting, POINTS_CAN_BUY_ONE);
        player.execute(roll);
        assertThat(player.getState(), is(Player.State.WAITING_FOR_RESPONSE));

        player.respond(RollCommand.BuyRoadBlock);
        assertThat(player.getToolsAmount(), is(1));
        assertThat(player.getPoints(), is(POINTS_CAN_BUY_ONE - Tool.RoadBlock.getPoints()));
        assertThat(player.getState(), is(Player.State.END_TURN));
    }

    @Test
    public void should_buy_tools_until_tools_amount_come_to_limit() {
        player = Player.createPlayerWithStartingAndPoints(starting, ENOUGH_POINTS);
        player.execute(roll);
        for (int i = 0; i < TOOLS_AMOUNT_LIMIT - 1; ++i) {
            player.respond(RollCommand.BuyRoadBlock);
            assertThat(player.getState(), is(Player.State.WAITING_FOR_RESPONSE));
        }
        player.respond(RollCommand.BuyRoadBlock);
        assertThat(player.getToolsAmount(), is(TOOLS_AMOUNT_LIMIT));
        assertThat(player.getState(), is(Player.State.END_TURN));
    }

    @Test
    public void should_end_turn_when_player_choose_to_exit() {
        player = Player.createPlayerWithStartingAndPoints(starting, ENOUGH_POINTS);
        player.execute(roll);
        player.respond(RollCommand.ExitToolsRoom);
        assertThat(player.getState(), is(Player.State.END_TURN));
    }
}
