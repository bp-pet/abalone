package abalone;

import abalone.AI.ItsOverAnakinIHaveTheHighGroundStrategy;
import abalone.AI.RandomStrategy;
import ss.utils.TextIO;

public class LocalGame extends Game {

	/**
	 * creates a new LocalGame using {@Link Game#Game(int)} and initialize players according to {@link #createPlayer(String, Color)}.
	 * 
	 * @param stringPlayers
	 */
	public LocalGame(String[] stringPlayers) {
		super(stringPlayers.length);
		Color current = Color.WHITE;
		for (int i = 0; i < stringPlayers.length; i++) {
			players[i] = createPlayer(stringPlayers[i], current);
			current = current.next(stringPlayers.length);
		}
	}
	
	/**
	 * creates player with Color color, if arg contains -R then Random Computer, else arg is
	 * the name of the Human player.
	 */
	public static Player createPlayer(String arg, Color color) {
		if (arg.contains("-R")) {
			return new ComputerPlayer(color, new RandomStrategy());
		} else if (arg.contains("-A")) {
			return new ComputerPlayer(color,
					new ItsOverAnakinIHaveTheHighGroundStrategy());
		} else {
			return new HumanPlayer(arg, color);
		}
	}

	@Override
	public void start() {
		boolean again = true;
		while (again) {
			play();
			System.out.println("> Want to play again? (Y/n)");
			String yn = TextIO.getlnString();
			  if (yn.equals("n")) {
			    again = false;
			    break;
			  } else if (yn.equals("y") || yn.equals("")) {
			  } else {
			     System.out.println("Sorry, I didn't catch that. Please answer y/n");
			  }
		}
	}


}
