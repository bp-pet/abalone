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
//		System.out.println("Very inaccurate expected run time in minutes:");
//		System.out.println(calculateExpectedTime() / 60);
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
	
//	public static void main(String[] args) {
//		double[] current1 = new double[4];
//		current1[0] = 1;
//		current1[1] = 0;
//		current1[2] = 0;
//		current1[3] = 0;
//		double[] current2 = new double[4];
//		current2[0] = 2;
//		current2[1] = 20;
//		current2[2] = 2;
//		current2[3] = 200;
//		if (play1v1(current1, current2) == 1) {
//			System.out.println("victory");
//		}
//	}
	
//	public static void main(String[] args) {
//		double[] current = new double[4];
//		current[0] = 1;
//		current[1] = 10;
//		current[2] = 1;
//		current[3] = 10;
//		Player[] players = new Player[2];
//		players[0] = new ComputerPlayer(Color.WHITE,
//				new ItsOverAnakinIHaveTheHighGroundStrategy(current));
//		players[1] = new ComputerPlayer(Color.WHITE,
//				new ItsOverAnakinIHaveTheHighGroundStrategy());
//		Map<Player, Integer> result = new LocalGame(players).playNTimes(10);
//		System.out.println(result.get(players[0]));
//	}
	
	/**
	 * .
	 */
	private static double calculateExpectedTime() {
		return generations * mutationsPerGen * (mutationsPerGen + 1) *
				roundsPerDuel * 2 / 2;
	}
	
	private static int play1v1(double[] factors1, double[] factors2) {
//		System.out.println("playing:");
//		System.out.println(factorsToString(factors1));
//		System.out.println(factorsToString(factors2));
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
//			System.out.println("win");
			return 0;
		} else if (winner == players[1]) {
			return 1;
		} else {
			return 2;
		}
	}
	
	/**
	 * Creates a tournament where every AI plays against all others
	 * once. The complexity is then n^2 with the number of AI.
	 * @return the factors of the winning AI
	 */
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
	
	private static String factorsToString(double[] factors) {
		String s = "Factors:";
		for (double f : factors) {
			s += "\n" + String.valueOf(f);
		}
		return s;
	}
	
//	private static ArrayList<double[]> makeArraysInRange() {
//		ArrayList<double[]> result = new ArrayList<double[]>();
//		int maxParameter = 4;
//		for (int i = 0; i <= maxParameter; i++) {
//			for (int j = 0; j <= maxParameter; j++) {
//				for (int k = 0; k <= maxParameter; k++) {
//					for (int l = 0; l <= maxParameter; l++) {
//						double[] factors = new double[4];
//						factors[0] = i;
//						factors[1] = j;
//						factors[2] = k;
//						factors[3] = l;
//						result.add(factors);
//					}
//				}
//			}
//		}
//		return result;
//	}
	
	private static ArrayList<double[]> makeMutatedArrays(double[] factors) {
		ArrayList<double[]> result = new ArrayList<double[]>();
		result.add(factors);
		for (int i = 0; i < mutationsPerGen; i++) {
			double[] mutation = makeMutation(factors.length);
			result.add(mutate(factors, mutation));
		}
		return result;
	}
	
	private static double[] makeMutation(int size) {
		double[] result = new double[size];
		for (int i = 0; i < size; i++) {
			result[i] = new Random().nextGaussian() + 1;
		}
		return result;
	}
	
	private static double[] mutate(double[] factors, double[] mutation) {
		double[] result = new double[factors.length];
		for (int i = 0; i < factors.length; i++) {
			result[i] = factors[i] * mutation[i];
		}
		return result;
	}
}




