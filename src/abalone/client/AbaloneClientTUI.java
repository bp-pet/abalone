package abalone.client;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

import abalone.exceptions.ExitProgram;
import abalone.exceptions.ServerUnavailableException;
import abalone.protocol.ProtocolMessages;

public class AbaloneClientTUI implements AbaloneClientView {

	AbaloneClient c;
	private static final String INPUT = "Command ? ";
	private static final String GETIP = "Give a valid ip? ";
	private static final String IP_PATTERN = "^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.)"
			+ "{3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$";
	private static final String HELPMENU = "help menu needs to be implemented"; //TODO: implement help menu
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
				handleUserInput(msg);
				msg = getString(INPUT);
			} catch (ExitProgram e) {
				msg = String.valueOf(ProtocolMessages.EXIT);
			}
		}
	}

	@Override
	public void handleUserInput(String input) throws ExitProgram, ServerUnavailableException {
		// TODO Auto-generated method stub
		
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

	@Override
	public void printHelpMenu() {
		System.out.println(HELPMENU);
	}

}
