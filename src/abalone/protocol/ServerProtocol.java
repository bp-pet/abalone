package abalone.protocol;

import abalone.exceptions.InvalidMoveException;
import abalone.exceptions.ServerUnavailableException;

/**
 * Defines the methods that the Abalone Server should support.
 * 
 * @version Other3V0.9
 * @author Daan Pluister
 */
public interface ServerProtocol {

	/**
	 * Returns a String to be sent as a response to a Client HELLO request
	 * (<code>pm.HELLO + pm.DELIMITER + pm.VERSION + optional
	 * features to be decided</code>) including the number of lobbies already in:
	 * pm.HELLO + pm.DELIMITER + numOfLobbies; However if the Protocol version does
	 * not match no message is send to the client.
	 * 
	 * @return String to be sent to client as a handshake response.
	 */
	public String getHello();

	/**
	 * If the join request is successful the method returns the String
	 * <code>pm.JOIN + pm.DELIMITER + playerName + pm.DELIMITER + teamName + pm.DELIMITER + assignedColor</code>
	 * 
	 * If the join request has failed the server retruns the String
	 * <code>pm.ERROR + pm.DELIMITER + pm.ERROR3 + pm.DELIMITER + errorMessage</code>
	 * The errorMessage could be: (Lobby is full) - (There already is the same
	 * player with the same team) - (Only two teams are possible).
	 * 
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
	public String doJoinGame(String lobbyName, String playerName, String teamName);

	/**
	 * Returns a String to be sent to all Clients in the same lobby as a response to
	 * a Client START request. String to be send is <code>pm.START</code>.
	 * 
	 * @return string to be send to all Clients who are in client.getLobby()
	 */
	public String doStart();

	/**
	 * Returns String <code>pm.TURN + pm.DELIMITER + color</code> to all clients in
	 * the game.
	 * 
	 * @return protocol message to be send to the user
	 */
	public String doTurn();

	/**
	 * Returns <code>pm.GAME_END + pm.DELIMITER + result</code> if there is a winner
	 * or alternatively if a player disconnects:
	 * <code>pm.GAME_END + pm.DELIMITER + result + pm.DELIMITER + disconnectedColor</code>..
	 *  
	 * @return protocol message to be send to the user
	 */
	public String doGameEnd();
}
