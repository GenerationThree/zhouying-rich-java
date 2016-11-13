package rich;

import org.junit.Before;
import org.junit.Test;
import rich.command.Command;
import rich.command.RollCommand;
import rich.place.Place;
import rich.place.ToolsRoom;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ToolsRoomResponseTest {

    private static final int START_POINTS = 40;
    private static final int WITHIN_BUDGET = 30;
    private static final int WITHOUT_BUDGET = 50;
    private static final int POINTS_CAN_BUY_ONE = 40;

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
        player = Player.createPlayerWithStartingAndPoints(starting, 0);
        player.execute(roll);
        assertThat(player.getState(), is(Player.State.END_TURN));
    }

    @Test
    public void should_buy_tools_until_tools_quantity_equals_ten() {
        player = Player.createPlayerWithStartingAndPoints(starting, POINTS_CAN_BUY_ONE);
        player.execute(roll);
        assertThat(player.getState(), is(Player.State.WAITING_FOR_RESPONSE));

        player.respond(RollCommand.BuyRoadBlock);
        assertThat(player.getToolsAmount(), is(1));
    }
}
