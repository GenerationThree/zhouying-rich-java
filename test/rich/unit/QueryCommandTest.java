package rich.unit;

import org.junit.Test;
import rich.Player;
import rich.command.Command;
import rich.command.QueryCommand;
import rich.helper.PlayerFixture;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class QueryCommandTest {

    @Test
    public void should_show_query_player_asset_information() {
        Player player = PlayerFixture.getPlayerWithSomeAssets();
        Command query = new QueryCommand();
        assertThat(player.getState(), is(Player.State.WAITING_FOR_COMMAND));
        player.execute(query);
        assertThat(player.getState(), is(Player.State.WAITING_FOR_COMMAND));
        assertThat(player.query(), not(nullValue()));
    }
}
