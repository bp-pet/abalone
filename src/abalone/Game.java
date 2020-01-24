package abalone;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import abalone.AI.RandomStrategy;
import abalone.exceptions.InvalidMoveException;
import abalone.server.ClientPlayer;

public class Game {
	// -- Constants --------------------------------------------------

	private static final int MAX_TURNS = 96;

	// If you want to play the real game uncomment this.
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
	private Player[] players;

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
	 * creates player with Color color, if arg contains -R then Random Computer, else arg is
	 * the name of the Human player.
	 */
	public static Player createPlayer(String arg, Color color) {
		if (arg.contains("-R")) {
			return new ComputerPlayer(color, new RandomStrategy());
		} else {
			return new HumanPlayer(arg, color);
		}
	}
	
	/**
	 * creates a new game with new board with an array with players. The starting
	 * player is random. And the 
	 * 
	 * @requires 2 <= players.length <= MAX_PLAYERS
	 * @param players array of players
	 */
	public Game(String[] stringPlayers) {
		
		Player[] players = new Player[stringPlayers.length];
		Color current = Color.WHITE;
		for (int i = 0; i < stringPlayers.length; i++) {
			players[i] = createPlayer(stringPlayers[i], current);
			current = current.next(stringPlayers.length);
		}
		
		board = new Board();
		this.players = players;
		reset();
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
	 * The board is emptied and random player becomes the current player.
	 */
	public void reset() {
		Random r = new Random();
		numberOfTurns = 0;
		current = players[r.nextInt(players.length)].getColor();
		board.reset(getNumberOfPlayers());
		scores = new HashMap<Color, Integer>();
		for (Player player : players) {
			scores.put(player.getColor(), 0);
		}
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
		if (getNumberOfPlayers() == 4) {
			return (scores.get(p.getColor()) + scores.get(p.getColor().teamColor()) >= 6);
		} else {
			return (scores.get(p.getColor()) >= 6);
		}
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
				int score = scores.get(player.getColor());
				if (players.length == 4) {
					score += scores.get(player.getColor().teamColor());
				}
				if (score > highscore) {
					highscore = score;
					winner = player;
				} else if (highscore != 0 && score == highscore) {
					if (winner.getColor() != player.getColor().teamColor()) {
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
		System.out.println("current score: " + scores.toString());
		System.out.println(board.toString());
	}

	public void start() {
		// TODO: implement stop game when disconnection
		while (true) {
			play();
		}
	}
}
