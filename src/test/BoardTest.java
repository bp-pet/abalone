package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import abalone.Board;

/**
 * Test program for HumanPlayer
 * @author Daan Pluister
 */
public class BoardTest {

	/** test variable human player. */
	Board board;
	
    /**
     * 
	 */
    @BeforeEach
    public void setUp() {
    	board = new Board();
    }

    /**
	 * Tests if the initial condition complies to the specification.
	 */
    @Test
    public void testGetCol() {
        assertEquals(0, board.getCol('1'));
        assertEquals(8, board.getCol('9'));
    }
    
    @Test
    public void testGetRow() {
        assertEquals(0, board.getRow('A'));
        assertEquals(0, board.getRow('a'));
        assertEquals(8, board.getRow('I'));
        assertEquals(8, board.getRow('i'));
    }
}
