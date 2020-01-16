package abalone;

public enum Color {

	WHITE, BLACK, BLUE, RED;

	/**
	 * defines if next returns clockwise color or not, will be initialized when
	 * first next is called.
	 */
	private Boolean clockwise;

	public Color next(int numOfPlayers) {
    	switch (numOfPlayers) {
	    	case 2:
	    		switch (this) {
		    		case WHITE:
		    			return BLACK;
	    			default:
	    				return WHITE;
	    		}
	    	default:
	    		if (clockwise == null) {
	            	clockwise = (Math.random() < 0.5);
	        	}
	    		if (clockwise) {
	    			switch (this) {
		    		case BLUE:
		    			return WHITE;
		    		case BLACK:
		    			return BLUE;
	    			case RED:
	    				return BLACK;
	    			default: //WHITE
	    				if (numOfPlayers == 4) {
		    				return RED;
		    			}
		    			return BLACK;
	    			}	
	    		} else {
	    			switch (this) {
		    		case BLUE:
		    			return BLACK;
		    		case WHITE:
		    			return BLUE;
	    			case RED:
	    				return WHITE;
	    			default: //BLACK
	    				if (numOfPlayers == 4) {
		    				return RED;
		    			}
		    			return WHITE;
	    			}	
    			}
    		}
	
    	/**
    	 * 
    	 * @return (WHITE,BLACK,BLUE,RED) => (1,2,3,4)
    	 */
    	public int getInt() {
    		switch (this) {
    		case WHITE:
    			return 1;
    		case BLACK:
    			return 2;
    		case BLUE:
    			return 3;
    		case RED:
    			return 4;
    		default:
    			return 0;
    		}
    	}
    	

    	/**
    	 * toString method of enum Marble.
    	 */
    	public String toString() {
    		switch (this) {
    		case WHITE:
    			return "W";
    		case BLACK:
    			return "B";
    		case BLUE:
    			return "P";
    		case RED:
    			return "R";
    		default:
    			return " ";
    		}
    	}

	}

	
	