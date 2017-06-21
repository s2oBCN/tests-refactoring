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

    @Test(expected = RuntimeException.class)
    public void shouldFailOnPuttingOnOccupiedField() throws Exception {
        BOARD.put(1,1);
        BOARD.put(1,1);
    }

    @Test
    public void shouldWinPlayerAOnRows() throws Exception {

        BOARD.put(0,0);
        BOARD.put(1,0);
        BOARD.put(0,1);
        BOARD.put(1,1);
        BOARD.put(0,2);
        assertEquals(Player.A, BOARD.getWinner());
            }

    @Test
    public void shouldWinPlayerAOnThirdRow() throws Exception {

        BOARD.put(2,0);
        BOARD.put(1,0);
        BOARD.put(2,1);
        BOARD.put(1,1);
        BOARD.put(2,2);
        assertEquals(Player.A, BOARD.getWinner());
    }

    @Test
    public void shouldWinPlayerAOnSecondRow() throws Exception {

        BOARD.put(1,0);
        BOARD.put(0,0);
        BOARD.put(1,1);
        BOARD.put(0,1);
        BOARD.put(1,2);
        assertEquals(Player.A, BOARD.getWinner());
    }
}
