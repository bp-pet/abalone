package test;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import abalone.Board;
import abalone.Color;
import abalone.Marble;
import abalone.Move;
import abalone.exceptions.InvalidMoveException;
import abalone.Field;

/**
 * Test program for HumanPlayer
 * 
 * @author Daan Pluister
 */
public class BoardTest {

	/** test variable board. */
	Board board;

	/** test variable random marble */
	Marble m;

	/**
	 * create a test board with 2 player setup
	 */
	@BeforeEach
	public void setUp() {
		board = new Board(2);
		m = new Marble(Color.RED);
	}

	/**
	 * Tests if the input 1 to 9 gets translated to 0 to 8.
	 */
	@Test
	public void testGetColFromLetter() {
		assertEquals(0, board.getColFromLetter('1'));
		assertEquals(8, board.getColFromLetter('9'));
	}

	/**
	 * Tests if the input A to I gets translated to 0 to 8.
	 */
	@Test
	public void testGetRowFromLetter() {
		assertEquals(0, board.getRowFromLetter('A'));
		assertEquals(0, board.getRowFromLetter('a'));
		assertEquals(8, board.getRowFromLetter('I'));
		assertEquals(8, board.getRowFromLetter('i'));
	}
	
	/**
	 * Tests inverse of getRowFromLetter.
	 */
	@Test
	public void testGetRowLetter() {
		assertEquals('A', board.getRowLetter(0));
		assertEquals('I', board.getRowLetter(8));
	}
	
	/**
	 * Tests inverse of getColFromLetter.
	 */
	@Test
	public void testGetColLetter() {
		assertEquals('1', board.getColLetter(0));
		assertEquals('9', board.getColLetter(8));
	}

	/**
	 * creates boards for 2 to 4 players which are printed to the standard output so
	 * that they can be checked visually.
	 */
	@Test
	public void testReset() {
		board = new Board();
//		System.out.println(board.toString());
//		board.reset(2);
//		System.out.println(board.toString());
//		board.reset(3);
//		System.out.println(board.toString());
//		board.reset(4);
//		System.out.println(board.toString());
	}

	/**
	 * test if toString of the deepcopy is the same as the original and if you
	 * modify the deepcopy object that the original stays the same.
	 */
	@Test
	public void testDeepCopy() {
		board = new Board(2);
		Board copy = board.deepCopy();
		assertEquals(copy.toString(), board.toString());
		copy.getField(4, 4).setMarble(m);
		assertNotEquals(copy.toString(), board.toString());
	}
	
	@Test
	public void testMapOfColors() {
		Map<Color, ArrayList<Field>> map = board.getMapOfColors();
		assertTrue(map.keySet().size() == 2);
		assertTrue(map.get(Color.BLACK).size() == 14);
		assertTrue(map.get(Color.WHITE).size() == 14);
		for (Field f : map.get(Color.BLACK)) {
			assertTrue(f.getMarble().getColor() == Color.BLACK);
		}
		for (Field f : map.get(Color.WHITE)) {
			assertTrue(f.getMarble() != null);
			assertEquals(f.getMarble().getColor(),Color.WHITE);
		}
	}
	
	@Test
	public void testMapOfColorsAfterMove() {
		try {
			new Move(board, Color.WHITE, 1, 0, 1, 0, 2, 0).perform();
		} catch (InvalidMoveException e) {
			fail();
		}
		testMapOfColors();
	}
	
	@Test
	public void testRotate() {
		assertEquals(board.rotate180(0, 0)[0], board.getWidth() - 1);
		assertEquals(board.rotate180(0, 0)[1], board.getWidth() - 1);
	}
}
