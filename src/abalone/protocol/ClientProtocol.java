package abalone.protocol;

import abalone.Color;
import abalone.exceptions.*;

/**
 * Defines the methods that the Abalone Client should support.
 * 
 * @version Other3V1.3
 * @author Daan Pluister
 */
public interface ClientProtocol {

	/** imports the ProtocolMessages as pm. */
	ProtocolMessages pm = new ProtocolMessages();

	/**
	 * Handles the following server-client handshake: 1. Client sends
	 * <code>pm.HELLO +
	 * pm.DELIMITER + pm.VERSION</code> to server. 2. Server sends hello back then
	 * getLobbies should be called
	 * 
	 * This method sends the <code>pm.HELLO + pm.DELIMITER + pm.VERSION + optional
	 * features to be decided</code> and checks whether the server response is valid
	 * (first argument must be <code>pm.HELLO</code> second argument <code>int
	 * numberOfLobbies</code>).
	 * 
	 * - If the response is not valid, this method throws a
	 * <code>ProtocolException</code>.
	 * 
	 * - If the response is valid, <code>handleLobbies(numberOfLobbies)</code> is
	 * called.
	 * 
	 * @throws ServerUnavailableException if IO errors occur.
	 * @throws ProtocolException          if the server response is invalid.
	 */
	public void handleHello() throws ServerUnavailableException, ProtocolException;

	/**
	 * this method sends <code>pm.LOBBIES</code> to the server then getLobbies()
	 * should be called.
	 */
	public void doLobbies() throws ServerUnavailableException;

	/**
	 * Sends a join game request to the server.
	 * 
	 * Given the name of an existing or non-existing lobby lobbyName, a player name
	 * playerName and team name teamName, the doJoinGame() method sends the
	 * following message to the server:
	 * <code>pm.JOIN + pm.DELIMITER + lobbyName + pm.DELIMITER + playerName + pm.DELIMITER + teamName</code>
	 * 
	 * 
	 * if the join request is successful, it will confirm the join by sending the
	 * same message back. If <code>received lobbyName != lobbyName</code> or
	 * <code>received playerName != playerName</code> a
	 * <code>ProtocolException</code> is thrown with appropriate message.
	 * 
	 * else the server sends an exception of type 3 and tell the client what is
	 * wrong:
	 * <code> pm.ERROR + pm.DELIMITER + pm.ERROR3 + pm.DELIMITER + String errorMessage</code>.
	 * then doLobbies() will be called.
	 * 
	 * 
	 * @requires lobbyName != null
	 * @requires playerName != null
	 * @requires teamName != null
	 * @param lobbyName  Name of the lobby
	 * @param playerName Name of the player
	 * @param teamName   Name of the team
	 * @throws ServerUnavailableException if IO errors occur.
	 * @throws ProtocolException          if server responds with unexpected
	 *                                    message.
	 */
	public void doJoinLobby(String lobbyName, String playerName, String teamName)
			throws ServerUnavailableException, ProtocolException;

	/**
	 * Sends a game ready request to the server.
	 * 
	 * The doReady() method sends the following message to the server:
	 * <code>pm.READY</code>.
	 * 
	 * If not all players are ready getReady() should be called.
	 * 
	 * If all players are ready getStart() should be called.
	 *
	 * @throws ServerUnavailableException if IO errors occur.
	 * @throws ProtocolException 
	 */
	public void doReady() throws ServerUnavailableException, ProtocolException;

	/**
	 * Returns a string of the color as a response to a
	 * <code>pm.TURN + pm.DELIMITER + String color</code> server message.
	 * 
	 * @throws ServerUnavailableException if IO errors occur.
	 * @throws ProtocolException
	 */
	public Color getTurn() throws ServerUnavailableException, ProtocolException;

	/**
	 * given the move parameters doMove() sends a move request to the server.
	 * 
	 * This methods sends the following message to the server
	 * <code>pm.Move + pm.DELIMITER + arg1 + pm.DELIMITER + arg2 + pm.DELIMITER + arg3</code>.
	 * if move is valid the message is received back from the server.
	 * 
	 * if the move is invalid a single <code>pm.UNACCEPTED_MOVE</code> is received
	 * from the server. This method will then throw a InvalidMoveException.
	 * 
	 * @param arg1 coordinates of Marble 1
	 * @param arg2 coordinates of Marble 2
	 * @param arg3 destination of Marble 1
	 * @return 
	 * @requires arg1 != null && arg2 != null && arg3 != null.
	 * @throws ServerUnavailableException if IO errors occur.
	 * @throws InvalidMoveException       if move is invalid.
	 * @throws ProtocolException
	 */
	public void sendMove(String arg1, String arg2, String arg3)
			throws ServerUnavailableException, InvalidMoveException, ProtocolException;

	/**
	 * Sends a message to the server indicating that this client will exit:
	 * <code>ProtocolMessages.EXIT</code>;
	 * 
	 * If in a game or lobby the getLobbies() is called
	 * 
	 * If not in a game both the server and the client then close the connection.
	 * The client does this using the {@link #closeConnection()} method.
	 * 
	 * @throws ServerUnavailableException if IO errors occur.
	 */
	public void sendExit() throws ServerUnavailableException;
}
