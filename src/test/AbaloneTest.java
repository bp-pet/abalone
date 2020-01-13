package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.Test;

import abalone.*;
	
public class AbaloneTest {

	    /** Test variable for a <tt>Game</tt> object. */
	    private Game game;
	    	    
	    /**
	     * Sets up a game
	     */
	    @BeforeEach
	    public void setUp() {
	    	game = new Game(null);
	    }

	    /**
	     * checkIn First room should be a PricedRoom.
	     */
	    @Test
	    public void testMove() {	        
	    }
	    
}
