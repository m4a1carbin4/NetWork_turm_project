package Client;

import java.util.Vector;

import Client.GameModel;

public class GameModelList {
	public Vector<GameModel> gameModelList;
	
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
