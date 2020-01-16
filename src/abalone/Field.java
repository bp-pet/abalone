package abalone;

public class Field {
	private Marble marble;
	private boolean valid;
	private int row;
	private int col;
	
	public Field(boolean valid) {
		this.valid = valid;
		marble = null;
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
