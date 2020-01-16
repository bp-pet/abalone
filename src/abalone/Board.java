package abalone;

public class Board {
	
	private static final int DIM = 5;
	
    // -- Constants --------------------------------------------------

    private static final char FIELD = '+';
    private static final int WIDTH = 2 * DIM - 1;
	
    // -- Instance variables -----------------------------------------

    /*@
       private invariant fields.length == DIM*DIM;
       invariant (\forall int i; 0 <= i & i < DIM*DIM;
           getField(i) == Marble.EMPTY || getField(i) == Marble.XX || getField(i) == Marble.OO);
     */
    /**
     * The DIM by DIM fields of the Tic Tac Toe board. See NUMBERING for the
     * coding of the fields.
     */
    private Field[][] fields;

    // -- Constructors -----------------------------------------------

    /*@
       ensures (\forall int i; 0 <= i & i < DIM * DIM; this.getField(i) == Marble.EMPTY);
     */
    /**
     * Creates an empty board.
     */
    public Board() {
    	boolean valid;
        fields = new Field[WIDTH][WIDTH];
        for (int i = 0; i < 2 * DIM - 1; i++) {
        	for (int j = 0; j < 2 * DIM -1; j++) {
        		if (i - j > DIM || i - j < -DIM) {
                	valid = false;
        		} else {
        			valid = true;
        		}
        		fields[i][j] = new Field(valid, i, j);
        	}
        }
    }

    // -- Queries ----------------------------------------------------

    /**
     * Creates a deep copy of this field.
     */
    public Board deepCopy() {
        // TODO: add implementation
        return null;
    }

    /*@
       ensures \result == (0 <= row && row < DIM && 0 <= col && col < DIM);
     */
    /**
     * Returns true of the (row,col) pair refers to a valid field on the board.
     * 
     * @return true if <code>0 <= row < DIM && 0 <= col < DIM</code>
     */
    /*@pure*/
    public boolean isField(int row, int col) {
        return row < WIDTH && col < WIDTH && getField(row, col).isValid();
    }

    /**
     * get Field with given row and col
     * @param row
     * @param col
     * @return
     */
    public Field getField(int row, int col) {
        return fields[row][col];
    }
    
    /**
     * get row with given letter; translates [A-I] to [0-8] or [a-i] to [0-8]
     * @requires letter must match pattern [A-Ia-i]
     * @ensures 0 <= return value <= 8
     * @param letter
     * @return
     */
    public int getRow(char letter) {
    	if (letter >= 'a' && letter <= 'z') {
			return letter - 97;
		}
		return letter - 65;
    }
    
    /**
     * get row with given letter; translates [1-9] to [0-8]
     * @param letter
     * @requires letter must match pattern [1-9]
     * @ensures 0 <= return value <= 8
     * @return letter - 1
     */
    public int getCol(char letter) {
    	return letter - 48 - 1;
    }

    /*@
       requires this.isField(row, col);
       ensures \result == Marble.EMPTY || \result == Marble.XX || \result == Marble.OO;
       pure
     */
    /**
     * Returns the content of the field referred to by the (row,col) pair.
     * 
     * @param row
     *            the row of the field
     * @param col
     *            the column of the field
     * @return the marble on the field
     */
    public Marble getFieldContent(int row, int col) {
        return fields[row][col].getMarble();
    }

    /*@
       requires this.isField(row, col);
       ensures \result == (this.getField(row, col) == Marble.EMPTY);

     */
    /**
     * Returns true if the field referred to by the (row, col) pair it empty.
     * 
     * @param row
     *            the row of the field
     * @param col
     *            the column of the field
     * @return true if the field is empty
     */
    /*@pure*/
    public boolean isEmptyField(int row, int col) {
        return isField(row, col) && getFieldContent(row, col) == null;
    }

    /*@
       ensures \result == this.hasWinner();

Game is over when:
- Push off 6 marbles of an opponent
 - 2 players: no complications
 - 3 players: don't distinguish colours
 - 4 players: 6 marbles of the opposing team
   - No, pushing off your teammate does not count
   
     */
    /**
     * Returns true if the game is over. The game is over when there is a winner.
     * 
     * @return true if the game is over
     */
    /*@pure*/
    public boolean gameOver() {
        // TODO: add implementation
        return false;
    }

    /*@
//       requires m == Marble.XX | m == Marble.OO;
//       ensures \result == this.hasRow(m) ||
//                                this.hasColumn(m) |
//                                this.hasDiagonal(m);
     */
    /**
     * Checks if the marble <code>m</code> has won. A marble wins if it has thrown off 6 marbles of an opponent
     * 
     * @param m
     *            the marble of interest
     * @return true if the marble has won
     */
    /*@pure*/
    public boolean isWinner(Marble m) {
        // TODO: add implementation
        return false;
    }

    /*@
       ensures \result == isWinner(Marble.XX) | \result == isWinner(Marble.OO);

     */
    /**
     * Returns true if the game has a winner. This is the case when one of the
     * marbles controls at least one row, column or diagonal.
     * 
     * @return true if the board has a winner.
     */
    /*@pure*/
    public boolean hasWinner() {
        // TODO: add implementation
        return false;
    }

    /**
     * Returns a String representation of this board. In addition to the current
     * situation, the String also shows the numbering of the fields.
     * 
     * @return the game situation as String
     */
    public String toString() {
    	// TODO: add implementation
        return null;
    }

    // -- Commands ---------------------------------------------------

    /*@
       ensures (\forall int i; 0 <= i & i < DIM * DIM; this.getField(i) == Marble.EMPTY);
     */
    /**
     * Empties all fields of this board (i.e., let them refer to the value
     * Marble.EMPTY).
     */
    public void reset() {
        // TODO: add implementation
    }

    /*@
       requires this.isField(row,col);
       ensures this.getField(row,col) == m;

     */
    /**
     * Sets the content of the field represented by the (row,col) pair to the
     * marble <code>m</code>.
     * 
     * @param row
     *            the field's row
     * @param col
     *            the field's column
     * @param m
     *            the marble to be placed
     */
    public void setField(int row, int col, Marble m) {
        fields[row][col].setMarble(m);
    }
    
    /**
     * First checks if move is valid, then if destinations is valid.
     * @param rowTail
     * @param colTail
     * @param rowHead
     * @param colHead
     * @param rowDest
     * @param colDest
     */
    public void move(int rowTail, int colTail, int rowHead, int colHead,
    		int rowDest, int colDest) {
    	Move move = new Move(this, Color.BLUE, rowTail, colTail, rowHead,
    			colHead, rowDest, colDest);
    	if (move.isValidmove()) {
    		if (move.areAdjacent()) {
    			Field[] selectedFields = move.getFields();
    		}
    	}
    }

}
