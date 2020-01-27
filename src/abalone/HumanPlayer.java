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
	public HumanPlayer(AbaloneClientView view, String name, Color color) {
		super(name, color);
		this.view = view;
	}

	// -- Commands ---------------------------------------------------
	
	public Move determineMove(Board board) {
		String prompt = "> " + getName() + " (" + getColor().toString() +
				")" + ", what is your choice? ";
		String choice;

		view.showMessage(board.toString());
		choice = view.getString(prompt);
		
		Move move;
		try {
			move = board.parseMovePattern(getColor(), choice);
			move.isValidMove();
		} catch (InvalidMoveException e1) {
			view.showMessage(e1.getMessage());
			move = determineMove(board);
		}
		
		return move;
	}
	

}
