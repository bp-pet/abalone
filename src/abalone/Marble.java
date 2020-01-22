package abalone;

/**
 * Marble class
 * 
 * @invariance ensures color != null
 * @author Daan Pluister
 */
public class Marble {

	// -- Instance variables -----------------------------------------

	private Color color;
	
	// -- Constructors -----------------------------------------------
    
	/**
	 * creates a new marble of color color.
	 * @param color
	 */
	public Marble(Color color) {
		this.color = color;
	}
	
	// -- Queries ----------------------------------------------------
	
	public Color getColor() {
		return color;
	}
	
	// -- Commands ---------------------------------------------------
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public String toString() {
		return color.toString();
	}
}
