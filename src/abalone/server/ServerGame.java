package abalone.server;

import abalone.Color;
import abalone.Game;
import abalone.Player;
import abalone.protocol.ProtocolMessages;

public class ServerGame extends Game implements Runnable {
    
    AbaloneServerLobby lobby;
    
    /**
     * constructs a game with all ClientPlayers.
     * @requires team name match and stuff
     */
    public ServerGame(AbaloneServerLobby lobby) {
        super(lobby.getNumberOfPlayers());
        this.lobby = lobby;        
        if (getNumberOfPlayers() == 4) {
            int i = 0;
            String teamPlayer1 = null;
            currentColor = Color.BLUE;
            for (AbaloneClientHandler client : lobby.getClients()) {
                if (i == 0) {
                    teamPlayer1 = lobby.getTeamName(client);
                    players[i] = new AbaloneClientPlayer(lobby, lobby.getPlayerName(client),
                            lobby.getTeamName(client), currentColor);
                    lobby.setColor(client, currentColor);
                } else {
                    if (teamPlayer1.equals(lobby.getTeamName(client))) {
                        players[i] = new AbaloneClientPlayer(lobby, lobby.getPlayerName(client),
                                lobby.getTeamName(client), currentColor);
                        lobby.setColor(client, currentColor);
                    } else {
                        players[i] = new AbaloneClientPlayer(lobby, lobby.getPlayerName(client),
                                lobby.getTeamName(client), currentColor);
                        lobby.setColor(client, currentColor);
                        currentColor = getNextColor();
                    }
                }
                i++;
            }
            
        } else {
            currentColor = Color.WHITE;
            int i = 0;
            for (AbaloneClientHandler client : lobby.getClients()) {
                players[i++] = new AbaloneClientPlayer(lobby, lobby.getPlayerName(client),
                        lobby.getTeamName(client), currentColor);
                lobby.setColor(client, currentColor);
                currentColor = getNextColor();
            }
        }
    }

    @Override
    public void start() {
        Player winner = play();
        if (winner != null) {
            lobby.doGameEnd(ProtocolMessages.GAME_END_MESSAGE_GAME_WON, winner.getColor());
        } else {
            lobby.doGameEnd(ProtocolMessages.GAME_END_MESSAGE_DRAW, null);    
        }
    }
    
    public Player[] getPlayers() {
        return players;
    }

    @Override
    public void run() {
        start();
    }

}
