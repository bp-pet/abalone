package abalone.server2;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import abalone.exceptions.ExitProgram;
import abalone.exceptions.InvalidMoveException;
import abalone.protocol.ProtocolMessages;
import abalone.protocol.ServerProtocol;

public class AbaloneServer implements Runnable, ServerProtocol {

	private ServerSocket ssock;

	private List<AbaloneClientHandler> clients;
	
	private int next_client_no;

	private AbaloneServerView view;
	
	private ArrayList<AbaloneLobby> lobbies;
	
	public AbaloneServer() {
		clients = new ArrayList<AbaloneClientHandler>();
		view = new AbaloneServerTui();
		next_client_no = 1;
		lobbies = new ArrayList<AbaloneLobby>();
	}

	public void run() {
		boolean openNewSocket = true;
		while (openNewSocket) {
			try {
				setup();
				while (true) {
					Socket sock = ssock.accept();
					String name = "Client " 
							+ String.format("%02d", next_client_no++);
					view.showMessage("New client [" + name + "] connected!");
					AbaloneClientHandler handler = 
							new AbaloneClientHandler(sock, this, name);
					new Thread(handler).start();
					clients.add(handler);
				}
			} catch (ExitProgram e) {
				openNewSocket = false;
			} catch (IOException e) {
				view.showMessage("A server IO error occurred: " + e.getMessage());
				if (!view.getBoolean("Do you want to open a new socket?")) {
					openNewSocket = false;
				}
			}
		}
		view.showMessage("See you later!");
	}
	
	public void sendToView(String s) {
		view.showMessage(s);
	}


	public void setup() throws ExitProgram {
		ssock = null;
		while (ssock == null) {
			int port = view.getInt("Please enter the server port.");
			try {
				view.showMessage("Attempting to open a socket at 127.0.0.1 "
						+ "on port " + port + "...");
				ssock = new ServerSocket(port, 0, 
						InetAddress.getByName("127.0.0.1"));
				view.showMessage("Server started at port " + port);
			} catch (IOException e) {
				view.showMessage("ERROR: could not create a socket on "
						+ "127.0.0.1" + " and port " + port + ".");

				if (!view.getBoolean("Do you want to try again?")) {
					throw new ExitProgram("User indicated to exit the "
							+ "program.");
				}
			}
		}
	}

	public void removeClient(AbaloneClientHandler client) {
		this.clients.remove(client);
	}

	public static void main(String[] args) {
		AbaloneServer server = new AbaloneServer();
		new Thread(server).start();
	}

	@Override
	public String doError(int errorType, String errorMessage) {
		// TODO Auto-generated method stub
		return null;
	}

	public String doHello(String[] cmd) {
		if (cmd.length >= 2 && cmd[1].equals(ProtocolMessages.VERSION)) {
			return ProtocolMessages.HELLO + "";
		} else {
			return "";
		}
	}

	public String doLobby(String[] cmd) {
		String result = "";
		for (AbaloneLobby lobby : lobbies) {
			result += ProtocolMessages.LOBBY + ProtocolMessages.DELIMITER +
					lobby.getLobbyName();
			for (AbaloneClientHandler client : lobby.getClients()) {
				result += ProtocolMessages.DELIMITER +
						lobby.getPlayerNames().get(client) +
						ProtocolMessages.DELIMITER +
						lobby.getTeamNames().get(client);
			}
			result += "\n";
		}
		result += ProtocolMessages.EOT;
		return result;
	}

	public String doJoin(String[] cmd, AbaloneClientHandler client) {
		if (cmd.length == 4) {
			for (AbaloneLobby lobby : lobbies) {
				if (lobby.getLobbyName().equals(cmd[1])) {
					lobby.addClient(client, cmd[2], cmd[3]);
					return "";
				}
			}
			makeLobby(cmd[1]).addClient(client, cmd[2], cmd[3]);
			return "";
		}
		return "";
	}
	
	private AbaloneLobby makeLobby(String lobbyName) {
		AbaloneLobby lobby = new AbaloneLobby(lobbyName);
		lobbies.add(lobby);
		return lobby;
	}
	
	public AbaloneLobby findLobbyOfClient(AbaloneClientHandler client) {
		for (AbaloneLobby lobby : lobbies) {
			for (AbaloneClientHandler c : lobby.getClients()) {
				if (c == client) {
					return lobby;
				}
			}
		}
		return null;
	}

	public String doMove(String[] cmd, AbaloneClientHandler client) throws IOException {
		AbaloneLobby lobby = findLobbyOfClient(client);
		try {
			lobby.doMove(cmd);
		} catch (InvalidMoveException e) {
			return "";
		}
		String msg = cmd[0] + ProtocolMessages.DELIMITER + cmd[1] +
				ProtocolMessages.DELIMITER + cmd[2] + ProtocolMessages.DELIMITER +
				cmd[3];
		lobby.sendToAll(msg);
		return "";
	}

	public String doExit(String[] cmd) {
		// TODO Auto-generated method stub
		return null;
		
	}

	public String doReady(String[] cmd, AbaloneClientHandler client) {
		findLobbyOfClient(client).readyClient(client);
		return "";
	}
	
}
