package abalone.server;

import abalone.Board;
import abalone.Color;
import abalone.Move;
import abalone.Player;

public class AbaloneClientPlayer extends Player {

	private AbaloneServerLobby lobby;
	private String playerName;
	private String teamName;
	
	public AbaloneClientPlayer(AbaloneServerLobby lobby, String playerName, String teamName, Color color) {
		super(playerName + " from team " + teamName, color);
		this.playerName = playerName;
		this.teamName = teamName;
		this.lobby = lobby;
	}

	@Override
	public Move determineMove(Board board, String stateOfGame) {
		lobby.doTurn(color);
		return lobby.getMove();
	}

	/**
	 * Query that returns the playerName.
	 * @return
	 */
	public String getPlayerName() {
		return playerName;
	}

	/**
	 * Query that returns the teamName.
	 * @return
	 */
	public String getTeamName() {
		return teamName;
	}
}
