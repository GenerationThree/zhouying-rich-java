package rich.unit;

import org.junit.Before;
import org.junit.Test;
import rich.Dice;
import rich.GameMap;
import rich.Player;
import rich.command.Command;
import rich.command.RollCommand;
import rich.place.Place;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RollCommandTest {

    private GameMap map;
    private Dice dice;

    private Place target;

    private Player player;
    private Command roll;

    @Before
    public void before() {
        map = mock(GameMap.class);
        dice = mock(Dice.class);
        target = mock(Place.class);

        roll = new RollCommand(map, dice);
        player = new Player();

        when(dice.next()).thenReturn(1);
        when(map.move(any(), eq(1))).thenReturn(target);
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

    @Test
    public void should_end_turn_when_roll_to_tools_room_but_can_not_buy() {
        when(target.actionTo(eq(player))).thenReturn(Player.State.END_TURN);

        player.execute(roll);
        assertThat(player.getState(), is(Player.State.END_TURN));
        assertThat(player.getCurrentPlace(), is(target));
    }

    @Test
    public void should_waiting_for_response_when_roll_to_tools_room_and_can_buy() {
        when(target.actionTo(eq(player))).thenReturn(Player.State.WAITING_FOR_RESPONSE);

        player.execute(roll);
        assertThat(player.getState(), is(Player.State.WAITING_FOR_RESPONSE));
        assertThat(player.getCurrentPlace(), is(target));
    }

    @Test
    public void should_end_turn_when_roll_to_gifts_room_but_choose_a_wrong_one() {
        when(target.actionTo(eq(player))).thenReturn(Player.State.END_TURN);

        player.execute(roll);
        assertThat(player.getState(), is(Player.State.END_TURN));
        assertThat(player.getCurrentPlace(), is(target));
    }

    @Test
    public void should_waiting_for_response_when_roll_to_gifts_room_and_choose_a_correct_one() {
        when(target.actionTo(eq(player))).thenReturn(Player.State.WAITING_FOR_RESPONSE);

        player.execute(roll);
        assertThat(player.getState(), is(Player.State.WAITING_FOR_RESPONSE));
        assertThat(player.getCurrentPlace(), is(target));
    }

    @Test
    public void should_end_turn_when_roll_to_prison() {
        when(target.actionTo(eq(player))).thenReturn(Player.State.END_TURN);

        player.execute(roll);
        assertThat(player.getState(), is(Player.State.END_TURN));
        assertThat(player.getCurrentPlace(), is(target));
    }

    @Test
    public void should_end_turn_when_roll_to_road_block() {
        when(target.actionTo(eq(player))).thenReturn(Player.State.END_TURN);

        player.execute(roll);
        assertThat(player.getState(), is(Player.State.END_TURN));
        assertThat(player.getCurrentPlace(), is(target));
    }

    @Test
    public void should_end_turn_when_roll_to_bomb() {
        when(target.actionTo(eq(player))).thenReturn(Player.State.END_TURN);

        player.execute(roll);
        assertThat(player.getState(), is(Player.State.END_TURN));
        assertThat(player.getCurrentPlace(), is(target));
    }

}
