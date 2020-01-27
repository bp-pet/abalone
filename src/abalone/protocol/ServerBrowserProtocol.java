package abalone.protocol;

import abalone.server.AbaloneClientHandler;

/**
 * Defines the methods that the Abalone Server should support while not in lobby nor in game.
 * 
 * @version {@link ProtocolMessages#VERSION}
 * @author Daan Pluister
 */
public interface ServerBrowserProtocol extends ServerProtocol {

	/**
	 * Returns a String to be sent as a response to a Client HELLO request
	 * (<code>pm.HELLO + pm.DELIMITER + pm.VERSION + optional
	 * features to be decided</code>). After this a newline and doLobbies();
	 * 
	 * @return String to be sent to client as a handshake response.
	 */
	public String getHello();

	/**
	 * to a response of a lobby request the server sends a new line with a lobby for
	 * each lobby off the form:
	 * <code>pm.LOBBIES + pm.DELIMITER + lobbyName + pm.DELIMITER + player1Name + pm.DELIMITER + player1TeamName + etc</code>
	 * (lobby.toString()) <code> + \n + etc + \n + pm.EOT </code>.
	 */
	public String doLobbies();

	/**
	 * If the join request is successful the method returns the String
	 * <code>pm.JOIN + pm.DELIMITER + playerName + pm.DELIMITER + teamName</code> to
	 * be send to all clients in lobby. And lobby.toString() to joining player
	 * 
	 * If the join request has failed the server returns the String
	 * <code>pm.ERROR + pm.DELIMITER + pm.ERROR3 + pm.DELIMITER + errorMessage</code>
	 * The errorMessage could be: (pm.ERROR_MESSAGE_LOBBY_FULL) - (pm.ERROR_MESSAGE_LOBBY_TEAMS).
	 * 
	 * @requires lobbyName != null
	 * @requires playerName != null
	 * @requires teamName != null
	 * @param lobbyName  Name of the lobby
	 * @param playerName Name of the player
	 * @param teamName   Name of the team
	 * @return A join protocol message to be sent to all clients in lobbyName. Or
	 *         error protocol message to be send to the client with playerName.
	 */
	public String doJoin(AbaloneClientHandler client, String lobbyName, String playerName, String teamName);

}
