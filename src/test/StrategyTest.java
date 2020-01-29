package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import abalone.Board;
import abalone.Color;
import abalone.Move;
import abalone.ai.ItsOverAnakinIHaveTheHighGroundStrategy;
import abalone.ai.RandomStrategy;
import abalone.exceptions.InvalidMoveException;
import abalone.exceptions.MarbleKilledException;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/**
 * Test program for the Strategy interface.
 * 
 * @author Bozhidar Petrov, Daan Pluister
 */
public class StrategyTest {
	
	private Board board;
	private RandomStrategy randomStrategy;
	private ItsOverAnakinIHaveTheHighGroundStrategy advancedStrategy;
	ArrayList<Move> movesList;
	
	/**
	 * Create a test board with 2 player setup.
	 */
	@BeforeEach
	public void setUp() {
		board = new Board(2);
		randomStrategy = new RandomStrategy();
		advancedStrategy = new ItsOverAnakinIHaveTheHighGroundStrategy();
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
		movesList = randomStrategy.makeMovesList(board, Color.WHITE);
		for (Move m : movesList) {
			try {
				m.isValidMove();
			} catch (InvalidMoveException e) {
				fail();
			}
		}

		assertEquals(movesList.size(), 54);
		
		ArrayList<Move> movesList2 = randomStrategy.makeMovesList(board, Color.BLACK);
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
		assertEquals(movesList2.size(), 54);
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
			movesList = randomStrategy.makeMovesList(board, Color.WHITE);
			assertEquals(randomStrategy.makeMovesList(board, Color.WHITE).size(),
					randomStrategy.makeMovesList(board, Color.BLACK).size());
			for (Move m : movesList) {
				try {
					m.isValidMove();
				} catch (InvalidMoveException e) {
					fail();
				}
			}
		} catch (InvalidMoveException e) {
			fail();
		} catch (MarbleKilledException e) {
			fail();
		}
	}
	
	/**
	 * Checks whether the distance finding method works.
	 */
	@Test
	public void testDistance() {
		assertEquals(advancedStrategy.fieldDistanceFromCenter(board, board.getField(0, 0)), 16);
		assertEquals(advancedStrategy.fieldDistanceFromCenter(board, board.getField(1, 1)), 9);
		assertEquals(advancedStrategy.fieldDistanceFromCenter(board, board.getField(2, 2)), 4);
		assertEquals(advancedStrategy.fieldDistanceFromCenter(board, board.getField(3, 3)), 1);
		assertEquals(advancedStrategy.fieldDistanceFromCenter(board, board.getField(4, 4)), 0);
		assertEquals(advancedStrategy.fieldDistanceFromCenter(board, board.getField(8, 6)), 16);
		assertEquals(advancedStrategy.fieldDistanceFromCenter(board, board.getField(5, 8)), 16);
		assertEquals(advancedStrategy.fieldDistanceFromCenter(board, board.getField(6, 2)), 16);
	}
	
	/**
	 * Tests whether the distance of black and white to the center is the same.
	 */
	@Test
	public void testDistanceSymmetry() {
		assertEquals(advancedStrategy.colorDistanceFromCenter(board, advancedStrategy
				.getOpponentColor(board, Color.WHITE)), advancedStrategy
				.colorDistanceFromCenter(board, Color.WHITE));
	}
	
	/**
	 * Test whether triplets are counted correctly.
	 */
	@Test
	public void testCountTriplets() {
		assertEquals(advancedStrategy.countTriplets(board, Color.WHITE), 22.1, 0.01);
	}
}
