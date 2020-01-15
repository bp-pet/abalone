package test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import abalone.Board;
import abalone.Move;

public class MoveTest {

	Move move;
	Board board;
	
	/**
	 * create a empty board
	 */
	@BeforeEach
    public void setUp() {
		Board board = new Board();
		board.setField(0 , 0, new Marble(Color.BLACK));
    }
	
    @Test
    public void testValidMove() {
    	assertTrue(new Move(board, 0, 0, 2, 2, 1, 1).isValidMove());
    	assertFalse(new Move(board, 0, 0, 3, 3, 1, 1).isValidMove());
    	assertFalse(new Move(board, 0, 1, 1, 0, 1, 1).isValidMove());
    	assertTrue(new Move(board, 0, 0, 1, 0, 1, 1).isValidMove());
    }
}
