package abalone;
	
public enum Marble {
	
    EMPTY, WHITE, BLACK, BLUE, RED;
	
    /**
     * Returns the next marble.
     * @ensures that WHITE is returned if this marble is BLACK, that BLACK is returned 
     * 			when if marble is WHITE and EMPTY otherwise 
     * @return the other marble is this marble is not EMPTY or EMPTY
     */
    public Marble next() {
    	switch (this) {
    		case WHITE:
    			return BLACK;
    		case BLACK:
    			return WHITE;
    		default:
    			return EMPTY;
    	}
    }
    
    /**
     * toString method of enum Marble.
     */
    public String toString() {
    	switch (this) {
		case WHITE:
			return "B";
		case BLACK:
			return "W";
		default:
			return " ";
	}
}
}
