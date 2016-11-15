package rich.unit;

import org.junit.Before;
import org.junit.Test;
import rich.Dice;
import rich.GameMapImp;
import rich.Player;
import rich.command.BlockCommand;
import rich.command.Command;
import rich.command.RollCommand;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BlockCommandTest {

    private static final int POSITION = 1;

    GameMapImp map;
    Dice dice;

    @Before
    public void before() {
        map = mock(GameMapImp.class);
        dice = mock(Dice.class);
    }

    @Test
    public void should_block_a_position() {
        when(map.putBlock(eq(POSITION))).thenReturn(true);

        Player player = Player.createPlayerWithBalanceAndAMap(map, 0);
        Command block = new BlockCommand(POSITION);

        player.execute(block);
        assertThat(player.getState(), is(Player.State.WAITING_FOR_COMMAND));
    }

    @Test
    public void should_not_block_a_position() {
        when(map.putBlock(eq(POSITION))).thenReturn(false);

        Player player = Player.createPlayerWithBalanceAndAMap(map, 0);
        Command block = new BlockCommand(POSITION);

        player.execute(block);
        assertThat(player.getState(), is(Player.State.WAITING_FOR_COMMAND));
    }

    @Test
    public void should_not_block_a_place_when_there_is_another_player() {
        GameMapImp realMap = new GameMapImp();
        Player player = Player.createPlayerWithStartingAndBalance(realMap.starting(), 0);

        when(dice.next()).thenReturn(3);
        player.execute(new RollCommand(realMap, dice));
        assertThat(player.getCurrentPlace().isPlayerOn(), is(true));
        player.execute(new BlockCommand(0));
    }

}
