package abalone.client;

import abalone.Color;
import abalone.exceptions.ProtocolException;
import abalone.exceptions.ServerUnavailableException;
import abalone.protocol.ProtocolMessages;

public class AbaloneServerHandler implements Runnable {

	/** The client */
	private AbaloneClient c;

	/** The view of the client */
	AbaloneClientView view;

	/**
	 * Constructs a new AbaloneServerHandler
	 */
	public AbaloneServerHandler(AbaloneClient client, AbaloneClientView view) {
		this.c = client;
		this.view = view;
	}

	/**
	 * Continuously listens to client input and forwards the input to the
	 * {@link #handleCommand(String)} method.
	 */
	public void run() {
		// TODO: make exit when exception
		while (true) {
			String cmd = null;
			try {
				cmd = c.readLineFromServer();
			} catch (ServerUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			view.showMessage("Got from server: " + cmd);
			handleServerInput(cmd);

		}
	}

	private void handleServerInput(String cmd) {
		switch (c.getState()) {
			case BROWSER:
				switch (cmd.charAt(0)) {
					case ProtocolMessages.LOBBY:
						view.showMessage(cmd);
						break;
					case ProtocolMessages.JOIN:
						c.putInLobby(cmd);
						break;
					default:
						view.showMessage("expected l but got:");
						view.showMessage(cmd);
						break;
				}
				break;
			case LOBBY:
				switch (cmd.charAt(0)) {
					case ProtocolMessages.JOIN:
						c.putInLobby(cmd);
						break;
					case ProtocolMessages.READY:
						break;
					case ProtocolMessages.START:
						c.makeGame(cmd);
						break;
					case ProtocolMessages.EXIT:
						c.resetReady();
						break;
					default:
						// TODO: add expected stuff
						break;
				}
				break;
			case GAME:
				switch (cmd.charAt(0)) {
					case ProtocolMessages.TURN:
						try {
							c.setCurrentColor(getColor(cmd.split(ProtocolMessages.DELIMITER)[1]));
						} catch (ProtocolException e) {
							// invalid color should not happen
							e.printStackTrace();
						}
						break;
					case ProtocolMessages.MOVE:
						try {
							c.setCurrentMove(getMove(cmd));
						} catch (ProtocolException e) {
							view.showMessage("ProcolException client received: " + e.getMessage());
						}
						break;
					case ProtocolMessages.GAME_END:
						String[] lineFromServer = cmd.split(ProtocolMessages.DELIMITER);
						switch (lineFromServer.length) {
							case 2:
								view.showMessage(lineFromServer[1]);
								break;
							case 3:
								view.showMessage(lineFromServer[1] + " by " + lineFromServer[2]);
								break;
							case 4:
								view.showMessage(lineFromServer[1] + " by " + lineFromServer[2] + " and " + lineFromServer[3]);
								break;
							default:
								view.showMessage("Unexpected number of arguments from server this should not happen");
						}
						break;
					default:
						// TODO: add expected stuff
						view.showMessage(cmd);
						break;
				}
				break;
			default:
				view.showMessage("this should not happen");
				break;
		}

	}
	
	/**
	 * translates pm.COLOR_COLOR to enum Color.
	 * 
	 * @param color
	 * @return
	 */
	public Color getColor(String color) throws ProtocolException {
		switch (color) {
			case ProtocolMessages.COLOR_BLACK:
				return Color.BLACK;
			case ProtocolMessages.COLOR_WHITE:
				return Color.WHITE;
			case ProtocolMessages.COLOR_BLUE:
				return Color.BLUE;
			case ProtocolMessages.COLOR_RED:
				return Color.RED;
			default:
				throw new ProtocolException(color + " is not a valid color according to the protocol");
		}
	}
	
	/**
	 * translates m;<coordinates of marble 1>;<coordinates of marble 2>;<destinationof marble 1> to the form that parsemoves accepts
	 * 
	 * @param color
	 * @return
	 */
	public String getMove(String move) throws ProtocolException {
		String[] movesplit = move.split(ProtocolMessages.DELIMITER);
		if (movesplit.length != 4) {
			throw new ProtocolException("invalid number of arguments");
		}
		return movesplit[1] + " " + movesplit[2] + " " + movesplit[3];
	}
	

}
