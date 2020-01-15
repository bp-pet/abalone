package abalone.client;

import java.net.InetAddress;

import abalone.exceptions.*;

/**
 * Interface for the Hotel Client View.
 * 
 * @author Daan Pluister
 */
public interface AbaloneClientView {
	
	/**
	 * Asks for user input continuously and handles communication accordingly using
	 * the {@link #handleUserInput(String input)} method.
	 * 
	 * If an ExitProgram exception is thrown, stop asking for input, send an exit
	 * message to the server according to the protocol and close the connection.
	 * 
	 * @throws ServerUnavailableException in case of IO exceptions.
	 */
	public void start() throws ServerUnavailableException;

	/**
	 * TODO: design handleUserInput
	 * 
	 * @param input The user input.
	 * @throws ExitProgram               	When the user has indicated to exit the
	 *                                    	program.
	 * @throws ServerUnavailableException 	if an IO error occurs in taking the
	 *                                    	corresponding actions.
	 */
	public void handleUserInput(String input) throws ExitProgram, ServerUnavailableException;

	/**
	 * Writes the given message to standard output.
	 * 
	 * @param msg the message to write to the standard output.
	 */
	public void showMessage(String message);

	/**
	 * Ask the user to input a valid IP. If it is not valid, show a message and ask
	 * again.
	 * 
	 * @return a valid IP
	 */
	public InetAddress getIp();

	/**
	 * Prints the question and asks the user to input a String.
	 * 
	 * @param question The question to show to the user
	 * @return The user input as a String
	 */
	public String getString(String question);

	/**
	 * Prints the question and asks the user to input an Integer.
	 * 
	 * @param question The question to show to the user
	 * @return The written Integer.
	 */
	public int getInt(String question);

	/**
	 * Prints the question and asks the user for a yes/no answer.
	 * 
	 * @param question The question to show to the user
	 * @return The user input as boolean.
	 */
	public boolean getBoolean(String question);

	/**
	 * Prints the help menu with available input options.
	 */
	public void printHelpMenu();
}
