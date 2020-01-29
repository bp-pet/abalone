package abalone;

import java.util.ArrayList;

import abalone.client.AbaloneClientTUI;
import abalone.client.AbaloneClientView;
import ss.utils.TextIO;

/**
 * Creates a standard Abalone game for local play.
 * 
 * @authors Daan Pluister, Bozhidar Petrov
 */
public class Abalone {
	/**
	 * Creates a LocalGame with players as given arguments. The first 4 given args are
	 * copied to LocalGame.createplayer. If no arguments or not enough arguments are
	 * given the arguments are asked manually.
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
			// if not enough input arguments ask for players
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
		
		AbaloneClientView view = new AbaloneClientTUI(null);

		Game game = new LocalGame(stringPlayers.toArray(new String[stringPlayers.size()]), view);
		game.start();
	}
}
