package abalone.server;

import abalone.Color;
import abalone.Game;
import abalone.Move;
import abalone.Player;
import abalone.exceptions.InvalidMoveException;
import abalone.exceptions.LobbyException;
import abalone.protocol.ProtocolMessages;
import abalone.protocol.ServerLobbyProtocol;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AbaloneServerLobby extends AbaloneServer implements ServerLobbyProtocol {
    /**
     * .
     * @invariance for clients in ready.keySet(): client.getPlayerName() != null &&
     *             client.getTeamName() != null.
     * @invariance 1 <= ready.size() <= game.MAX_PLAYERS
     */
    private Map<AbaloneClientHandler, Boolean> ready;
    private Map<AbaloneClientHandler, String> playerNames;
    private Map<AbaloneClientHandler, String> teamNames;
    private Map<AbaloneClientHandler, Color> colors;
    /**
     * the nextMove which is always null except when a next move is determined.
     * 
     * @invariance if nextMove != null then nextMove.isValidMove() == true
     */
    private Move nextMove;
    private String name;
    /** The game object of the lobby. */
    private ServerGame game;

    private Thread gameThread;
    
    private Object object;

    /**
     * constructor for a new lobby that adds the first client.
     */
    public AbaloneServerLobby(String name, AbaloneClientHandler client, String playerName, String teamName) {
        ready = new HashMap<AbaloneClientHandler, Boolean>();
        playerNames = new HashMap<AbaloneClientHandler, String>();
        teamNames = new HashMap<AbaloneClientHandler, String>();
        colors = new HashMap<AbaloneClientHandler, Color>();
        object = new Object();
        try {
            addClient(client, playerName, teamName);
        } catch (LobbyException e) {
            // This should not happen since adding a client to empty lobby always works.
            e.printStackTrace();
        }
        this.name = name;
    }

    /**
     * to lobby if client.getPlayerName() together with client.getTeamName() is not
     * in lobby and the maximum is not MAX_PLAYERS yet.
     * 
     * @requires client.getPlayerName() != null && client.getTeamName() != null
     * @ensures getReady(client) == false;
     */
    public void addClient(AbaloneClientHandler client, String playerName, String teamName) throws LobbyException {
        if (ready.size() >= Game.MAX_PLAYERS) {
            throw new LobbyException(ProtocolMessages.ERROR_MESSAGE_LOBBY_FULL);
        }
        if (hasPlayerNameAndTeamName(playerName, teamName)) {
            throw new LobbyException(ProtocolMessages.ERROR_MESSAGE_LOBBY_TEAMS);
        }
        this.ready.put(client, false);
        resetReady();
        this.playerNames.put(client, playerName);
        this.teamNames.put(client, teamName);
        client.setLobby(this);
    }

    /**
     * Query if there is a client with the same playerName and teamName in the
     * lobby.
     */
    public boolean hasPlayerNameAndTeamName(String playerName, String teamName) {
        for (AbaloneClientHandler client : getClients()) {
            if (getPlayerName(client).equals(playerName) && getTeamName(client).equals(teamName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Query that returns if client is in Lobby.
     */
    public boolean hasClient(AbaloneClientHandler client) {
        return ready.containsKey(client);
    }

    /**
     * Query that returns if client is ready. If client is not in lobby this returns
     * null.
     */
    public Boolean getReady(AbaloneClientHandler client) {
        return ready.get(client);
    }

    /**
     * query that returns if all clients are ready.
     * 
     * @return
     */
    private boolean everyoneReady() {
        boolean everyoneReady = true;
        for (AbaloneClientHandler client : ready.keySet()) {
            everyoneReady = everyoneReady && getReady(client);
        }
        return everyoneReady;
    }

    /**
     * Setter that set client to ready.
     */
    public void setReady(AbaloneClientHandler client) {
        ready.put(client, true);

    }

    /**
     * Setter that sets all the clients ready to false.
     */
    public void resetReady() {
        for (AbaloneClientHandler client : getClients()) {
            ready.put(client, false);
        }
    }

    /**
     * Deletes client from the lobby.
     * 
     * @ensures if hasClient(client) then getNumberOfPlayers() decreases with 1.
     */
    public void delClient(AbaloneClientHandler client) {
        ready.remove(client);
        playerNames.remove(client);
        teamNames.remove(client);
        client.setLobby(null);
    }

    /**
     * Query that returns the lobbyName.
     * 
     * @return lobbyName
     */
    public String getName() {
        return name;
    }

    /**
     * Query that returns a list of all clients in game.
     * 
     * @ensures 1 < lobbyClients.size() < 4
     * @return
     */
    public ArrayList<AbaloneClientHandler> getClients() {
        ArrayList<AbaloneClientHandler> lobbyClients = new ArrayList<AbaloneClientHandler>();
        for (AbaloneClientHandler client : ready.keySet()) {
            lobbyClients.add(client);
        }
        return lobbyClients;
    }

    /**
     * Query that returns a set of all teams in game.
     * 
     * @ensures 1 < lobbyClients.size() < 4
     * @return
     */
    public Set<String> getTeams() {
        Set<String> teams = new HashSet<String>();
        for (AbaloneClientHandler client : ready.keySet()) {
            teams.add(getTeamName(client));
        }
        return teams;
    }

    /**
     * Query that returns the number of clients in the lobby.
     * 
     * @ensures 1 < numberOfPlayers < 4
     * @return numberOfPlayers
     */
    public int getNumberOfPlayers() {
        return ready.size();
    }

    /**
     * Getter for the playerName of the client in the lobby. <code>null</code> if
     * player not in lobby.
     * 
     * @param client the client whose player we want the name of
     * @return
     */
    public String getPlayerName(AbaloneClientHandler client) {
        return playerNames.get(client);
    }

    public void setPlayerName(AbaloneClientHandler client, String playerName) {
        this.playerNames.put(client, playerName);
    }

    /**
     * Getter for the playerName of the client in the lobby. <code>null</code> if
     * player not in lobby.
     * 
     * @param client the client whose team we want the anem of
     * @return
     */
    public String getTeamName(AbaloneClientHandler client) {
        return teamNames.get(client);
    }

    public void setTeamName(AbaloneClientHandler client, String teamName) {
        this.teamNames.put(client, teamName);
    }

    public Color getColor(AbaloneClientHandler client) {
        return colors.get(client);
    }

    public void setColor(AbaloneClientHandler client, Color color) {
        this.colors.put(client, color);
    }

    /**
     * toString of lobby, prints the format:
     * <code>pm.LOBBY + pm.DELIMITER + lobbyName +
     * pm.DELIMITER + pm.PLAYER + pm.DELIMITER + player1Name + pm.DELIMITER +
     * player1teamName + etc</code>.
     */
    public String toString() {
        String s = ProtocolMessages.LOBBY + ProtocolMessages.DELIMITER + name;
        for (AbaloneClientHandler client : getClients()) {
            s += ProtocolMessages.DELIMITER + ProtocolMessages.PLAYER + ProtocolMessages.DELIMITER
                    + playerNames.get(client) + ProtocolMessages.DELIMITER + teamNames.get(client);
        }
        return s;
    }

    /**
     * sends the message given to the clients in the given lobby.
     */
    public void sendMessageToLobby(String message) {
        for (AbaloneClientHandler client : getClients()) {
            client.sendMessage(message);
        }
    }

    /**
     * Query that returns if a lobby is inGame.
     * 
     * @return
     */
    public boolean inGame() {
        return (game != null);
    }

    // -- Server Game methods
    // --------------------------------------------------------

    /**
     * Creates a new game instance.
     */
    public void setupGame() {
        game = new ServerGame(this);
    }

    /**
     * starts the game.
     */
    public void startGame() {
        gameThread = new Thread(game);
        gameThread.start();
    }

    @Override
    public void doTurn(Color color) {
        sendMessageToLobby(ProtocolMessages.TURN + ProtocolMessages.DELIMITER + getProtocolColor(color));
    }

    /**
     * translate Color to protocol Color string.
     * 
     * @return
     */
    public String getProtocolColor(Color color) {
        switch (color) {
            case WHITE:
                return ProtocolMessages.COLOR_WHITE;
            case BLACK:
                return ProtocolMessages.COLOR_BLACK;
            case BLUE:
                return ProtocolMessages.COLOR_BLUE;
            case RED:
                return ProtocolMessages.COLOR_RED;
            default:
                return ProtocolMessages.COLOR_WHITE;
        }
    }

    @Override
    public String doGameEnd(String result, Color color) {
        if (color != null) {
            sendMessageToLobby(ProtocolMessages.GAME_END + ProtocolMessages.DELIMITER + result
                    + ProtocolMessages.DELIMITER + getProtocolColor(color));
        } else {
            sendMessageToLobby(ProtocolMessages.GAME_END + ProtocolMessages.DELIMITER + result);
        }
        gameThread.interrupt();
        game = null;
        return null;
    }

    @Override
    public void doReady(AbaloneClientHandler client) {
        setReady(client);
        if (everyoneReady() && getNumberOfPlayers() >= 2) {
            sendMessageToLobby(doStart());
            startGame();
        } else {
            sendMessageToLobby(ProtocolMessages.READY + ProtocolMessages.DELIMITER + getPlayerName(client)
                    + ProtocolMessages.DELIMITER + getTeamName(client));
        }
    }

    @Override
    public String doStart() {
        setupGame();
        String s = Character.toString(ProtocolMessages.START);
        for (Player player : game.getPlayers()) {
            s += ProtocolMessages.DELIMITER + ((AbaloneClientPlayer) player).getPlayerName()
                    + ProtocolMessages.DELIMITER + ((AbaloneClientPlayer) player).getTeamName();
        }
        return s;
    }

    @Override
    public void exitGame(AbaloneClientHandler client) {
        sendMessageToLobby(ProtocolMessages.EXIT + ProtocolMessages.DELIMITER + getPlayerName(client)
                + ProtocolMessages.DELIMITER + getTeamName(client));
        resetReady();
    }

    @Override
    public String doMove(AbaloneClientHandler client, String pos1, String pos2, String des1) {
        Color color = colors.get(client);
        Move move = null;
        try {
            move = game.getBoard().parseMovePattern(color, pos1 + " " + pos2 + " " + des1);
        } catch (InvalidMoveException e1) {
            return (doError(2, "Unexcpected arguments we want the form m;a1;b2;c3"));
        }
        try {
            move.isValidMove();
        } catch (InvalidMoveException e) {
            return Character.toString(ProtocolMessages.UNEXPECTED_MOVE);
        }
        synchronized (object) {
            nextMove = move;
            object.notifyAll();
        }
        sendMessageToLobby(ProtocolMessages.MOVE + ProtocolMessages.DELIMITER + pos1
                + ProtocolMessages.DELIMITER + pos2
                + ProtocolMessages.DELIMITER + des1);
        return null;
    }

    /**
     * Returns if it is client's turn.
     * 
     * @requires game != null
     * @return
     */
    public boolean isTurn(AbaloneClientHandler client) {
        return colors.get(client) == game.getCurrentColor();
    }

    /**
     * returns the nextMove when nextMove is set to not null.
     * 
     * @return
     */
    public Move getMove() {
        Move toReturnMove;
        synchronized (object) {
            try {
                object.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            toReturnMove = nextMove;
            nextMove = null;
        }
        nextMove = null;
        return toReturnMove;
    }

}
