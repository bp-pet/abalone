package abalone;

import ss.utils.TextIO;

public class HumanPlayer extends Player {

	// -- Constructors -----------------------------------------------

    /**
     * Creates a new human player object.
     * @requires name is not null
     * @requires marble is of enum Marble
     * @ensures the Name of this player will be name
     * @ensures the Mark of this player will be mark
     */
    public HumanPlayer(String name, Marble marble) {
        super(name, marble);
    }

    // -- Commands ---------------------------------------------------

    /**
     * Asks the user to input the field where to place the next marble. This is
     * done using the standard input/output.
     * @requires board has marble of player
     * @ensures the returned in is a valid field index and that field is empty 
     * @param board the game board
     * @return the player's chosen field
     */
    public int determineMove(Board board) {
        String prompt = "> " + getName() + " (" + getMarble().toString() + ")"
                + ", what is your choice? ";
        
        System.out.println(prompt);
        int choice = TextIO.getInt();
        
        boolean valid = board.isField(choice) && board.isEmptyField(choice);
        while (!valid) {
            System.out.println("ERROR: field " + choice
                    + " is no valid choice.");
            System.out.println(prompt);
            choice = TextIO.getInt();
            valid = board.isField(choice) && board.isEmptyField(choice);
        }
        return choice;
    }

	
}
