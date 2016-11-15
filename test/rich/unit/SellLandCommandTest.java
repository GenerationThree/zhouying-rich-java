package rich.unit;

import org.junit.Before;
import org.junit.Test;
import rich.GameMap;
import rich.GameMapImp;
import rich.Player;
import rich.command.Command;
import rich.command.SellLandCommand;
import rich.place.Land;
import rich.place.Place;
import rich.place.Prison;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SellLandCommandTest {

    private static final int START_BALANCE = 1000;
    private static final int LAND_PRICE = 200;
    private static final int POSITION = 1;
    private Player player;
    private Land land;
    private GameMapImp map;

    @Before
    public void before() {
        // First, prepare a player with a land
        map = mock(GameMapImp.class);
        player = Player.createPlayerWithBalanceAndAMap(map, START_BALANCE);
        land = Land.createLandWithPrice(LAND_PRICE);
        player.buy(land);

        assertThat(player.getState(), is(Player.State.WAITING_FOR_COMMAND));
        assertThat(player.getLands().size(), is(1));
    }

    @Test
    public void should_sell_land() {
        when(map.findByPosition(eq(POSITION))).thenReturn(land);
        Command sellLand = new SellLandCommand(POSITION);
        player.execute(sellLand);

        assertThat(player.getState(), is(Player.State.WAITING_FOR_COMMAND));
        assertThat(player.getLands().size(), is(0));
        assertThat(player.getBalance(), is(START_BALANCE));
        assertThat(land.getOwner(), is(nullValue()));
        assertThat(land.getCurrentLevel(), is(0));
        assertThat(land.roadToll(), is(0));
    }

    @Test
    public void should_not_sell_land_when_place_is_not_a_land() {
        Place prison = new Prison();
        when(map.findByPosition(eq(POSITION))).thenReturn(prison);
        Command sellLand = new SellLandCommand(POSITION);
        player.execute(sellLand);
        assertThat(player.getState(), is(Player.State.WAITING_FOR_COMMAND));
    }

    @Test
    public void should_not_sell_land_when_land_not_belongs_to_this_player() {
        Land otherLand = new Land();
        otherLand.setOwner(new Player());
        when(map.findByPosition((eq(POSITION)))).thenReturn(otherLand);
        Command sellLand = new SellLandCommand(POSITION);
        player.execute(sellLand);
        assertThat(player.getState(), is(Player.State.WAITING_FOR_COMMAND));
    }
}

