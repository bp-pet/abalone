package abalone.ai;

import abalone.Board;
import abalone.Color;
import abalone.Move;
import java.util.ArrayList;
import java.util.Random;

public class RandomStrategy  implements Strategy {

    /**
     * Return the name of the strategy.
     */
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
