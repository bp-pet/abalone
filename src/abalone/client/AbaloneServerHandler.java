package abalone.client;

import abalone.exceptions.ProtocolException;
import abalone.exceptions.ServerUnavailableException;

public class AbaloneServerHandler implements Runnable {

	/** The client */
	private AbaloneClient client;

	/** The view of the client */
	AbaloneClientView view;

	/**
	 * Constructs a new AbaloneServerHandler
	 */
	public AbaloneServerHandler(AbaloneClient client, AbaloneClientView view) {
		this.client = client;
		this.view = view;
	}

	/**
	 * Continuously listens to client input and forwards the input to the
	 * {@link #handleCommand(String)} method.
	 */
	public void run() {
		while (client.getState() == State.LOBBY) {
			try {
				client.getLobbyMessages();
			} catch (ServerUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ProtocolException e) {
				view.showMessage(e.getMessage());
			}			
		}
	}

}
