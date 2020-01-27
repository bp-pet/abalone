package abalone.client;

import abalone.Color;
import abalone.Game;
import abalone.HumanPlayer;
import abalone.LocalGame;
import abalone.Move;
import abalone.Player;
import abalone.exceptions.InvalidMoveException;
import abalone.exceptions.MarbleKilledException;
import abalone.exceptions.ProtocolException;
import abalone.exceptions.ServerUnavailableException;

/**
 * A local game run by the ClientGame that is synced with the server game. This method overwrites //TODO:"add overwrite" from Game.
 * 
 * @author Daan Pluiser
 */
public class ClientGame extends Game {

	AbaloneClient c;
	
	/**
	 * Creates a clientGame with players given by the server of the form given as stringPlayers.
	 * 
	 * @param ownName the name of the player of your own.
	 * @param ownTeam the name of the team of your own.
	 * @param stringPlayers is of the form: "pm.START;whitePlayerName;whitePlayerTeam;blackPlayerName;blackPlayerTeam[;bluePlayerName;bluePlayerTeam;redPlayerName;redPlayerTeam]".split(";").
	 */
	public ClientGame(String[] stringPlayers, AbaloneClient c, AbaloneClientView view, String ownName, String ownTeam) {
		super((stringPlayers.length - 1 ) / 2);
		this.c = c;
		Color current = Color.WHITE;
		for (int i = 0; i < (stringPlayers.length - 1 ) / 2; i++){
			if (stringPlayers[1 + i].equals(ownName) && stringPlayers[2 + i].equals(ownTeam)) {
				Player controlPlayer = LocalGame.createPlayer(view, stringPlayers[1 + i], current);
				players[i] = new AbaloneOwnPlayer(c, controlPlayer);
			} else {
				players[i] = new AbaloneServerPlayer(c, stringPlayers[1 + i], current);
			}
		}
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
		
	}

}
