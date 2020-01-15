package abalone.server;

import java.io.PrintWriter;
import java.util.Scanner;

public class AbaloneServerTUI implements AbaloneServerView {

	/** The PrintWriter to write messages to */
	private PrintWriter console;
	
	/** The Scanner to get the input */
	Scanner input = new Scanner(System.in);

	/**
	 * Constructs a new HotelServerTUI. Initializes the console.
	 */
	public AbaloneServerTUI() {
		console = new PrintWriter(System.out, true);
	}

	@Override
	public void showMessage(String message) {
		console.println(message);
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
