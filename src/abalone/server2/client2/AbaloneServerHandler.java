package abalone.server2.client2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import abalone.exceptions.ExitProgram;
import abalone.protocol.ProtocolMessages;

public class AbaloneServerHandler implements Runnable {
	
	private BufferedReader in;
	private BufferedWriter out;
	private Socket sock;
	private AbaloneClient client;
	private String name;

	public AbaloneServerHandler(Socket sock, AbaloneClient client, String name) {
		try {
			in = new BufferedReader(
					new InputStreamReader(sock.getInputStream()));
			out = new BufferedWriter(
					new OutputStreamWriter(sock.getOutputStream()));
			this.sock = sock;
			this.client = client;
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
		String[] split = msg.split(ProtocolMessages.DELIMITER);
		String response = "";
		switch (split[0].charAt(0)) {
			case ProtocolMessages.START:
				client.doStart(split);
				break;
			case ProtocolMessages.TURN:
				response = client.doTurn(split);
				break;
			case ProtocolMessages.MOVE:
				client.doMove(split);
				break;
			case ProtocolMessages.GAME_END:
				client.doGameEnd();
				break;
			default:
				break;
		}
		client.sendToView("Received from " + name + ": " + msg);
		if (!response.equals("")) {
			sendLine(response);
		}
	}
	
	private void readMultipleLines() {
		String msg;
		try {
			msg = in.readLine();
			while (msg != ProtocolMessages.EOT) {
				try {
					handleCommand(msg);
				} catch (ExitProgram e) {
					shutdown();
				}
				msg = in.readLine();
			}
		} catch (IOException e) {
			//idk tbh
		}
	}
	
	/**
	 * This method is particularly elegant.
	 */
	private void sendLine(String msg) throws IOException {
		out.write(msg);
		out.newLine();
		out.flush();
	}
	
	private void shutdown() {
		try {
			in.close();
			out.close();
			sock.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
