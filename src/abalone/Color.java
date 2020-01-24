package abalone;

public enum Color {

	WHITE, BLACK, BLUE, RED;

	public Color next(int numOfPlayers) {
		switch (numOfPlayers) {
		case 2:
			switch (this) {
				case WHITE:
					return BLACK;
				default:
					return WHITE;
			}
		case 3:
			switch (this) {
				case BLUE:
					return BLACK;
				case WHITE:
					return BLUE;
				default: 
					return next(2);
			}
		case 4:
			switch(this) {
				case BLACK:
					return RED;
				case RED:
					return WHITE;
				default:
					return next(3);
			}
		default:
			return next(2);
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
