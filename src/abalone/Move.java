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
	
    /**
     * Check if two coordinates are adjacent.
     * @param rowTail
     * @param colTail
     * @param rowDest
     * @param colDest
     * @return
     */
    private boolean areAdjacent() {
        if (!((rowTail - rowDest) * (rowTail - rowDest)
        		+ (colTail - colDest) * (colTail - colDest) == 1)) {
            return false;
        }
    return !(rowTail - rowDest == colTail - colDest);

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
    	if (isValidSelection()) {
    		this.fields = getSelectedFields();
    		if (areAllOccupied() && areAdjacent()) {
    			if (moveIsAlongAxis()) {
    				if (canMoveField(findHead(), fields.length)) {
    					moveAllFields();
    				} else {
    					throw new InvalidMoveException("Cannot move");
    				}
    			} else {
    				if (canMoveOneByOne()) {
    					moveAllFields();
    				} else {
    					throw new InvalidMoveException("Cannot move");
    				}
    			}
    		} else {
				throw new InvalidMoveException("Move not valid");
    		}
    	} else {
			throw new InvalidMoveException("Selection not valid");
    	}
    }
    
    /**
     * Moves every field in selection.
     * @requires move is valid
     */
    public void moveAllFields() {
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
     * Finds head field.
     * If move is not along axis, returns any field.
     * @return
     */
    private Field findHead() {
    	if (!moveIsAlongAxis()) {
    		return fields[0];
    	}
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
     * @requires not along axis
     */
    private boolean canMoveOneByOne() {
    	for (Field f : fields) {
    		if (!canMoveField(f, 0)) {
    			return false;
    		}
    	}
    	return true;
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
     */
    private boolean canMoveField(Field f, int force) {
		Field nextField = getNextField(f);
		Color currentColor = f.getMarble().getColor();
		if (nextField == null || !nextField.isValid()) {
			return currentColor != color;
		}
    	if (force == 0) {
    		return nextField.getMarble() == null;
		} else {
    		if (nextField.getMarble() == null) {
    			return true;
    		}
    		Marble nextMarble = nextField.getMarble();
    		if (currentColor == color) {
    			if (nextMarble.getColor() == color) {
    				if (force == 3) {
    					return false;
    				} else {
    					return canMoveField(nextField, force + 1);
    				}
    			} else {
    				return canMoveField(nextField, force - 1);
    			}
    		} else {
    			if (nextMarble.getColor() != color) {
    				return canMoveField(nextField, force - 1);
    			}
    		}
		}
    	return false;
    }

    /**
     * Check if move is along axis by checking if moving tail in the direction of
     * the move vector (rowMove, colMove) or opposite that direction ends
     * up in one of the other fields. 
     * @return
     */
    private boolean moveIsAlongAxis() {
    	if (fields.length == 1) {
    		return true;
    	}
    	rowMove = rowDest - rowTail;
    	colMove = colDest - colTail;
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
     */
    public boolean isValidSelection() {
    	if (!board.isField(rowTail, colTail) || !board.isField(rowHead, colHead)) {
    		return false;
    	}
    	if (!areInSameLine()) {
    		return false;
    	}
    	if (!distance2orSmaller()) {
    		return false;
    	}
    	return true;
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
     * @requires fields are in line
     */
    private boolean distance2orSmaller() {
    	return Math.abs(rowTail - rowHead) <= 2 || Math.abs(colTail - colHead) <= 2;
    }
    
    /**
     * Goes through coordinates from tail to head and for each checks if it
     * contains a marble.
     * @param rowTail
     * @param colTail
     * @param rowHead
     * @param colHead
     * @return
     */
    private boolean areAllOccupied() {
    	for (Field f : fields) {
    		if (f.getMarble().getColor() == color) {
    			return false;
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
     */
    private boolean areInSameLine() {
    	return (rowTail == rowHead) || (colTail == colHead) || (rowTail - colTail
    			== rowHead - colHead);
    }
    
    /**
     * Returns the array of fields.
     * This is null until the selection gets checked and the fields are found.
     */
    public Field[] getFields() {
    	return this.fields;
    }
    
    /**
     * toString of Move
     * @returns "Color " + color + " moves (" + rowTail + " , " + colTail + "),(" + rowHead + "," + colHead + ") to (" + rowDest + "," + colDest + ")";
     */
    public String toString() {
    	return "Color " + color + " moves (" + rowTail + " , " + colTail + "),(" + rowHead + "," + colHead + ") to (" + rowDest + "," + colDest + ")";
    }
}
