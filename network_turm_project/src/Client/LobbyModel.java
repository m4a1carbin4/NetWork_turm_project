package Client;

import java.util.Vector;

public class LobbyModel {	
	public Vector<LobbyModelUser> lobbyModelUser;
	
	public LobbyModel() {
		// TODO Auto-generated constructor stub
		lobbyModelUser = new Vector<LobbyModelUser>();
	}

	public LobbyModel(Integer datatype, Vector<LobbyModelUser> lobbyModelUser) {
		this.lobbyModelUser = lobbyModelUser;
	}

	public Vector<LobbyModelUser> getLobbyModelUser() {
		return lobbyModelUser;
	}

	public void setLobbyModelUser(Vector<LobbyModelUser> lobbyModelUser) {
		this.lobbyModelUser = lobbyModelUser;
	}
	
	public void delLobbyModelUser(LobbyModelUser lobbyModelUser) {
		this.lobbyModelUser.remove(this.lobbyModelUser.indexOf(lobbyModelUser));
	}
	
	public void addLobbyModelUser(LobbyModelUser lobbyModelUser) {
		this.lobbyModelUser.add(lobbyModelUser);
	}

}
