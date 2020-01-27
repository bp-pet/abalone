package abalone.server;

import java.util.ArrayList;

import abalone.exceptions.LobbyException;
import abalone.protocol.ProtocolMessages;
import abalone.protocol.ServerBrowserProtocol;

public class AbaloneServerBrowser extends AbaloneServer implements ServerBrowserProtocol {
	
	// -- Instance variables -----------------------------------------
	
	/** The list of lobbies */
	protected ArrayList<AbaloneServerLobby> lobbies;
	
	// -- Constructors -----------------------------------------------
	
	public AbaloneServerBrowser() {
		super();
		lobbies = new ArrayList<AbaloneServerLobby>();
	}

	// -- Queries ----------------------------------------------------
	
	/**
	 * Getter for the lobby of a client. If client not in lobby return null.
	 * @param client
	 * @return
	 */
	public AbaloneServerLobby getLobby(AbaloneClientHandler client) {
		for (AbaloneServerLobby lobby : lobbies) {
			if (lobby.hasClient(client)) {
				return lobby;
			}
		}
		return null;
	}
	
	// ------------------ Browser Methods --------------------------
	
	@Override
	public String getHello() {
		//TODO: throw exception or something
		String s = ProtocolMessages.HELLO + ProtocolMessages.DELIMITER + ProtocolMessages.VERSION;
		return s + "\n" + doLobbies();
	}

	@Override
	public String doLobbies() {
		String s = ProtocolMessages.EOT;
		for (AbaloneServerLobby lobby : lobbies) {
			s = lobby.toString() + "\n" + s;
		}
		return s;
	}

	@Override
	public String doJoin(AbaloneClientHandler client, String lobbyName, String playerName, String teamName) {
		AbaloneServerLobby addedToLobby = null;
		for (AbaloneServerLobby lobby : lobbies) {
			if (lobby.getName().equals(lobbyName)) {
				try {
					// try to add client to lobby
					lobby.addClient(client, playerName, teamName);
					addedToLobby = lobby;
				} catch (LobbyException e) {
					// send an error of type 3
					return doError(3, e.getMessage());
				}
			}
		}
		if (addedToLobby == null) {
			addedToLobby = new AbaloneServerLobby(lobbyName, client, playerName, teamName);
		}
		addedToLobby.sendMessage(ProtocolMessages.JOIN + ProtocolMessages.DELIMITER + playerName + ProtocolMessages.DELIMITER + teamName);
		return addedToLobby.toString();
	}

}
