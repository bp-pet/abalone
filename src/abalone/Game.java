package abalone;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import abalone.exceptions.InvalidMoveException;
import abalone.exceptions.MarbleKilledException;

/**
 * An abstract class for, which is a generalization of LocalGame, ClientGame and ServerGame.
 * 
 * @authors Daan Pluister, Bozhidar Petrov
 */
public abstract class Game {
	// -- Constants --------------------------------------------------

	private static final int MAX_TURNS = 96;
//	private static final int MAX_TURNS = Integer.MAX_VALUE;

	public static final int MAX_PLAYERS = 4;

	public static final int MIN_PLAYERS = 2;

	// -- Instance variables -----------------------------------------

	/*
	 * @ private invariant board != null;
	 */
	/**
	 * The board.
	 */
	protected Board board;

	/**
	 * Index of the current player.
	 */
	protected Color currentColor;

	/**
	 * Array of teams.
	 * 
	 * @invariance 2 <= players.length <= MAX_PLAYERS
	 */
	protected Player[] players;

	/**
	 * Map of Color to the score that color has.
	 * 
	 * @invariance 0 <= Integer <= 6
	 * @invariance players.size == players.length;
	 */
	private Map<Color, Integer> scores;

	/**
	 * current number of moves.
	 * 
	 * @invariance 0 <= numberOfTurns <= MAX_NUMBER_OF_TURNS
	 */
	private int numberOfTurns;

	// -- Constructors -----------------------------------------------

	/**
	 * creates a new game with empty player array (Player[]) a new Board and new
	 * scores list will be created.
	 * 
	 * @requires 2 <= players.length <= MAX_PLAYERS
	 * @param players NumberOfPlayers.
	 */
	public Game(int numberOfPlayers) {
		this.players = new Player[numberOfPlayers];
		scores = new HashMap<Color, Integer>();
		board = new Board();
	}

	// -- Queries ----------------------------------------------------

	/**
	 * Returns the board.
	 */
	public Board getBoard() {
		return board;
	}

	/**
	 * Returns the mark of the player whose turn it is.
	 */
	public Color getCurrentColor() {
		return currentColor;
	}

	public int getNumberOfPlayers() {
		return players.length;
	}

	// -- Commands ---------------------------------------------------

	/**
	 * Return next color in rotation.
	 * @return the next color; if color given not valid for given number
	 * of players, return null.
	 */
	public Color getNextColor() {
		switch (getNumberOfPlayers()) {
			case 2:
				switch (currentColor) {
					case WHITE:
						return Color.BLACK;
					case BLACK:
						return Color.WHITE;
					default:
						return null;	
				}
			case 3:
				switch (currentColor) {
					case BLUE:
						return Color.BLACK;
					case BLACK:
						return Color.WHITE;
					case WHITE: 
						return Color.BLUE;
					default:
						return null;
				}
			case 4:
				switch(currentColor) {
					case BLACK:
						return Color.RED;
					case RED:
						return Color.WHITE;
					case WHITE:
						return Color.BLUE;
					case BLUE:
						return Color.BLACK;
					default:
						return null;
				}
			default:
				return null;
		}
	}
	
	/**
	 * Returns int representation of color.
	 * @return (WHITE,BLACK,BLUE,RED) => (1,2,3,4)
	 */
	protected int getIntOfCurrentColor() {
		switch (currentColor) {
			case WHITE:
				return 0;
			case BLACK:
				return 1;
			case BLUE:
				return 2;
			case RED:
				return 3;
			default:
				return 0;
		}
	}
	
	/**
	 * Resets the game. <br>
	 * The following is done:
	 * <p>
	 * <ul>
	 * <li>Number of turns gets reset.
	 * <li>Random player starts (becomes the current player).
	 * <li>Board will be reset for getNumberOfPlayers() players.
	 * <li>Scores are reset.
	 * <li>Teams are set.
	 * </ul>
	 * <p>
	 */
	public void reset() {
		numberOfTurns = 0;
		currentColor = getStartingColor();
		board.reset(getNumberOfPlayers());
		resetScores();
		board.setTeams(makeTeams(getNumberOfPlayers()));
	}
	
	/**
	 * returns the color of a starting player which is random
	 * @return
	 */
	public Color getStartingColor() {
		Random r = new Random();
		return players[r.nextInt(players.length)].getColor();
	}
	
	
	/**
	 * Returns the color for the next turn.
	 * @return
	 */
	public Color getNextTurn() {
		return getNextColor();
	}

	private void resetScores() {
		for (Player player : players) {
			scores.put(player.getColor(), 0);
		}
		
	}

	public Player play() {
		reset();
		Move nextMove;
		while (!hasWinner() && numberOfTurns < MAX_TURNS) {
			Player nextPlayer = players[getIntOfCurrentColor()];
			nextMove = nextPlayer.determineMove(board, toString());
			try {
				board.move(nextMove);
			} catch (InvalidMoveException e1) {
				System.out.println("Player not correctly implemented!");
				System.out.println(e1.getMessage());
			} catch (MarbleKilledException e2) {
				increaseScore(currentColor);
			}
			currentColor = getNextTurn();
			numberOfTurns++;
		}
//		System.out.println(toString());
		Player winner = determineWinner();
		// TODO: send to server or not
		if (winner != null) {
//			System.out.println(winner.getName() + " has won!");
			return winner;
		} else {
//			System.out.println("DRAW!");
			return null;
		}
	}

	public Color[][] makeTeams(int numberOfPlayers) {
		Color[][] result;
		Color[] team;
		switch (numberOfPlayers) {
		case 2:
			result = new Color[2][1];
			team = new Color[1];
			team[0] = Color.BLACK;
			result[0] = team;
			team = new Color[1];
			team[0] = Color.WHITE;
			result[1] = team;
			break;
		case 3:
			result = new Color[3][1];
			team = new Color[1];
			team[0] = Color.BLACK;
			result[0] = team;
			team = new Color[1];
			team[0] = Color.WHITE;
			result[1] = team;
			team = new Color[1];
			team[0] = Color.RED;
			result[1] = team;
			break;
		case 4:
			result = new Color[2][2];
			team = new Color[2];
			team[0] = Color.BLACK;
			team[0] = Color.WHITE;
			result[0] = team;
			team = new Color[2];
			team[0] = Color.RED;
			team[0] = Color.BLUE;
			result[0] = team;
			break;
		default:
			result = null;
		}
		return result;
	}

	/**
	 * If players.length < 4: returns scores.get(p.getColor()) == 6 If
	 * players.length == 4: returns scores.get(p.getColor()) +
	 * scores.get(p.getColor().teamColor()) == 6
	 * 
	 * @param m the marble of interest
	 * @return true if the marble has won
	 */
	/* @pure */
	public boolean isWinner(Player p) {
		return (determineTeamScore(p) >= 6);
	}

	/**
	 * Determine the score of the team of a given color by summing the
	 * scores of its team.
	 */
	public int determineTeamScore(Player p) {
		int result = 0;
		for (Color c : board.getTeam(p.getColor())) {
			result += scores.get(c);
		}
		return result;
	}

	/**
	 * Checks if the game has a winner by checking if any of the players
	 * is a winner.
	 * @return true if the game has a winner.
	 */
	public boolean hasWinner() {
		for (Player p : players) {
			if (isWinner(p)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Increments the score of a color by one.
	 */
	public void increaseScore(Color color) {
		scores.put(color, scores.get(color) + 1);
	}

	/**
	 * Determines winning player at any given game state.
	 * First checks if any player has won by reaching the maximum score.
	 * If not, then checks which player has the highest score.
	 * If draw, return null.
	 */
	private Player determineWinner() {
		Player winner = null;
		if (hasWinner()) {
			for (Player player : players) {
				if (isWinner(player)) {
					winner = player;
					break;
				}
			}
		} else {
			int highscore = 0;
			for (Player player : players) {
				int score = determineTeamScore(player);
				if (score > highscore) {
					highscore = score;
					winner = player;
				} else if (highscore != 0 && score == highscore) {
					if (winner.getColor() != player.getColor()) {
						winner = null;
					}
				}
			}
		}
		return winner;
	}
	
	public String toString() {
		return "Moves left: " + String.valueOf(MAX_TURNS - numberOfTurns) +
				"\nMoves done: " + String.valueOf(numberOfTurns) +
				"\nCurrent score :" + scores.toString() + "\n" + board.toString();
	}
	
	// -- Abstract methods --------------------------------------
	
	/**
	 * Method that calls play().
	 */
	abstract public void start();
	
}
