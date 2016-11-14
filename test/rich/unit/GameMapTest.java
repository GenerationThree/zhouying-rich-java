package rich.unit;

import org.junit.Before;
import org.junit.Test;
import rich.GameConstant;
import rich.GameMapImp;
import rich.place.Land;
import rich.place.Place;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class GameMapTest {

    private GameMapImp gameMap;

    @Before
    public void before() {
        gameMap = new GameMapImp();
    }

    @Test
    public void should_initialize_a_map() {
        assertThat(gameMap.length(), is(GameConstant.PLACE_AMOUNT));
    }

    @Test
    public void should_move_with_step() {
        Place target = gameMap.move(gameMap.starting(), 1);
        assertThat(target instanceof Land, is(true));
    }

    @Test
    public void should_find_place_by_position() {
        Place target = gameMap.findBy(0);
        assertThat(target, is(gameMap.starting()));
    }

    @Test
    public void should_set_block_and_stop_player() {
        int position = 3;
        gameMap.putBlock(position);
        Place expectedTarget = gameMap.findBy(position);
        assertThat(expectedTarget.isToolAttached(), is(true));

        // when there is a block, can't across it
        Place target = gameMap.move(gameMap.starting(), 6);
        assertThat(target, is(expectedTarget));
        assertThat(expectedTarget.isToolAttached(), is(false));
    }

    @Test
    public void should_set_bomb_and_send_player_to_hospital() {
        int position = 4;
        gameMap.putBomb(position);
        Place expectedTarget = gameMap.hospital();

        Place target = gameMap.move(gameMap.starting(), 6);
        assertThat(target, is(expectedTarget));
    }

    @Test
    public void should_not_set_block_again() {
        int position = 3;
        gameMap.putBlock(position);
        assertThat(gameMap.putBlock(position), is(false));
    }

}
