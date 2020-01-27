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
<<<<<<< HEAD
						players[i] = new AbaloneClientPlayer(lobby, lobby.getPlayerName(client), lobby.getTeamName(client), current);
						current = getNextColor();
=======
						players[i] = new AbaloneClientPlayer(lobby, lobby.getPlayerName(client), lobby.getTeamName(client), currentColor);
						currentColor = getNextColor();
>>>>>>> c8c1b7dd9e35f7e6e24057f10d7d61ff8d74a705
					}
				}
				i++;
			}
			
		} else {
			currentColor = Color.WHITE;
			int i = 0;
			for (AbaloneClientHandler client : lobby.getClients()) {
<<<<<<< HEAD
				players[i++] = new AbaloneClientPlayer(lobby, lobby.getPlayerName(client), lobby.getTeamName(client), current);
				current = getNextColor();
=======
				players[i++] = new AbaloneClientPlayer(lobby, lobby.getPlayerName(client), lobby.getTeamName(client), currentColor);
				currentColor = getNextColor();
>>>>>>> c8c1b7dd9e35f7e6e24057f10d7d61ff8d74a705
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
	public Color getNextTurn() {
		return getNextColor();
	}
	
	@Override
	public void start() {
		// TODO: implement stop game when disconnection and go back to lobby
		while (true) {
			play();
		}
	}
}
