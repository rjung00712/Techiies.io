package io.techies.com.puzzle_8;

/**
 * Created by Richard on 4/4/17.
 */

public class Player {
    private String userName;
    private int moves = 0;

    public Player(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getMoves() {
        return moves;
    }

    public void setMoves(int moves) {
        this.moves = moves;
    }
}
