package abalone;

import abalone.exceptions.InvalidMoveException;
import abalone.exceptions.MarbleKilledException;

/**
 * A class representing a move on a board.
 * 
 * @authors Bozhidar Petrov, Daan Pluister
 */
public class Move {
    
    // -- Instance variables -----------------------------------------
    
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
    private boolean marbleKilled = false;
    
    /**
     * Makes a new selection associated to given board and given coordinates.
     * In this case head and tail do not indicate direction of movement,
     * and are only relevant in the context of the destination being where the
     * tail should go.
     * Later the true tail and head of the selection are defined, in case it is
     * along the axis.
     * @param board on which the move is to be performed
     * @param color which performs the move
     * @param rowTail row of the tail field
     * @param colTail column of the tail field
     * @param rowHead row of the head field
     * @param colHead column of the head field
     * @param rowDest row of the destination field for the tail
     * @param colDest column of the destination field for the tail
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
     * Makes a move from an array of coordinates. Used for convenience.
     * @requires coordinates has size 6 and no null entries
     */
    public Move(Board board, Color color, int[] coordinates) {
        this(board, color, coordinates[0], coordinates[1], coordinates[2], coordinates[3],
                coordinates[4], coordinates[5]);
    }
    
    /**
     * Makes a move from two fields AND A MOVE VECTOR, not a destination. Used for convenience.
     * @param field1 is the tail
     * @param field2 is the head
     */
    public Move(Board board, Color color, Field field1, Field field2, int rowMove, int colMove) {
        this(board, color, field1.getRow(), field1.getCol(), field2.getRow(),
                field2.getCol(), field1.getRow() + rowMove, field1.getCol() + colMove);
    }

    /**
     * Checks whether move is valid. If it is, it performs it. After that,
     * the map of colors of the board is updated (perhaps this should be done
     * in the board class, however moves are not always performed through that
     * class, instead some methods call move.perform directly).
     * @throws InvalidMoveException with appropriate message if any of the
     *     conditions are violated
     * @throws MarbleKilledException if a marble was killed (the boolean
     *     marbleKilled was set to true)
     *     Note: the two exceptions are mutually exclusive
     */
    public void perform() throws InvalidMoveException, MarbleKilledException {
        isValidMove();
        moveAllFields();
        board.makeMapOfColors();
        if (marbleKilled) {
            throw new MarbleKilledException();
        }
    }
    
    /**
     * Checks if a move is valid without performing it.
     * First it checks if the selection is valid, then makes an array of the fields,
     * so it can check if they are occupied and if the destination is adjacent.
     * After that it checks whether the move is along the axis of the selection
     * or not, then in calls the appropriate method for each case.
     * @throws InvalidMoveException if any of the conditions are violated
     */
    public void isValidMove() throws InvalidMoveException {
        isValidSelection();
        this.fields = getSelectedFields();
        areAllOccupied();
        destinationIsAdjacent();
        if (moveIsAlongAxis()) {
            canMoveField(findLocomotive(), fields.length - 1);
        } else {
            canMoveOneByOne();
        }
    }
    
    /**
     * Sees if a move is valid but does not check if selection is valid.
     * @requires selection is valid
     */
    public void isValidMoveQuick() throws InvalidMoveException {
        isValidSelection();
        this.fields = getSelectedFields();
        areAllOccupied();
        destinationIsAdjacent();
        if (moveIsAlongAxis()) {
            canMoveField(findLocomotive(), fields.length - 1);
        } else {
            canMoveOneByOne();
        }
    }
    
    
    /**
     * Get the size of the selection.
     * @requires selection is valid
     * @return the length of the field array
     */
    public int getSelectionSize() {
        return getSelectedFields().length;
    }
    
    /**
     * Moves every field in selection. This assumes the move is valid. So force
     * is not considered.
     * If the move is along the axis, the tail is moved first, which recursively
     * moves all other marbles in front of it.
     * If the move is not along the axis, all marbles are moved separately.
     * @throws InvalidMoveException from moveIsAlongAxis()
     * @requires move is valid
     */
    private void moveAllFields() throws InvalidMoveException {
        if (!moveIsAlongAxis()) {
            for (Field f : fields) {
                doMoveField(f);
            }
        } else {
            doMoveField(findLastWagon());
        }
    }

    /**
     * Moves a field.
     * If next field is empty, nothing special.
     * If next field is taken, recursively move next field.
     * If next field is invalid, kill marble and set marbleKilled
     * to true so an exception will be thrown.
     * @param field to be moved
     */
    private void doMoveField(Field f) {
        Field nextField = getNextField(f);
        if (nextField == null || !nextField.isValid()) {
            f.setMarble(null);
            marbleKilled = true;
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
     * Check if the destination is adjacent to the tail marble.
     * There are three cases when this is true:
     * if the row difference is 1 and the column difference is 0 (vertical move)
     * if the row difference is 0 and the column difference is 1 (horizontal move)
     * if the row difference is 1 and the column difference is 1 (diagonal move)
     * @throws InvalidMoveException if condition is violated
     */
    private void destinationIsAdjacent() throws InvalidMoveException {
        if (!(Math.abs(rowTail - rowDest) == 1 && Math.abs(colTail - colDest) == 0)
                && !(Math.abs(rowTail - rowDest) == 0
                && Math.abs(colTail - colDest) == 1)
                && !(Math.abs(rowTail - rowDest) == 1
                && Math.abs(colTail - colDest) == 1)) {
            throw new InvalidMoveException("Move destination not adjacent; "
                    + toHumanString());
        }
    }
        
    /**
     * Finds the field of the locomotive marble. This means the field in front.
     * This is only relevant if the move is along the axis. 
     * If move is not along axis, returns any field.
     * @return a field in the selection
     */
    private Field findLocomotive() {
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
     * Finds last wagon of the selection. This is the marble in the back.
     * Only relevant for moving along axis.
     * If move is not along axis, returns any field.
     * @return a field of the selection
     * @throws InvalidMoveException from moveIsAlongAxis()
     */
    private Field findLastWagon() throws InvalidMoveException {
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
     * Checks if each marble in selection can be moved without pushing.
     * Used for moving laterally.
     * The concept of force is explained in the canMoveField method.
     * @throws InvalidMoveException if either marble cannot be moved 
     */
    private void canMoveOneByOne() throws InvalidMoveException {
        for (Field f : fields) {
            canMoveField(f, -1);
        }
    }
    
    /**
     * From a given field, gets the next field in direction of the movement.
     * rowMove and colMove is the movement vector.
     * @requires move along axis since moving vector is only created in such a case
     */
    private Field getNextField(Field field) {
        return board.getField(field.getRow() + rowMove, field.getCol() + colMove);
    }

    /**
     * Same as getNextField but the other way.
     */
    private Field getPrevField(Field field) {
        return board.getField(field.getRow() - rowMove, field.getCol() - colMove);
    }
    
    /**
     * Check if a marble can be moved with a given force. The force basically means
     * how many more enemy marbles we have the capacity to push, and is increased
     * if a friendly marble is encountered, or decreased if an enemy marble is
     * encountered.
     * If next field is out of board, movement can only be done if current marble
     * to be pushed is not friendly, since suicide is not allowed.
     * If the force is -1, which only happens if the move is lateral, nothing can
     * be pushed.
     * If force is 0, the only way a push can happen is if the current marble and
     * the next marble are both from the color of the color initiating the move (to
     * be referred as color of the move).
     * If the force is positive it gets complicated. If the next field is empty,
     * the move is allowed. Otherwise there are two combinations where a push is
     * allowed:
     * -if the current and the next marble are friendly; in this case a push is
     * allowed if the maximum force is not reached (in the normal game a maximum
     * of 3 friendly marbles can be pushed.
     * -if the current is friendly and the next is an enemy; then the next one is
     * pushed with the force decremented by one
     * -if the current and the next marble are both of an opponent; in this case
     * the function is called on the next marble with the force decremented by 1.
     * These cases are necessary in order to prevent moves like (FFEFE) where F
     * is friendly and E is an enemy. Thus once an enemy marble is being pushed,
     * a friendly one cannot be added to the stack.
     * This method works recursively: if a push is allowed for the current marble,
     * the method is called for the next marble using the appropriate force.
     * UPDATE: this method was changed to comply with protocol.
     * 
     * @param f current field that is pushed
     * @param force indicates how many enemy marbles can still be pushed
     * @throws InvalidMoveException if not valid for whatever reason
     */
    private void canMoveField(Field field, int force) throws InvalidMoveException {
        Field nextField = getNextField(field);
        Color currentColor = field.getMarble().getColor();
        if (nextField == null || !nextField.isValid()) {
            if (board.areTeammates(currentColor, color)) {
                throw new InvalidMoveException("You are not allowed to "
                        + "commit suicide; " + toHumanString());
            }
        } else {
            if (force == -1) {
                if (nextField.getMarble() != null) {
                    throw new InvalidMoveException("Cannot push laterally; "
                            + toHumanString());
                }
            } else if (force == 0) {
                if (nextField.getMarble() != null) {
                    throw new InvalidMoveException("Can't push without force;");
                }
            } else {
                if (!(nextField.getMarble() == null)) {
                    Marble nextMarble = nextField.getMarble();
                    if (board.areTeammates(currentColor, color)) {
                        if (board.areTeammates(nextMarble.getColor(), color)) {
                            throw new InvalidMoveException("Can't push own;");
                        } else {
                            canMoveField(nextField, force - 1);
                        }
                    } else {
                        if (!board.areTeammates(nextMarble.getColor(), color)) {
                            canMoveField(nextField, force - 1);
                        } else {
                            throw new InvalidMoveException("Invalid push: "
                                    + toHumanString());
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Check if move is along axis by checking if moving from the tail field
     * of the selection in the direction of the move vector (rowMove, colMove)
     * or opposite that direction ends up in one of the other fields. This is
     * only the case if the movement is along the axis.
     * If selection is of size one, the selection counts as being along the axis
     * for pushing purposes.
     * @return true if along axis, false otherwise
     * @throws InvalidMoveException if along the axis but not the way that is
     *     correct according to Wikipedia
     */
    private boolean moveIsAlongAxis() throws InvalidMoveException {
        rowMove = rowDest - rowTail;
        colMove = colDest - colTail;
        if (fields.length == 1) {
            return true;
        }
        for (Field f : fields) {
            if (f == board.getField(rowDest, colDest)) {
                return true;
            }
        }
        for (Field f : fields) {
            if (board.getField(rowTail + rowMove, colTail + colMove)
                    == f || board.getField(rowTail - rowMove, colTail
                    - colMove) == f) {
                throw new InvalidMoveException("Wikipedia does not allow this");
            }
        }
        return false;
    }
    
    /**
     * Checks if selection is valid based on three conditions:
     * -head and tail fields are valid.
     * -they are in the same line (horizontal, vertical or diagonal).
     * -the distance between is in the bounds given by the board (normally 3 or less).
     * @throws InvalidMoveException if either is violated
     */
    public void isValidSelection() throws InvalidMoveException {
        if (!board.isField(rowTail, colTail) || !board.isField(rowHead, colHead)) {
            throw new InvalidMoveException("Selection not valid; " + toHumanString());
        }
        areInSameLine();
        distanceWithinBounds();
    }
    
    /**
     * Finds the fields belonging to selection and makes an array of them.
     * First it calculates the size of the selection by looking at the vertical
     * and horizontal distance and taking the maximum (and adding 1). So if the
     * fields are 2 apart the distance is 3.
     * Then, starting at the tail, it goes through the fields in the direction of
     * the movement and stops when the array is filled.
     * @requires selection is valid
     * @return an array of fields in selection
     */
    private Field[] getSelectedFields() {
        int rowDiff = rowHead - rowTail;
        int colDiff = colHead - colTail;
        int size = Math.max(Math.abs(rowDiff), Math.abs(colDiff)) + 1;
        Field[] result = new Field[size];
        if (rowDiff != 0) {
            rowDiff = rowDiff / Math.abs(rowDiff);
        }
        if (colDiff != 0) {
            colDiff = colDiff / Math.abs(colDiff);
        }
        int row = rowTail;
        int col = colTail;
        for (int i = 0; i < size; i++) {
            result[i] = board.getField(row + rowDiff * i, col + colDiff * i);
        }
        return result;
    }
    
    /**
     * Checks if distance between head and tail is within the bounds given by
     * the board.
     * @throws InvalidMoveException if distance too large
     * @requires fields are in line
     */
    private void distanceWithinBounds() throws InvalidMoveException {
        if (!(Math.abs(rowTail - rowHead) < board.getMaxPush()
                && Math.abs(colTail - colHead) < board.getMaxPush())) {
            throw new InvalidMoveException("Selection too long; " + toHumanString());
        }
    }
    
    /**
     * Goes through the fields and checks whether they all contain a marble of the
     * color of the move (the color who initiated the move).
     * @throws InvalidMoveException if a field is not occupied
     */
    private void areAllOccupied() throws InvalidMoveException {
        for (Field f : fields) {
            if (f.getMarble() == null || f.getMarble().getColor() != color) {
                throw new InvalidMoveException("Field does not contain valid marble: "
                        + f.getFullString() + "; " + toHumanString());
            }
        }
    }
    
    /**
     * Checks if the head and tail of the selection are either in the same row,
     * same column, or same diagonal.
     * @throws InvalidMoveException if neither is the case
     */
    private void areInSameLine() throws InvalidMoveException {
        if (!((rowTail == rowHead) || (colTail == colHead) || (rowTail - colTail
                == rowHead - colHead))) {
            throw new InvalidMoveException("Fields not in same line; " + toHumanString());
        }
    }
    
    /**
     * Returns the array of the fields in the selection.
     * This is null until the selection gets checked and the fields are found.
     * @return an array of fields
     */
    public Field[] getFields() {
        return this.fields;
    }
    
    /**
     * Makes a deepcopy of the move. This is used when having to perform the move
     * on a different board (for example a deepcopy of the board).
     * @param newBoard is the board that the move should be copied to
     * @return a new move, which is the same move but on a different board.
     */
    public Move deepCopy(Board newBoard) {
        return new Move(newBoard, color, rowTail, colTail, rowHead, colHead,
                rowDest, colDest);
    }
    
    /**
     * Coordinate query.
     */
    public int getRowTail() {
        return rowTail;
    }
    
    /**
     * Coordinate query.
     */
    public int getColTail() {
        return colTail;
    }
    
    /**
     * Coordinate query.
     */
    public int getRowHead() {
        return rowHead;
    }
    
    /**
     * Coordinate query.
     */
    public int getColHead() {
        return colHead;
    }
    
    /**
     * Coordinate query.
     */
    public int getRowDest() {
        return rowDest;
    }
    
    /**
     * Coordinate query.
     */
    public int getColDest() {
        return colDest;
    }
    
    /**
     * Checks if two moves are equal by checking if all parameters are equal.
     * It is irrelevant if the board or color of both moves is different.
     * This is used for testing only.
     * @param m is the move that is to be compared to this one
     * @return true is the moves are equal, false if they differ in at least one
     *     coordinate
     */
    public boolean equalsMove(Move m) {
        return rowTail == m.getRowTail() && colTail == m.getColTail()
                && rowHead == m.getRowHead() && colHead == m.getColHead()
                && rowDest == m.getRowDest() && colDest == m.getColDest();
    }
    
    
    /**
     * Makes a new move that is the exact same, except on the other side of the
     * board (meaning after a 180 degrees rotation) and potentially belonging to
     * a different color.
     * This is used for testing only.
     * @param newColor is the color the new move should correspond to
     * @return a new move on the other side of the board
     */
    public Move getMirroredMove(Color newColor) {
        return new Move(board, newColor,
                board.rotate180(getRowTail()),
                board.rotate180(getColTail()),
                board.rotate180(getRowHead()),
                board.rotate180(getColHead()),
                board.rotate180(getRowDest()),
                board.rotate180(getColDest()));
    }
    
    /**
     * Make the same move but with the head and the tail flipped.
     * Naturally the destination also need to be changed.
     * This is used for testing only.
     * @return effectively the same move
     */
    public Move getFlipMove() {
        return new Move(board, color, rowHead, colHead, rowTail, colTail,
                rowHead + (rowDest - rowTail), colHead + (colDest - colTail));
    }
    
    /**
     * Make a string of the fields contained in the move.
     * Note: fields is empty if move is not checked yet.
     * @return a string containing the toString of the move and of the fields
     *     related to it
     */
    public String getStringOfFields() {
        String s = "";
        s += toString() + "\n";
        for (Field f : fields) {
            s += f.getFullString() + "\n";
        }
        return s;
    }
    
    /**
     * Transform the internal coordinates into the user-friendly ones.
     * @return
     */
    public String[] getUserCoordinates() {
        String[] result = new String[3];
        result[0] = "" + board.getRowLetter(rowTail) + board.getColLetter(colTail);
        result[1] = "" + board.getRowLetter(rowHead) + board.getColLetter(colHead);
        result[2] = "" + board.getRowLetter(rowDest) + board.getColLetter(colDest);
        return result;
    }
    
    /**
     * Method toString of Move.
     * @return string that contains the color and the head, tail and destination
     *     coordinates.
     */
    public String toString() {
        return "Color " + color + " moves (" + rowTail + ", " + colTail + "),"
                + "(" + rowHead + ", " + colHead + ") to (" + rowDest + ", " + colDest + ")";
    }
    
    /**
     * Method toString of Move.
     * @return string that contains the color and the human readable head, tail
     *     and destination (in the form of board.MOVE_PATTERN).
     */
    public String toHumanString() {
        String s = "";
        for (String userCoordinate : getUserCoordinates()) {
            s += " " + userCoordinate;
        }
        return "Color " + color + " moves" + s + ".";
    }
}
