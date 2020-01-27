package abalone;

/**
 * An enum representing the color of a marble.
 * 
 * @authors Daan Pluister, Bozhidar Petrov
 */
public enum Color {

	WHITE, BLACK, BLUE, RED;

	/**
	 * toString method of enum Marble.
	 */
	public String toString() {
		switch (this) {
		case WHITE:
			return "W";
		case BLACK:
			return "B";
		case BLUE:
			return "P";
		case RED:
			return "R";
		default:
			return " ";
		}
	}

}
