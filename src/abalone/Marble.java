package abalone;

/**
 * Marble class.
 * 
 * @invariance ensures color != null
 * 
 * @authors Daan Pluister, Bozhidar Petrov
 */
public class Marble {

    // -- Instance variables -----------------------------------------

    private Color color;
    
    // -- Constructors -----------------------------------------------
    
    /**
     * creates a new marble of color color.
     */
    public Marble(Color color) {
        this.color = color;
    }
    
    // -- Queries ----------------------------------------------------
    
    /**
     * The color of the marble.
     * @ensures color != null
     */
    public Color getColor() {
        return color;
    }
    
    // -- Commands ---------------------------------------------------
    
    /**
     * Sets the color of the marble.
     * @requires color is a valid color
     */
    public void setColor(Color color) {
        this.color = color;
    }
    
    /**
     * The toString of the marble is the toString of the color.
     */
    public String toString() {
        return color.toString();
    }
}
