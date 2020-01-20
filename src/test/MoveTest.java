package test;

import org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import abalone.Board;
import abalone.Color;
import abalone.Move;
import abalone.exceptions.InvalidMoveException;

class MoveTest {

	/** board test instance */
	private Board board;
	private Move move1;
	
	/**
	 * creates a two player board.
	 * @throws Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		board = new Board(2);
	}

	@Test
	void testBasic() {
		try {
			new Move(board, Color.WHITE, 0, 0, 2, 2, 1, 1).perform();
			new Move(board, Color.WHITE, 1, 1, 3, 3, 2, 2).perform();
			new Move(board, Color.BLACK, 6, 6, 7, 7, 5, 5).perform();
			new Move(board, Color.WHITE, 2, 2, 4, 4, 3, 3).perform();
		} catch (InvalidMoveException e) {
			System.out.println(board.toString());
			System.out.println(e.getMessage());
			fail();
		} finally {
			System.out.println(board.toString());
		}
	}
}