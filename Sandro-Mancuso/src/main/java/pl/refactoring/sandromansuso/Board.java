package pl.refactoring.sandromansuso;

/**
 * Created by w.krakowski on 6/21/2017.
 */
public class Board {
    private Player currentPlayer = Player.A;

    public Player getWinner() {
        return null;
    }

    public void put(int i, int i1) {
        currentPlayer = currentPlayer == Player.B ? Player.A : Player.B;

    }


    public Player getNextPlayer() {
        return currentPlayer;
    }
}
