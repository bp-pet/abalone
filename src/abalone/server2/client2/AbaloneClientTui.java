package abalone.server2.client2;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

import abalone.exceptions.ExitProgram;
import abalone.exceptions.ServerUnavailableException;

public class AbaloneClientTui implements AbaloneClientView {
	
	private AbaloneClient client;
	Scanner input = new Scanner(System.in);

	public AbaloneClientTui(AbaloneClient client) {
		this.client = client;
	}
	
	@Override
	public void showMessage(String message) {
		System.out.println(message);
	}

	@Override
	public String getString(String question) {
		showMessage(question);
		return input.nextLine();
	}

	@Override
	public int getInt(String question) {
		showMessage(question);
		String result = input.nextLine();
		if (result.equals("")) {
			return 4444;
		} else {
			return Integer.parseInt(result);
		}
	}

	@Override
	public boolean getBoolean(String question) {
		showMessage(question);
		return input.nextBoolean();
	}

	@Override
	public void start() throws ServerUnavailableException {
		while (true) {
			try {
				handleUserInput(getString("Give input:"));
			} catch (ExitProgram e) {
				client.closeConnection();
				break;
			}
		}
	}

	@Override
	public void handleUserInput(String input) throws ExitProgram, ServerUnavailableException {
		try {
			client.sendMessage(input);
		} catch (IOException e) {
		}
	}

	@Override
	public InetAddress getIp() {
		while (true) {
			System.out.println("Give IP:");
			String result = input.nextLine();
			if (result.equals("")) {
				try {
					return InetAddress.getByName("localhost");
				} catch (UnknownHostException e) {
					System.out.println("Invalid IP");
				}
			} else {
				try {
					return InetAddress.getByName(result);
				} catch (UnknownHostException e) {
					System.out.println("Invalid IP");
				}
			}
		}
	}

}
