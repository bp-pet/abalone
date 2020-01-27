package abalone.AI;

import java.util.ArrayList;
import abalone.Board;
import abalone.Color;
import abalone.Field;
import abalone.Move;

/**
 * A deliberately bad strategy for an Abalone AI.
 * 
 * @authors Bozhidar Petrov, Daan Pluister
 */
public class ReverseAnakinStrategy implements Strategy {

	public String getName() {
		return "It's over, Anakin, I have the low ground!";
	}

	/**
	 * Finds the move that puts the player in the worst possible
	 * position (far away from the center.
	 */
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
	
	/**
	 * Evaluates a move by the state of the board after performing it.
	 * In this case the score of the board is determined by the sum
	 * of all the marbles' distance to the center of the board.
	 */
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
	
	/**
	 * Get a field's distance from the center of the board.
	 * @param board
	 * @param field
	 * @return
	 */
	private int distanceFromCenter(Board board, Field field) {
		int center = board.getDim() - 1;
		return (field.getRow() - center) *
				(field.getRow() - center) +
				(field.getCol() - center) *
				(field.getCol() - center);		
	}
}
