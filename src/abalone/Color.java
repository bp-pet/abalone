package abalone;

public enum Color {

	WHITE, BLACK, BLUE, RED;

	public Color next(int numOfPlayers, boolean clockwise) {
		switch (numOfPlayers) {
		case 2:
			switch (this) {
			case WHITE:
				return BLACK;
			default:
				return WHITE;
			}
		default:
			if (clockwise) {
				switch (this) {
				case BLUE:
					return WHITE;
				case BLACK:
					return BLUE;
				case RED:
					return BLACK;
				default: // WHITE
					if (numOfPlayers == 4) {
						return RED;
					}
					return BLACK;
				}
			} else {
				switch (this) {
				case BLUE:
					return BLACK;
				case WHITE:
					return BLUE;
				case RED:
					return WHITE;
				default: // BLACK
					if (numOfPlayers == 4) {
						return RED;
					}
					return WHITE;
				}
			}
		}
	}

	/**
	 * 
	 * @return (WHITE,BLACK,BLUE,RED) => (1,2,3,4)
	 */
	public int getInt() {
		switch (this) {
		case WHITE:
			return 0;
		case BLACK:
			return 1;
		case BLUE:
			return 2;
		case RED:
			return 3;
		default:
			return 0;
		}
	}

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
