package abalone.server2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import abalone.Color;
import abalone.exceptions.InvalidMoveException;

public class AbaloneLobby {
	
	private ArrayList<AbaloneClientHandler> clients;
	private Map<AbaloneClientHandler, Boolean> ready;
	private Map<AbaloneClientHandler, String> playerNames;
	private Map<AbaloneClientHandler, String> teamNames;
	private AbaloneServerGame game;
	private String name;
	
	public AbaloneLobby(String name) {
		this.name = name;
		clients = new ArrayList<AbaloneClientHandler>();
		ready = new HashMap<AbaloneClientHandler, Boolean>();
		playerNames = new HashMap<AbaloneClientHandler, String>();
		teamNames = new HashMap<AbaloneClientHandler, String>();
	}
	
	public void addClient(AbaloneClientHandler client, String playerName,
			String teamName) {
		clients.add(client);
		ready.put(client, false);
		playerNames.put(client, playerName);
		teamNames.put(client, teamName);
		resetReady();
	}
	
	public void readyClient(AbaloneClientHandler client) {
		ready.put(client, true);
		tryStartGame();
	}
	
	public void resetReady() {
		for (AbaloneClientHandler c : clients) {
			ready.put(c, false);
		}
	}
	
	public void removeClient(AbaloneClientHandler client) {
		clients.remove(client);
		resetReady();
	}
	
	public String getLobbyName() {
		return name;
	}
	
	public ArrayList<AbaloneClientHandler> getClients() {
		return clients;
	}
	
	public Map<AbaloneClientHandler, Boolean> getReadyMap() {
		return ready;
	}
	
	public boolean isReady(AbaloneClientHandler client) {
		return ready.get(client);
	}
	
	public boolean allReady() {
		for (AbaloneClientHandler client : clients) {
			if (!ready.get(client)) {
				return false;
			}
		}
		try {
			sendToAll("all ready");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	
	public Map<AbaloneClientHandler, String> getPlayerNames() {
		return playerNames;
	}
	
	public Map<AbaloneClientHandler, String> getTeamNames() {
		return teamNames;
	}
	
	public int getNumberOfClients() {
		return clients.size();
	}
	
	public Map<String, Integer> getTeams() {
		Map<String, Integer> teams = new HashMap<String, Integer>();
		for (AbaloneClientHandler client : clients) {
			for (String teamName : teams.keySet())
				if (teamNames.get(client).equals(teamName)) {
					teams.put(teamName, teams.get(teamName) + 1);
					break;
			}
		}
		return teams;
	}
	
	public int getNumberOfTeams() {
		return getTeams().keySet().size();
	}
	
	public void tryStartGame() {
		if (allReady())
			switch (getNumberOfClients()) {
				case 2:
					if (getNumberOfTeams() == 2) {
						startGame();
					}
					break;
				case 3:
					if (getNumberOfTeams() == 3) {
						startGame();
					}
					break;
				case 4:
					if (getNumberOfTeams() == 2) {
						for (String team : getTeams().keySet()) {
							if (getTeams().get(team) == 2) {
								startGame();
								break;
							}
						}
					}
					break;
				default:
					break;
		}
	}
	
	public void startGame() {
		game = new AbaloneServerGame(getNumberOfClients(), this);
		try {
			clients.get(0).sendLine("game started");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void askForMove(Color color) {
		for (AbaloneClientHandler client : clients) {
			client.askForMove(color);
		}
	}

	public void doMove(String[] cmd) throws InvalidMoveException {
		game.doMove(cmd);
	}
	
	public void sendToAll(String msg) throws IOException {
		for (AbaloneClientHandler client : clients) {
			client.sendLine(msg);
		}
	}
}
