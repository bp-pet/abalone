package abalone;

public class Selection {
	
	private Board board;
	private int rowTail;
	private int colTail;
	private int rowHead;
	private int colHead;
	private int rowDest;
	private int colDest;
	private Field[] fields;
	
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
	public Selection(Board board, int rowTail, int colTail, int rowHead,
    		int colHead, int rowDest, int colDest) {
		this.board = board;
		this.rowTail = rowTail;
		this.colTail = colTail;
		this.rowHead = rowHead;
		this.colHead = colHead;
		this.rowDest = rowDest;
		this.colDest = colDest;
		this.fields = getSelectedFields();
	}
	
    /**
     * Check if two coordinates are adjacent.
     * @param rowTail
     * @param colTail
     * @param rowDest
     * @param colDest
     * @return
     */
    public boolean areAdjacent() {
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
    	if (!areAllOccupied()) {
    		return false;
    	}
    	return true;
    }
    
    public Field[] getSelectedFields() {
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
    
    public boolean distance2orSmaller() {
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
    public boolean areAllOccupied() {
    	for (Field f : fields) {
    		if (f.getMarble() == null) {
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
    public boolean areInSameLine() {
    	return (rowTail == rowHead) || (colTail == colHead) || (rowTail - colTail
    			== rowHead - colHead);
    }
    
    public Field[] getFields() {
    	return this.fields;
    }
}
