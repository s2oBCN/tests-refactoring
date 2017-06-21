package pl.refactoring.sandromansuso;

/**
 * Created by w.krakowski on 6/21/2017.
 */
public class Board {

    private Player currentPlayer = Player.X;

    public Player getWinner() {
        return null;
    }

    public void put(int i, int i1) {
        currentPlayer = Player.Y;

    }


    public Player getNextPlayer() {
        return currentPlayer;
    }
}
