package abalone;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import abalone.AI.ItsOverAnakinIHaveTheHighGroundStrategy;
import abalone.AI.RandomStrategy;
import abalone.exceptions.InvalidMoveException;

public abstract class Game {
	// -- Constants --------------------------------------------------

//	private static final int MAX_TURNS = 96;

	// If you want to play the real game uncomment this.
	private static final int MAX_TURNS = Integer.MAX_VALUE;

	public static final int MAX_PLAYERS = 4;

	public static final int MIN_PLAYERS = 2;

	// -- Instance variables -----------------------------------------

	/*
	 * @ private invariant board != null;
	 */
	/**
	 * The board.
	 */
	private Board board;

	/**
	 * Index of the current player.
	 */
	private Color current;

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
	public Game(int NumberOfPlayers) {
		this.players = new Player[NumberOfPlayers];
		scores = new HashMap<Color, Integer>();
		board = new Board();
	}

	// -- Queries ----------------------------------------------------

	/**
	 * Returns the board.
	 */
	public /* @ pure */ Board getBoard() {
		return board;
	}

	/**
	 * Returns the mark of the player whose turn it is.
	 */
	public /* @ pure */ Color getCurrent() {
		return current;
	}

	public int getNumberOfPlayers() {
		return players.length;
	}

	// -- Commands ---------------------------------------------------

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
		Random r = new Random();
		numberOfTurns = 0;
		current = players[r.nextInt(players.length)].getColor();
		board.reset(getNumberOfPlayers());
		for (Player player : players) {
			scores.put(player.getColor(), 0);
		}
		board.setTeams(makeTeams(getNumberOfPlayers()));
	}

	public void play() {
		reset();
		Move nextMove;
		while (!hasWinner() && numberOfTurns < MAX_TURNS) {
			nextMove = players[current.getInt()].determineMove(board);
			try {
				nextMove.perform();
			} catch (InvalidMoveException e) {
				System.out.println("Player not correctly implemented!");
				e.printStackTrace();
			}
			current = current.next(getNumberOfPlayers());
			// TODO: add score increase if marbles amount is lower
			numberOfTurns++;
			// TODO: remove method showBoard() or not
			showBoard();
		}

		Player winner = determineWinner();
		// TODO: send to server or not
		if (winner != null) {
			System.out.println(winner.getName() + " has won!");
		} else {
			System.out.println("DRAW!");
		}
	}

	public Color[][] makeTeams(int numberOfPlayers) {
		Color[][] result;
		Color[] team;
		switch (numberOfPlayers) {
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
		default:
			result = new Color[2][1];
			team = new Color[1];
			team[0] = Color.BLACK;
			result[0] = team;
			team = new Color[1];
			team[0] = Color.WHITE;
			result[1] = team;
			break;
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

	public int determineTeamScore(Player p) {
		int result = 0;
		for (Color c : board.getTeam(p.getColor())) {
			result += scores.get(c);
		}
		return result;
	}

	/**
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

	public void increaseScore(Color color) {
		scores.put(color, scores.get(color) + 1);
	}

	/**
	 * Determines winning player at any given game state.
	 * 
	 * @return one player off the team that pushed off the most marbles. null if
	 *         draw.
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

	/**
	 * TODO: remove method or not
	 */
	public void showBoard() {
		System.out.println("Moves left: " + (MAX_TURNS - numberOfTurns));
		System.out.println("Moves done: " + numberOfTurns);
		System.out.println("current score: " + scores.toString());
		System.out.println(board.toString());
	}

	/**
	 * Method that calls play(). When play is done one player has won.
	 */
	abstract public void start();
}
