package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import abalone.Color;
import abalone.HumanPlayer;

/**
 * Test program for HumanPlayer
 * @author Daan Pluister
 */
public class HumanPlayerTest {

	/** test variable human player. */
	HumanPlayer player;
	
    /**
     * 
	 */
    @BeforeEach
    public void setUp() {
    	player = new HumanPlayer("Daan", Color.BLACK);
    }

    /**
	 * Tests if the initial condition complies to the specification.
	 */
    @Test
    public void testToInt() {
        assertEquals(0, player.toInt('A'));
        assertEquals(0, player.toInt('a'));
        assertEquals(0, player.toInt('1'));
        assertEquals(8, player.toInt('I'));
        assertEquals(8, player.toInt('i'));
        assertEquals(8, player.toInt('9'));
    }
}
