package abalone.protocol;

import abalone.Color;
import abalone.server.AbaloneClientHandler;

/**
 * Defines the methods that the Abalone Server should support when client is in a lobby or in game.
 * 
 * @version {@link ProtocolMessages#VERSION}
 * @author Daan Pluister
 */
public interface ServerLobbyProtocol extends ServerProtocol {
	
	/**
	 * When a ready request is send the server sends a confirmation to all clients.
	 * If the last player sends a ready request the server will send a start
	 * specified at "Game Start".
	 * 
	 * returns <code>pm.READY + pm.DELIMITER + String playerName + pm.DELIMITER + teamName</code>
	 * 
	 * @param playerName
	 */
	public void doReady(AbaloneClientHandler client);

	/**
	 * Returns a String to be sent to all Clients in the same lobby as a response to
	 * the last Client READY request. String to be send is <code>pm.START + </code>
	 * for every player the p below
	 * 
	 * for every player:
	 * <code> + pm.DELIMITER + pm.PLAYER + pm.DELIMITER + String player1Name + pm.DELIMITER + String player1TeamName + pm.DELIMITER + String player1Color </code>
	 * 
	 * @return string to be send to all Clients who are in client.getLobby()
	 */
	public String doStart();

	/**
	 * Returns String <code>pm.TURN + pm.DELIMITER + String pm.COLOR_GIVEN_COLOR</code> to all clients in
	 * the game.
	 * <p>
	 * pm.COLOR_GIVEN_COLOR could be one of: pm.COLOR_WHITE, pm.COLOR_BLACK, pm.COLOR_BLUE, pm.COLOR_RED.
	 * 
	 */
	public void doTurn(Color color);

	/**
	 * to update all other clients that a client exited the game the exitGame
	 * returns String
	 * <code>pm.EXIT + pm.DELIMITER + playerName + pm.DELIMITER + playerTeamName</code>
	 * 
	 */
	public void exitGame(AbaloneClientHandler client);

	/**
	 * Returns <code>pm.GAME_END + pm.DELIMITER + result</code> if there is a winner
	 * or alternatively if a player disconnects:
	 * <code>pm.GAME_END + pm.DELIMITER + result + pm.DELIMITER + disconnectedColor</code>..
	 * 
	 * @return protocol message to be send to the user
	 */
	public String doGameEnd();

	/**
	 * After a move request the server checks the validity of the move and then
	 * sends message described in "Accepted move" and "Unaccepted move"
	 * accordingly..
	 * 
	 * If the move is accepted, the server confirms the move by forwarding the move
	 * to all clients. The new turn can be send after this:
	 * <code>pm.MOVE + pm.DELIMITER + <coordinates of marble 1>;<coordinates of marble 2>;<destinationof marble 1><code>.
	 * 
	 * If the move is deemed illegal by the server, the server responds only to the client whose turn it is. The client can then try another move: <code>pm.UNEXPECTED_MOVE</code>.
	 * 
	 * @return protocol message to be send to the user
	 */
	public String doMove(AbaloneClientHandler client, String pos1, String pos2, String des1);


}
