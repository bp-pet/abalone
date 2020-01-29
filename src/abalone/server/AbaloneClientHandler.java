package abalone.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import abalone.protocol.ProtocolMessages;

public class AbaloneClientHandler implements Runnable {

	/** The socket and In- and OutputStreams */
	private BufferedReader in;
	private BufferedWriter out;
	private Socket sock;

	/** The connected AbaloneServer */
	private AbaloneServer srv;

	/** The connected AbaloneServer */
	private AbaloneServerBrowser browser;

	/** The lobby where the player is in */
	private AbaloneServerLobby lobby;

	/** Name of this ClientHandler */
	private String name;

	/**
	 * Constructs a new AbaloneClientHandler. Opens the In- and OutputStreams.
	 * 
	 * @param sock The client socket
	 * @param srv  The connected server
	 * @param name The name of this ClientHandler
	 */
	public AbaloneClientHandler(Socket sock, AbaloneServer srv, AbaloneServerBrowser browser, String name) {
		try {
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
			this.sock = sock;
			this.srv = srv;
			this.browser = browser;
			this.name = name;
		} catch (IOException e) {
			shutdown();
		}
	}

	/**
	 * Continuously listens to client input and forwards the input to the
	 * {@link #handleCommand(String)} method.
	 */
	public void run() {
		String msg;
		try {
			msg = in.readLine();
			while (msg != null) {
				//TODO: make this print to serverView.
				System.out.println("> [" + name + "] Incoming: " + msg);
				handleCommand(msg);
				msg = in.readLine();
			}
			shutdown();
		} catch (IOException e) {
			shutdown();
		}
	}

	/**
	 * Handles commands received from the client by calling the according methods at
	 * the AbaloneServer.
	 * 
	 * If the received input is not valid, send an "Unknown Command" message to the
	 * server.
	 * 
	 * @param msg command from client
	 * @throws IOException if an IO errors occur.
	 */
	private void handleCommand(String msg) throws IOException {
		String[] cmd = msg.split(ProtocolMessages.DELIMITER);

		String s = null;
		if (cmd[0].equals("")) {
			s = browser.doError(1, ProtocolMessages.INVALID_COMMAND + ", you send empty");
		} else {
			if (lobby == null) {
				switch (cmd[0].charAt(0)) {
					case ProtocolMessages.HELLO:
						s = browser.getHello();
						break;
					case ProtocolMessages.LOBBY:
						s = browser.doLobbies();
						break;
					case ProtocolMessages.JOIN:
						if (cmd.length != 4) {
							s = browser.doError(2, "Unexpected argument expected 3");
						} else {
							s = browser.doJoin(this, cmd[1], cmd[2], cmd[3]);
						}
						break;
					case ProtocolMessages.EXIT:
						srv.removeClient(this);
					default:
						s = srv.doError(1, ProtocolMessages.INVALID_COMMAND + ", expected l, j or x");
				}
			} else if (! lobby.inGame()) {
				switch (cmd[0].charAt(0)) {
					case ProtocolMessages.READY:
						lobby.doReady(this);
						break;
					case ProtocolMessages.EXIT:
						lobby.exitGame(this);
						break;
					default:
						s = srv.doError(1, ProtocolMessages.INVALID_COMMAND + ", expected r or x");
				}
			} else {
				switch (cmd[0].charAt(0)) {
					case ProtocolMessages.MOVE:
						if (! lobby.inGame()) {
							s = srv.doError(3, "Not in a game!");
						} else if (! lobby.isTurn(this)) {
							s = srv.doError(3, "not your turn");
						} else {
							if (cmd.length != 4) {
								s = browser.doError(2, "Unexpected argument expected 3");
							} else {
								s = lobby.doMove(this, cmd[1], cmd[2], cmd[3]);
							}
						}
						break;
					case ProtocolMessages.EXIT:
						lobby.exitGame(this);
						break;
					default:
						s = srv.doError(1, ProtocolMessages.INVALID_COMMAND + ", expected m or x");
				}
			}
		}
		
		if (s != null) {
			sendMessage(s);
		}
			
	}

	/**
	 * Shut down the connection to this client by closing the socket and the In- and
	 * OutputStreams.
	 */
	private void shutdown() {
		//TODO: make this print to serverView.
		System.out.println("> [" + name + "] Shutting down.");
		try {
			in.close();
			out.close();
			sock.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		srv.removeClient(this);
	}

	/**
	 * Sends a protocol message to the output stream.
	 * 
	 * @param msg
	 */
	public void sendMessage(String msg) {
		try {
			synchronized (this) { 
				out.append(msg);
	
				// TODO: comment following debug line
				System.out.println("Message back to client [" + name + "]: " + msg);
				out.flush();
				out.newLine();
				out.flush();
			}
		} catch (IOException e) {
			shutdown();
		}
	}

	/**
	 * Setter for playerName and teamName of the client. these can only be set
	 * Simultaneously.
	 * 
	 * @requires playerName != null && teamName != null
	 */
	public void setLobby(AbaloneServerLobby lobby) {
		this.lobby = lobby;
	}
}
