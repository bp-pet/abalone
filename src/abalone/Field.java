package abalone;

public class Field {
	private Marble marble;
	private boolean valid;
	
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
}
