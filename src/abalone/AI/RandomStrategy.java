package abalone.AI;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

import abalone.Board;
import abalone.Color;
import abalone.Field;
import abalone.Move;

public class RandomStrategy  implements Strategy {

	public String getName() {
		return "Random";
	}

	@Override
	public Move determineMove(Board board, Color color) {
		ArrayList<Move> moveList = makeMovesList(board, color);
//		if (moveList.size() < 5) {
//			System.out.println(board.toString());
//			System.out.println("Color:");
//			System.out.println(color);
//			System.out.println("Possible moves:");
//			System.out.println(moveList.size());
//			System.out.println(board.getStringMapOfColors());
//		}
		int random = new Random().nextInt(moveList.size());
		return moveList.get(random);
	}
	
}
