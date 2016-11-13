package rich;

import org.junit.Before;
import org.junit.Test;
import rich.command.Command;
import rich.command.Response;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PlayerTest {

    Player player;
    private Command command;

    @Before
    public void before() {
        player = new Player();
        command = mock(Command.class);
    }
    
    @Test
    public void should_remain_in_waiting_for_command_after_executing_command_no_need_response() {
        when(command.execute(eq(player))).thenReturn(Player.State.WAITING_FOR_COMMAND);

        assertThat(player.getState(), is(Player.State.WAITING_FOR_COMMAND));

        player.execute(command);

        assertThat(player.execute(command), is(Player.State.WAITING_FOR_COMMAND));
    }

    @Test
    public void should_be_in_waiting_for_response_after_executing_command_needs_response() {
        when(command.execute(eq(player))).thenReturn(Player.State.WAITING_FOR_RESPONSE);

        assertThat(player.getState(), is(Player.State.WAITING_FOR_COMMAND));

        player.execute(command);

        assertThat(player.execute(command), is(Player.State.WAITING_FOR_RESPONSE));
    }

    @Test
    public void should_be_end_turn_after_response() {
        when(command.execute(eq(player))).thenReturn(Player.State.WAITING_FOR_RESPONSE);

        assertThat(player.getState(), is(Player.State.WAITING_FOR_COMMAND));
        player.execute(command);
        assertThat(player.getState(), is(Player.State.WAITING_FOR_RESPONSE));


        Response response = mock(Response.class);
        when(command.respondWith(eq(player), eq(response))).thenReturn(Player.State.END_TURN);

        player.respond(response);
        assertThat(player.getState(), is(Player.State.END_TURN));
    }
}
