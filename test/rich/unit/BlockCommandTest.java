package rich.unit;

import org.junit.Before;
import org.junit.Test;
import rich.GameMap;
import rich.Player;
import rich.command.BlockCommand;
import rich.command.Command;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BlockCommandTest {

    private static final int POSITION = 1;

    GameMap map;

    @Before
    public void before() {
        map = mock(GameMap.class);
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

}
