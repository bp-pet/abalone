package abalone.AI;

import java.util.ArrayList;

import abalone.Board;
import abalone.Color;
import abalone.Field;
import abalone.Move;
import abalone.exceptions.InvalidMoveException;

public interface Strategy {
	/**
	 * returns the name of the strategy.
	 * @return the name of the strategy
	 */
	public String getName();
	
	/**
	 * returns a next legal move.
	 * @ensures a valid move.
	 * @param board given board
	 * @param mark given mark
	 * @return
	 */
	public Move determineMove(Board board, Color color);
	
	default public ArrayList<Move> makeMovesList(Board board, Color color) {
		ArrayList<Move> moveList = new ArrayList<Move>();
		ArrayList<Move> tempMoveList;
		ArrayList<Field> fieldList = board.getMapOfcolors().get(color);
		boolean valid1;
		boolean valid2;
		int l = fieldList.size();
		for (Field f1 : fieldList) {
			for (Field f2 : fieldList) {
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
