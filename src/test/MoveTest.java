package test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import abalone.Board;
import abalone.Color;
import abalone.Move;
import abalone.exceptions.InvalidMoveException;
import abalone.exceptions.MarbleKilledException;


/**
 * A test for the move class. Checks for all kinds of specific
 * moves whether they are valid or invalid, depending on the move.
 * 
 * @author Bozhidar Petrov, Daan Pluister
 */
class MoveTest {

	/** board test instance */
	private Board board;
	/** used to make sure exception messages are correct */
	private String msg;
	
	/**
	 * The way this class works is that it tries different moves. Some of them are
	 * expected to be valid so if an exception is caught, the test fails. Others
	 * should be invalid so the test checks if an exception with the appropriate
	 * message is thrown (only a specific word in it).
	 */
	@BeforeEach
	void setUp() {
		board = new Board(2);
		msg = "";
	}
	
	@Test
	//should not work
	void test1() {
		try {
			new Move(board, Color.BLACK, 4, 1, 4, 4, 4, 2).perform();
		} catch (InvalidMoveException e) {
			msg = e.getMessage();
		} catch (MarbleKilledException e) {
			fail();
		}
		assertTrue(msg.contains("too long"));
	}
	
	@Test
	//should not work
	void test2() {
		try {
			new Move(board, Color.BLACK, 6, 4, 8, 4, 5, 3).perform();
		} catch (InvalidMoveException e) {
			msg = e.getMessage();
		} catch (MarbleKilledException e) {
			fail();
		}
		assertTrue(msg.contains("lateral"));
	}
	
	@Test
	void testPushOff4v3() {
		try {
			new Move(board, Color.WHITE, 0, 0, 2, 2, 1, 1).perform();
			new Move(board, Color.WHITE, 0, 1, 0, 1, 0, 0).perform();
			new Move(board, Color.WHITE, 1, 1, 3, 3, 2, 2).perform();
			new Move(board, Color.WHITE, 2, 2, 4, 4, 3, 3).perform();
			new Move(board, Color.WHITE, 0, 0, 0, 0, 1, 1).perform();
			new Move(board, Color.WHITE, 1, 1, 1, 1, 2, 2).perform();
			new Move(board, Color.WHITE, 2, 2, 2, 2, 3, 3).perform();
		} catch (InvalidMoveException e) {
			msg = e.getMessage();
		} catch (MarbleKilledException e) {
			fail();
		}
		assertTrue(msg.contains("push"));
	}
	
	@Test
	void testSelection() {
		try {
			new Move(board, Color.WHITE, 0, 0, 2, 2, 3, 3).isValidSelection();
			new Move(board, Color.WHITE, 0, 0, 1, 0, 3, 3).isValidSelection();
			new Move(board, Color.WHITE, 0, 0, 0, 2, 3, 3).isValidSelection();
		} catch (InvalidMoveException e) {
			fail();
		}
		try {
			new Move(board, Color.WHITE, 0, 0, 3, 3, 3, 3).isValidSelection();
		} catch (InvalidMoveException e) {
			msg = e.getMessage();
		}
		assertTrue(msg.contains("Selection"));
		msg = "";
		try {
			new Move(board, Color.WHITE, 1, 0, 0, 1, 3, 3).isValidSelection();
		} catch (InvalidMoveException e) {
			msg = e.getMessage();
		}
		assertTrue(msg.contains("line"));
		msg = "";
		try {
			new Move(board, Color.WHITE, -1, -1, 0, 0, 3, 3).isValidSelection();
		} catch (InvalidMoveException e) {
			msg = e.getMessage();
		}
		assertTrue(msg.contains("Selection"));
	}
	
	@Test
	//should work
	void testMove1() {
		try {
			new Move(board, Color.WHITE, 2, 2, 2, 2, 3, 3).perform();
		} catch (InvalidMoveException e) {
			fail();
		} catch (MarbleKilledException e) {
			fail();
		}
	}
	
	@Test
	//should work
	void testMove2() {
		try {
			new Move(board, Color.WHITE, 1, 1, 2, 2, 2, 2).perform();
		} catch (InvalidMoveException e) {
			fail();
		} catch (MarbleKilledException e) {
			fail();
		}
	}

	@Test
	//should work
	void testMove3() {
		try {
			new Move(board, Color.WHITE, 0, 0, 2, 2, 1, 1).perform();
		} catch (InvalidMoveException e) {
			fail();
		} catch (MarbleKilledException e) {
			fail();
		}
	}
	
	@Test
	//should work
	void testMoveLateral() {
		try {
			new Move(board, Color.WHITE, 2, 2, 2, 4, 3, 3).perform();
		} catch (InvalidMoveException e) {
			fail();
		} catch (MarbleKilledException e) {
			fail();
		}
	}
	
	@Test
	//should not work
	void testPushAgainstOwn() {
		try {
			new Move(board, Color.WHITE, 1, 0, 1, 0, 2, 1).perform();
			new Move(board, Color.WHITE, 2, 2, 2, 4, 2, 1).perform();
		} catch (InvalidMoveException e) {
			msg = e.getMessage();
		} catch (MarbleKilledException e) {
			fail();
		}
		assertTrue(msg.contains("push"));
	}
	
	@Test
	//should not work
	void testSuicide() {
		try {
			new Move(board, Color.WHITE, 2, 2, 0, 0, 1, 1).perform();
		} catch (InvalidMoveException e) {
			msg = e.getMessage();
		} catch (MarbleKilledException e) {
			fail();
		}
		assertTrue(msg.contains("suicide"));
	}
	
	@Test
	//should work
	void testPushOff() {
		try {
			new Move(board, Color.WHITE, 0, 0, 2, 2, 1, 1).perform();
			new Move(board, Color.WHITE, 1, 1, 3, 3, 2, 2).perform();
			new Move(board, Color.WHITE, 2, 2, 4, 4, 3, 3).perform();
			new Move(board, Color.BLACK, 6, 6, 6, 6, 6, 7).perform();
			new Move(board, Color.WHITE, 3, 3, 5, 5, 4, 4).perform();
			new Move(board, Color.WHITE, 4, 4, 6, 6, 5, 5).perform();
		} catch (InvalidMoveException e) {
			fail();
		} catch (MarbleKilledException e) {
			msg = e.getMessage();
		}
		assertTrue(!msg.equals(""));
	}
	
	@Test
	//should not work
	void testPushLateral() {
		try {
			new Move(board, Color.WHITE, 0, 0, 2, 2, 1, 1).perform();
			new Move(board, Color.WHITE, 1, 1, 3, 3, 2, 2).perform();
			new Move(board, Color.WHITE, 2, 2, 4, 4, 3, 3).perform();
			new Move(board, Color.BLACK, 6, 5, 6, 5, 5, 4).perform();
			new Move(board, Color.WHITE, 3, 3, 5, 5, 2, 3).perform();
		} catch (InvalidMoveException e) {
			msg = e.getMessage();
		} catch (MarbleKilledException e) {
			fail();
		}
		assertTrue(msg.contains("push"));
	}
	
	@Test
	//should not work
	void testPush3v3() {
		try {
			new Move(board, Color.WHITE, 0, 0, 2, 2, 1, 1).perform();
			new Move(board, Color.WHITE, 1, 1, 3, 3, 2, 2).perform();
			new Move(board, Color.WHITE, 2, 2, 4, 4, 3, 3).perform();
			new Move(board, Color.WHITE, 3, 3, 5, 5, 4, 4).perform();
		} catch (InvalidMoveException e) {
			msg = e.getMessage();
		} catch (MarbleKilledException e) {
			fail();
		}
		assertTrue(msg.contains("push"));
	}
	
	@Test
	//should work
	void testPush3v2() {
		try {
			new Move(board, Color.WHITE, 0, 0, 2, 2, 1, 1).perform();
			new Move(board, Color.WHITE, 1, 1, 3, 3, 2, 2).perform();
			new Move(board, Color.BLACK, 7, 7, 6, 6, 6, 6).perform();
			new Move(board, Color.WHITE, 2, 2, 4, 4, 3, 3).perform();
		} catch (InvalidMoveException e) {
			fail();
		} catch (MarbleKilledException e) {
			fail();
		}
	}
	
	@Test
	//should work
	void testPush3v1() {
		try {
			new Move(board, Color.WHITE, 0, 0, 2, 2, 1, 1).perform();
			new Move(board, Color.WHITE, 1, 1, 3, 3, 2, 2).perform();
			new Move(board, Color.BLACK, 6, 6, 6, 6, 5, 5).perform();
			new Move(board, Color.WHITE, 2, 2, 4, 4, 3, 3).perform();
		} catch (InvalidMoveException e) {
			fail();
		} catch (MarbleKilledException e) {
			fail();
		}
	}
	
	@Test
	//should not work
	void testPush2v2() {
		try {
			new Move(board, Color.WHITE, 1, 1, 2, 2, 2, 2).perform();
			new Move(board, Color.WHITE, 2, 2, 3, 3, 3, 3).perform();
			new Move(board, Color.BLACK, 6, 6, 7, 7, 5, 5).perform();
			new Move(board, Color.WHITE, 3, 3, 4, 4, 4, 4).perform();
		} catch (InvalidMoveException e) {
			msg = e.getMessage();
		} catch (MarbleKilledException e) {
			fail();
		}
		assertTrue(msg.contains("push"));
	}
	
	@Test
	//should work
	void testPush2v1(){
		try {
			new Move(board, Color.WHITE, 1, 1, 2, 2, 2, 2).perform();
			new Move(board, Color.WHITE, 2, 2, 3, 3, 3, 3).perform();
			new Move(board, Color.BLACK, 6, 6, 6, 6, 5, 5).perform();
			new Move(board, Color.WHITE, 3, 3, 4, 4, 4, 4).perform();
		} catch (InvalidMoveException e) {
			fail();
		} catch (MarbleKilledException e) {
			fail();
		}
	}
	
	@Test
	//should work
	void testPushOwn2v1() {
		try {
			new Move(board, Color.WHITE, 1, 4, 0, 4, 2, 4).perform();
		} catch (InvalidMoveException e) {
			fail();
		} catch (MarbleKilledException e) {
			fail();
		}
	}
	
	@Test
	//should work
	void testPushOw1v2() {
		try {
			new Move(board, Color.WHITE, 0, 4, 0, 4, 1, 4).perform();
		} catch (InvalidMoveException e) {
			fail();
		} catch (MarbleKilledException e) {
			fail();
		}
	}
	
	@Test
	//should work
	void testPushOwn1v2() {
		try {
			new Move(board, Color.WHITE, 0, 4, 0, 4, 1, 4).perform();
		} catch (InvalidMoveException e) {
			fail();
		} catch (MarbleKilledException e) {
			fail();
		}
	}
	
	@Test
	//should work
	void testPushOwn1v1() {
		try {
			new Move(board, Color.WHITE, 0, 0, 0, 0, 1, 0).perform();
		} catch (InvalidMoveException e) {
			fail();
		} catch (MarbleKilledException e) {
			fail();
		}
	}
	
	@Test
	//should not work
	void testPushOwn1v3() {
		try {
			new Move(board, Color.WHITE, 1, 1, 1, 1, 2, 1).perform();
			new Move(board, Color.WHITE, 2, 1, 2, 1, 2, 2).perform();
		} catch (InvalidMoveException e) {
			msg = e.getMessage();
		} catch (MarbleKilledException e) {
			fail();
		}
		assertTrue(msg.contains("push"));
	}
	
	@Test
	//should not work
	void testPushOwn3v1() {
		try {
			new Move(board, Color.WHITE, 0, 0, 2, 2, 1, 1).perform();
			new Move(board, Color.WHITE, 0, 1, 0, 1, 0, 0).perform();
			new Move(board, Color.WHITE, 0, 0, 2, 2, 1, 1).perform();
		} catch (InvalidMoveException e) {
			msg = e.getMessage();
		} catch (MarbleKilledException e) {
			fail();
		}
		assertTrue(msg.contains("push"));
	}
	
	@Test
	//should not work
	void testPush1v1() {
		try {
			new Move(board, Color.WHITE, 2, 2, 2, 2, 3, 3).perform();
			new Move(board, Color.WHITE, 3, 3, 3, 3, 4, 4).perform();
			new Move(board, Color.BLACK, 6, 6, 6, 6, 5, 5).perform();
			new Move(board, Color.WHITE, 4, 4, 4, 4, 5, 5).perform();
		} catch (InvalidMoveException e) {
			msg = e.getMessage();
		} catch (MarbleKilledException e) {
			fail();
		};
		assertTrue(msg.contains("push"));
	}
	
	@Test
	//should not work
	void testMove4() {
		try {
			new Move(board, Color.WHITE, 0, 0, 2, 2, 1, 1).perform();
			new Move(board, Color.WHITE, 1, 0, 1, 0, 0, 0).perform();
			new Move(board, Color.WHITE, 0, 0, 3, 3, 1, 1).perform();
		} catch (InvalidMoveException e) {
			msg = e.getMessage();
		} catch (MarbleKilledException e) {
			fail();
		}
		assertTrue(msg.contains("too long"));
	}
	
	@Test
	//should not work
	void testMoveWrongColor() {
		try {
			new Move(board, Color.BLACK, 2, 2, 2, 2, 3, 3).perform();
		} catch (InvalidMoveException e) {
			msg = e.getMessage();
		} catch (MarbleKilledException e) {
			fail();
		}
		assertTrue(msg.contains("contain"));
	}
	
	@Test
	void testMirroredMove() {
		int[] coords = new int[6];
		coords[0] = 8;
		coords[1] = 6;
		coords[2] = 7;
		coords[3] = 7;
		coords[4] = 9;
		coords[5] = 9;
		int[] invCoords = new int[6];
		for (int i = 0; i < 6; i++) {
			invCoords[i] = board.rotate180(coords[i]);
		}
		Move m1 = new Move(board, Color.BLACK, coords);
		Move m2 = new Move(board, Color.BLACK, invCoords);
		assertTrue(m1.getMirroredMove(Color.BLACK).equalsMove(m2));
	}
}