package abalone;

import abalone.AI.Strategy;

public class ComputerPlayer extends Player {	
	private Strategy strategy;
	
	/**
     * Creates a new computer player object with strategy strategy.
     * @ensures the Name of this player will be strategy.getName() + "-" + super.mark.toString()
     * @ensures the Color of this player will be color
     */
	public ComputerPlayer(Color color, Strategy strategy) {
        super(strategy.getName(), color);
        this.strategy = strategy;
    }
	
	@Override
	public Move determineMove(Board board, String string) {
		return this.strategy.determineMove(board, super.getColor());
	}
	
	/**
	 * Getter for strategy.
	 */
	public Strategy getStrategy() {
		return this.strategy;
	}
	
	/**
	 * Setter for strategy.
	 */
	public void setStrategy(Strategy strategy) {
		this.strategy = strategy;
	}
}
