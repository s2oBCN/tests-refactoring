package pl.refactoring.sandromancuso;

import org.junit.Test;
import pl.refactoring.sandromansuso.Board;
import pl.refactoring.sandromansuso.Player;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by w.krakowski on 6/21/2017.
 */
public class FirstTest {
    public static final Board BOARD = new Board();

    @Test
    public void shouldHaveNoWinnerAtStart() throws Exception {
        Player winner = BOARD.getWinner();

        assertNull(winner);
        assertTrue(true);
    }

    @Test
    public void shouldAllowToStartWithPlayerA() throws Exception {
        BOARD.getNextPlayer();

        assertEquals(BOARD.getNextPlayer(), Player.X);
    }

    @Test
    public void shouldAllowToPutX() throws Exception {
        assertEquals(BOARD.getNextPlayer(), Player.X);

        BOARD.put(1,1);

        assertEquals(BOARD.getNextPlayer(), Player.Y);
        assertNull(BOARD.getWinner());
    }
}
