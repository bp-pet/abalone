package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import abalone.Board;
import abalone.Color;
import abalone.Move;
import abalone.exceptions.InvalidMoveException;

class MoveTest {

	/** board test instance */
	private Board board;
	private String msg;
	
	/**
	 * creates a two player board.
	 * @throws Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		board = new Board(2);
		msg = "ayy lmao";
	}
	
	@Test
	//should work
	void testMove1() {
		try {
			new Move(board, Color.WHITE, 2, 2, 2, 2, 3, 3).perform();
		} catch (InvalidMoveException e) {
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
		}
	}

	@Test
	//should work
	void testMove3() {
		try {
			new Move(board, Color.WHITE, 0, 0, 2, 2, 1, 1).perform();
		} catch (InvalidMoveException e) {
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
		}
	}
	
	@Test
	//should not work
	void testPushAgainstOwn() {
		try {
			new Move(board, Color.WHITE, 0, 2, 1, 2, 1, 2).perform();
			new Move(board, Color.WHITE, 0, 2, 2, 4, 0, 1).perform();
		} catch (InvalidMoveException e) {
			msg = e.getMessage();
		}
		assertTrue(msg.contains("ush"));
	}
	
	@Test
	//should not work
	void testSuicide() {
		try {
			new Move(board, Color.WHITE, 2, 2, 0, 0, 1, 1).perform();
		} catch (InvalidMoveException e) {
			msg = e.getMessage();
		}
		assertTrue(msg.contains("uicide"));
	}
	
	@Test
	//should work
	void testPushOff() {
		try {
			new Move(board, Color.WHITE, 0, 0, 2, 2, 1, 1).perform();
			new Move(board, Color.WHITE, 1, 1, 3, 3, 2, 2).perform();
			new Move(board, Color.WHITE, 2, 2, 4, 4, 3, 3).perform();
			new Move(board, Color.BLACK, 6, 6, 6, 6, 7, 6).perform();
			new Move(board, Color.WHITE, 3, 3, 5, 5, 4, 4).perform();
			new Move(board, Color.WHITE, 4, 4, 6, 6, 5, 5).perform();
		} catch (InvalidMoveException e) {
			fail();
		}
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
		}
		assertTrue(msg.contains("ush"));
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
		}
		assertTrue(msg.contains("ush"));
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
		}
		assertTrue(msg.contains("ush"));
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
		}
	}
	
	@Test
	//should not work
	void testPush1v1() {
		try {
			System.out.println(board.toString());
			new Move(board, Color.WHITE, 2, 2, 2, 2, 3, 3).perform();
			new Move(board, Color.WHITE, 3, 3, 3, 3, 4, 4).perform();
			new Move(board, Color.BLACK, 6, 6, 6, 6, 5, 5).perform();
			new Move(board, Color.WHITE, 4, 4, 4, 4, 5, 5).perform();
			System.out.println(board.toString());
		} catch (InvalidMoveException e) {
			msg = e.getMessage();
		};
		assertTrue(msg.contains("ush"));
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
		}
		assertTrue(msg.contains("idk"));
	}
}