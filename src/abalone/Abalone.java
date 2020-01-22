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
	 * creates player with Mark mark, if arg contains -R then Random Computer, else arg is
	 * the name of the Human player.
	 */
	public static Player createPlayer(String arg, Color color) {
		if (arg.contains("-R")) {
			return new ComputerPlayer(color, new RandomStrategy());
		} else {
			return new HumanPlayer(arg, color);
		}
	}

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

		Player[] players = new Player[stringPlayers.size()];
		Color current = Color.WHITE;
		boolean clockwise = (Math.random() < 0.5);
		for (int i = 0; i < stringPlayers.size(); i++) {
			players[i] = createPlayer(stringPlayers.get(i), current);
			current = current.next(stringPlayers.size(), clockwise);
		}
		Game game = new Game(players, clockwise);
		game.start();
	}
}
