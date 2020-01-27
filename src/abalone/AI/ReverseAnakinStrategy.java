package abalone.AI;

import java.util.ArrayList;
import abalone.Board;
import abalone.Color;
import abalone.Field;
import abalone.Move;

public class ReverseAnakinStrategy implements Strategy {

	public String getName() {
		return "It's over, Anakin, I have the low ground!";
	}

	@Override
	public Move determineMove(Board board, Color color) {
		ArrayList<Move> moveList = makeMovesList(board, color);
		Board simBoard;
		int bestScore = Integer.MIN_VALUE;
		int currentScore;
		Move bestMove = moveList.get(0);
		for (Move currentMove : moveList) {
			simBoard = simulateMove(board, currentMove);
			currentScore = evaluateBoard(simBoard, color);
			if (currentScore > bestScore) {
				bestScore = currentScore;
				bestMove = currentMove;
			}
		}
		return bestMove;
	}
	
	private int evaluateBoard(Board board, Color color) {
		ArrayList<Field> fields = board.getMapOfColors().get(color);
		int totalScore = 0;
		for (Field field : fields) {
			totalScore += distanceFromCenter(board, field);
			if (board.marbleKilled) {
				totalScore = totalScore * 1;
			}
		}
		return totalScore;
	}
	
	private int distanceFromCenter(Board board, Field field) {
		int center = board.getDim() - 1;
		return (field.getRow() - center) *
				(field.getRow() - center) +
				(field.getCol() - center) *
				(field.getCol() - center);		
	}
}
