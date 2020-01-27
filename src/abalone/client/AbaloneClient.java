package abalone.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

import abalone.exceptions.*;
import abalone.protocol.ClientProtocol;
import abalone.protocol.ProtocolMessages;

public class AbaloneClient implements ClientProtocol {

	private Socket serverSock;
	private BufferedReader in;
	private BufferedWriter out;
	private AbaloneClientView view;
	private ClientGame game;
	private String ownName;
	private String ownTeam;
	private int numberOfPlayers;
	private int ready;
	private boolean isReady;

	/**
	 * Constructs a new AbaloneClient. Initializes the view in this case the
	 * standard AbaloneClientTUI.
	 */
	public AbaloneClient() {
		view = new AbaloneClientTUI(this);
	}

	/**
	 * Starts a new AbaloneClient by creating a connection, followed by the HELLO
	 * handshake as defined in the protocol. After a successful connection and
	 * handshake, the view is started. The view asks for used input and handles all
	 * further calls to methods of this class.
	 * 
	 * When errors occur, or when the user terminates a server connection, the user
	 * is asked whether a new connection should be made.
	 */
	public void start() {
		try {
			createConnection();
		} catch (ExitProgram e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			handleHello();
			view.start();
		} catch (ServerUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Creates a connection to the server. Requests the IP and port to connect to at
	 * the view (view).
	 * 
	 * The method continues to ask for an IP and port and attempts to connect until
	 * a connection is established or until the user indicates to exit the program.
	 * 
	 * @throws ExitProgram if a connection is not established and the user indicates
	 *                     to want to exit the program.
	 * @ensures serverSock contains a valid socket connection to a server
	 */
	public void createConnection() throws ExitProgram {
		clearConnection();
		while (serverSock == null) {
			String host = "127.0.0.1";
			int port = 2727;

			// try to open a Socket to the server
			try {
				InetAddress addr = InetAddress.getByName(host);
				view.showMessage("Attempting to connect to " + addr + ":" + port + "...");
				serverSock = new Socket(addr, port);
				in = new BufferedReader(new InputStreamReader(serverSock.getInputStream()));
				out = new BufferedWriter(new OutputStreamWriter(serverSock.getOutputStream()));
			} catch (IOException e) {
				view.showMessage("ERROR: could not create a socket on " + host + " and port " + port + ".");

				// Do you want to try again? (ask user) TODO: implement
				if (false) {
					throw new ExitProgram("User indicated to exit.");
				}
			}
		}
	}

	/**
	 * Resets the serverSocket and In- and OutputStreams to null.
	 * 
	 * Always make sure to close current connections via shutdown() before calling
	 * this method!
	 */
	public void clearConnection() {
		serverSock = null;
		in = null;
		out = null;
	}

	/**
	 * Sends a message to the connected server, followed by a new line. The stream
	 * is then flushed.
	 * 
	 * @param msg the message to write to the OutputStream.
	 * @throws ServerUnavailableException if IO errors occur.
	 */
	public synchronized void sendMessage(String msg) throws ServerUnavailableException {
		if (out != null) {
			try {
				out.write(msg);
				out.newLine();
				out.flush();
			} catch (IOException e) {
				view.showMessage(e.getMessage());
				throw new ServerUnavailableException("Could not write " + "to server.");
			}
		} else {
			throw new ServerUnavailableException("Could not write " + "to server.");
		}
	}

	/**
	 * sends a single char to the server using {@link #sendMessage(String msg)}.
	 * 
	 * @param command
	 * @throws ServerUnavailableException
	 */
	public synchronized void sendMessage(char command) throws ServerUnavailableException {
		sendMessage(Character.toString(command));
	}

	/**
	 * Reads and returns one line split on pm.DELITMITER from the server.
	 * 
	 * @return the line split on pm.DELITMITER sent by the server.
	 * @throws ServerUnavailableException if IO errors occur.
	 */
	public String[] readLineFromServer() throws ServerUnavailableException {
		if (in != null) {
			try {
				// Read and return answer from Server
				String answer = in.readLine();
				if (answer == null) {
					throw new ServerUnavailableException("Could not read " + "from server.");
				}
				return answer.split(ProtocolMessages.DELIMITER);
			} catch (IOException e) {
				throw new ServerUnavailableException("Could not read " + "from server.");
			}
		} else {
			throw new ServerUnavailableException("Could not read " + "from server.");
		}
	}

	/**
	 * Reads and returns multiple lines from the server until the end of the text is
	 * indicated using a line containing ProtocolMessages.EOT.
	 * 
	 * @return the concatenated lines sent by the server.
	 * @throws ServerUnavailableException if IO errors occur.
	 */
	public String readMultipleLinesFromServer() throws ServerUnavailableException {
		if (in != null) {
			try {
				// Read and return answer from Server
				StringBuilder sb = new StringBuilder();
				for (String line = in.readLine(); line != null
						&& !line.equals(ProtocolMessages.EOT); line = in.readLine()) {
					sb.append(line + System.lineSeparator());
				}
				return sb.toString();
			} catch (IOException e) {
				throw new ServerUnavailableException("Could not read " + "from server.");
			}
		} else {
			throw new ServerUnavailableException("Could not read " + "from server.");
		}
	}

	/**
	 * Closes the connection by closing the In- and OutputStreams, as well as the
	 * serverSocket.
	 */
	public void closeConnection() {
		view.showMessage("Closing the connection...");
		try {
			in.close();
			out.close();
			serverSock.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// -- non connection methods
	// ---------------------------------------------------------

	
	//TODO: let isCommand throw an exception if not command.
	/**
	 * checks if the lineFromServer is the command given by the char command.
	 * 
	 * @param command one of ProtocolMassages commands.
	 */
	public boolean isCommand(char command, String[] lineFromServer) {
		return (lineFromServer[0].equals(Character.toString(command)));
	}

	/**
	 * returns true if lineFromServer is pm.ERRORx
	 * @param error pm.ERRORx
	 * @param lineFromServer
	 * @return
	 */
	public boolean isError(String error, String[] lineFromServer) {
		String serverError = lineFromServer[0] + ProtocolMessages.DELIMITER + lineFromServer[1];
		return (isCommand(ProtocolMessages.ERROR, lineFromServer) && serverError.equals(error));
	}

	/**
	 * returns the message of the error.
	 * @requires isError(lineFromServer)
	 */
	public String getErrorMessage(String[] lineFromServer) {
		if (lineFromServer[2] != null) {
			return "Error of type 3 from server with message: " + lineFromServer[2];
		} else {
			return "Error of type 3 from server without message.";
		}
	}
	
	public void resetReady() {
		ready = 0;
		this.isReady = false;
	}
	
	public boolean isReady() {
		return isReady;
	}

	public void setReady() {
		this.isReady = true;
	}
	
	/**
	 * if game != null returns State.GAME. <br>
	 * else if ownName != null returns State.LOBBY <br>
	 * else returns State.BROWSER 
	 * @return
	 */
	public State getState() {
		if (! (game == null)) {
			return State.GAME;
		} else if (! (ownName == null)) {
			return State.LOBBY;
		} else {
			return State.BROWSER;
		}
	}
	
	// -- Methods for/from SuperClass/Interfaces ---------------------

	@Override
	public void handleHello() throws ServerUnavailableException, ProtocolException {
		sendMessage(ProtocolMessages.HELLO + ProtocolMessages.DELIMITER + ProtocolMessages.VERSION);

		String[] lineFromServer = readLineFromServer();

		if (lineFromServer[1].equals(ProtocolMessages.VERSION) && isCommand(ProtocolMessages.HELLO, lineFromServer)) {
			view.showMessage("> Welcome to the Abalone Browser");
		} else {
			throw new ProtocolException(lineFromServer + " does not satisfy Server returns one line"
					+ " containing ProtocolMessages.HELLO + ProtocolMessages.DELIMITER + ProtocolMessages.VERSION");
		}

	}

	@Override
	public void doLobbies() throws ServerUnavailableException {
		sendMessage(ProtocolMessages.LOBBY);
		getLobbies();
	}

	@Override
	public void getLobbies() throws ServerUnavailableException {
		// TODO: make lobbies in nice human readable form.
		view.showMessage(readMultipleLinesFromServer());
	}

	@Override
	public void doJoinLobby(String lobbyName, String playerName, String teamName)
			throws ServerUnavailableException, ProtocolException {
		sendMessage(ProtocolMessages.JOIN + ProtocolMessages.DELIMITER + lobbyName + ProtocolMessages.DELIMITER
				+ playerName + ProtocolMessages.DELIMITER + teamName);
		//TODO: process the readMultipleLinesFromServer();
		view.showMessage(readMultipleLinesFromServer());
		this.ownName = playerName;
		this.ownTeam = teamName;
		//TODO: set number of players correct.
		numberOfPlayers = 1;
		resetReady();
//		String[] response = readMultipleLinesFromServer().split(ProtocolMessages.DELIMITER);
//		if (response[0].charAt(0) == (ProtocolMessages.ERROR)) {
//			//TODO: add if response[2] exists.
//			view.showMessage(response[2]);
//		}

	}
	
	@Override
	public void getJoinGame() throws ServerUnavailableException, ProtocolException {
		String[] lineFromServer = readLineFromServer();
		if (! isCommand(ProtocolMessages.JOIN, lineFromServer)) {
			throw new ProtocolException("Unexpected command from server");
		}
		numberOfPlayers++;
		resetReady();
	}
	
	@Override
	public void doReady() throws ServerUnavailableException, ProtocolException {
		sendMessage(ProtocolMessages.READY);
		if (ready + 1 == numberOfPlayers) {
			getStart();
		} else {
			String[] lineFromServer = readLineFromServer();
			if (! isCommand(ProtocolMessages.READY, lineFromServer)) {
				throw new ProtocolException("Unexpected command from server");
			}
			if (lineFromServer.length != 2) {
				throw new ProtocolException("Unexpected arguments from server");
			}
			setReady();
			ready++;
		}
	}
	
	@Override
	public void getReady() throws ServerUnavailableException, ProtocolException {
		String[] lineFromServer = readLineFromServer();
		if (! isCommand(ProtocolMessages.READY, lineFromServer)) {
			throw new ProtocolException("Unexpected command from server");
		}
		ready++;
	}

	@Override
	public void getStart() throws ServerUnavailableException, ProtocolException {
		String[] lineFromServer = readLineFromServer();
		if (!isCommand(ProtocolMessages.START, lineFromServer)) {
			throw new ProtocolException("Unexpected command from server");
		}
		game = new ClientGame(lineFromServer, ownName, ownTeam);
		game.start();
	}

	@Override
	public String getTurn() throws ServerUnavailableException, ProtocolException {
		String[] lineFromServer = readLineFromServer();
		if (!isCommand(ProtocolMessages.TURN, lineFromServer)) {
			throw new ProtocolException("Unexpected command from server");
		}
		if (lineFromServer.length != 2) {
			return "It is " + lineFromServer[1] + " turn.";
		} else {
			throw new ProtocolException("Unexpected arguments from server");
		}
	}

	@Override
	public void sendMove(String arg1, String arg2, String arg3)
			throws ServerUnavailableException, InvalidMoveException, ProtocolException {
		boolean valid = false;
		String[] lineFromServer = null;
		while (!valid) {
			sendMessage(ProtocolMessages.MOVE + ProtocolMessages.DELIMITER + arg1 + ProtocolMessages.DELIMITER + arg2
					+ ProtocolMessages.DELIMITER + arg3);
			lineFromServer = readLineFromServer();
			if (isCommand(ProtocolMessages.UNEXPECTED_MOVE, lineFromServer)) {
				throw new InvalidMoveException("UNEXPECTED_MOVE");
			} else {
				if (!isCommand(ProtocolMessages.MOVE, lineFromServer)) {
					throw new ProtocolException("Unexpected command from server");
				}
				valid = true;
			}
		}
		if (lineFromServer.length != 4) {
			throw new ProtocolException("Unexpected arguments from server");
		}
		game.doMove(lineFromServer);
	}

	@Override
	public void getMove() throws ServerUnavailableException, ProtocolException {
		String[] lineFromServer = readLineFromServer();
		if (! isCommand(ProtocolMessages.MOVE, lineFromServer)) {
			throw new ProtocolException("Unexpected command from server");
		}
		if (lineFromServer.length != 4) {
			throw new ProtocolException("Unexpected arguments from server");
		}
		game.doMove(lineFromServer);
	}

	@Override
	public void getGameEnd() throws ServerUnavailableException, ProtocolException {
		String[] lineFromServer = readLineFromServer();
		if (! isCommand(ProtocolMessages.GAME_END, lineFromServer)) {
			throw new ProtocolException("Unexpected command from server");
		}
		if (!(lineFromServer.length >= 2 && lineFromServer.length <= 4)) {
			throw new ProtocolException("Unexpected arguments from server");
		}
		switch(lineFromServer.length) {
		case 2:
			view.showMessage(lineFromServer[1]);
			break;
		case 3:
			view.showMessage(lineFromServer[1] + " by " + lineFromServer[2]);
			break;
		case 4:
			view.showMessage(lineFromServer[1] + " by " + lineFromServer[2] + " and " + lineFromServer[3]);
			break;
		default:
			throw new ProtocolException("Unexpected arguments from server");
		}
	}

	@Override
	public void sendExit() throws ServerUnavailableException {
		sendMessage(ProtocolMessages.EXIT);
		getLobbies();
	}
	
	// ------------------ Main --------------------------

	/**
	 * This method starts a new AbaloneClient.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		(new AbaloneClient()).start();
	}
}
