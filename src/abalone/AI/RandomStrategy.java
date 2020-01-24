package abalone.AI;

import java.util.ArrayList;
import java.util.Random;

import abalone.Board;
import abalone.Color;
import abalone.Move;

public class RandomStrategy  implements Strategy {

	public String getName() {
		return "Random";
	}

	@Override
	public Move determineMove(Board board, Color color) {
		ArrayList<Move> moveList = makeMovesList(board, color);
		int random = new Random().nextInt(moveList.size());
		return moveList.get(random);
	}
	
}
