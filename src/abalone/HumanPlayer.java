package abalone;

import abalone.client.AbaloneClientTUI;
import abalone.client.AbaloneClientView;

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
		view = new AbaloneClientTUI();
	}

	// -- Commands ---------------------------------------------------

	/**
	 * Converts the following.
	 * - [A-I] to [0-8]
	 * - [a-i] to [0-8]
	 * - [1-9] to [0-8]
	 * @requires letter.matches([A-Ia-i1-9])
	 * @ensures return value matches [0-8]
	 * @param letter
	 * @return corresponding integer
	 */
	public int toInt(char letter) {
		if (Character.isDigit(letter)) {
			return Character.getNumericValue(letter) - 1;
		}
		if (letter >= 'a' && letter <= 'z') {
			return letter - 97;
		}
		return letter - 65;
	}
	
	public Selection determineMove(Board board) {
		String prompt = "> " + getName() + " (" + getColor().toString() + ")" + ", what is your choice? ";
		String choice;
		Selection selection = null;
		
		choice = view.getString(prompt);
		if (choice.matches(PATTERN)) {
			selection = new Selection(board, toInt(choice.charAt(0)), toInt(choice.charAt(1)), toInt(choice.charAt(3)), toInt(choice.charAt(4)), toInt(choice.charAt(6)), toInt(choice.charAt(7)));
		}

		while (! (choice.matches(PATTERN) && selection.isValidSelection())) {
			view.showMessage("ERROR: field " + choice + " is no valid choice (must match pattern " + PATTERN + ").");
			choice = view.getString(prompt);
			if (choice.matches(PATTERN)) {
				selection = new Selection(board, toInt(choice.charAt(0)), toInt(choice.charAt(1)), toInt(choice.charAt(3)), toInt(choice.charAt(4)), toInt(choice.charAt(6)), toInt(choice.charAt(7)));
			}
		}
		
		return selection;
	}

}
