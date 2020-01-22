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

	/**
	 * create a test board with 2 player setup
	 */
	@BeforeEach
	public void setUp() {
		board = new Board(2);
		strategy = new RandomStrategy();
	}

	@Test
	public void testMakeMovesList() {
		ArrayList<Move> movesList = strategy.makeMovesList(board, Color.WHITE);
		for (Move m : movesList) {
			try {
				m.isValidMove();
			} catch (InvalidMoveException e) {
				fail();
			}
		}
		assertEquals(movesList.size(), 72);
	}
	
	@Test
	public void testRandomStrategy() {
		
	}
}
