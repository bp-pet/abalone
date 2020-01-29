package abalone.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

import abalone.Board;
import abalone.Color;
import abalone.LocalGame;
import abalone.Player;
import abalone.exceptions.*;
import abalone.protocol.ClientProtocol;
import abalone.protocol.ProtocolMessages;

public class AbaloneClient implements ClientProtocol {

	private Socket serverSock;
	private BufferedReader in;
	private BufferedWriter out;
	private AbaloneClientView view;
	private Board board;
	private String ownName;
	private String ownTeam;
	private String lobbyName;
	private boolean isReady;
	private Color currentColor;
	private Player controlPlayer;
	private boolean inlobby;
	private Object objectColor;
	private boolean everAsked;
	private Object objectEverAsked;

	/**
	 * Constructs a new AbaloneClient. Initializes the view in this case the
	 * standard AbaloneClientTUI.
	 */
	public AbaloneClient() {
		view = new AbaloneClientTUI(this);
		objectColor = new Object();
		objectEverAsked = new Object();
		everAsked = false;
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
			Thread serverInputHandler = new Thread(new AbaloneServerHandler(this, view));
			serverInputHandler.start();
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
				InetAddress addr = view.getIp();
				port = view.getInt("What is the port you want to connect to (2727) ?");
				view.showMessage("Attempting to connect to " + addr + ":" + port + "...");
				serverSock = new Socket(addr, port);
				in = new BufferedReader(new InputStreamReader(serverSock.getInputStream()));
				out = new BufferedWriter(new OutputStreamWriter(serverSock.getOutputStream()));
			} catch (IOException e) {
				view.showMessage("ERROR: could not create a socket on " + host + " and port " + port + ".");

				String again = view.getString("want to try again (Y/n) ? ");
				if (!(again.equals("") || again.equals("y"))) {
					throw new ExitProgram("User indicated to exit.");
				} else {
					createConnection();
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
				//TODO: remove debug
				view.showMessage("Going to send to server: " + msg);
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
	public String readLineFromServer() throws ServerUnavailableException {
		if (in != null) {
			try {
				// Read and return answer from Server
				String answer = in.readLine();
				if (answer == null) {
					throw new ServerUnavailableException("Could not read " + "from server.");
				}
				// TODO: remove debug line
				view.showMessage("Incoming message: " + answer);
				return answer;
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
	 * @return a array of lines sent by the server.
	 * @throws ServerUnavailableException if IO errors occur.
	 */
	public String[] readMultipleLinesFromServer() throws ServerUnavailableException {
		if (in != null) {
			try {
				// Read and return answer from Server
				StringBuilder sb = new StringBuilder();
				for (String line = in.readLine(); line != null
						&& !line.equals(ProtocolMessages.EOT); line = in.readLine()) {
					sb.append(line + System.lineSeparator());
				}
				return sb.toString().split(System.lineSeparator());
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

	// TODO: let isCommand throw an exception if not command. for nicer code.
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
	 * 
	 * @param error          pm.ERRORx
	 * @param lineFromServer
	 * @return
	 */
	public boolean isError(String error, String[] lineFromServer) {
		return (isCommand(ProtocolMessages.ERROR, lineFromServer)
				&& error.equals(lineFromServer[0] + ProtocolMessages.DELIMITER + lineFromServer[1]));
	}

	/**
	 * returns the message of the error.
	 * 
	 * @requires isError(lineFromServer)
	 */
	public String getErrorMessage(String[] lineFromServer) {
		if (lineFromServer[2] != null) {
			return "Error of type 3 from server with message: " + lineFromServer[2];
		} else {
			return "Error of type 3 from server without message.";
		}
	}

	// TODO: add javadoc to these trival methods

	public void resetReady() {
		this.isReady = false;
	}

	public boolean isReady() {
		return isReady;
	}

	public void setReady() {
		this.isReady = true;
	}

	public void resetInLobby() {
		this.inlobby = false;
	}

	public boolean inLobby() {
		return inlobby;
	}

	public void setInLobby() {
		this.inlobby = true;
	}

	/**
	 * if game != null returns State.GAME. <br>
	 * else if ownName != null returns State.LOBBY <br>
	 * else returns State.BROWSER
	 * 
	 * @return
	 */
	public State getState() {
		if (!(board == null)) {
			return State.GAME;
		} else if (inLobby()) {
			return State.LOBBY;
		} else {
			return State.BROWSER;
		}
	}

	// -- Methods for/from SuperClass/Interfaces ---------------------

	@Override
	public void handleHello() throws ServerUnavailableException, ProtocolException {
		sendMessage(ProtocolMessages.HELLO + ProtocolMessages.DELIMITER + ProtocolMessages.VERSION);

		String[] linesFromServer = readMultipleLinesFromServer();

		if (isCommand(ProtocolMessages.HELLO, linesFromServer[0].split(ProtocolMessages.DELIMITER))) {
			view.showMessage("> Welcome to the Abalone Browser");
		} else {
			throw new ProtocolException(linesFromServer[0] + " does not satisfy Server returns one line"
					+ " containing ProtocolMessages.HELLO");
		}
		for (String line : linesFromServer) {
			// TODO: make lobbies in nice human readable form.
			view.showMessage(line);
		}
	}

	@Override
	public void doLobbies() throws ServerUnavailableException {
		sendMessage(ProtocolMessages.LOBBY);
	}

	@Override
	public void doJoinLobby(String lobbyName, String playerName, String teamName) throws ServerUnavailableException {
		sendMessage(ProtocolMessages.JOIN + ProtocolMessages.DELIMITER + lobbyName + ProtocolMessages.DELIMITER
				+ playerName + ProtocolMessages.DELIMITER + teamName);
		this.lobbyName = lobbyName;
		ownName = playerName;
		ownTeam = teamName;

		// TODO: for now we just put the state true but it should get confirmation from
		// server.
		setInLobby();
	}

	@Override
	public void doReady() throws ServerUnavailableException {
		sendMessage(ProtocolMessages.READY);
		setReady();
	}

	/**
	 * creates a game with the format
	 * pm.START;whitePlayerName;whitePlayerTeam;blackPlayerName;blackPlayerTeam[;bluePlayerName;bluePlayerTeam;redPlayerName;redPlayerTeam]
	 * and starts it
	 * 
	 * @param lineFromServer
	 */
	public void makeBoard(String lineFromServer) {
		resetReady();
		view.showMessage("Creating a board... ");
		String[] split = lineFromServer.split(ProtocolMessages.DELIMITER);

		Color tempCurrentColor = Color.WHITE;
		for (int i = 0; i < (split.length - 1); i = i + 2) {
			if (split[1 + i].equals(ownName) && split[2 + i].equals(ownTeam)) {
				controlPlayer = LocalGame.createPlayer(view, split[1 + i], tempCurrentColor);
			}
			tempCurrentColor = getNextColor((split.length - 1) / 2, tempCurrentColor);
		}
		board = new Board((split.length - 1) / 2);
	}

	public Color getNextColor(int numOfPlayers, Color currentColor) {
		switch (numOfPlayers) {
			case 2:
				switch (currentColor) {
					case WHITE:
						return Color.BLACK;
					case BLACK:
						return Color.WHITE;
					default:
						return null;
				}
			case 3:
				switch (currentColor) {
					case BLUE:
						return Color.BLACK;
					case BLACK:
						return Color.WHITE;
					case WHITE:
						return Color.BLUE;
					default:
						return null;
				}
			case 4:
				switch (currentColor) {
					case BLACK:
						return Color.RED;
					case RED:
						return Color.WHITE;
					case WHITE:
						return Color.BLUE;
					case BLUE:
						return Color.BLACK;
					default:
						return null;
				}
			default:
				return null;
		}
	}

	@Override
	public void sendMove(String arg1, String arg2, String arg3) throws ServerUnavailableException {
		sendMessage(ProtocolMessages.MOVE + ProtocolMessages.DELIMITER + arg1 + ProtocolMessages.DELIMITER + arg2
				+ ProtocolMessages.DELIMITER + arg3);
	}

	@Override
	public void sendExit() throws ServerUnavailableException {
		sendMessage(ProtocolMessages.EXIT);
		resetInLobby();
		doLobbies();
	}

	public void switchTeam(String newTeamName) throws ServerUnavailableException {
		sendMessage(ProtocolMessages.EXIT);
		ownTeam = newTeamName;
		doJoinLobby(lobbyName, ownName, newTeamName);
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

	@Override
	public Color getTurn() {
		Color toReturn = getCurrentColor();
		return toReturn;
	}

	@Override
	public void getGameEnd() throws ServerUnavailableException, ProtocolException {
		// TODO Auto-generated method stub

	}

	// TODO: add javadoc to these trival getters.

	public Color getCurrentColor() {
		synchronized(objectColor) {
			try {
				if (! everAsked) {
					synchronized (objectEverAsked) {
						everAsked = true;
						objectEverAsked.notifyAll();
					}
				}
				objectColor.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return currentColor;
	}

	public void setCurrentColor(Color currentColor) {
		if (! everAsked) {
			synchronized (objectEverAsked) {
				try {
					objectEverAsked.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		synchronized (objectColor) {
			this.currentColor = currentColor;
			objectColor.notifyAll();
		}
	}

	/**
	 * 
	 * @requires currentMove matches pattern used for board.parseMovePattern
	 * @param currentMove
	 */
	public void setCurrentMove(String currentMove) {
		try {
			board.move(board.parseMovePattern(currentColor, currentMove));
			view.showMessage("Current board:\n" + board.toString());
			view.showMessage("Number of marbles: " + board.getNumberOfMarbles());
		} catch (InvalidMoveException | MarbleKilledException e) {
			view.showMessage(e.getMessage());
		}
	}

	/**
	 * Always resets the ready status and puts player in state lobby if join is
	 * player.
	 * 
	 * @ensures if cmd.equals(j;ownName;ownTeam) then setInLobby()
	 * @param cmd
	 */
	public void putInLobby(String cmd) {
		String[] split = cmd.split(ProtocolMessages.DELIMITER);
		// TODO: fix this implementation and remove setInLobby() in doJoin.
		if (ownName.equals(split[1]) && ownName.equals(split[2])) {
			setInLobby();
		}
		resetReady();
	}

	public Player getControlPlayer() {
		return controlPlayer;
	}
	
	/**
	 * TODO: remove this:
	 */
	@Override
	public String getMove(Color color) throws ServerUnavailableException, ProtocolException {
		return null;
	}

	public Board getBoard() {
		return board;
	}

	/**
	 * set the current color received from server
	 * @param cmd
	 */
	public void doTurn(Color color) {
		setCurrentColor(color);
	}

	public void resetBoard() {
		board = null;
		everAsked = false;
		synchronized (objectColor) {
			objectColor.notifyAll();
		}
		synchronized (objectEverAsked) {
			objectEverAsked.notifyAll();
		}
	}
}
