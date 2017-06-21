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
public class BoardTest {
    public final Board BOARD = new Board();

    @Test
    public void shouldHaveNoWinnerAtStart() throws Exception {
        Player winner = BOARD.getWinner();

        assertNull(winner);
        assertTrue(true);
    }

    @Test
    public void shouldAllowToStartWithPlayerA() throws Exception {
        BOARD.getNextPlayer();

        assertEquals(BOARD.getNextPlayer(), Player.A);
    }

    @Test
    public void shouldAllowToPutX() throws Exception {
        assertEquals(BOARD.getNextPlayer(), Player.A);

        BOARD.put(1,1);

        assertEquals(BOARD.getNextPlayer(), Player.B);
        assertNull(BOARD.getWinner());
    }

    @Test
    public void shouldAllowToPutYAfterPutX() throws Exception {
        assertEquals(BOARD.getNextPlayer(), Player.A);

        BOARD.put(1,1);
        assertEquals(BOARD.getNextPlayer(), Player.B);

        BOARD.put(1,2);
        assertEquals(BOARD.getNextPlayer(), Player.A);

        assertNull(BOARD.getWinner());
    }

    @Test
    public void shouldReturnOccupiedField() throws Exception {
        assertEquals(BOARD.getNextPlayer(), Player.A);

        BOARD.put(1,1);
        assertEquals(BOARD.getNextPlayer(), Player.B);

        Player field = BOARD.getOccupiedField(1,1);

        assertEquals(Player.A, field);

    }

    @Test(expected = RuntimeException.class)
    public void shouldFailOnPuttingOnOccupiedField() throws Exception {
        assertEquals(BOARD.getNextPlayer(), Player.A);

        BOARD.put(1,1);
        BOARD.put(1,1);

    }

    @Test
    public void shouldWinPlayerAOnRows() throws Exception {
        assertEquals(BOARD.getNextPlayer(), Player.A);

        BOARD.put(0,0);
        BOARD.put(1,0);
        BOARD.put(0,1);
        BOARD.put(1,1);
        BOARD.put(0,2);
        assertEquals(Player.A, BOARD.getWinner());
            }
}
