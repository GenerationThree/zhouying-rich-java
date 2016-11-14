package rich.integration;

import org.junit.Before;
import org.junit.Test;
import rich.Dice;
import rich.GameMap;
import rich.Player;
import rich.command.RollCommand;
import rich.place.Land;
import rich.place.Place;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ResponseOfSelfLandTest {

    private static final int START_BALANCE = 3000;
    private static final int BUY_OR_UPGRADE_COST = 300;
    private static final int WITHOUT_BUDGET = 999999;
    private static final int TOP_LEVEL = 3;
    private static final int AFTER_BUY_BALANCE = START_BALANCE - BUY_OR_UPGRADE_COST;

    private GameMap map;
    private Dice dice;
    private Player player;

    private Place starting;
    private Land owingLand;
    private RollCommand roll;

    @Before
    public void before() {
        map = mock(GameMap.class);
        dice = mock(Dice.class);
        starting = mock(Place.class);
        owingLand = Land.createLandWithPrice(BUY_OR_UPGRADE_COST);
        roll = new RollCommand(map, dice);

        player = Player.createPlayerWithStartingAndBalance(starting, START_BALANCE);
        player.buy(owingLand);

        when(dice.next()).thenReturn(1);
        when(map.move(eq(starting), eq(1))).thenReturn(owingLand);
        player.execute(roll);
        assertThat(player.getState(), is(Player.State.WAITING_FOR_RESPONSE));
        assertThat(player.getBalance(), is(AFTER_BUY_BALANCE));
    }

    @Test
    public void should_end_turn_if_player_say_no_to_upgrade() {
        player.respond(RollCommand.NoToUpgrade);
        assertThat(player.getState(), is(Player.State.END_TURN));
        assertThat(owingLand.getCurrentLevel(), is(0));
    }

    @Test
    public void should_upgrade_land_if_player_say_yes() {
        assertThat(owingLand.roadToll(), is(BUY_OR_UPGRADE_COST / 2));
        player.respond(RollCommand.YesToUpgrade);
        assertThat(player.getState(), is(Player.State.END_TURN));
        assertThat(player.getBalance(), is(AFTER_BUY_BALANCE - BUY_OR_UPGRADE_COST));
        assertThat(owingLand.getCurrentLevel(), is(1));
        assertThat(owingLand.roadToll(), is(BUY_OR_UPGRADE_COST));
    }

    @Test
    public void should_not_upgrade_if_player_say_yes_but_no_enough_money() {
        owingLand.setPrice(WITHOUT_BUDGET);
        player.respond(RollCommand.YesToUpgrade);
        assertThat(player.getState(), is(Player.State.END_TURN));
        assertThat(player.getBalance(), is(AFTER_BUY_BALANCE));
        assertThat(owingLand.getCurrentLevel(), is(0));
    }

    @Test
    public void should_not_upgrade_if_player_say_yes_but_land_is_top_level() {
        owingLand.setLevel(TOP_LEVEL);
        player.respond(RollCommand.YesToUpgrade);
        assertThat(player.getState(), is(Player.State.END_TURN));
        assertThat(player.getBalance(), is(AFTER_BUY_BALANCE));
        assertThat(owingLand.getCurrentLevel(), is(TOP_LEVEL));
    }
}
