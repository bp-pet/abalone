package abalone.server;

import abalone.Color;
import abalone.Game;

public class ServerGame extends Game {

	private AbaloneClientPlayer[] players;
	
	/**
	 * constructs a game with all ClientPlayers
	 * @requires team name match and stuff.
	 * @param stringPlayers
	 */
	public ServerGame(AbaloneServerLobby lobby) {
		super(lobby.getNumberOfPlayers());
		if (getNumberOfPlayers() == 4) {
			int i = 0;
			String teamPlayer1 = null;
			currentColor = Color.BLUE;
			for (AbaloneClientHandler client : lobby.getClients()) {
				if (i == 0) {
					teamPlayer1 = lobby.getTeamName(client);
					players[i] = new AbaloneClientPlayer(lobby, lobby.getPlayerName(client), lobby.getTeamName(client), currentColor);
				} else {
					if (teamPlayer1.equals(lobby.getTeamName(client))) {
						players[i] = new AbaloneClientPlayer(lobby, lobby.getPlayerName(client), lobby.getTeamName(client), currentColor);
					} else {
						players[i] = new AbaloneClientPlayer(lobby, lobby.getPlayerName(client), lobby.getTeamName(client), currentColor);
						currentColor = getNextColor();
					}
				}
				i++;
			}
			
		} else {
			currentColor = Color.WHITE;
			int i = 0;
			for (AbaloneClientHandler client : lobby.getClients()) {
				players[i++] = new AbaloneClientPlayer(lobby, lobby.getPlayerName(client), lobby.getTeamName(client), currentColor);
				currentColor = getNextColor();
			}
		}
	}

	/**
	 * Query that returns the players
	 */
	public AbaloneClientPlayer[] getPlayers() {
		return players;
	}
	
	@Override
	public void start() {
		// TODO: implement stop game when disconnection
		while (true) {
			play();
		}
	}
}
