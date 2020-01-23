package abalone;

import java.util.ArrayList;

import abalone.AI.RandomStrategy;
import ss.utils.TextIO;

/**
 * creates a standard Abalone game for local play.
 * 
 * @author Daan Pluister
 */
public class Abalone {

	/**
	 * priem.
	 * 
	 * @param args player1, player2
	 */
	public static void main(String[] args) {
		ArrayList<String> stringPlayers = new ArrayList<String>();
		if (args.length >= Game.MIN_PLAYERS) {
			// copies args to players
			for (int i = 0; i < args.length; i++) {
				if (i <= Game.MAX_PLAYERS) {
					stringPlayers.add(args[i]);
				} else {
					break;
				}
			}
		} else {
			//if not enough input arguments ask for players
			if (stringPlayers.size() < Game.MIN_PLAYERS) {
				System.out.printf("\n> Name player " + (stringPlayers.size() + 1) + "?\n? ");
			} else {
				System.out.printf("\n> Name player " + (stringPlayers.size() + 1) + " (or 'x' to exit)?\n? ");
			}
			String in = TextIO.getln();
			if (!in.equals("x") && stringPlayers.size() < Game.MAX_PLAYERS) {
				stringPlayers.add(in);
			}
			while (stringPlayers.size() < 2 || (!in.equals("x") && stringPlayers.size() < Game.MAX_PLAYERS)) {
				if (stringPlayers.size() < 2) {
					System.out.printf("\n> Name player " + (stringPlayers.size() + 1) + "?\n? ");
				} else {
					System.out.printf("\n> Name player " + (stringPlayers.size() + 1) + " (or 'x' to exit)?\n? ");
				}
				in = TextIO.getln();
				if (!in.equals("x") && !in.equals("") && stringPlayers.size() < Game.MAX_PLAYERS) {
					stringPlayers.add(in);
				}
			}
		}

		Game game = new Game(stringPlayers.toArray(new String[stringPlayers.size()]));
		game.start();
	}
}
