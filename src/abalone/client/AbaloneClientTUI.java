package abalone.client;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

import abalone.exceptions.ExitProgram;
import abalone.exceptions.InvalidMoveException;
import abalone.exceptions.ProtocolException;
import abalone.exceptions.ServerUnavailableException;
import abalone.protocol.ProtocolMessages;

public class AbaloneClientTUI implements AbaloneClientView {

	AbaloneClient c;
	private static final String INPUT = "Command ? ";
	private static final String GETIP = "Give a ip (default: 127.0.0.1) ? ";
	private static final String IP_PATTERN = "^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.)"
			+ "{3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$";
	private static final String HELPMENU_BROWSER = "\n" + "> Welcome to the Abalone game\r\n" + "Commands :\r\n"
			+ "l .................... show list of lobbies.\r\n" + "j lobbyName name team. Join a lobby.\r\n"
			+ "h .................... this menu.\r\n" + "x .................... exit.\n";
	private static final String HELPMENU_LOBBY = "\n" + "> Welcome in the lobby\r\n" + "Commands :\r\n"
			+ "r .................... send that you are ready.\r\n" + "h .................... this menu.\r\n"
			+ "x .................... exit.\n";
	private static final String HELPMENU_GAME = "\n" + "> Welcome in the game\r\n" + "Commands :\r\n"
			+ "m 1stMarble 2ndMarble destMarble ................. move that you want.\r\n"
			+ "h .................... this menu.\r\n" + "x .................... exit.\n";
	Scanner input = new Scanner(System.in);

	public AbaloneClientTUI(AbaloneClient c) {
		this.c = c;
	}

	@Override
	public void start() throws ServerUnavailableException {
		String msg = getString(INPUT);
		while (!msg.equals(String.valueOf(ProtocolMessages.EXIT))) {
			try {
				handleUserInput(msg);
				// TODO: remove debug line
				showMessage("debug, state: " + (c.getState() == State.LOBBY) + " and is ready: " + (c.isReady()));
				msg = getString(INPUT);
			} catch (ExitProgram e) {
				msg = String.valueOf(ProtocolMessages.EXIT);
			}
		}
	}

	@Override
	public void handleUserInput(String input) throws ExitProgram, ServerUnavailableException {
		String[] cmd = input.split(" ");
		if (cmd.length == 0 || cmd[0].equals("")) {
			return;
		}
		switch (c.getState()) {
			case BROWSER:
				switch (cmd[0].charAt(0)) {
					case ProtocolMessages.LOBBY:
						c.doLobbies();
						break;
					case ProtocolMessages.JOIN:
						if (cmd.length != 4) {
							showMessage("Expected 3 arguments.");
						} else {
							c.doJoinLobby(cmd[1], cmd[2], cmd[3]);
						}
						break;
					case ProtocolMessages.EXIT:
						c.sendExit();
						break;
					case ProtocolMessages.HELP:
					default:
						showMessage(HELPMENU_BROWSER);
				}
				break;
			case LOBBY:
				switch (cmd[0].charAt(0)) {
					case ProtocolMessages.READY:
						c.doReady();
						break;
					case ProtocolMessages.EXIT:
						c.sendExit();
						break;
					case ProtocolMessages.HELP:
					default:
						showMessage(HELPMENU_LOBBY);
				}
				break;
			case GAME:
				switch (cmd[0].charAt(0)) {
					case ProtocolMessages.MOVE:
						if (cmd.length != 4) {
							showMessage("Expected 3 arguments.");
						}
						c.sendMove(cmd[1], cmd[2], cmd[3]);
						break;
					case ProtocolMessages.EXIT:
						c.sendExit();
						break;
					case ProtocolMessages.HELP:
					default:
						showMessage(HELPMENU_GAME);
				}
				break;
			default:
				showMessage("something went wrong that shouldn't");
		}
	}

	@Override
	public void showMessage(String message) {
		System.out.println(message);
	}

	@Override
	public InetAddress getIp() {
		String ip = getString(GETIP);

		while (!(ip.matches(IP_PATTERN) || ip.equals(""))) {
			ip = getString("WRONG! " + GETIP);
		}

		try {
			if (ip.equals("")) {
				ip = "127.0.0.1";
			}
			return InetAddress.getByName(ip);
		} catch (UnknownHostException e) {
			return getIp();
		}
	}

	@Override
	public String getString(String question) {
		showMessage(question);
		return input.nextLine();
	}

	@Override
	public int getInt(String question) {
		String in = getString(question);
		if (in.equals("")) {
			return 2727;
		} else
			return Integer.parseInt(in);
	}

	// TODO: this does not work and is never used
	@Override
	public boolean getBoolean(String question) {
		showMessage(question);
		return input.nextBoolean();
	}
}
