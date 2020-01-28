package abalone.client;

import abalone.Board;
import abalone.Color;
import abalone.Move;
import abalone.Player;
import abalone.exceptions.InvalidMoveException;
import abalone.exceptions.ProtocolException;
import abalone.exceptions.ServerUnavailableException;

public class AbaloneServerPlayer extends Player {

	private AbaloneClient c;
	
	/**
	 * constructs a serverplayer for client c
	 * @param c
	 * @param name
	 * @param color
	 */
	public AbaloneServerPlayer(AbaloneClient c, String name, Color color) {
		super(name, color);
		this.c = c;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public Move determineMove(Board board, String stateOfGame) {
		String choice = null;
		try {
			choice = c.getMove(color);
		} catch (ServerUnavailableException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Move move = null;
		try {
			move = board.parseMovePattern(color, choice);
		} catch (InvalidMoveException e) {
			// TODO Auto-generated catch block (now println)
			System.out.println("Server not correctly inplemented protocol exception");
			e.printStackTrace();
		}
		try {
			move.isValidMove();
		} catch (InvalidMoveException e) {
			// TODO Auto-generated catch block (now println)
			System.out.println("Server not correctly inplemented move invalid");
			e.printStackTrace();
		}
		return move;
	}
}
