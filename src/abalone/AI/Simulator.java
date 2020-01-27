package abalone.AI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import abalone.Color;
import abalone.ComputerPlayer;
import abalone.Game;
import abalone.LocalGame;
import abalone.Player;

/**
 * Class used to simulate a large number of games between AI.
 * Specifically 2 player games.
 * 
 * @author Bozhidar Petrov, Daan Pluister
 */
public class Simulator {
	

	private static int roundsPerDuel = 1;
	
	private static ComputerPlayer[] players;
	private static Map<Player, Integer> result;
	
	public static void main(String[] args) {
		double[] result = tournament();
		System.out.println(factorsToString(result));
	}
	
	private static boolean play1v1(double[] factors1, double[] factors2) {
		result = new HashMap<Player, Integer>();
		players = new ComputerPlayer[2];
		players[0] = new ComputerPlayer(Color.WHITE, new ItsOverAnakinIHaveTheHighGroundStrategy(factors1));
		players[1] = new ComputerPlayer(Color.WHITE, new ItsOverAnakinIHaveTheHighGroundStrategy(factors2));
		Game game = new LocalGame(players);
		result = game.start(roundsPerDuel);
		for (Player p : result.keySet()) {
			result.get(p);
		}
		int highScore = 0;
		Player winner = null;
		for (Player p : result.keySet()) { 
			if (result.get(p) > highScore) {
				winner = p;
				highScore = result.get(p);
			}
		}
		return winner == players[0];
	}
	
	private static double[] tournament() {
		ArrayList<double[]> factorsList= makeArrays();
		int bestScore = 0;
		double[] winner = null;
		for (double[] factors1 : factorsList) {
			int score = 0;
			for (double[] factors2 : factorsList) {
				if (play1v1(factors1, factors2)) {
					score++;
				}
				System.out.println("game finished");
			}
			if (score > bestScore) {
				winner = factors1;
				bestScore = score;
			}
		}
		return winner;
	}
	
	private static String factorsToString(double[] factors) {
		String s = "Factors:";
		for (double f : factors) {
			s += "\n" + String.valueOf(f);
		}
		return s;
	}
	
	private static ArrayList<double[]> makeArrays() {
		ArrayList<double[]> result = new ArrayList<double[]>();
		int maxParameter = 2;
		for (int i = 0; i <= maxParameter; i++) {
			for (int j = 0; i <= maxParameter; i++) {
				for (int k = 0; i <= maxParameter; i++) {
					for (int l = 0; i <= maxParameter; i++) {
						double[] factors = new double[4];
						factors[0] = i;
						factors[1] = j;
						factors[2] = k;
						factors[3] = l;
						result.add(factors);
					}
				}
			}
		}
		System.out.println(re