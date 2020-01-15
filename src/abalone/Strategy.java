package abalone;
	
public interface Strategy {
	/**
	 * returns the name of the strategy.
	 * @return the name of the strategy
	 */
	public String getName();
	
	/**
	 * returns a next legal move.
	 * @param board given board
	 * @param mark given mark
	 * @return
	 */
	public int determineMove(Board board, Color mark);
}
