package abalone;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import abalone.exceptions.InvalidMoveException;

public class Board {

	private static final int DIM = 5;
	private final int maxPush = 3;

	// -- Constants --------------------------------------------------

	private static final char FIELD = '+';
	private static final int WIDTH = 2 * DIM - 1;

	// -- Instance variables -----------------------------------------

	private Field[][] fields;
	private Map<Color, ArrayList<Field>> mapOfColors;
	private Color[][] teams;

	// -- Constructors -----------------------------------------------

	/*
	 * @ ensures (\forall int i; 0 <= i & i < DIM * DIM; this.getField(i) ==
	 * Marble.EMPTY);
	 */
	/**
	 * Creates an empty board.
	 */
	public Board() {
		this.fields = new Field[WIDTH][WIDTH];
		this.reset();
	}

	/**
	 * Creates a new board with Marbles for playerCount players.
	 * 
	 * @param playerCount
	 * @requires 2 <= playerCount <= 4;
	 */
	public Board(int playerCount) {
		this();
		this.reset(playerCount);
		this.makeMapOfColors();
	}

	// -- Queries ----------------------------------------------------
	
	public void setTeams(Color[][] teams) {
		this.teams = teams;
	}
	
	public boolean areTeammates(Color colorA, Color colorB) {
		if (colorA == colorB) {
			return true;
		}
		if (teams == null) {
			return false;
		}
		for (Color[] team : teams) {
			for (Color c1 : team) {
				if (c1 == colorA) {
					for (Color c2 : team) {
						if (c2 == colorB) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * Creates a deep copy of this field.
	 */
	public Board deepCopy() {
		Board copy = new Board();
		for (int i = 0; i < WIDTH; i++) {
			for (int j = 0; j < WIDTH; j++) {
				copy.fields[i][j].setMarble(this.fields[i][j].getMarble());
			}
		}
		return copy;
	}

	/*
	 * @ ensures \result == (0 <= row && row < DIM && 0 <= col && col < DIM);
	 */
	/**
	 * Returns true of the (row,col) pair refers to a valid field on the board.
	 * 
	 * @return true if <code>0 <= row < DIM && 0 <= col < DIM</code>
	 */
	/* @pure */
	public boolean isField(int row, int col) {
		return row < WIDTH  && row >= 0
				&& col < WIDTH && col >= 0 && fields[row][col].isValid();
	}

	/**
	 * get Field with given row and col
	 * 
	 * @requires 
	 * @param row
	 * @param col
	 * @return will return null if field doesn't exist
	 */
	public Field getField(int row, int col) {
		if (isField(row, col)) {
			return fields[row][col];
		} else {
			return null;
		}
	}

	/*
	 * @ requires this.isField(row, col); ensures \result == Marble.EMPTY || \result
	 * == Marble.XX || \result == Marble.OO; pure
	 */
	/**
	 * Returns the content of the field referred to by the (row,col) pair.
	 * 
	 * @param row the row of the field
	 * @param col the column of the field
	 * @return the marble on the field
	 */
	public Marble getFieldContent(int row, int col) {
		return getField(row, col).getMarble();
	}

	/**
	 * Returns true if the field referred to by the (row, col) pair it empty.
	 * 
	 * @param row the row of the field
	 * @param col the column of the field
	 * @return true if the field is empty
	 */
	/* @pure */
	public boolean isEmptyField(int row, int col) {
		return isField(row, col) && getFieldContent(row, col) == null;
	}

	/**
	 * get row with given letter; translates [A-I] to [0-8] or [a-i] to [0-8]
	 * 
	 * @requires letter must match pattern [A-Ia-i]
	 * @ensures 0 <= return value <= 8
	 * @param letter
	 * @return
	 */
	public int getRowFromLetter(char letter) {
		if (letter >= 'a' && letter <= 'z') {
			return ((int)letter) - 97;
		}
		return ((int)letter) - 65;
	}

	/**
	 * get row with given letter; translates [1-9] to [0-8]
	 * 
	 * @param letter
	 * @requires letter must match pattern [1-9]
	 * @ensures 0 <= return value <= 8
	 * @return letter - 1
	 */
	public int getColFromLetter(char letter) {
		return letter - 48 - 1;
	}

	/**
	 * inverse function of {@link #getRowFromLetter(int)}.
	 * 
	 * @param row
	 * @requires 0 <= row <= 8
	 * @ensures return matches pattern [A-I]
	 */
	public char getRowLetter(int row) {
		return (char) (row + 65);
	}

	/**
	 * inverse function of {@link #getColFromLetter(int)}.
	 * 
	 * @param col
	 * @requires 0 <= col <= 8
	 * @ensures return matches pattern [1-9]
	 */
	public char getColLetter(int col) {
		return (char) (col + 48 + 1);
	}

	// -- Commands ---------------------------------------------------

	/**
	 * empties board and makes a new mapOfColors afterwards
	 * 
	 * @param numberOfPlayers
	 */
	public void reset() {
		boolean valid;
		for (int i = 0; i < WIDTH; i++) {
			for (int j = 0; j < WIDTH; j++) {
				if (i - j >= DIM || i - j <= -DIM) {
					valid = false;
				} else {
					valid = true;
				}
				fields[i][j] = new Field(valid, i, j);
			}
		}
		this.makeMapOfColors();
	}

	/**
	 * empties board and fills it with marbles for numberOfPlayers and makes a new mapOfColors afterwards
	 * 
	 * @param numberOfPlayers
	 */
	public void reset(int numberOfPlayers) {
		Marble m;
		int i180;
		int j180;
		int imirror;
		int jmirror;
		int imirror180;
		int jmirror180;
		switch (numberOfPlayers) {
		case 2:
			for (int i = 0; i < WIDTH; i++) {
				for (int j = 0; j < WIDTH; j++) {
					i180 = -1 * (i - (DIM - 1)) + (DIM - 1);
					j180 = -1 * (j - (DIM - 1)) + (DIM - 1);
					if (fields[i][j].isValid()
							&& (i <= DIM / 2 - 1 || (i == DIM / 2 + 1 - 1 && DIM / 2 - 1 < j && j <= DIM - 1))) {
						m = new Marble(Color.WHITE);
					} else if (fields[i][j].isValid() && (i180 <= DIM / 2 - 1
							|| (i180 == DIM / 2 + 1 - 1 && DIM / 2 - 1 < j180 && j180 <= DIM - 1))) {
						m = new Marble(Color.BLACK);
					} else {
						m = null;
					}
					fields[i][j].setMarble(m);
				}
			}
			break;
		case 3:
			for (int i = 0; i < WIDTH; i++) {
				for (int j = 0; j < WIDTH; j++) {
					if (fields[i][j].isValid() && (i < DIM / 2)) {
						m = new Marble(Color.BLUE);
					} else if (fields[i][j].isValid() && (i > j + DIM / 2)) {
						m = new Marble(Color.WHITE);
					} else if (fields[i][j].isValid() && (j > WIDTH - 1 - DIM / 2)) {
						m = new Marble(Color.BLACK);
					} else {
						m = null;
					}
					fields[i][j].setMarble(m);
				}
			}
			break;
		case 4:
			for (int i = 0; i < WIDTH; i++) {
				for (int j = 0; j < WIDTH; j++) {
					i180 = -1 * (i - (DIM - 1)) + (DIM - 1);
					j180 = -1 * (j - (DIM - 1)) + (DIM - 1);
					imirror = j;
					jmirror = i;
					imirror180 = j180;
					jmirror180 = i180;
					if (fields[i][j].isValid() && extracted(i, j)) {
						m = new Marble(Color.BLUE);
					} else if (fields[i][j].isValid() && extracted(i180, j180)) {
						m = new Marble(Color.RED);
					} else if (fields[i][j].isValid() && extracted(imirror, jmirror)) {
						m = new Marble(Color.WHITE);
					} else if (fields[i][j].isValid() && extracted(imirror180, jmirror180)) {
						m = new Marble(Color.BLACK);
					} else {
						m = null;
					}
					fields[i][j].setMarble(m);
				}
			}
			break;
		default:
			break;
		}
		this.makeMapOfColors();
	}

	/**
	 * extraction used for {@link #Board(int)} which calculates if the given i, j
	 * field is in the region where the blue marbles should be placed on a 4 player
	 * board.
	 * 
	 * @param i
	 * @param j
	 * @return boolean
	 */
	private boolean extracted(int i, int j) {
		return i < j && i <= DIM / 2 + 1 - 1 && j < DIM + 1 - 1;
	}

	/*
	 * @ requires this.isField(row,col); ensures this.getField(row,col) == m;
	 * 
	 */
	/**
	 * Sets the content of the field represented by the (row,col) pair to the marble
	 * <code>m</code>.
	 * 
	 * @param row the field's row
	 * @param col the field's column
	 * @param m   the marble to be placed
	 */
	public void setField(int row, int col, Marble m) {
		fields[row][col].setMarble(m);
	}

	/**
	 * Tries to move the move if move is invalid InvalidMoveException is trown and
	 * no marbles are moved.
	 * 
	 * @param rowTail
	 * @param colTail
	 * @param rowHead
	 * @param colHead
	 * @param rowDest
	 * @param colDest
	 * @throws InvalidMoveException
	 */
	public void move(Color color, int rowTail, int colTail, int rowHead, int colHead, int rowDest, int colDest)
			throws InvalidMoveException {
		move(new Move(this, color, rowTail, colTail, rowHead, colHead, rowDest, colDest));
	}

	public void move(Move move) throws InvalidMoveException {
		move.perform();
	}
	
	public void makeMapOfColors() {
		mapOfColors = new HashMap<Color, ArrayList<Field>>();
		ArrayList<Field> fieldArray;
		for (Field[] ff : fields) {
			for (Field f : ff) {
				if (f.getMarble() != null) {
					Color color = f.getMarble().getColor();
					if (mapOfColors.containsKey(color)) {
						mapOfColors.get(color).add(f);
					} else {
						fieldArray = new ArrayList<Field>();
						fieldArray.add(f);
						mapOfColors.put(color, fieldArray);
					}
				}
			}
		}
	}
	
	public Map<Color, ArrayList<Field>> getMapOfColors() {
		return mapOfColors;
	}
	
	public String getStringMapOfColors() {
		String s = "MapOfColors:\n";
		for (Color c : getMapOfColors().keySet()) {
			s += "Color: " + c.toString() + "\n";
			for (Field f : getMapOfColors().get(c)) {
				s += f.getFullString() + "\n";
			}
		}
		return s;
	}
	
	public String getNumberOfMarbles() {
		String s = "";
		for (Color c : mapOfColors.keySet()) {
			s += c.toString() + String.valueOf(mapOfColors.get(c).size());
		}
		return s;
	}
	
	public int[] rotate180(int row, int col){
		int[] result = new int[2];
		result[0] = rotate180(row);
		result[1] = rotate180(col);
		return result;
	}
	
	public int rotate180(int i) {
		return 2 * DIM - i - 2;
	}
	
	public int getDim() {
		return DIM;
	}
	
	public int getWidth() {
		return WIDTH;
	}
	
	public int getMaxPush() {
		return maxPush;
	}
	
	/**
	 * Returns String representation of the board
	 */
	public String toString() {
		String full = "";
		String line;
		for (int i = WIDTH - 1; i >= 0; i--) {
			line = "";
			for (int k = 0; k < WIDTH - 1 - i; k++) {
				line = line + " ";
			}
			// add column index in row
			line = line + getRowLetter(i) + "  ";
			for (int j = 0; j < WIDTH; j++) {
				line += fields[i][j].toString() + " ";
			}
			full += line + "\n";
		}
		// add row of indexes
		line = "";
		int i = -1;
		for (int k = 0; k < WIDTH - 1 - i; k++) {
			line = line + " ";
		}
		line = line + " " + "  ";
		for (int j = 0; j < WIDTH; j++) {
			line += getColLetter(j) + " ";
		}
		full += line + "\n";
		return full;
	}

}
