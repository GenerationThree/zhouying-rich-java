package rich.unit;

import org.junit.Test;
import rich.Player;
import rich.command.Command;
import rich.command.HelpCommand;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class HelpCommandTest {

    @Test
    public void should_show_help_info() {
        Player player = new Player();
        Command help = new HelpCommand();
        player.execute(help);
        assertThat(player.getState(), is(Player.State.WAITING_FOR_COMMAND));
    }
}
