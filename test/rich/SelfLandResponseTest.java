package rich;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SelfLandResponseTest {

    private static final int START_BALANCE = 3000;
    private static final int WITHIN_BUDGET = 200;
    private static final int WITHOUT_BUDGET = 100000000;
    private static final int TOP_LEVEL = 3;

    private GameMap map;
    private Dice dice;
    private Land targetLand;
    private Land starting;
    private Player player;
    private Command roll;

    @Before
    public void before() {
        map = mock(GameMap.class);
        dice = mock(Dice.class);
        starting = new Land();
        targetLand = Land.createLandWithPrice(WITHIN_BUDGET);
        roll = new RollCommand(map, dice);

        when(dice.next()).thenReturn(1);
        when(map.move(eq(starting), eq(1))).thenReturn(targetLand);

        player = Player.createPlayerWithBalance(starting, START_BALANCE);

        // First, player buy an empty land
        assertThat(targetLand.roadToll(), is(0));
        player.execute(roll);
        player.respond(RollCommand.YesToBuy);

        assertThat(player.getCurrentLand(), is(targetLand));
        assertThat(player.getBalance(), is(START_BALANCE - WITHIN_BUDGET));

        assertThat(targetLand.getOwner(), is(player));
        assertThat(targetLand.getCurrentLevel(), is(0));
        assertThat(targetLand.roadToll(), is(WITHIN_BUDGET / 2));

        // Then, player come to his own land
        when(map.move(any(Land.class), eq(1))).thenReturn(targetLand);
        player.execute(roll);
    }

    @Test
    public void should_not_upgrade_land_if_player_say_no() {
        player.respond(RollCommand.NoToUpgrade);

        assertThat(player.getBalance(), is(START_BALANCE - WITHIN_BUDGET));
        assertThat(player.getState(), is(Player.State.END_TURN));

        assertThat(targetLand.getCurrentLevel(), is(0));
        assertThat(targetLand.roadToll(), is(WITHIN_BUDGET / 2));
    }

    @Test
    public void should_upgrade_land_if_player_say_yes() {
        player.respond(RollCommand.YesToUpgrade);

        assertThat(player.getBalance(), is(START_BALANCE - WITHIN_BUDGET - WITHIN_BUDGET));
        assertThat(player.getState(), is(Player.State.END_TURN));

        assertThat(targetLand.getCurrentLevel(), is(1));
        assertThat(targetLand.roadToll(), is(WITHIN_BUDGET));
    }

    @Test
    public void should_not_upgrade_if_player_say_yes_but_without_budget() {
        targetLand.setPrice(WITHOUT_BUDGET);
        player.respond(RollCommand.YesToUpgrade);

        assertThat(player.getBalance(), is(START_BALANCE - WITHIN_BUDGET));
        assertThat(player.getState(), is(Player.State.END_TURN));

        assertThat(targetLand.getCurrentLevel(), is(0));
        assertThat(targetLand.roadToll(), is(WITHOUT_BUDGET / 2));
    }

    @Test
    public void should_not_upgrade_if_player_say_yes_but_target_land_can_not_be_upgraded() {
        targetLand.setLevel(TOP_LEVEL);
        player.respond(RollCommand.YesToUpgrade);

        assertThat(player.getBalance(), is(START_BALANCE - WITHIN_BUDGET));
        assertThat(player.getState(), is(Player.State.END_TURN));

        assertThat(targetLand.getCurrentLevel(), is(TOP_LEVEL));
        assertThat(targetLand.roadToll(), is(WITHIN_BUDGET * 4));
    }

}
