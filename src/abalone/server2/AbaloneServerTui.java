package abalone.server2;
	
import java.io.PrintWriter;
import java.util.Scanner;

public class AbaloneServerTui implements AbaloneServerView {
	
	private PrintWriter output;
	private Scanner input;

	public AbaloneServerTui() {
		output = new PrintWriter(System.out, true);
		input = new Scanner(System.in);
	}

	@Override
	public void showMessage(String message) {
		output.println(message);
	}
	
	@Override
	public String getString(String question) {
		output.println(question);
		return input.nextLine();
	}

	@Override
	public int getInt(String question) {
		System.out.println(question);
		String result = input.nextLine();
		if (result.equals("")) {
			return 4444;
		} else {
			return Integer.parseInt(result);
		}
	}

	@Override
	public boolean getBoolean(String question) {
		System.out.println(question);
		return input.nextBoolean();
	}

}
