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
	
	public AbaloneServerPlayer(AbaloneClient c, String name, Color color) {
		super(name, color);
		this.c = c;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public Move determineMove(Board board, String stateOfGame) {
		String[] lineFromServer = null;
		try {
			lineFromServer = c.getMove(color);
		} catch (ServerUnavailableException | ProtocolException e) {
			// TODO Auto-generated catch block (now printStackTrace)
			e.printStackTrace();
		}
		Move move = null;
		try {
			move = board.parseMovePattern(color, lineFromServer[1] + " " + lineFromServer[2] + " " + lineFromServer[3]);
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
