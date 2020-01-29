package abalone.server2;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import abalone.Board;
import abalone.Color;
import abalone.Move;
import abalone.exceptions.InvalidMoveException;
import abalone.exceptions.MarbleKilledException;

public class AbaloneServerGame {

	private static final int MAX_TURNS = 96;

	private Board board;

	private Color currentColor;
	
	private Color[] colors;

	private Map<Color, Integer> scores;

	private int numberOfTurns;
	
	private AbaloneLobby lobby;

	public AbaloneServerGame(int numberOfPlayers, AbaloneLobby lobby) {
		colors = makeColors(numberOfPlayers);
		scores = new HashMap<Color, Integer>();
		board = new Board();
		this.lobby = lobby;
	}
	
	public Color[] makeColors(int numberOfPlayers) {
		Color[] result = new Color[numberOfPlayers];
		switch (numberOfPlayers) {
			case 2:
				result[0] = Color.WHITE;
				result[1] = Color.BLACK;
				break;
			case 3:
				result[0] = Color.WHITE;
				result[1] = Color.BLACK;
				result[2] = Color.BLUE;
				break;
			case 4:
				result[0] = Color.WHITE;
				result[1] = Color.BLACK;
				result[2] = Color.BLUE;
				result[3] = Color.RED;
				break;
		}
		return result;
	}

	public Board getBoard() {
		return board;
	}

	public Color getCurrentColor() {
		return currentColor;
	}

	public int getNumberOfPlayers() {
		return colors.length;
	}

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
	
	public int getIntOfCurrentColor() {
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
	
	public void reset() {
		numberOfTurns = 0;
		currentColor = getStartingColor();
		board.reset(getNumberOfPlayers());
		resetScores();
		board.setTeams(makeTeams(getNumberOfPlayers()));
	}
	
	public Color getStartingColor() {
		Random r = new Random();
		return colors[r.nextInt(colors.length)];
	}
	
	public Color getNextTurn() {
		return getNextColor();
	}

	private void resetScores() {
		for (Color color : colors) {
			scores.put(color, 0);
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

	public boolean isWinner(Color color) {
		return (determineTeamScore(color) >= 6);
	}

	public int determineTeamScore(Color color) {
		int result = 0;
		for (Color c : board.getTeam(color)) {
			result += scores.get(c);
		}
		return result;
	}

	public boolean hasWinner() {
		for (Color color : colors) {
			if (isWinner(color)) {
				return true;
			}
		}
		return false;
	}

	public void increaseScore(Color color) {
		scores.put(color, scores.get(color) + 1);
	}

	private Color determineWinner() {
		Color winner = null;
		if (hasWinner()) {
			for (Color color : colors) {
				if (isWinner(color)) {
					winner = color;
					break;
				}
			}
		} else {
			int highscore = 0;
			for (Color color : colors) {
				int score = determineTeamScore(color);
				if (score > highscore) {
					highscore = score;
					winner = color;
				} else if (highscore != 0 && score == highscore) {
					if (winner != color) {
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

	public void doMove(String[] cmd) throws InvalidMoveException {
		Move move = board.parseMovePattern(currentColor, cmd[1] + " " + cmd[2] +
				" " + cmd[3]);
		try {
			move.perform();
		} catch (MarbleKilledException e) {
			increaseScore(currentColor);
		}
		update();
		lobby.askForMove(currentColor);
	}

	private void update() {
		if (!hasWinner() && numberOfTurns < MAX_TURNS) {
			currentColor = getNextTurn();
			numberOfTurns++;
		} else {
			Color winner = determineWinner();
		}
	}
}
