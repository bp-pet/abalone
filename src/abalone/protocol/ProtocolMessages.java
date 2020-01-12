package abalone.protocol;
	
/**
 * Protocol for Networked Abalone other3.
 * 
 * @author Daan Pluister
 */
public class ProtocolMessages {

	/**
	 * Version of the protocol in the form other3V([0-9])^+;
	 */
	public static final String VERSION = "other3V1";
	
	/**
	 * Delimiter used to separate arguments sent over the network.
	 */
	public static final String DELIMITER = ";";

	/**
	 * Sent as last line in a multi-line response to indicate the end of the text.
	 */
	public static final String EOT = "--EOT--";

	/** Used for the server-client handshake. */
	public static final char HELLO = 'h';

	/**
	 * The following chars are both used by the TUI to receive user input, and the
	 * server and client to distinguish messages.
	 */
	public static final char EXIT = 'x';
	public static final char LOBBY = 'l';
	public static final char JOIN = 'j';
	public static final char ERROR = 'e';
	public static final char START = 's';
	public static final char MOVE = 'm';
	public static final char TURN = 't';
	

}
