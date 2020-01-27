package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import abalone.Board;
import abalone.Color;
import abalone.Move;
import abalone.AI.ItsOverAnakinIHaveTheHighGroundStrategy;
import abalone.AI.RandomStrategy;
import abalone.AI.Strategy;
import abalone.exceptions.InvalidMoveException;
import abalone.exceptions.MarbleKilledException;

/**
 * Test program for the Strategy interface.
 * 
 * @author Bozhidar Petrov, Daan Pluister
 */
public class StrategyTest {
	
	private Board board;
	private Strategy strategy;
	ArrayList<Move> movesList;
	
	/**
	 * create a test board with 2 player setup
	 */
	@BeforeEach
	public void setUp() {
		board = new Board(2);
		strategy = new RandomStrategy();
	}

	/**
	 * Checks if moves list at start of the game is complete and valid.
	 * For white it simply checks if every move is valid and whether the
	 * total number of moves is equal to 72 (computed by hand).
	 * For black there was an issue so more extensive tests are implemented. For
	 * every move of black it is checked not only if it is valid, but also if its
	 * mirrored move (or the flipped move of its mirrored move) is valid for white.
	 * This way the 73rd move was detected and the bug in the move class was fixed.
	 * True story.
	 */
	@Test
	public void testMakeMovesList() {
		movesList = strategy.makeMovesList(board, Color.WHITE);
		for (Move m : movesList) {
			try {
				m.isValidMove();
			} catch (InvalidMoveException e) {
				fail();
			}
		}
		assertEquals(movesList.size(), 72);
		ArrayList<Move> movesList2 = strategy.makeMovesList(board, Color.BLACK);
		Move mirroredMove = new Move(board, Color.WHITE, 0, 0, 0, 0, 0, 0);
		for (Move m : movesList2) {
			try {
				m.isValidMove();
				mirroredMove = m.getMirroredMove(Color.WHITE);
				mirroredMove.isValidMove();
				boolean ind = false;
				for (Move moveW : movesList) {
					if (mirroredMove.equalsMove(moveW)) {
						ind = true;
					}
				}
				mirroredMove = mirroredMove.getFlipMove();
				for (Move moveW : movesList) {
					if (mirroredMove.equalsMove(moveW)) {
						ind = true;
					}
				}
				if (!ind) {
					fail();
				}
			} catch (InvalidMoveException e) {
				fail();
			}
		}
		assertEquals(movesList2.size(), 72);
	}
	
	
	/**
	 * Checks whether the move list test also works after marbles have moved.
	 */
	@Test
	public void testMovesListAfterMoving() {
		try {
			new Move(board, Color.WHITE, 2, 3, 2, 4, 3, 3).perform();
			new Move(board, Color.BLACK, 6, 6, 6, 5, 5, 6).perform();
			new Move(board, Color.WHITE, 3, 3, 3, 4, 4, 3).perform();
			new Move(board, Color.BLACK, 5, 5, 5, 6, 4, 5).perform();
			movesList = strategy.makeMovesList(board, Color.WHITE);
			assertEquals(strategy.makeMovesList(board, Color.WHITE).size(),
					strategy.makeMovesList(board, Color.BLACK).size());
			for (Move m : movesList) {
				try {
					m.isValidMove();
				} catch (InvalidMoveException e) {
					fail();
				}
			}
		} catch (InvalidMoveException e) {
			fail();
		} catch (MarbleKilledException e1) {
		}
	}
	
	/**
	 * Checks whether the distance finding method works.
	 */
	@Test
	public void testDistance() {
		ItsOverAnakinIHaveTheHighGroundStrategy strategy2 = new ItsOverAnakinIHaveTheHighGroundStrategy("20");
		assertEquals(strategy2.fieldDistanceFromCenter(board, board.getField(0, 0)), 4);
		assertEquals(strategy2.fieldDistanceFromCenter(board, board.getField(1, 1)), 3);
		assertEquals(strategy2.fieldDistanceFromCenter(board, board.getField(2, 2)), 2);
		assertEquals(strategy2.fieldDistanceFromCenter(board, board.getField(3, 3)), 1);
		assertEquals(strategy2.fieldDistanceFromCenter(board, board.getField(4, 4)), 0);
		assertEquals(strategy2.fieldDistanceFromCenter(board, board.getField(8, 6)), 4);
		assertEquals(strategy2.fieldDistanceFromCenter(board, board.getField(5, 8)), 4);
		assertEquals(strategy2.fieldDistanceFromCenter(board, board.getField(6, 2)), 4);
	}
}
