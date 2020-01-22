package abalone.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import abalone.exceptions.*;
import abalone.protocol.ServerProtocol;

public class AbaloneServer implements Runnable, ServerProtocol {

	/** The ServerSocket of this HotelServer. */
	private ServerSocket ssock;

	/** List of HotelClientHandlers, one for each connected client. */
	private List<AbaloneClientHandler> clients;

	/** Next client number, increasing for every new connection. */
	private int next_client_no;

	/** The view of this HotelServer. */
	private AbaloneServerView view;

	/**
	 * Constructs a new AbaloneServer. Initializes the clients list, the view and the
	 * next_client_no.
	 */
	public AbaloneServer() {
		clients = new ArrayList<>();
		view = new AbaloneServerTUI();
		next_client_no = 1;
	}
	
	/**
	 * Opens a new socket by calling {@link #setup()} and starts a new
	 * HotelClientHandler for every connecting client.
	 * 
	 * If {@link #setup()} throws a ExitProgram exception, stop the program. In case
	 * of any other errors, ask the user whether the setup should be ran again to
	 * open a new socket.
	 */
	public void run() {
		boolean openNewSocket = true;
		while (openNewSocket) {
			try {
				// Sets up the abalone application
				setup();

				while (true) {
					Socket sock = ssock.accept();
					String name = "Client " + String.format("%02d", next_client_no++);
					view.showMessage("New client [" + name + "] connected!");
					AbaloneClientHandler handler = new AbaloneClientHandler(sock, this, name);
					new Thread(handler).start();
					clients.add(handler);
				}

			} catch (ExitProgram e1) {
				// If setup() throws an ExitProgram exception,
				// stop the program.
				openNewSocket = false;
			} catch (IOException e) {
				System.out.println("A server IO error occurred: " + e.getMessage());

				if (!view.getBoolean("Do you want to open a new socket?")) {
					openNewSocket = false;
				}
			}
		}
		view.showMessage("See you later!");
	}

	/**
	 * Sets up a new Game using {@link #setupGame()} and opens a new ServerSocket
	 * at localhost on a user-defined port.
	 * 
	 * The user is asked to input a port, after which a socket is attempted to be
	 * opened. If the attempt succeeds, the method ends, If the attempt fails, the
	 * user decides to try again, after which an ExitProgram exception is thrown or
	 * a new port is entered.
	 * 
	 * @throws ExitProgram if a connection can not be created on the given port and
	 *                     the user decides to exit the program.
	 * @ensures a serverSocket is opened.
	 */
	public void setup() throws ExitProgram {
		// First, initialize the Game.
		setupGame();

		ssock = null;
		while (ssock == null) {
			int port = view.getInt("Please enter the server port.");

			// try to open a new ServerSocket
			try {
				view.showMessage("Attempting to open a socket at 127.0.0.1 " + "on port " + port + "...");
				ssock = new ServerSocket(port, 0, InetAddress.getByName("127.0.0.1"));
				view.showMessage("Server started at port " + port);
			} catch (IOException e) {
				view.showMessage("ERROR: could not create a socket on " + "127.0.0.1" + " and port " + port + ".");

				if (!view.getBoolean("Do you want to try again?")) {
					throw new ExitProgram("User indicated to exit the " + "program.");
				}
			}
		}
	}

	/**
	 * Creates a new game instance
	 */
	public void setupGame() {
		//TODO: implement setupGame
	}
	
	/**
	 * Removes a clientHandler from the client list.
	 * 
	 * @requires client != null
	 */
	public void removeClient(AbaloneClientHandler client) {
		this.clients.remove(client);
	}

	// ------------------ Server Methods --------------------------
	
	@Override
	public String getHello() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String doJoinGame(String lobbyName, String playerName, String teamName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String doTurn() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String doGameEnd() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String doLobbies() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String doReady(String playerName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String doStart(String[] players) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String exitGame() {
		// TODO Auto-generated method stub
		return null;
	}

}
