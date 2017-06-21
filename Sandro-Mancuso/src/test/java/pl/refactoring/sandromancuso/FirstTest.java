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
    @Test
    public void shouldHaveNoWinnerAtStart() throws Exception {
        Board board = new Board();

        Player winner = board.getWinner();

        assertNull(winner);
        assertTrue(true);
    }

    @Test
    public void shouldAllowToStartWithX() throws Exception {
        Board board = new Board();

        board.getNextPlayer();

        assertEquals(board.getNextPlayer(), Player.X);
    }

    @Test
    public void shouldAllowToPutX() throws Exception {
        Board board = new Board();

        assertEquals(board.getNextPlayer(), Player.X);

        board.put(1,1);

        assertEquals(board.getNextPlayer(), Player.Y);
        assertNull(board.getWinner());
    }
}
