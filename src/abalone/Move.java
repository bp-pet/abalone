package abalone;

import abalone.exceptions.InvalidMoveException;

public class Move {
	
	private Board board;
	private int rowTail;
	private int colTail;
	private int rowHead;
	private int colHead;
	private int rowDest;
	private int colDest;
	private int rowMove;
	private int colMove;
	private Field[] fields;
	private Color color;
	
	/**
	 * Makes a new selection associated to given board and given coordinates.
	 * @param board
	 * @param rowTail
	 * @param colTail
	 * @param rowHead
	 * @param colHead
	 * @param rowDest
	 * @param colDest
	 */
	public Move(Board board, Color color, int rowTail, int colTail, int rowHead,
    		int colHead, int rowDest, int colDest) {
		this.board = board;
		this.color = color;
		this.rowTail = rowTail;
		this.colTail = colTail;
		this.rowHead = rowHead;
		this.colHead = colHead;
		this.rowDest = rowDest;
		this.colDest = colDest;
	}
	
	public Move(Board board, Color color, int[] coords) {
		this(board, color, coords[0], coords[1], coords[2], coords[3],
				coords[4], coords[5]);
	}

    /**
     * Checks if given coordinates are fields, if they are in same line,
     * if they are distance 2 or smaller away from each other and if they
     * are occupied.
     * If all of these conditions are okay, the selection is valid.
     * @param rowTail
     * @param colTail
     * @param rowHead
     * @param colHead
     * @return
     */
    public void perform() throws InvalidMoveException {
    	isValidMove();
		moveAllFields();
		board.makeMapOfColors();
    }
    
    /**
     * Checks if a move is valid without performing it.
     * @throws InvalidMoveException
     */
    public void isValidMove() throws InvalidMoveException {
    	isValidSelection();
    	this.fields = getSelectedFields();
    	areAllOccupied();
    	areAdjacent();
    	if (moveIsAlongAxis()) {
    		canMoveField(findHead(), fields.length - 1);
    	} else {
    		canMoveOneByOne();
    	}
    }
    
    /**
     * Moves every field in selection.
     * @requires move is valid
     */
    private void moveAllFields() {
    	if (!moveIsAlongAxis()) {
    		for (Field f : fields) {
    			doMoveField(f);
    		}
    	} else {
    		doMoveField(findTail());
    	}
    }

    /**
     * Moves a field.
     * If next field is empty, nothing special.
     * If next field is taken, recursively move next field.
     * If next field is invalid, kill marble.
     * @param field
     */
    private void doMoveField(Field f) {
    	Field nextField = getNextField(f);
    	if (nextField == null || !nextField.isValid()) {
    		f.setMarble(null);
    	} else if (nextField.getMarble() == null) {
    		nextField.setMarble(f.getMarble());
    		f.setMarble(null);
    	} else {
    		doMoveField(nextField);
    		nextField.setMarble(f.getMarble());
    		f.setMarble(null);
    	}
    }
    
    /**
     * Check if two coordinates are adjacent.
     * @param rowTail
     * @param colTail
     * @param rowDest
     * @param colDest
     * @return
     * @throws InvalidMoveException 
     */
    private void areAdjacent() throws InvalidMoveException {
    	if (!(Math.abs(rowTail - rowDest) == 1 && Math.abs(colTail - colDest) == 0)
    			&& !(Math.abs(rowTail - rowDest) == 0
    			&& Math.abs(colTail - colDest) == 1)
    			&& !(Math.abs(rowTail - rowDest) == 1
    			&& Math.abs(colTail - colDest) == 1)){
    		throw new InvalidMoveException("Move destination not adjacent; "
    				+ toString());
    	}
    }
        
    /**
     * Finds head field.
     * If move is not along axis, returns any field.
     * @return
     */
    private Field findHead() {
    	for (Field f : fields) {
    		boolean ind = true;
    		Field nextField = getNextField(f);
    		for (Field g : fields) {
    			if (g == nextField) {
    				ind = false;
    				break;
    			}
    		}
    		if (ind) {
    			return f;
    		}
    	}
    	return null;
    }
    
    /**
     * Finds tail field.
     * If move is not along axis, returns any field.
     * @return
     */
    private Field findTail() {
    	if (!moveIsAlongAxis()) {
    		return fields[0];
    	}
    	for (Field f : fields) {
    		boolean ind = true;
    		Field prevField = getPrevField(f);
    		for (Field g : fields) {
    			if (g == prevField) {
    				ind = false;
    				break;
    			}
    		}
    		if (ind) {
    			return f;
    		}
    	}
    	return null;
    }
    
    /**
     * Checks if each marble in selection can be moved without pushing
     * @throws InvalidMoveException 
     * @requires not along axis
     */
    private void canMoveOneByOne() throws InvalidMoveException {
    	for (Field f : fields) {
    		canMoveField(f, -1);
    	}
    }
    
    /**
     * Gets the next field in direction of the movement vector.
     */
    private Field getNextField(Field f) {
    	return board.getField(f.getRow() + rowMove, f.getCol() + colMove);
    }

    /**
     * Gets the previous field, or the one opposite the direction
     * of the movement vector.
     */
    private Field getPrevField(Field f) {
    	return board.getField(f.getRow() - rowMove, f.getCol() - colMove);
    }
    
    /**
     * Check if a marble can be moved with a given force.
     * If next field is out of board, movement can only be done if current marble
     * to be pushed is not friendly, since suicide is not allowed.
     * If force is 0, nothing can be pushed, so move only works towards
     * a free space.
     * If force is positive, but less than three: a friendly marble can be pushed
     * which starts a move of said marble with force increased by 1; an enemy marble
     * can be pushed which decreases force by 1.
     * An enemy marble cannot again push a friendly one.
     * Also force cannot increase above three since only 3 marbles can be moved at
     * a time. 
     * @param f
     * @param force
     * @return
     * TODO: currently, friendly color means specifically the same color as the
     * selection.
     * @throws InvalidMoveException 
     */
    private void canMoveField(Field f, int force) throws InvalidMoveException {
		Field nextField = getNextField(f);
		Color currentColor = f.getMarble().getColor();
		if (nextField == null || !nextField.isValid()) {
			if (currentColor == color) {
				throw new InvalidMoveException("You are not allowed to "
						+ "commit suicide; " + toString());
			}
		} else {
			if (force == -1) {
				if (nextField.getMarble() != null) {
	    			throw new InvalidMoveException("Cannot push laterally; "
	    					+ toString());
				}
			} else if (force == 0) {
	    		if (currentColor != color) {
	    			throw new InvalidMoveException("Not enough force to push; "
	    					+ toString());
	    		}
	    		if (nextField.getMarble() != null) {
	    			if (nextField.getMarble().getColor() == color) {
	    				canMoveField(nextField, force + 1);
	    			} else {
		    			throw new InvalidMoveException("Not enough force to push; "
		    					+ toString());
	    			}
	    		}
			} else {
	    		if (!(nextField.getMarble() == null)) {
	    			Marble nextMarble = nextField.getMarble();
	    			if (currentColor == color) {
	    				if (nextMarble.getColor() == color) {
	    					if (force == board.maxPush - 1) {
	    						throw new InvalidMoveException("Too many pushed; "
	    								+ toString());
	    					} else {
	    						canMoveField(nextField, force + 1);
	    					}
	    				} else {
	    					canMoveField(nextField, force - 1);
	    				}
	    			} else {
	    				if (nextMarble.getColor() != color) {
	    					canMoveField(nextField, force - 1);
	    				}
	    			}
	    		}
			}
		}
    }

    /**
     * Check if move is along axis by checking if moving tail in the direction of
     * the move vector (rowMove, colMove) or opposite that direction ends
     * up in one of the other fields. 
     * @return
     */
    private boolean moveIsAlongAxis() {
    	rowMove = rowDest - rowTail;
    	colMove = colDest - colTail;
    	if (fields.length == 1) {
    		return true;
    	}
    	for (Field f : fields) {
    		if (board.getField(rowTail + rowMove, colTail + colMove)
    				== f || board.getField(rowTail - rowMove, colTail
    				- colMove) == f) {
    			return true;
    		}
    	}
    	return false;
    }
    
    /**
     * Checks if selection is valid based on three conditions:
     * -head and tail fields are valid
     * -they are in the same line
     * -the distance between is 2 or less
     * @throws InvalidMoveException 
     */
    public void isValidSelection() throws InvalidMoveException {
    	if (!board.isField(rowTail, colTail) || !board.isField(rowHead, colHead)) {
    		throw new InvalidMoveException("Selection not valid; " + toString());
    	}
    	areInSameLine();
    	distance2orSmaller();
    }
    
    /**
     * Finds the fields belonging to selection and makes an array of them.
     * @requires selection is valid
     * @return an array of fields in selection
     */
    private Field[] getSelectedFields() {
    	int rowDiff = rowHead - rowTail;
    	if (rowDiff != 0) {
    		rowDiff = rowDiff/Math.abs(rowDiff);
    	}
    	int colDiff = colHead - colTail;
    	int size = Math.max(Math.abs(rowDiff), Math.abs(colDiff)) + 1;
    	Field[] result = new Field[size];
    	if (rowDiff != 0) {
     		rowDiff = rowDiff/Math.abs(rowDiff);
    	}
    	if (colDiff != 0) {
    		colDiff = colDiff/Math.abs(colDiff);
    	}
    	int row = rowTail;
    	int col = colTail;
    	for (int i = 0; i < size; i++) {
    		result[i] = board.getField(row + rowDiff * i, col + colDiff * i);
    	}
    	return result;
    }
    
    /**
     * Checks if distance between head and tail is 2 or less.
     * @throws InvalidMoveException 
     * @requires fields are in line
     */
    private void distance2orSmaller() throws InvalidMoveException {
    	if (!(Math.abs(rowTail - rowHead) <= board.maxPush - 1
    			|| Math.abs(colTail - colHead) <= board.maxPush - 1)) {
    		throw new InvalidMoveException("Selection too long; " + toString());
    	}
    }
    
    /**
     * Goes through coordinates from tail to head and for each checks if it
     * contains a marble.
     * @param rowTail
     * @param colTail
     * @param rowHead
     * @param colHead
     * @return
     * @throws InvalidMoveException 
     */
    private boolean areAllOccupied() throws InvalidMoveException {
    	for (Field f : fields) {
    		if (f.getMarble() == null || f.getMarble().getColor() != color) {
    			throw new InvalidMoveException("Field does not contain valid marble: "
    					+ f.getFullString() + "; " + toString());
    		}
    	}
    	return true;
    }
    
    /**
     * Either in the same row, same column, or same diagonal.
     * @param rowTail
     * @param colTail
     * @param rowHead
     * @param colHead
     * @return
     * @throws InvalidMoveException 
     */
    private void areInSameLine() throws InvalidMoveException {
    	if (!((rowTail == rowHead) || (colTail == colHead) || (rowTail - colTail
    			== rowHead - colHead))){
    		throw new InvalidMoveException("Fields not in same line; " + toString());
    	}
    }
    
    /**
     * Returns the array of fields.
     * This is null until the selection gets checked and the fields are found.
     */
    public Field[] getFields() {
    	return this.fields;
    }
    
    public Move deepCopy(Board newBoard) {
    	return new Move(newBoard, color, rowTail, colTail, rowHead, colHead,
    			rowDest, colDest);
    }
    
    public int getRowTail() {
    	return rowTail;
    }
    
    public int getColTail() {
    	return colTail;
    }
    
    public int getRowHead() {
    	return rowHead;
    }
    
    public int getColHead() {
    	return colHead;
    }
    
    public int getRowDest() {
    	return rowDest;
    }
    
    public int getColDest() {
    	return colDest;
    }
    
    public boolean equalsMove(Move m) {
    	return rowTail == m.getRowTail() && colTail == m.getColTail() &&
    			rowHead == m.getRowHead() && colHead == m.getColHead() &&
    			rowDest == m.getRowDest() && colDest == m.getColDest();
    }
    
    public Move getMirroredMove(Color newColor ) {
    	return new Move(board, newColor,
				board.rotate180(getRowTail()),
				board.rotate180(getColTail()),
				board.rotate180(getRowHead()),
				board.rotate180(getColHead()),
				board.rotate180(getRowDest()),
				board.rotate180(getColDest()));
    }
    
    /**
     * toString of Move
     * @returns "Color " + color + " moves (" + rowTail + " , " + colTail + "),(" + rowHead + "," + colHead + ") to (" + rowDest + "," + colDest + ")";
     */
    public String toString() {
    	return "Move: Color " + color + " moves (" + rowTail + ", " + colTail + "), (" + rowHead + ", " + colHead + ") to (" + rowDest + ", " + colDest + ")";
    }
}
