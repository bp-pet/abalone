package abalone;

import java.util.Random;

public class Game {
	 // -- Instance variables -----------------------------------------

    /*@
       private invariant board != null;
     */
    /**
     * The board.
     */
    private Board board;

    /**
     * Index of the current player.
     */
    private Marble current;
    
    /**
     * Array of players.
     */
    private Player[] players;

    // -- Constructors -----------------------------------------------
    
    /**
     * creates a new game with new board with an array with players. The starting player is random.
     * @requires 2 <= players.length <= 4
     * @param players array of players
     */
    public Game(Player[] players) {
        board = new Board();
        this.players = players;
        reset();
    }

    // -- Queries ----------------------------------------------------

    /**
     * Returns the board.
     */
    public /*@ pure */ Board getBoard() {
        return board;
    }

    /**
     * Returns the mark of the player whose turn it is.
     */
    public /*@ pure */ Marble getCurrent() {
        return current;
    }


    // -- Commands ---------------------------------------------------


    /**
     * Resets the game. <br>
     * The board is emptied and random player becomes the current player.
     */
    public void reset() {
    	Random r = new Random();
    	current = players[r.nextInt(players.length)].getMarble();
        board.reset();
    }

    /*@
       requires 0 <= i & i < Board.DIM * Board.DIM;
       requires this.getBoard().isEmptyField(i);
     */
    /**
     * Sets the current mark in field i. 
     * Passes the turn to the other mark.
     * @param    i the index of the field where to place the mark
     */
    public void takeTurn(int i) {
        board.setField(i, current);
        current = current.next();
    }
}
