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
import abalone.exceptions.MarbleKilledException;
import abalone.Field;

/**
 * Test program for the board class. Tests the coordinate system (the
 * transformation between the user interface coordinates and the internal ones,
 * as well as other functions of the class.
 * 
 * @authors Daan Pluister, Bozhidar Petrov
 */
public class BoardTest {

	/** test variable board. */
	Board board;

	/** Test variable random marble. */
	Marble m;

	/**
	 * Create a test board with 2 player setup.
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
	 * Creates boards for 2 to 4 players which are printed to the standard output so
	 * that they can be checked visually.
	 */
	@Test
	public void testReset() {
		board = new Board();
		// System.out.println(board.toString());
		board.reset(2);
		// System.out.println(board.toString());
		board.reset(3);
		// System.out.println(board.toString());
		board.reset(4);
		// System.out.println(board.toString());
	}

	/**
	 * Test if toString of the deepcopy is the same as the original and if you
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

	/**
	 * Tests the map of colors by checking whether there are 14 marbles of each
	 * color in the beginning, and if every field in the map is indeed of the color
	 * of the key.
	 */
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
			assertEquals(f.getMarble().getColor(), Color.WHITE);
		}
		assertTrue(board.getStringMapOfColors().contains("W Coordinates (0, 0)"));
		assertTrue(board.getStringMapOfColors().contains("B Coordinates (7, 5)"));
	}

	/*
	 * Test whether the map of colors also works after a marble has been moved.
	 */
	@Test
	public void testMapOfColorsAfterMove() {
		try {
			new Move(board, Color.WHITE, 1, 0, 1, 0, 2, 0).perform();
		} catch (InvalidMoveException e) {
			fail();
		} catch (MarbleKilledException e) {
			fail();
		}
		testMapOfColors();
	}

	/**
	 * Test whether rotations work.
	 */
	@Test
	public void testRotate() {
		assertEquals(board.rotate180(0, 0)[0], board.getWidth() - 1);
		assertEquals(board.rotate180(0, 0)[1], board.getWidth() - 1);
	}

	/**
	 * Test whether the team system works.
	 */
	@Test
	public void testTeams() {
		Color[] team1 = new Color[2];
		team1[0] = Color.WHITE;
		team1[1] = Color.BLACK;
		Color[] team2 = new Color[2];
		team2[0] = Color.RED;
		team2[1] = Color.BLUE;
		Color[][] teams = new Color[2][2];
		teams[0] = team1;
		teams[1] = team2;
		board.setTeams(teams);
		assertTrue(board.areTeammates(Color.WHITE, Color.WHITE));
		assertTrue(board.areTeammates(Color.BLACK, Color.BLACK));
		assertTrue(board.areTeammates(Color.RED, Color.RED));
		assertTrue(board.areTeammates(Color.BLUE, Color.BLUE));
		assertTrue(board.areTeammates(Color.WHITE, Color.BLACK));
		assertTrue(board.areTeammates(Color.BLACK, Color.WHITE));
		assertTrue(board.areTeammates(Color.RED, Color.BLUE));
		assertTrue(board.areTeammates(Color.BLUE, Color.RED));
		assertFalse(board.areTeammates(Color.WHITE, Color.RED));
		assertFalse(board.areTeammates(Color.RED, Color.WHITE));
		assertFalse(board.areTeammates(Color.WHITE, Color.BLUE));
		assertFalse(board.areTeammates(Color.BLUE, Color.WHITE));
		assertFalse(board.areTeammates(Color.BLACK, Color.RED));
		assertFalse(board.areTeammates(Color.RED, Color.BLACK));
		assertFalse(board.areTeammates(Color.BLACK, Color.BLUE));
		assertFalse(board.areTeammates(Color.BLUE, Color.BLACK));
		assertTrue(board.getTeam(Color.WHITE).length == 2);
		assertTrue(board.getTeam(Color.BLUE).length == 2);
	}

	/**
	 * test for getFieldContent(int, int) and isEmptyField(int, int) in a two player board. Tested for the
	 * following cases: field has white marble, field has black marble and field has
	 * no content.
	 */
	@Test
	public void testGetField() {
		assertTrue(board.getFieldContent(0, 1).toString().contains("W"));
		assertFalse(board.isEmptyField(0, 1));
		assertTrue(board.getFieldContent(8, 8).toString().contains("B"));
		assertFalse(board.isEmptyField(8, 8));
		assertEquals(null, board.getFieldContent(4, 4));
		assertTrue(board.isEmptyField(4, 4));
	}
	
	/**
	 * test for setField.
	 */
	@Test
	public void testSetField() {
		board.setField(4, 4, new Marble(Color.BLACK));
		assertTrue(board.getFieldContent(4, 4) != null);
		assertTrue(board.getFieldContent(4, 4).getColor().toString().contains("B"));
	}
	
	/**
	 * test for move in board.
	 */
	@Test
	public void testMove() {

		assertTrue(board.getFieldContent(3, 3) == null);
		
		try {
			board.move(Color.WHITE, 0, 0, 2, 2, 1, 1);
		} catch (InvalidMoveException | MarbleKilledException e) {
			// should not happen
			e.printStackTrace();
		}
		assertTrue(board.getFieldContent(3, 3).getColor().toString().contains("W"));
	}
	
	/**
	 * test parseMovePattern.
	 */
	@Test
	public void testParseMovePattern() {
		Move move = null;
		try {
			move = board.parseMovePattern(Color.WHITE, "A1 C3 B2");
		} catch (InvalidMoveException e) {
			fail();
		}
		
		try {
			move = board.parseMovePattern(Color.WHITE, "A1 CB2");
		} catch (InvalidMoveException e) {
			assertTrue(e.getMessage().contains("not a valid choice"));
		}
		
		assertTrue(move.toString().contains("Color W moves"));
		assertTrue(move.toString().contains("(0, 0)"));
		assertTrue(move.toString().contains("(2, 2)"));
		assertTrue(move.toString().contains("to (1, 1)"));
	}

	
	/**
	 * test getNumberOfMarbles.
	 */
	@Test
	public void testGetNumberOfMarbles() {
		assertTrue(board.getNumberOfMarbles().contains("W14"));
		assertTrue(board.getNumberOfMarbles().contains("B14"));
	}
	
}
