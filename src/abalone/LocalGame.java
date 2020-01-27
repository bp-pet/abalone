package abalone;

import abalone.AI.ItsOverAnakinIHaveTheHighGroundStrategy;
import abalone.AI.RandomStrategy;
import abalone.AI.ReverseAnakinStrategy;
import abalone.client.AbaloneClientView;
import ss.utils.TextIO;

/**
 * A game of Abalone to be played locally.
 * 
 * @authors Daan Pluister, Bozhidar Petrov
 */
public class LocalGame extends Game {

	/**
	 * creates a new LocalGame using {@Link Game#Game(int)} and initialize players according to {@link #createPlayer(String, Color)}.
	 * 
	 * @param stringPlayers
	 */
	public LocalGame(String[] stringPlayers) {
		super(stringPlayers.length);
		currentColor = Color.WHITE;
		for (int i = 0; i < stringPlayers.length; i++) {
			players[i] = createPlayer(null, stringPlayers[i], currentColor);
			currentColor = getNextColor();
		}
	}
	
	/**
	 * creates player with Color color with name as name of human player except if one of the following:
	 * <li>if name.contains("RandomStrategy") RandomStrategy
	 * <li>if name.contains("ItsOverAnakinIHaveTheHighGroundStrategy") ItsOverAnakinIHaveTheHighGroundStrategy
	 * <li>if name.contains("ReverseAnakinStrategy") ReverseAnakinStrategy
	 * <li>else HumanPlayer with view
	 * 
	 * @requires if humanplayer then view != null.
	 * @param view the view needed for humanplayer
	 */
	public static Player createPlayer(AbaloneClientView view, String name, Color color) {
		if (name.contains("RandomStrategy")) {
			return new ComputerPlayer(color, new RandomStrategy());
		} else if (name.contains("ItsOverAnakinIHaveTheHighGroundStrategy")) {
			return new ComputerPlayer(color,
					new ItsOverAnakinIHaveTheHighGroundStrategy());
		} else if (name.contains("ReverseAnakinStrategy")) {
			return new ComputerPlayer(color, new ReverseAnakinStrategy());
		} else {
			return new HumanPlayer(view, name, color);
		}
	}

	@Override
	public Color getNextTurn() {
		return getNextColor();
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
