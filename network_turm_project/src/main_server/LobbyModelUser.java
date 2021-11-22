package main_server;

public class LobbyModelUser {
	public Integer user_id;
	public Integer WIN;
	public Integer LOSE;
	public String Stz_logstate;
	public String stz_ready;
	
	public LobbyModelUser() {
	}

	public LobbyModelUser(int user_id, Integer WIN,Integer LOSE,String stz_ready,String Stz_logstate) {
		this.user_id = user_id;
		this.WIN = WIN;
		this.LOSE = LOSE;
		this.stz_ready = stz_ready;
		this.Stz_logstate = Stz_logstate;
	}

	public String getStz_logstate() {
		return Stz_logstate;
	}
	
	public Integer getStz_username() {
		return user_id;
	}

	public void setStz_username(Integer user_id) {
		this.user_id = user_id;
	}

	public Integer getWin() {
		return WIN;
	}

	public void setWIN(Integer WIN) {
		this.WIN = WIN;
	}

	public Integer getLOSE() {
		return LOSE;
	}

	public void setLOSE(Integer LOSE) {
		this.LOSE = LOSE;
	}

	public String getStz_ready() {
		return stz_ready;
	}

	public void setStz_ready(String stz_ready) {
		this.stz_ready = stz_ready;
	}

}
