package abalone.AI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import abalone.Color;
import abalone.ComputerPlayer;
import abalone.LocalGame;
import abalone.Player;

/**
 * Class used to simulate a large number of games between AI.
 * Specifically 2 player games.
 * 
 * In this class an AI usually refers to an array of doubles which
 * characterize its behaviour.
 * 
 * @author Bozhidar Petrov, Daan Pluister
 */
public class Simulator {
	
	private static final int roundsPerDuel = 1;
	private static final int generations = 20;
	private static final int mutationsPerGen = 50;
	
	private static ComputerPlayer[] players;
	private static Map<Player, Integer> result;
	
	/**
	 * Runs the program.
	 */
	public static void main(String[] args) {
		double[] current = new double[4];
		current[0] = 1;
		current[1] = 10;
		current[2] = 1;
		current[3] = 100;
		for (int j = 0; j < generations; j++) {
			current = tournamentRandomElimination(current);
			System.out.println("winner of generation " + j);
			System.out.println(factorsToString(current));
		}
		System.out.println("Winner:");
		System.out.println(factorsToString(current));
	}
	
	
	/**
	 * Plays two versions of the AI against each other.
	 * @return 0 if first AI wins, 1 if second, 2 if draw.
	 */
	private static int play1v1(double[] factors1, double[] factors2) {
		result = new HashMap<Player, Integer>();
		players = new ComputerPlayer[2];
		players[0] = new ComputerPlayer(Color.WHITE, new ItsOverAnakinIHaveTheHighGroundStrategy(factors1));
		players[1] = new ComputerPlayer(Color.WHITE, new ItsOverAnakinIHaveTheHighGroundStrategy(factors2));
		LocalGame game = new LocalGame(players);
		result = game.playNTimes(roundsPerDuel);
		int highScore = 0;
		Player winner = null;
		for (Player p : result.keySet()) { 
			if (result.get(p) > highScore) {
				winner = p;
				highScore = result.get(p);
			} else if (result.get(p) == highScore) {
				winner = null;
			}
		}
		if (winner == players[0]) {
			return 0;
		} else if (winner == players[1]) {
			return 1;
		} else {
			return 2;
		}
	}
	
	/**
	 * Creates a tournament where every AI plays against all others.
	 * @return the factors of the winning AI
	 */
	@SuppressWarnings("unused")
	private static double[] tournament(double[] factors) {
		ArrayList<double[]> factorsList = makeMutatedArrays(factors);
		int bestScore = 0;
		double[] winner = factors;
		for (int i = 0; i < factorsList.size(); i++) {
			int score = 0;
			double[] factors1 = factorsList.get(i);
			for (int j = 0; j < factorsList.size(); j++) {
				double[] factors2 = factorsList.get(j);
				if (factors1 != factors2 &&
						play1v1(factors1, factors2) == 0) {
					score++;
				}
			}
			if (score > bestScore) {
				winner = factors1;
				bestScore = score;
			}
		}
		return winner;
	}
	
	/**
	 * Tournament where two random players play against each other
	 * and the loser is thrown out. The last one left is the winner.
	 * Complexity n.
	 * @return the factors of the winning AI
	 */
	private static double[] tournamentRandomElimination(double[] current) {
		ArrayList<double[]> factorsList = makeMutatedArrays(current);
		while (factorsList.size() > 1) {
			double[] p1 = factorsList.get(
					new Random().nextInt(factorsList.size()));
			factorsList.remove(p1);
			double[] p2 = factorsList.get(
					new Random().nextInt(factorsList.size()));
			factorsList.remove(p2);
			int outcome = play1v1(p1, p2);
			if (outcome == 0) {
				factorsList.add(p1);
			} else {
				factorsList.add(p2);
			}
		}
		return factorsList.get(0);
	}
	
	/**
	 * Makes a string of an array of doubles for convenience.
	 */
	private static String factorsToString(double[] factors) {
		String s = "Factors:";
		for (double f : factors) {
			s += "\n" + String.valueOf(f);
		}
		return s;
	}
	
	/**
	 * Makes a list of mutated versions of a given AI.
	 * @param factors to start with
	 * @return a list of arrays of doubles of same size as given one
	 */
	private static ArrayList<double[]> makeMutatedArrays(double[] factors) {
		ArrayList<double[]> result = new ArrayList<double[]>();
		result.add(factors);
		for (int i = 0; i < mutationsPerGen; i++) {
			double[] mutation = makeMutation(factors.length);
			result.add(mutate(factors, mutation));
		}
		return result;
	}
	
	/**
	 * Uses normal distribution to make mutations.
	 * @param size of the resulting array
	 * @return array of doubles
	 */
	private static double[] makeMutation(int size) {
		double[] result = new double[size];
		for (int i = 0; i < size; i++) {
			result[i] = new Random().nextGaussian() + 1;
		}
		return result;
	}
	
	/**
	 * Applies a mutation to an AI.
	 * @return a new AI.
	 */
	private static double[] mutate(double[] factors, double[] mutation) {
		double[] result = new double[factors.length];
		for (int i = 0; i < factors.length; i++) {
			result[i] = factors[i] * mutation[i];
		}
		return result;
	}
}




