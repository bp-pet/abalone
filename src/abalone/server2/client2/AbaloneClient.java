package abalone.server2.client2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

import abalone.Board;
import abalone.Color;
import abalone.Move;
import abalone.AI.ItsOverAnakinIHaveTheHighGroundStrategy;
import abalone.AI.Strategy;
import abalone.exceptions.ExitProgram;
import abalone.exceptions.InvalidMoveException;
import abalone.exceptions.MarbleKilledException;
import abalone.exceptions.ServerUnavailableException;
import abalone.protocol.ProtocolMessages;

public class AbaloneClient {
	
	private Socket serverSock;
	private BufferedReader in;
	private BufferedWriter out;
	private AbaloneClientView view;
	private Board board;
	private Color color;
	private Strategy strategy = new ItsOverAnakinIHaveTheHighGroundStrategy();
	private String playerName;
	private String teamName;

	public AbaloneClient() {
		view = new AbaloneClientTui(this);
	}

	public void start() {
		try {
			createConnection();
		} catch (ExitProgram e) {
		}
		try {
			view.start();
		} catch (ServerUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void createConnection() throws ExitProgram {
		clearConnection();
		while (serverSock == null) {
			try {
				InetAddress host = view.getIp();
				int port = view.getInt("Give port:");
				System.out.println("Attempting to connect...");
				serverSock = new Socket(host, port);
				in = new BufferedReader(new InputStreamReader(
						serverSock.getInputStream()));
				out = new BufferedWriter(new OutputStreamWriter(
						serverSock.getOutputStream()));
				AbaloneServerHandler handler = 
						new AbaloneServerHandler(serverSock, this, "Server");
				new Thread(handler).start();
				System.out.println("Connected!");
			} catch (IOException e) {
				System.out.println("ERROR: could not create socket");
			}
		}
	}

	public void clearConnection() {
		serverSock = null;
		in = null;
		out = null;
	}

	public void closeConnection() {
		view.showMessage("Closing the connection...");
		try {
			in.close();
			out.close();
			serverSock.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		view.showMessage("Connection closed.");
	}

	public static void main(String[] args) {
		(new AbaloneClient()).start();
	}
	
	public void setPlayerName(String s) {
		playerName = s;
	}

	public void setTeamName(String s) {
		teamName = s;
	}
	
	public void sendToView(String string) {
		view.showMessage(string);
	}

	public void doStart(String[] split) {
		if (playerName.equals(split[1]) && teamName.equals(split[2])) {
			color = Color.WHITE;
		} else if (playerName.equals(split[3]) && teamName.equals(split[4])) {
			color = Color.BLACK;
		} else if (playerName.equals(split[5]) && teamName.equals(split[6])) {
			color = Color.BLUE;
		} else if (playerName.equals(split[7]) && teamName.equals(split[8])) {
			color = Color.RED;
		}
		int numPlayers = (split.length - 1) / 2;
		board = new Board(numPlayers);
		view.showMessage(board.toString());
	}

	public void doMove(String[] split) {
		String tail = split[1];
		String head = split[2];
		String dest = split[3];
		try {
			Move move = board.parseMovePattern(
					board.getField(board.getRowFromLetter(tail.charAt(0)),
					board.getColFromLetter(tail.charAt(1))).getMarble().
					getColor(), tail + " " + head + " " + dest);
			move.perform();
			view.showMessage(board.toString());
		} catch (InvalidMoveException e) {
			//server only sends valid moves
		} catch (MarbleKilledException e) {
			//not relevant
		}
	}

	public String doTurn(String[] split) {
		if (protocolToColor(split[1]) == color) {
			Move move = strategy.determineMove(board, color);
			String[] moveString = move.getUserCoordinates();
			return (ProtocolMessages.MOVE + ProtocolMessages.DELIMITER +
					moveString[0] + ProtocolMessages.DELIMITER +
					moveString[1] + ProtocolMessages.DELIMITER +
					moveString[2]);
		}
		return "";
	}
	
	public Color protocolToColor(String s) {
		if (s.equals(ProtocolMessages.COLOR_WHITE)) {
			return Color.WHITE;
		} else if (s.equals(ProtocolMessages.COLOR_BLACK)) {
				return Color.BLACK;
		} else if (s.equals(ProtocolMessages.COLOR_BLUE)) {
			return Color.BLUE;
		} else if (s.equals(ProtocolMessages.COLOR_RED)) {
			return Color.RED;
		} else {
			return Color.WHITE;
		}
	}
	
	public void sendMessage(String msg) throws IOException {
		if (msg.charAt(0) == ProtocolMessages.JOIN) {
			String[] split = msg.split(ProtocolMessages.DELIMITER);
			if (split.length == 4) {
				setPlayerName(split[2]);
				setTeamName(split[3]);
			}
		}
		out.write(msg);
		out.newLine();
		out.flush();
	}

	public void doGameEnd() {
		board.reset();
	}
}
