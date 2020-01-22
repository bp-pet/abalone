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

	/**
	 * Constructs a new AbaloneClient. Initializes the view in this case the standard AbaloneClientTUI.
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
				view.showMessage("ERROR: could not create a socket on " + host + " and port "
						+ port + ".");

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
	 * Reads and returns one line from the server.
	 * 
	 * @return the line sent by the server.
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

	
	@Override
	public void handleHello() throws ServerUnavailableException, ProtocolException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doLobbies() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doJoinGame(String lobbyName, String playerName, String teamName)
			throws ServerUnavailableException, ProtocolException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doStart() throws ServerUnavailableException, NotEnoughPlayersException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getStart() throws ServerUnavailableException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getTurn() throws ServerUnavailableException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void doMove(String arg1, String arg2, String arg3) throws ServerUnavailableException, InvalidMoveException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getMove() throws ServerUnavailableException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getGameEnd() throws ServerUnavailableException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void sendExit() throws ServerUnavailableException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getLobbies() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doReady() throws ServerUnavailableException, NotEnoughPlayersException {
		// TODO Auto-generated method stub
		
	}

}
