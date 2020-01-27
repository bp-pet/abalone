package abalone.client;

import abalone.Color;
import abalone.Game;
import abalone.Move;
import abalone.exceptions.InvalidMoveException;

/**
 * A local game run by the ClientGame that is synced with the server game. This method overwrites //TODO:"add overwrite" from Game.
 * 
 * @author Daan Pluiser
 */
public class ClientGame extends Game {

	/**
	 * Creates a clientGame with players given by the server of the form given as stringPlayers.
	 * 
	 * @param ownName the name of the player of your own.
	 * @param ownTeam the name of the team of your own.
	 * @param stringPlayers is of the form: "pm.START;whitePlayerName;whitePlayerTeam;blackPlayerName;blackPlayerTeam[;bluePlayerName;bluePlayerTeam;redPlayerName;redPlayerTeam]".split(";").
	 */
	public ClientGame(String[] stringPlayers, String ownName, String ownTeam) {
		super((stringPlayers.length - 1 ) / 2);
		Color current = Color.WHITE;
		for (int i = 0; i < (stringPlayers.length - 1 ) / 2; i++){
			if (stringPlayers[1 + i].equals(ownName) && stringPlayers[2 + i].equals(ownTeam)) {
				players[i] = new AbaloneOwnPlayer(stringPlayers[1 + i], current);
			} else {
				players[i] = new AbaloneServerPlayer(stringPlayers[1 + i], current);
			}
			current = current.next((stringPlayers.length - 1 ) / 2);
		}
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Does the move on the board. 
	 * @requires lineFromServer of the form "m(;[A-Ia-i][1-9]){3}".split(";")
	 * @param lineFromServer
	 */
	public void doMove(String[] lineFromServer) {
		Move move = board.parseMovePattern(current, lineFromServer[1] + " " + lineFromServer[2] + " " + lineFromServer[3]);
		try {
			board.move(move);
		} catch (InvalidMoveException e) {
			System.out.println("Server not correctly implemented.");
		}		
	}

}
