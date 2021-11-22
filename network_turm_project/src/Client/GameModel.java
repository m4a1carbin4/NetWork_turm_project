package Client;

import java.util.Vector;

import main_server.Bullet;
import starz502Client.data.DataTypes;

/*Client는 이거 2개 필요함. 서버로 보낼 데이터, 실시간으로 바꿀 데이터*/
public class GameModel {
	public Integer datatype = DataTypes.GAME;
	public Player player;
	public Vector<Bullet> bulletList;
	
	public GameModel() {
	}

	public GameModel(Integer datatype, Player player, Vector<Bullet> bulletList) {
		super();
		this.datatype = datatype;
		this.player = player;
		this.bulletList = bulletList;
	}

	public Integer getDatatype() {
		return datatype;
	}

	public void setDatatype(Integer datatype) {
		this.datatype = datatype;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Vector<Bullet> getBulletList() {
		return bulletList;
	}

	public void setBulletList(Vector<Bullet> bulletList) {
		this.bulletList = bulletList;
	}
		
}
