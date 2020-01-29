package abalone.ai;

import abalone.Board;
import abalone.Color;
import abalone.Field;
import abalone.Move;
import abalone.exceptions.InvalidMoveException;

import java.util.ArrayList;

/**
 * An interface for a strategy for an AI for Abalone.
 * 
 * @authors Bozhidar Petrov, Daan Pluister
 */
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
     * Makes a list of all possible moves for a given color on a given board.
     * @returns list of valid moves
     * @requires board contains marbles of given color
     */
    public default ArrayList<Move> makeMovesList(Board board, Color color) {
        ArrayList<Move> moveList = new ArrayList<Move>();
        ArrayList<Move> tempMoveList;
        ArrayList<Field> fieldList = board.getMapOfColors().get(color);
        int l = fieldList.size();
        for (int i = 0; i < l; i++) {
            for (int j = 0; j < l; j++) {
                Move move = new Move(board, color, fieldList.get(i),
                        fieldList.get(j), 0, 0);
                try {
                    move.isValidSelection();
                } catch (InvalidMoveException e) {
                    continue;
                }
                tempMoveList = new ArrayList<Move>();
                tempMoveList.add(new Move(board, color, fieldList.get(i),
                        fieldList.get(j), 1, 0));
                tempMoveList.add(new Move(board, color, fieldList.get(i),
                        fieldList.get(j), -1, 0));
                tempMoveList.add(new Move(board, color, fieldList.get(i),
                        fieldList.get(j), 0, 1));
                tempMoveList.add(new Move(board, color, fieldList.get(i),
                        fieldList.get(j), 0, -1));
                tempMoveList.add(new Move(board, color, fieldList.get(i),
                        fieldList.get(j), 1, 1));
                tempMoveList.add(new Move(board, color, fieldList.get(i),
                        fieldList.get(j), -1, -1));
                for (Move m : tempMoveList) {
                    try {
                        m.isValidMoveQuick();
                    } catch (InvalidMoveException e) {
                        continue;
                    }
                    moveList.add(m);
                }
            }
        }
        return moveList;
    }
}
