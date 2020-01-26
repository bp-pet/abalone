package abalone.AI;

import java.util.ArrayList;

import abalone.Board;
import abalone.Color;
import abalone.Field;
import abalone.Move;
import abalone.exceptions.InvalidMoveException;
import abalone.exceptions.MarbleKilledException;

public interface Strategy {
	/**
	 * Returns the name of the strategy.
	 */
	public String getName();
	
	/**
	 * Returns a next legal move.
	 * @ensures a valid move
	 * @returns the best move for this strategy
	 * @requires there are possible moves
	 */
	public Move determineMove(Board board, Color color);
	
	/**
	 * Simulates the move.
	 * Makes a copy of the board, performs the move, and returns it.
	 * @return a new board with the move performed on it
	 */
	default public Board simulateMove(Board board, Move move) {
		Board copyBoard = board.deepCopy();
		move = move.deepCopy(copyBoard);
		try {
			move.perform();
		} catch (InvalidMoveException e) {
			//Who cares, shouldn't happen right? (only valid moves are
			//simulated)
		} catch (MarbleKilledException e) {
			//Not relevant.
		}
		return copyBoard;
	}
	
	/**
	 * Makes a list of all possible moves for a given color on a given board.
	 * @returns list of valid moves
	 * @requires board contains marbles of given color
	 */
	default public ArrayList<Move> makeMovesList(Board board, Color color) {
		ArrayList<Move> moveList = new ArrayList<Move>();
		ArrayList<Move> tempMoveList;
		ArrayList<Field> fieldList = board.getMapOfColors().get(color);
		boolean valid1;
		boolean valid2;
		int l = fieldList.size();
		for (int i = 0; i < l; i++) {
			for (int j = i; j < l; j++) {
				Field f1 = fieldList.get(i);
				Field f2 = fieldList.get(j);
				int f1Row = f1.getRow();
				int f1Col = f1.getCol();
				int f2Row = f2.getRow();
				int f2Col = f2.getCol();
				Move move = new Move(board, color, f1Row, f1Col,
						f2Row, f2Col,0 ,0);
				valid1 = true;
				try {
					move.isValidSelection();
				} catch (InvalidMoveException e) {
					valid1 = false;
				}
				if (valid1) {
					 tempMoveList = new ArrayList<Move>();
					 tempMoveList.add(new Move(board, color, f1Row, f1Col,
							 f2Row, f2Col, f1Row + 1, f1Col + 0));
					 tempMoveList.add(new Move(board, color, f1Row, f1Col,
							 f2Row, f2Col, f1Row - 1, f1Col + 0));
					 tempMoveList.add(new Move(board, color, f1Row, f1Col,
							 f2Row, f2Col, f1Row + 0, f1Col + 1));
					 tempMoveList.add(new Move(board, color, f1Row, f1Col,
							 f2Row, f2Col, f1Row + 0, f1Col - 1));
					 tempMoveList.add(new Move(board, color, f1Row, f1Col,
							 f2Row, f2Col, f1Row + 1, f1Col + 1));
					 tempMoveList.add(new Move(board, color, f1Row, f1Col,
							 f2Row, f2Col, f1Row - 1, f1Col - 1));
					 for (Move m : tempMoveList) {
						 valid2 = true;
						 try {
								m.isValidMove();
						 } catch (InvalidMoveException e) {
								valid2 = false;
						 }
						 if (valid2) {
							 moveList.add(m);
						 }
					 }
				}
			}
		}
		return moveList;
	}
}
