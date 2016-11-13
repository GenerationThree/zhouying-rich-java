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

public class WaitingForCommandToEndTurnTest {

    private static final int BALANCE_CAN_AFFORD_ROAD_TOLL = 10000;
    private static final int BALANCE_CANT_AFFORD_ROAD_TOLL = 10;
    private GameMap map;
    private Dice dice;

    private Player player;
    private RollCommand roll;

    private Place starting;
    private Place target;

    @Before
    public void before() {
        map = mock(GameMap.class);
        dice = mock(Dice.class);
        starting = mock(Place.class);
        roll = new RollCommand(map, dice);

        when(dice.next()).thenReturn(1);
        when(map.move(eq(starting), eq(1))).thenReturn(target);
    }

    @Test
    public void should_end_turn_when_roll_to_others_land_and_can_afford_road_toll() {
        player = Player.createPlayerWithStartingAndBalance(starting, BALANCE_CAN_AFFORD_ROAD_TOLL);
        Land othersLand = new Land();
        othersLand.setOwner(new Player());
        when(map.move(eq(starting), eq(1))).thenReturn(othersLand);

        assertThat(player.getState(), is(Player.State.WAITING_FOR_COMMAND));
        player.execute(roll);
        assertThat(player.getState(), is(Player.State.END_TURN));
        assertThat(player.getBalance(), is(player.getBalance() - othersLand.roadToll()));
    }

    @Test
    public void should_game_over_when_roll_to_others_land_but_cant_afford_road_toll() {
        player = Player.createPlayerWithStartingAndBalance(starting, BALANCE_CANT_AFFORD_ROAD_TOLL);
        Land othersLand = Land.createLandWithPrice(300);
        othersLand.setOwner(new Player());
        when(map.move(eq(starting), eq(1))).thenReturn(othersLand);

        assertThat(player.getState(), is(Player.State.WAITING_FOR_COMMAND));
        player.execute(roll);
        assertThat(player.getState(), is(Player.State.GAME_OVER));
    }
}
