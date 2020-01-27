package abalone.AI;

import java.util.ArrayList;
import abalone.Board;
import abalone.Color;
import abalone.Field;
import abalone.Move;

/**
 * A strategy that focuses on staying in the middle of the board, going
 * from the assumption that once one team has the middle, the game over.
 * Inspired by the movie Star Wars Episode III: Revenge of the Sith.
 * 
 * @authors Bozhidar Petrov, Daan Pluister
 *
 */
public class ItsOverAnakinIHaveTheHighGroundStrategy implements Strategy {
	
	/**
	 * Metrics: 
	 * - distance from center (weighed)
	 * - enemy marble(s) killed
	 * - closeness together / vulnerability (?)
	 * 
	 * Simulate multiple turns.
	 */

	public String getName() {
		return "It's over, Anakin, I have the high ground!";
	}

	@Override
	public Move determineMove(Board board, Color color) {
		ArrayList<Move> moveList = makeMovesList(board, color);
		Board simBoard;
		int bestScore = Integer.MAX_VALUE;
		int currentScore;
		Move bestMove = moveList.get(0);
		for (Move currentMove : moveList) {
			simBoard = simulateMove(board, currentMove);
			currentScore = evaluateBoard(simBoard, color);
			if (currentScore < bestScore) {
				bestScore = currentScore;
				bestMove = currentMove;
			}
		}
		return bestMove;
	}
	
	/**
	 * 
	 */
	private int evaluateBoard(Board board, Color color) {
		ArrayList<Field> fields = board.getMapOfColors().get(color);
		int totalScore = 0;
		for (Field field : fields) {
			totalScore += distanceFromCenter(board, field);
			if (board.marbleKilled) {
				totalScore = totalScore * 20;
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
