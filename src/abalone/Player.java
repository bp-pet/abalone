package abalone;

/**
 * An abstact class for an Abalone player, generalization of
 * HumanPlayer and ComputerPlayer.
 * 
 * @authors Daan Pluister, Bozhidar Petrov
 */
public abstract class Player {
	// -- Instance variables -----------------------------------------

	protected String name;
    protected Color color;

    // -- Constructors -----------------------------------------------

    /**
     * Creates a new Player object.
     * @requires name is not null
     * @requires mark is either XX or OO
     * @ensures the Name of this player will be name
     * @ensures the Mark of this player will be mark
     */
    public Player(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    // -- Queries ----------------------------------------------------

    /**
     * Returns the name of the player.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the mark of the player.
     */
    public Color getColor() {
        return color;
    }
    
    /**
     * Set the color of the player.
     */
    public void setColor(Color color) {
    	this.color = color;
    }

    /**
     * Determines the Move for the next move.
     * @requires board is not null and not full
     * @ensures returned Move is valid;
     * @param board the current game board
     * @param stateOfGame includes the toString of the board, how many
     * moves have been performed/are left and what the current score is
     * @return the player's chosen Move
     */
    public abstract Move determineMove(Board board, String stateOfGame);
}
