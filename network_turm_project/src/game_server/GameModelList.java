package game_server;

import java.util.Vector;

import game_server.GameModel;

public class GameModelList {
	private Vector<GameModel> gameModelList;
	
	public GameModelList() {
		// TODO Auto-generated constructor stub
	}

	public GameModelList(Vector<GameModel> gameModelList) {
		this.gameModelList = gameModelList;
	}

	public Vector<GameModel> getGameModelList() {
		return gameModelList;
	}

	public void setGameModelList(Vector<GameModel> gameModelList) {
		this.gameModelList = gameModelList;
	}

}
