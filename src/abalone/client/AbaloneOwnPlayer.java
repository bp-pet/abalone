package abalone.client;

import abalone.Board;
import abalone.Color;
import abalone.HumanPlayer;
import abalone.Move;
import abalone.Player;
import abalone.exceptions.InvalidMoveException;
import abalone.exceptions.ProtocolException;
import abalone.exceptions.ServerUnavailableException;

public class AbaloneOwnPlayer extends Player {

	Player contolPlayer;
	private AbaloneClient c;

	public AbaloneOwnPlayer(AbaloneClient c, Player contolPlayer) {
		super(contolPlayer.getName(), contolPlayer.getColor());
		this.c = c;
		this.contolPlayer = contolPlayer;
	}

	@Override
	public Move determineMove(Board board) {
		Move move = contolPlayer.determineMove(board);
		try {
			c.sendMove("" + board.getRowLetter(move.getRowTail()) + board.getColLetter(move.getColTail()),
					"" + board.getRowLetter(move.getRowHead()) + board.getColLetter(move.getColHead()),
					"" + board.getRowLetter(move.getRowDest()) + board.getColLetter(move.getColDest()));
		} catch (ServerUnavailableException e) {
			// TODO Auto-generated catch block (now printStackTrace)
			e.printStackTrace();
		} catch (InvalidMoveException e) {
			// TODO Auto-generated catch block (now printStackTrace)
			e.printStackTrace();
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block (now printStackTrace)
			e.printStackTrace();
		}
		return move;
	}
}
