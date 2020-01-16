package abalone;

import abalone.client.AbaloneClientTUI;
import abalone.client.AbaloneClientView;
import abalone.exceptions.InvalidMoveException;

public class HumanPlayer extends Player {

	// -- Constants --------------------------------------------------
	
	public static final String PATTERN = "^([A-Ia-i][1-9][ ,.]){2}[A-Ia-i][1-9]$";
	
	// -- Instance variables -----------------------------------------

	/**
	 * view of the HumanPlayer
	 */
	AbaloneClientView view;
	
	// -- Constructors -----------------------------------------------

	/**
	 * Creates a new human player object with stardard view
	 * 
	 * @requires name is not null
	 * @requires marble is of enum Marble
	 * @ensures the Name of this player will be name
	 * @ensures the Mark of this player will be mark
	 */
	public HumanPlayer(String name, Color color) {
		super(name, color);
		view = new AbaloneClientTUI(null);
	}

	// -- Commands ---------------------------------------------------
	
	public Move determineMove(Board board) {
		String prompt = "> " + getName() + " (" + getColor().toString() + ")" + ", what is your choice? ";
		String choice;
		
		choice = view.getString(prompt);
		while (! (choice.matches(PATTERN))) {
			view.showMessage("ERROR: field " + choice + " is no valid choice (must match pattern " + PATTERN + ").");
			choice = view.getString(prompt);
		}

		Board copy = board.deepCopy();
		Move move = new Move(copy, getColor(), board.getColFromLetter(choice.charAt(0)), board.getColFromLetter(choice.charAt(1)), board.getColFromLetter(choice.charAt(3)), board.getColFromLetter(choice.charAt(4)), board.getColFromLetter(choice.charAt(6)), board.getColFromLetter(choice.charAt(7)));
		
		try {
			move.perform();
		} catch (InvalidMoveException e) {
			view.showMessage(e.getMessage());
			move = determineMove(board);
		}
		
		return new Move(board, getColor(), board.getColFromLetter(choice.charAt(0)), board.getColFromLetter(choice.charAt(1)), board.getColFromLetter(choice.charAt(3)), board.getColFromLetter(choice.charAt(4)), board.getColFromLetter(choice.charAt(6)), board.getColFromLetter(choice.charAt(7)));
	}

}
