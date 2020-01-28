package abalone.server;

import java.util.Map;

import abalone.Color;
import abalone.Game;
import abalone.Player;

public class ServerGame extends Game {
	
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
				//TODO: remove debug lines
				System.out.println(lobby.toString());
				System.out.println(lobby.getPlayerName(client));
				System.out.println(lobby.getTeamName(client));
				System.out.println(currentColor);
				players[i++] = new AbaloneClientPlayer(lobby, lobby.getPlayerName(client), lobby.getTeamName(client), currentColor);
				currentColor = getNextColor();
			}
		}
	}

	@Override
	public void start() {
		//TODO: stop when disconnection
		while (true) {
			play();
		}
	}
	
	public Player[] getPlayers() {
		// TODO Auto-generated method stub
		return players;
	}

}
