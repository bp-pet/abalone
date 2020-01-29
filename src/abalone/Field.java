package abalone;

/**
 * Field class.
 * 
 * @authors Bozhidar Petrov, Daan Pluister
 */
public class Field {
    private Marble marble;
    private boolean valid;
    private int row;
    private int col;
    
    /**
     * Makes a field with given coordinates and validity.
     */
    public Field(boolean valid, int row, int col) {
        this.valid = valid;
        marble = null;
        this.row = row;
        this.col = col;
    }
    
    /**
     * Getter for the marble on the field.
     * @return marble on field. if no marble return null.
     */
    public Marble getMarble() {
        return marble;
    }
    
    /**
     * Put a given marble on the field.
     */
    public void setMarble(Marble m) {
        marble = m;
    }
    
    /**
     * Check if field is valid.
     * @return true if field is valid
     */
    public boolean isValid() {
        return valid;
    }
    
    /**
     * Query.
     */
    public int getRow() {
        return row;
    }

    /**
     * Query.
     */
    public int getCol() {
        return col;
    }

    /**
     * Makes a string of the field with all of its details.
     */
    public String getFullString() {
        return "Field " + toString() + " Coordinates (" + row + ", " + col
                + ")";
    }

    /**
     * Makes a string of the field to be used in a TUI.
     */
    public String toString() {
        if (! valid) {
            return " ";
        }
        if (marble == null) {
            return "+";
        }
        return marble.toString();
    }
}
