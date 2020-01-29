package abalone.client;
    
import abalone.Board;
import abalone.Move;
import abalone.Player;
import abalone.exceptions.ServerUnavailableException;

public class AbaloneOwnPlayer extends Player {

    Player contolPlayer;
    private AbaloneClient client;

    /**
     * constructor sets client and controlplayer.
     * @param c the client connected
     * @param contolPlayer the player to control
     */
    public AbaloneOwnPlayer(AbaloneClient c, Player contolPlayer) {
        super(contolPlayer.getName(), contolPlayer.getColor());
        this.client = c;
        this.contolPlayer = contolPlayer;
    }

    @Override
    public Move determineMove(Board board, String stateOfGame) {
        Move move = contolPlayer.determineMove(board, stateOfGame);
        try {
            client.sendMove("" + board.getRowLetter(move.getRowTail())
                    + board.getColLetter(move.getColTail()),
                    "" + board.getRowLetter(move.getRowHead())
                    + board.getColLetter(move.getColHead()),
                    "" + board.getRowLetter(move.getRowDest())
                    + board.getColLetter(move.getColDest()));
        } catch (ServerUnavailableException e) {
            e.printStackTrace();
        }
        return move;
    }
}
