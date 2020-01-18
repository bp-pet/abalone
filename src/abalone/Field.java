package abalone;

public class Field {
	private Marble marble;
	private boolean valid;
	private int row;
	private int col;
	
	public Field(boolean valid, int row, int col) {
		this.valid = valid;
		marble = null;
		this.row = row;
		this.col = col;
	}
	
	/**
	 * getter for the marble on the field.
	 * @return marble on field. if no marble this is empty.
	 */
	public Marble getMarble() {
		return marble;
	}
	
	public void setMarble(Marble m) {
		marble = m;
	}
	
	public boolean isValid() {
		return valid;
	}
	
	public int getRow() {
		return row;
	}
	
	public int getCol() {
		return col;
	}
	
	public String getFullString() {
		return "Field " + toString() + " Coordinates " + row + col + "\n";
	}
	
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
