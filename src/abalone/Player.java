package abalone;


	
public abstract class Player {
	// -- Instance variables -----------------------------------------

    private String name;
    private Color color;

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
     * Determines the field for the next move.
     * @requires board is not null and not full
     * @ensures returned value satisfies Selection.isValidSelection();
     * @param board the current game board
     * @return the player's chosen selection
     */
    public abstract Selection determineMove(Board board);
}
