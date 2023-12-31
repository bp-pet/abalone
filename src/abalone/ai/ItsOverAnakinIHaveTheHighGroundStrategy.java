package abalone.ai;

import abalone.Board;
import abalone.Color;
import abalone.Field;
import abalone.Move;
import abalone.exceptions.InvalidMoveException;
import abalone.exceptions.MarbleKilledException;

import java.util.ArrayList;

/**
 * A strategy that focuses on staying in the middle of the board, going
 * from the assumption that once one team has the middle, the game over.
 * Also uses other metrics.
 * Inspired by the movie Star Wars Episode III: Revenge of the Sith.
 * 
 * @authors Budgetary Petrov, Damn Blister
 */
public class ItsOverAnakinIHaveTheHighGroundStrategy implements Strategy {
    
    private static int numberOfParameters = 4;
    private double[] factors; 

    /**
     * Metrics:
     * - own distance from center.
     * - opponent's distance from center.
     * - number of triplets.
     * - enemy marble(s) killed.
     */
    public ItsOverAnakinIHaveTheHighGroundStrategy(double[] factors) {
        this.factors = factors;
    }
    
    /**
     * In case no argument is given to the constructor, make a
     * strategy with default factors (offensive).
     */
    public ItsOverAnakinIHaveTheHighGroundStrategy() {
        factors = getOffensiveArray();
    }
    
    /**
     * Constructor from a string. Factors should be separated by a semi-colon.
     * @param string should have the from double;double;double;double
     */
    public ItsOverAnakinIHaveTheHighGroundStrategy(String string) {
        this(makeFactorsFromString(string));
    }
    
    /**
     * Make an array of factors from a string.
     */
    public static double[] makeFactorsFromString(String string) {
        String[] split = string.split(";");
        double[] result = new double[numberOfParameters];
        for (int i = 0; i < numberOfParameters; i++) {
            result[i] = Double.parseDouble(split[i]);
        }
        return result;
    }
    
    /**
     * Make a defensive array.
     */
    protected static double[] getDefensiveArray() {
        double[] result = new double[numberOfParameters];
        result[0] = 300;
        result[1] = 1;
        result[2] = 1;
        result[3] = 1;
        return result;
    }
    
    /**
     * Make an offensive array.
     */
    protected static double[] getOffensiveArray() {
        double[] result = new double[numberOfParameters];
        result[0] = 100;
        result[1] = 10;
        result[2] = 0.3;
        result[3] = 1000;
        return result;
    }

    /**
     * Gets the name of the strategy which includes the factors.
     */
    public String getName() {
        String s = "It's over, Anakin, I have the high ground! with factors ";
        for (double factor : factors) {
            s += "~" + String.valueOf(factor);
        }
        return s;
    }
    
    /**
     * Get an array of the factors.
     */
    public double[] getFactors() {
        return factors;
    }

    @Override
    public Move determineMove(Board board, Color color) {
        if (hasTheLead(board, color)) {
            factors = getDefensiveArray();
        } else {
            factors = getOffensiveArray();
        }
        ArrayList<Move> moveList = makeMovesList(board, color);
        double bestScore = Integer.MIN_VALUE;
        double currentScore;
        Move bestMove = moveList.get(0);
        for (Move currentMove : moveList) {
            currentScore = evaluateMove(board, color, currentMove);
            if (currentScore > bestScore) {
                bestScore = currentScore;
                bestMove = currentMove;
            }
        }
        return bestMove;
    }
    
    public boolean hasTheLead(Board board, Color color) {
        return board.getMapOfColors().get(color).size()
                > board.getMapOfColors().get(getOpponentColor(board, color)).size();
    }
    
    /**
     * Evaluates a move by simulating it and evaluating the board.
     */
    protected double evaluateMove(Board board, Color color, Move move) {
        return evaluateBoard(simulateMove(board, move), color);
    }
    
    /**
     * Determines the score of a board by using a linear combination of its
     * properties.
     */
    protected double evaluateBoard(Board board, Color color) {
        double ownDistance = colorDistanceFromCenter(board, color);
        double opponentDistance = colorDistanceFromCenter(board, getOpponentColor(board, color));
        double triplets = countTriplets(board, color);
        double killed = parseBoolean(board.marbleKilled);
        return -ownDistance * factors[0]
                + opponentDistance * factors[1]
                + triplets * factors[2]
                + killed * factors[3];
    }
    
    /**
     * Turn a boolean into a 0 or 1.
     */
    protected int parseBoolean(boolean b) {
        if (b) {
            return 1;
        } else {
            return 0;
        }
    }
    
    /**
     * Count how many lines of three marbles of a given color
     * there are on the board. This is an indicator of how defensive
     * the current position is.
     * Doubles also add to the score with a totally not arbitrarily
     * chosen factor of 0.3.
     */
    public double countTriplets(Board board, Color color) {
        ArrayList<Field> fields = board.getMapOfColors().get(color);
        int l = fields.size();
        double counter = 0;
        for (int i = 0; i < l; i++) {
            for (int j = i; j < l; j++) {
                Move move = new Move(board, color, fields.get(i),
                        fields.get(j), 0, 0);
                try {
                    move.isValidSelection();
                } catch (InvalidMoveException e) {
                    continue;
                }
                if (move.getSelectionSize() == 3) {
                    counter++;
                } else if (move.getSelectionSize() == 2) {
                    counter += 0.3;
                }
            }
        }
        return counter;
    }
    
    /**
     * Find the average distance from the center of the board for all marbles
     * of a given color.
     */
    public double colorDistanceFromCenter(Board board, Color color) {
        double total = 0;
        for (Field field : board.getMapOfColors().get(color)) {
            total += fieldDistanceFromCenter(board, field);
        }
        total = total / board.getMapOfColors().get(color).size();
        return total;
    }
    
    /**
     * Get a field's distance from the center of the board.
     * The distance is considered to be in which "ring" around the center
     * the field is in, so center is 0, outer rim is 4.
     * This result is then squared to make the cost of being close to the
     * edge of the board higher.
     */
    public int fieldDistanceFromCenter(Board board, Field field) {
        int center = board.getDim() - 1;
        int rowDist = field.getRow() - center;
        int colDist = field.getCol() - center;
        int result = 0;
        if (rowDist * colDist >= 0) {
            result = Math.max(Math.abs(rowDist), Math.abs(colDist));
        } else {
            result = Math.abs(rowDist) + Math.abs(colDist);
        }
        return result * result;
    }
    
    /**
     * Returns a color an opponent.
     * Only makes sense in a 2 player game.
     * Making this for more players is outside the scope of
     * this project.
     * @requires 2 player game
     */
    public Color getOpponentColor(Board board, Color color) {
        for (Color c : board.getMapOfColors().keySet()) {
            if (c != color) {
                return c;
            }
        }
        return color;
    }
    
    /**
     * Simulates the move.
     * Makes a copy of the board, performs the move, and returns it.
     * @return a new board with the move performed on it
     */
    public Board simulateMove(Board board, Move move) {
        Board copyBoard = board.deepCopy();
        move = move.deepCopy(copyBoard);
        try {
            move.perform();
        } catch (InvalidMoveException e) {
            System.out.println("PROBLEM: Only valid moves should be simulated.");
        } catch (MarbleKilledException e) {
            copyBoard.marbleKilled = true;
        }
        return copyBoard;
    }
}
