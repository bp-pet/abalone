package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import abalone.Board;
import abalone.Color;
import abalone.Marble;
import abalone.Move;
import abalone.AI.RandomStrategy;
import abalone.AI.Strategy;
import abalone.exceptions.InvalidMoveException;
import abalone.Field;

/**
 * Test program for HumanPlayer
 * 
 * @author Daan Pluister
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
			new Move(board, Color.WHITE, 1, 0, 1, 0, 2, 0).perform();
			movesList = strategy.makeMovesList(board, Color.WHITE);
			for (Move m : movesList) {
				try {
					m.isValidMove();
				} catch (InvalidMoveException e) {
					fail();
				}
			}
		} catch (InvalidMoveException e) {
			fail();
		}
	}
}
