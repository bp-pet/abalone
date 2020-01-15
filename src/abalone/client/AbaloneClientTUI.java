package abalone.client;

import java.net.InetAddress;

import abalone.exceptions.ExitProgram;
import abalone.exceptions.ServerUnavailableException;

public class AbaloneClientTUI implements AbaloneClientView {

	@Override
	public void start() throws ServerUnavailableException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleUserInput(String input) throws ExitProgram, ServerUnavailableException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showMessage(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public InetAddress getIp() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getString(String question) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getInt(String question) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean getBoolean(String question) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void printHelpMenu() {
		// TODO Auto-generated method stub
		
	}

}
