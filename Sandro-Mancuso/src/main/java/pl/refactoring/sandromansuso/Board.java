package pl.refactoring.sandromansuso;

/**
 * Created by w.krakowski on 6/21/2017.
 */
public class Board {
    private Player currentPlayer = Player.A;

    private Player[][] fields = new Player[3][3];

    public Player getWinner() {
        return playerHasRow(0, Player.A) ? Player.A:null;
    }

    private boolean playerHasRow(int row, Player player) {
        for(int i=0;i<3;i++) {
            if(fields[row][i] != player) {
                return false;
            }
        }
        return true;
    }

    public void put(int x, int y) {
        if(fields[x][y]!=null) {
            throw new RuntimeException();
        }
        fields[x][y] = currentPlayer;
        currentPlayer = currentPlayer == Player.B ? Player.A : Player.B;
    }

    public Player getNextPlayer() {
        return currentPlayer;
    }

    public Player getOccupiedField(int x, int y) {
        return fields[x][y];
    }
}
