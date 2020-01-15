package abalone;
	
public enum Color {
	
    WHITE, BLACK, BLUE, RED;
	
    /**
     * Returns the next marble.
     * @return WHITE is returned if this marble is BLACK, that BLACK is returned 
     * 			when if marble is WHITE and EMPTY otherwise 
     *
     */
    public Color next() {
    	switch (this) {
    		case WHITE:
    			return BLACK;
    		case BLACK:
    			return WHITE;
			default:
				return null;
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
