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
<<<<<<< HEAD
    	boolean valid;
        fields = new Field[WIDTH][WIDTH];
        for (int i = 0; i < 2 * DIM - 1; i++) {
        	for (int j = 0; j < 2 * DIM -1; j++) {
        		if (i - j > DIM || i - j < -DIM) {
                	valid = false;
        		} else {
        			valid = true;
        		}
        		fields[i][j] = new Field(valid);
        	}
        }
=======
        // TODO: add implementation
>>>>>>> c1088f0135df0559098f818ceaf524b3a9754ba7
    }

    // -- Queries ----------------------------------------------------

    /*@
       ensures \result != this;
       ensures (\forall int i; 0 <= i & i < DIM * DIM; \result.getField(i) == this.getField(i));
     */
    /**
     * Creates a deep copy of this field.
     */
    public Board deepCopy() {
        // TODO: add implementation
        return null;
    }

<<<<<<< HEAD
=======

    /*@
       requires 0 <= row & row < DIM;
       requires 0 <= col & col < DIM;
     */
    /**
     * Calculates the index in the linear array of fields from a (row, col)
     * pair.
     * @return the index belonging to the (row,col)-field
     */
    public int index(int row, int col) {
        // TODO: add implementation
        return 0;
    }


    /*@
       ensures \result == (0 <= ix && ix < DIM * DIM);
     */
    /**
     * Returns true if <code>ix</code> is a valid index of a field on tbe board.
     * @return <code>true</code> if <code>0 <= ix < DIM*DIM</code>
     */
    /*@pure*/
    public boolean isField(int ix) {
        // TODO: add implementation
        return false;
    }

>>>>>>> c1088f0135df0559098f818ceaf524b3a9754ba7
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
<<<<<<< HEAD
        return row < WIDTH && col < WIDTH && getField(row, col).isValid();
    }

    public Field getField(int row, int col) {
        return fields[row][col];
=======
        // TODO: add implementation
        return false;
    }


    /*@
       requires this.isField(i);
       ensures \result == Marble.EMPTY || \result == Marble.XX || \result == Marble.OO;
       pure
     */
    /**
     * Returns the content of the field <code>i</code>.
     * 
     * @param i
     *            the number of the field (see NUMBERING)
     * @return the marble on the field
     */
    public Marble getField(int i) {
        // TODO: add implementation
        return null;
>>>>>>> c1088f0135df0559098f818ceaf524b3a9754ba7
    }

    /*@
       requires this.isField(row,col);
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
<<<<<<< HEAD
    public Marble getFieldContent(int row, int col) {
        return fields[row][col].getMarble();
=======
    public Marble getField(int row, int col) {
        // TODO: add implementation
        return null;
    }

    /*@
       requires this.isField(i);
       ensures \result == (this.getField(i) == Marble.EMPTY);
       pure
     */
    /**
     * Returns true if the field <code>i</code> is empty.
     * 
     * @param i
     *            the index of the field (see NUMBERING)
     * @return true if the field is empty
     */
    public boolean isEmptyField(int i) {
        // TODO: add implementation
        return false;
>>>>>>> c1088f0135df0559098f818ceaf524b3a9754ba7
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
<<<<<<< HEAD
        return getFieldContent(row, col) == null;
=======
        // TODO: add implementation
        return false;
    }

    /*@
       ensures \result == (\forall int i; i <= 0 & i < DIM * DIM; this.getField(i) != Marble.EMPTY);
     */
    /**
     * Tests if the whole board is full.
     * 
     * @return true if all fields are occupied
     */
    /*@pure*/
    public boolean isFull() {
        // TODO: add implementation
        return false;
>>>>>>> c1088f0135df0559098f818ceaf524b3a9754ba7
    }

    /*@
       ensures \result == this.hasWinner();

Game is over when:
- Push off 6 marbles of an opponent
 - 2 players: no complications
 - 3 players: don�t distinguish colours
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
<<<<<<< HEAD
    	//TO DO
=======
        // TODO: add implementation
    }

    /*@
       requires this.isField(i);
       ensures this.getField(i) == m;
     */
    /**
     * Sets the content of field <code>i</code> to the marble <code>m</code>.
     * 
     * @param i
     *            the field number (see NUMBERING)
     * @param m
     *            the marble to be placed
     */
    public void setField(int i, Marble m) {
        // TODO: add implementation
>>>>>>> c1088f0135df0559098f818ceaf524b3a9754ba7
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
     * First checks if selection is valid, then if destinations is valid.
     * @param rowTail
     * @param colTail
     * @param rowHead
     * @param colHead
     * @param rowDest
     * @param colDest
     */
    public void move(int rowTail, int colTail, int rowHead, int colHead,
    		int rowDest, int colDest) {
    	Selection selection = new Selection(this, rowTail, colTail, rowHead,
    			colHead, rowDest, colDest);
    	if (selection.isValidSelection()) {
    		if (selection.areAdjacent()) {
    			Field[] selectedFields = selection.getFields();
    		}
    	}
    }
}
