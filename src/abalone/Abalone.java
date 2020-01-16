package abalone;

import java.util.ArrayList;

import ss.utils.TextIO;

/**
 * creates a standard abalone game for local play.
 * 
 * @author Daan Pluister
 */
public class Abalone {

	public static final int MAX_PLAYERS = 4;
	
	/**
	 * creates player with Mark mark, if arg -N then Naive Computer, 
	 * if -S then new SmartStrategy else
	 * arg is the name of the Human player.
	 */
	public static Player createPlayer(String arg, Color color) {
//		if (arg.equals("-N")) {
//			return new ComputerPlayer(mark, new NaiveStrategy());
//		} else if (arg.equals("-S")) {
//			return new ComputerPlayer(mark, new SmartStrategy());
//		} else if (arg.equals("-P")) {
//			return new ComputerPlayer(mark, new PerfectStrategy());
//		} else {
			return new HumanPlayer(arg, color);
//		}
	}
        
	/**
	 * priem.
	 * @param args player1, player2
	 */
	public static void main(String[] args) {
		ArrayList<String> stringPlayers = new ArrayList<String>();
		// copies args to players
		for (int i = 0; i < args.length; i++) {
			if (i <= 4) {
				stringPlayers.add(args[i]);
			} else {
				break;
			}
		}
		
		// if not 4 players ask for more players
		if (args.length <= MAX_PLAYERS) {
			if (stringPlayers.size() < 2) {
				System.out.printf("\n> Name player " + (stringPlayers.size() + 1) + "?\n? ");
			} else {
				System.out.printf("\n> Name player " + (stringPlayers.size() + 1) + " (or 'x' to exit)?\n? ");
			}
			String in = TextIO.getln();
			if (! in.equals("x") && stringPlayers.size() < MAX_PLAYERS) {
				stringPlayers.add(in);
			}
			while (stringPlayers.size() < 2 || (! in.equals("x") && stringPlayers.size() < MAX_PLAYERS)) {
					if (stringPlayers.size() < 2) {
						System.out.printf("\n> Name player " + (stringPlayers.size() + 1) + "?\n? ");
					} else {
						System.out.printf("\n> Name player " + (stringPlayers.size() + 1) + " (or 'x' to exit)?\n? ");
					}
					in = TextIO.getln();
					if (! in.equals("x") && stringPlayers.size() < MAX_PLAYERS) {
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