package abalone;

import java.util.Random;

import abalone.exceptions.InvalidMoveException;

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
    private Color current;
    
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
    public /*@ pure */ Color getCurrent() {
        return current;
    }
    
    public int getNumberOfPlayers() {
    	return players.length;
    }


    // -- Commands ---------------------------------------------------


    /**
     * Resets the game. <br>
     * The board is emptied and random player becomes the current player.
     */
    public void reset() {
    	Random r = new Random();
    	current = players[r.nextInt(players.length)].getColor();
        board.reset(getNumberOfPlayers());
    }

    public void takeTurn(Move move) throws InvalidMoveException {
    	board.move(move);
    }
}
