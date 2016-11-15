package rich.unit;

import org.junit.Before;
import org.junit.Test;
import rich.Dice;
import rich.GameMapImp;
import rich.Player;
import rich.command.BombCommand;
import rich.command.Command;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BombCommandTest {

    private static final int POSITION = 1;

    GameMapImp map;
    Dice dice;

    @Before
    public void before() {
        map = mock(GameMapImp.class);
        dice = mock(Dice.class);
    }

    @Test
    public void should_set_bomb_on_a_position() {
        when(map.putBomb(eq(POSITION))).thenReturn(true);

        Player player = Player.createPlayerWithBalanceAndAMap(map, 0);
        Command bomb = new BombCommand(POSITION);

        player.execute(bomb);
        assertThat(player.getState(), is(Player.State.WAITING_FOR_COMMAND));
    }


}
