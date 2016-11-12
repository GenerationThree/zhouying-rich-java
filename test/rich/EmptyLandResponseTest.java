package rich;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EmptyLandResponseTest {

    private static final int START_BALANCE = 3000;
    private static final int WITH_IN_BUDGET = 1;

    private GameMap map;
    private Dice dice;
    private Land emptyLand;
    private Land starting;
    private Player player;
    private Command roll;

    @Before
    public void before() {
        map = mock(GameMap.class);
        dice = mock(Dice.class);

        starting = new Land();
        emptyLand = Land.createLandWithPrice(WITH_IN_BUDGET);

        roll = new RollCommand(map, dice);
        when(dice.next()).thenReturn(1);
        when(map.move(eq(starting), eq(1))).thenReturn(emptyLand);
    }

    @Test
    public void should_buy_land_if_player_response_yes() {

        player = Player.createPlayerWithBalance(starting, START_BALANCE);
        player.execute(roll);
        assertThat(player.getState(), is(Player.State.WAITING_FOR_RESPONSE));
        player.respond(Response.YSE_TO_BUY);

        assertThat(player.getState(), is(Player.State.END_TURN));
        assertThat(player.getLands().size(), is(1));
        assertThat(player.getBalance(), is(START_BALANCE - WITH_IN_BUDGET));
        assertThat(emptyLand.getCurrentLevel(), is(0));
        assertThat(emptyLand.getOwner(), is(player));
    }
}
