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
	private Board copy;
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

	//TODO Find out why black has 73 available moves and white 72
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
//		int counter = 0;
//		Move mirroredMove = new Move(board, Color.WHITE, 0, 0, 0, 0, 0, 0);
		for (Move m : movesList2) {
			try {
				m.isValidMove();
//				mirroredMove = m.getMirroredMove(Color.WHITE);
//				mirroredMove.isValidMove();
//				boolean ind = false;
//				for (Move moveW : movesList2) {
//					if (mirroredMove.equalsMove(moveW)) {
//						System.out.println(mirroredMove.toString());
//					}
//				}
//				if (!ind) {
//					counter++;
//					System.out.println(m.toString());
//					System.out.println("." + mirroredMove.toString());
//				}
			} catch (InvalidMoveException e) {
				fail();
			}
		}
//		System.out.println(counter);
//		System.out.println(board.toString());
		assertEquals(movesList2.size(), 73);
//		System.out.println(board.toString());
	}
	
//	@Test
//	public void testMovesListAfterMoving() {
//		try {
//			testMakeMovesList();
//			new Move(board, Color.WHITE, 1, 0, 1, 0, 2, 0).perform();
//			System.out.println(board.toString());
//			System.out.println(board.getStringMapOfColors());
//			new Move(board, Color.WHITE, 2, 0, 2, 0, 1, 0).perform();
//			testMakeMovesList();
//		} catch (InvalidMoveException e) {
//			System.out.println(e.getMessage());
//			fail();
//		}
//	}
}
