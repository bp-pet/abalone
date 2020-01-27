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
	private static final String GETIP = "Give a valid ip ? ";
	private static final String IP_PATTERN = "^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.)"
			+ "{3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$";
	private static final String HELPMENU_BROWSER =   "\n" + "> Welcome to the Abalone game\r\n" + 
			"Commands :\r\n" + 
			"l .................... show list of lobbies.\r\n" + 
			"j lobbyName name team. Join a lobby.\r\n" + 
			"h .................... this menu.\r\n" + 
			"x .................... exit.\n";
	private static final String HELPMENU_LOBBY = "\n" + "> Welcome in the lobby\r\n" + 
			"Commands :\r\n" + 
			"r .................... send that you are ready.\r\n" + 
			"h .................... this menu.\r\n" + 
			"x .................... exit.\n";
	private static final String HELPMENU_GAME = "\n" + "> Welcome in the game\r\n" + 
			"Commands :\r\n" + 
			"m 1stMarble 2ndMarble destMarble ................. move that you want.\r\n" + 
			"h .................... this menu.\r\n" + 
			"x .................... exit.\n";
	Scanner input = new Scanner(System.in);
	
	public AbaloneClientTUI(AbaloneClient c) {
		this.c = c;
	}
	
	@Override
	public void start() throws ServerUnavailableException {
		String msg = getString(INPUT);
		System.out.println("input msg: " + msg);
		while (! msg.equals(String.valueOf(ProtocolMessages.EXIT))) {
			try {
				//TODO: implement the ready thingy motor.
				handleUserInput(msg);
				msg = getString(INPUT);
			} catch (ExitProgram e) {
				msg = String.valueOf(ProtocolMessages.EXIT);
			}
		}
	}

	@Override
	public void handleUserInput(String input) throws ExitProgram, ServerUnavailableException {
		String[] cmd = input.split(" ");
		System.out.println("length" + cmd.length + "input: " + input);
		switch (c.getState()) {
		case BROWSER:
			switch (cmd[0].charAt(0)) {
				case ProtocolMessages.LOBBY:
					c.doLobbies();
					break;
				case ProtocolMessages.JOIN:
					if (cmd.length != 4) {
						showMessage("Expected 3 arguments.");
					}
				try {
					c.doJoinLobby(cmd[1], cmd[2], cmd[3]);
				} catch (ProtocolException e1) {
					showMessage(e1.getMessage());
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
				try {
					c.doReady();
				} catch (ProtocolException e1) {
					showMessage(e1.getMessage());
				}
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
				try {
					c.sendMove(cmd[1], cmd[2], cmd[3]);
				} catch (InvalidMoveException e) {
					showMessage("Invalid move please try again");
				} catch (ProtocolException e) {
					showMessage(e.getMessage());
				}
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
		
		while (! ip.matches(IP_PATTERN)) {
			ip = getString("WRONG! " + GETIP);
		}
		
		try {
			return InetAddress.getByAddress(ip.getBytes());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getString(String question) {
		showMessage(question);
    	return input.nextLine();
	}

	@Override
	public int getInt(String question) {
		showMessage(question);
    	return Integer.parseInt(input.next());
	}

	@Override
	public boolean getBoolean(String question) {
		showMessage(question);
    	return input.nextBoolean();
	}
}
