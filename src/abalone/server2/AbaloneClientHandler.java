package abalone.server2;

import abalone.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import abalone.exceptions.ExitProgram;
import abalone.protocol.ProtocolMessages;

public class AbaloneClientHandler implements Runnable {
	
	private BufferedReader in;
	private BufferedWriter out;
	private Socket sock;
	private AbaloneServer server;
	private String name;

	public AbaloneClientHandler(Socket sock, AbaloneServer server, String name) {
		try {
			in = new BufferedReader(
					new InputStreamReader(sock.getInputStream()));
			out = new BufferedWriter(
					new OutputStreamWriter(sock.getOutputStream()));
			this.sock = sock;
			this.server = server;
			this.name = name;
		} catch (IOException e) {
			shutdown();
		}
	}

	public void run() {
		String msg;
		try {
			msg = in.readLine();
			while (msg != null) {
				try {
					handleCommand(msg);
				} catch (ExitProgram e) {
					shutdown();
				}
				msg = in.readLine();
			}
			shutdown();
		} catch (IOException e) {
			shutdown();
		}
	}

	private void handleCommand(String msg) throws IOException, ExitProgram {
		server.sendToView("Received from " + name + ": " + msg);
		String[] cmd = msg.split(ProtocolMessages.DELIMITER);
		String response = "";
		switch (cmd[0].charAt(0)) {
		case ProtocolMessages.HELLO:
			response = server.doHello(cmd);
			break;
		case ProtocolMessages.LOBBY:
			response = server.doLobby(cmd);
			break;
		case ProtocolMessages.JOIN:
			response = server.doJoin(cmd, this);
			break;
		case ProtocolMessages.MOVE:
			response = server.doMove(cmd, this);
			break;
		case ProtocolMessages.EXIT:
			response = server.doExit(cmd);
			break;
		case ProtocolMessages.READY:
			response = server.doReady(cmd, this);
		default:
			break;
		}
		if (!response.equals("")) {
			sendLine(response);
		}
	}
	
	public void sendLine(String msg) throws IOException {
		for (String s : msg.split("\n")) {
			out.write(s);
			out.newLine();
			out.flush();
		}
	}
	
	private void shutdown() {
		try {
			in.close();
			out.close();
			sock.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		server.removeClient(this);
	}

	public void askForMove(Color color) {
		try {
			sendLine(ProtocolMessages.TURN + ProtocolMessages.DELIMITER +
					colorToProtocol(color));
		} catch (IOException e) {
		}
	}
	
	public String colorToProtocol(Color c) {
		switch (c) {
			case WHITE:
				return ProtocolMessages.COLOR_WHITE;
			case BLACK:
				return ProtocolMessages.COLOR_BLACK;
			case BLUE:
				return ProtocolMessages.COLOR_BLUE;
			case RED:
				return ProtocolMessages.COLOR_RED;
			default:
				return null;
		}
	}
}
