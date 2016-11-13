package rich;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RollCommandTest {

    private GameMap map;
    private Dice dice;
    private Place starting;
    private Place target;
    private Player player;
    private Command roll;

    @Before
    public void before() {
        map = mock(GameMap.class);
        dice = mock(Dice.class);
        starting = mock(Place.class);
        target = mock(Land.class);

        roll = new RollCommand(map, dice);
        player =  Player.createPlayerWithStarting(starting);

        when(dice.next()).thenReturn(1);
        when(map.move(eq(starting), eq(1))).thenReturn(target);
        assertThat(player.getState(), is(Player.State.WAITING_FOR_COMMAND));
    }

    @Test
    public void should_wait_for_response_when_roll_to_empty_land() {
        when(target.actionTo(eq(player))).thenReturn(Player.State.WAITING_FOR_RESPONSE);

        player.execute(roll);
        assertThat(player.getState(), is(Player.State.WAITING_FOR_RESPONSE));
        assertThat(player.getCurrentPlace(), is(target));
    }

    @Test
    public void should_wait_for_response_when_roll_to_self_land() {
        when(target.actionTo(eq(player))).thenReturn(Player.State.WAITING_FOR_RESPONSE);

        player.execute(roll);
        assertThat(player.getState(), is(Player.State.WAITING_FOR_RESPONSE));
        assertThat(player.getCurrentPlace(), is(target));
    }

    @Test
    public void should_end_turn_when_roll_to_others_land() {
        when(target.actionTo(eq(player))).thenReturn(Player.State.END_TURN);

        player.execute(roll);
        assertThat(player.getState(), is(Player.State.END_TURN));
        assertThat(player.getCurrentPlace(), is(target));
    }

}
