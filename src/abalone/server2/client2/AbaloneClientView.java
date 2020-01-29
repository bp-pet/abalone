package abalone.server2.client2;

import java.net.InetAddress;

import abalone.exceptions.ExitProgram;
import abalone.exceptions.ServerUnavailableException;

public interface AbaloneClientView {
	
	public void start() throws ServerUnavailableException;
	
	public void handleUserInput(String input) throws ExitProgram, ServerUnavailableException;
	
	public void showMessage(String message);

	public String getString(String question);

	public int getInt(String question);
	
	public InetAddress getIp();

	public boolean getBoolean(String question);
}
