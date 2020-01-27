package abalone;

import abalone.client.AbaloneClientTUI;
import abalone.client.AbaloneClientView;
import abalone.exceptions.InvalidMoveException;

public class HumanPlayer extends Player {

	// -- Instance variables -----------------------------------------

	/**
	 * view of the HumanPlayer
	 */
	AbaloneClientView view;
	
	// -- Constructors -----------------------------------------------

	/**
	 * Creates a new human player object with standard view
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
		String prompt = "> " + getName() + " (" + getColor().toString() +
				")" + ", what is your choice? ";
		String choice;

		view.showMessage(board.toString());
		choice = view.getString(prompt);
		while (! (choice.matches(board.getMovePattern()))) {
			view.showMessage("ERROR: field " + choice +
					" is not a valid choice (must match pattern " + board.getMovePattern() + ").");
			choice = view.getString(prompt);
		}
		Move move = board.parseMovePattern(getColor(), choice);

		try {
			move.isValidMove();
		} catch (InvalidMoveException e) {
			view.showMessage(e.getMessage());
			move = determineMove(board);
		}
		
		return move;
	}
	

}
