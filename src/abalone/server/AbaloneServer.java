package abalone.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import abalone.exceptions.*;
import abalone.protocol.ProtocolMessages;
import abalone.protocol.ServerProtocol;

public class AbaloneServer implements Runnable, ServerProtocol {

	// -- Instance variables -----------------------------------------
	
	/** The ServerSocket of this AbaloneServer. */
	private ServerSocket ssock;

	/** List of AbaloneClientHandlers, one for each connected client. */
	private List<AbaloneClientHandler> clients;

	/** Next client number, increasing for every new connection. */
	private int next_client_no;

	/** The view of this AbaloneServer. */
	private AbaloneServerView view;

	/** The unique browser of the server */
	private AbaloneServerBrowser browser;
	
	// -- Constructors -----------------------------------------------
	
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
				// Sets up the AbaloneServer
				setup();

				while (true) {
					Socket sock = ssock.accept();
					String name = "Client " + String.format("%02d", next_client_no++);
					view.showMessage("New client [" + name + "] connected!");
					AbaloneClientHandler handler = new AbaloneClientHandler(sock, this, browser, name);
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
	 * Sets up a new Browser using {@link #setupBrowser()} and opens a new ServerSocket
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
		// First, initialize the Browser.
		setupBrowser();

		ssock = null;
		while (ssock == null) {
			int port = view.getInt("Please enter the server port.");

			// try to open a new ServerSocket
			try {
				view.showMessage("Attempting to open a socket at 127.0.0.1 " + "on port " + port + "...");
				ssock = new ServerSocket(port);
				view.showMessage("Server started at port " + port);
			} catch (IOException e) {
				view.showMessage("ERROR: could not create a socket on " + "127.0.0.1" + " and port " + port + ".");

				if (!view.getBoolean("Do you want to try again?")) {
					throw new ExitProgram("User indicated to exit the " + "program.");
				}
			}
		}
	}
	
	private void setupBrowser() {
		browser = new AbaloneServerBrowser();		
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
	public String doError(int errorType, String errorMessage) {
		String errorTypeString;
		switch(errorType) {
			case 3:
				errorTypeString = ProtocolMessages.ERROR3;
			case 2:
				errorTypeString = ProtocolMessages.ERROR2;
			default:
				errorTypeString = ProtocolMessages.ERROR1;
				
		} 
		return errorTypeString + ProtocolMessages.DELIMITER + errorMessage;
	}
	
	// ------------------ Main --------------------------

	/** Start a new AbaloneServer */
	public static void main(String[] args) {
		AbaloneServer server = new AbaloneServer();
		System.out.println("Welcome to the Abalone Server! Starting...");
		new Thread(server).start();
	}
}
