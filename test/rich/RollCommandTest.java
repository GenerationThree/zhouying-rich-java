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
    private Land emptyLand;
    private Land starting;
    private Land othersLand;
    private Land selfLand;
    private Player player;
    private Command roll;

    @Before
    public void before() {
        map = mock(GameMap.class);
        dice = mock(Dice.class);
        starting = mock(Land.class);
        emptyLand = mock(Land.class);
        othersLand = mock(Land.class);
        selfLand = mock(Land.class);
        roll = new RollCommand(map, dice);
        when(dice.next()).thenReturn(1);
    }

    @Test
    public void should_wait_for_response_when_roll_to_empty_land() {
        when(map.move(eq(starting), eq(1))).thenReturn(emptyLand);
        Player player =  Player.createPlayerWithStarting(starting);

        assertThat(player.getState(), is(Player.State.WAITING_FOR_COMMAND));
        player.execute(roll);
        assertThat(player.getState(), is(Player.State.WAITING_FOR_RESPONSE));
        assertThat(player.getCurrentLand(), is(emptyLand));
    }

    @Test
    public void should_wait_for_response_when_roll_to_self_land() {
        when(map.move(eq(starting), eq(1))).thenReturn(selfLand);
        Player player =  Player.createPlayerWithStarting(starting);

        assertThat(player.getState(), is(Player.State.WAITING_FOR_COMMAND));
        player.execute(roll);
        assertThat(player.getState(), is(Player.State.WAITING_FOR_RESPONSE));
        assertThat(player.getCurrentLand(), is(selfLand));
    }

    @Test
    public void should_end_turn_when_roll_to_others_land() {
        when(map.move(eq(starting), eq(1))).thenReturn(othersLand);
        when(othersLand.getOwner()).thenReturn(new Player());
        Player player =  Player.createPlayerWithStarting(starting);

        assertThat(player.getState(), is(Player.State.WAITING_FOR_COMMAND));
        player.execute(roll);
        assertThat(player.getState(), is(Player.State.END_TURN));
        assertThat(player.getCurrentLand(), is(othersLand));
    }

}
