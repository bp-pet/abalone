package abalone.protocol;

/**
 * Protocol for Networked Abalone other3.
 * 
 * @version Other3V1.1
 * @author Daan Pluister
 */
public class ProtocolMessages {

	/**
	 * Version of the protocol in the form other3V([0-9])^+;
	 */
	public static final String VERSION = "other3V1.3";

	/**
	 * Delimiter used to separate arguments sent over the network.
	 */
	public static final String DELIMITER = ";";

	/**
	 * Sent as last line in a multi-line response to indicate the end of the text.
	 */
	public static final String EOT = "---EOT---";

	/** Used for the server-client handshake. */
	public static final char HELLO = 'h';

	/**
	 * The following chars are both used by the TUI to receive user input, and the
	 * server and client to distinguish messages.
	 */
	public static final char EXIT = 'x';
	public static final char LOBBY = 'l';
	public static final char PLAYER = 'p';
	public static final char JOIN = 'j';
	public static final char READY = 'r';
	public static final char START = 's';
	public static final char MOVE = 'm';
	public static final char TURN = 't';
	public static final char GAME_END = 'g';
	public static final char ERROR = 'e';
	public static final char HELP = 'h';
	/**
	 * Unexpected command: The command letter is not available.
	 * <p>
	 * String ERROR1 = ERROR + DELIMITER + '1'
	 */
	public static final String ERROR1 = ERROR + DELIMITER + '1';

	/**
	 * Unexpected argument: One or more arguments is invalid.
	 * <p>
	 * String ERROR1 = ERROR + DELIMITER + '2'
	 */
	public static final String ERROR2 = ERROR + DELIMITER + '2';

	/**
	 * Illegal argument: Performing this action is expected but not allowed. The
	 * server will indicate what is wrong. Examples: not your turn, lobby is full,
	 * etc.
	 * <p>
	 * String ERROR1 = ERROR + DELIMITER + '3'
	 */
	public static final String ERROR3 = ERROR + DELIMITER + '3';

	/**
	 * Unaccepted move: Special case of e;3, the performed move is not allowed by
	 * the server. The client can try again.
	 * <p>
	 * char UNEXPECTED_MOVE = 'u'
	 */
	public static final char UNEXPECTED_MOVE = 'u';
	
	/**
	 * The following are the allowed colors according to the protocol.
	 */
	public static final String COLOR_WHITE = "white";
	public static final String COLOR_BLACK = "black";
	public static final String COLOR_BLUE = "blue";
	public static final String COLOR_RED = "red";
	
	/**
	 * The following strings are possible error messages that can be combined with an error of type 3
	 */
	public static final String ERROR_MESSAGE_LOBBY_TEAMS = "player with that name and team exists in lobby";
	public static final String ERROR_MESSAGE_LOBBY_FULL = "lobby full";
	public static final String ERROR_MESSAGE_START_TEAMS = "teams do not match";
	public static final String ERROR_MESSAGE_START_NOT_ENOUGH = "not enough players";

	public static final String GAME_END_MESSAGE_GAME_WON = "game won";
	public static final String GAME_END_MESSAGE_DRAW = "draw";
	public static final String GAME_END_MESSAGE_DISCONNECTION = "disconnection";
	
	/**
	 * The following strings are possible error messages that can be combined with an error of type 1
	 */
	public static final String INVALID_COMMAND = "Invalid command";


}
