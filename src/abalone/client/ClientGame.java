package abalone.client;

import abalone.Color;
import abalone.Game;
import abalone.LocalGame;
import abalone.Player;
import abalone.exceptions.ProtocolException;
import abalone.exceptions.ServerUnavailableException;

/**
 * A local game run by the ClientGame that is synced with the server game. This
 * method overwrites //TODO:"add overwrite" from Game.
 * 
 * @author Daan Pluiser
 */
public class ClientGame extends Game {

	AbaloneClient c;
	private Color ownColor;
	private boolean isStarted;

	/**
	 * Creates a clientGame with players given by the server of the form given as
	 * stringPlayers.
	 * 
	 * @param ownName       the name of the player of your own.
	 * @param ownTeam       the name of the team of your own.
	 * @param stringPlayers is of the form:
	 *                      "pm.START;whitePlayerName;whitePlayerTeam;blackPlayerName;blackPlayerTeam[;bluePlayerName;bluePlayerTeam;redPlayerName;redPlayerTeam]".split(";").
	 */
	public ClientGame(String[] stringPlayers, AbaloneClient c, AbaloneClientView view, String ownName, String ownTeam) {
		super((stringPlayers.length - 1) / 2);
		isStarted = false;
		this.c = c;
		currentColor = Color.WHITE;
		for (int i = 0; i < (stringPlayers.length - 1); i = i + 2) {
			//TODO:remove debug line
			System.out.println("Client game from " + ownName + ", i: " + i + ", player Name: " + stringPlayers[1 + i] + ", team Name: " + stringPlayers[2 + i] + ", create ownplayer: " + (stringPlayers[1 + i].equals(ownName) && stringPlayers[2 + i].equals(ownTeam)));
			if (stringPlayers[1 + i].equals(ownName) && stringPlayers[2 + i].equals(ownTeam)) {
				Player controlPlayer = LocalGame.createPlayer(view, stringPlayers[1 + i], currentColor);
				players[i / 2] = new AbaloneOwnPlayer(c, controlPlayer);
				this.ownColor = (players[i / 2].getColor());
				//TODO: remove debug line
				System.out.println("Created ownPlayer");
			} else {
				players[i / 2] = new AbaloneServerPlayer(c, stringPlayers[1 + i], currentColor);
				//TODO: remove debug line
				System.out.println("Created serverPlayer");
			}
			currentColor = getNextColor();
		}
		//Print list of players to view
		for (Player player : players) {
			view.showMessage("Client " + ownName + " game: player: " + player.getName() + ", has color: " + player.getColor());
		}
	}

	/**
	 * gets the color of the ownplayer.
	 */
	public Color getOwnColor() {
		return ownColor;
	}
	
	/**
	 * gets the starting color from the server. Overrides the random player starts.
	 */
	@Override
	public Color getStartingColor() {
		return getNextTurn();
	}

	@Override
	public Color getNextTurn() {
		try {
			return c.getTurn();
		} catch (ServerUnavailableException | ProtocolException e) {
			// TODO Auto-generated catch block (now printStackTrace)
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void start() {
		isStarted = true;
		// TODO: stop when disconnection
		while (true) {
			//TODO: remove debug line
			System.out.println("going to play:");
			play();
		}
	}
	
	//TODO: fix this with signals
	public boolean isStarted() {
		return isStarted;
	}

}
