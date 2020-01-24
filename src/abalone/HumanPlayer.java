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
		while (! (choice.matches(PATTERN))) {
			view.showMessage("ERROR: field " + choice +
					" is not a valid choice (must match pattern " + PATTERN + ").");
			choice = view.getString(prompt);
		}
		Move move = parseChoice(board, choice);

		try {
			move.isValidMove();
		} catch (InvalidMoveException e) {
			view.showMessage(e.getMessage());
			move = determineMove(board);
		}
		
		return move;
	}
	
	/**
	 * 
	 * @requires choice.matches(PATTERN);
	 * @param board
	 * @param choice
	 * @return
	 */
	public Move parseChoice(Board board, String choice) {
		return new Move(board, getColor(), board.getRowFromLetter(choice.charAt(0)),
				board.getColFromLetter(choice.charAt(1)),
				board.getRowFromLetter(choice.charAt(3)),
				board.getColFromLetter(choice.charAt(4)),
				board.getRowFromLetter(choice.charAt(6)),
				board.getColFromLetter(choice.charAt(7)));
	}

}
