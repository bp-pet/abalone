package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import abalone.Board;
import abalone.exceptions.InvalidMoveException;

class MoveTest {

	/** board test instance */
	private Board board;
	
	/**
	 * creates a two player board.
	 * @throws Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		board = new Board(2);
	}

	@Test
	void testMove() {
		fail("Not yet implemented");
	}

	@Test
	void testPerform() {
		
		//Two ways to test for get Exception:
		//First way:
		Exception selectionException = assertThrows(InvalidMoveException.class, () -> {
	    	new Move(board, Color.WHITE, 0, 0, 3, 3, 1, 1).perform();
	    });
	    assertTrue(selectionException.getMessage().contains("Selection not valid"));
	    
	    //Second way:
	    try {
			new Move(board, Color.WHITE, 0, 0, 3, 3, 1, 1).perform();
		} catch (InvalidMoveException e) {
			assertTrue(selectionException.getMessage().contains("Selection not valid"));
		}
	    
	    //Two ways to test for no Exception:
	  	//First way:
		Exception noException = assertThrows(InvalidMoveException.class, () -> {
	    	new Move(board, Color.WHITE, 0, 0, 2, 2, 1, 1).perform();
	    });
	    assertEquals(null, noException);
	    
	    //Second way:
	    try {
			new Move(board, Color.WHITE, 0, 0, 3, 3, 1, 1).perform();
		} catch (InvalidMoveException e) {
			fail("Should not throw exception: " + e.getMessage());
		}
		
		fail("Not yet completely implemented");
//		assertTrue(new Move(board, 0, 0, 2, 2, 1, 1).isValidMove());
//		assertFalse(new Move(board, 0, 0, 3, 3, 1, 1).isValidMove());
//		assertFalse(new Move(board, 0, 1, 1, 0, 1, 1).isValidMove());
//		assertFalse(new Move(board, 0, 0, 1, 0, 1, 1).isValidMove());
	}

	@Test
	void testMoveAllFields() {
		fail("Not yet implemented");
	}

	@Test
	void testIsValidSelection() {
		fail("Not yet implemented");
	}

	@Test
	void testGetFields() {
		fail("Not yet implemented");
	}

	@Test
	void testToString() {
		fail("Not yet implemented");
	}

}
