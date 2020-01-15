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

	/** Name of this ClientHandler */
	private String name;

	/**
	 * Constructs a new AbaloneClientHandler. Opens the In- and OutputStreams.
	 * 
	 * @param sock The client socket
	 * @param srv  The connected server
	 * @param name The name of this ClientHandler
	 */
	public AbaloneClientHandler(Socket sock, AbaloneServer srv, String name) {
		try {
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
			this.sock = sock;
			this.srv = srv;
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
				System.out.println("> [" + name + "] Incoming: " + msg);
				handleCommand(msg);
				out.newLine();
				out.flush();
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
		String s = cmd[0];
//		System.out.println("Message from client:" + msg);

		//TODO: implement switch case
		out.append(s);
//		System.out.println("Message back to client:" + s);
		out.flush();
	}

	/**
	 * Shut down the connection to this client by closing the socket and the In- and
	 * OutputStreams.
	 */
	private void shutdown() {
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
}
