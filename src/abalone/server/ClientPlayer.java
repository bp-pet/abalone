package abalone.server;

import abalone.Board;
import abalone.Color;
import abalone.Move;
import abalone.Player;

public class ClientPlayer extends Player {

	public ClientPlayer(String name, Color color) {
		super(name, color);
	}

	@Override
	public Move determineMove(Board board) {
		
		// TODO Auto-generated method stub
		return srv.getMove();
	}

}
