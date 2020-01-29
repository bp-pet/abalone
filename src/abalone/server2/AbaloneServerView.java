package abalone.server2;

public interface AbaloneServerView {

	public void showMessage(String message);

	public String getString(String question);

	public int getInt(String question);

	public boolean getBoolean(String question);
}
