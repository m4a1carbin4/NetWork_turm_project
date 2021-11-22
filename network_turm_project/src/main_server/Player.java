package main_server;

public class Player {
	public int stz_username;
	public int x;
	public int y;
	public double angle;
	public int curHp;
	
	public Player() {
	}

	public Player(int stz_username, int x, int y, double angle, int curHp) {
		this.stz_username = stz_username;
		this.x = x;
		this.y = y;
		this.angle = angle;
		this.curHp = curHp;
	}

	public int getStz_username() {
		return stz_username;
	}

	public void setStz_username(int stz_username) {
		this.stz_username = stz_username;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public double getAngle() {
		return angle;
	}

	public void setAngle(double angle) {
		this.angle = angle;
	}

	public int getCurHp() {
		return curHp;
	}

	public void setCurHp(int curHp) {
		this.curHp = curHp;
	}
	
}
