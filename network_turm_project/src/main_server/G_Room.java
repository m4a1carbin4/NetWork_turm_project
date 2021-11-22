package main_server;

import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import org.json.simple.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class G_Room {

	Main_server Orgin = null;
	Room_manager R_manager = null;
	Client_manager manager = null;
	LobbyModel lobby_server = null;
	DataExportToAllClient export = null;
	
	public G_Room(Room_manager R_manager, Main_server orgin) {
		this.R_manager = R_manager;
		Orgin = orgin;
		manager = new Client_manager(this,orgin);
		lobby_server = new LobbyModel();
	}
	
	public Boolean add_user(Client_socket user,int user_id,LobbyModelUser user_lobby) {
		
		if(manager.client_access(user_id, user)) {
			manager.Echo_change(user);
			lobby_server.addLobbyModelUser(user_lobby);
			return true;
		}else {
			return false;
		}
		
	}
	
	public Boolean Leave_user(Client_socket user,int user_id,LobbyModelUser user_lobby) {
		
		if(manager.client_leave(user_id, user)) {
			manager.return_main(user_id, user);
			lobby_server.delLobbyModelUser(user_lobby);
			return true;
		}else {
			return false;
		}
		
	}
	
	public synchronized void brodcast_lobby() {
		
		Gson gson = new Gson();
		String json = gson.toJson(lobby_server);
		System.out.println(json);
		
		for(Client_socket socket : manager.client_list.values()) {
			
			socket.lobby_echo(json);
			
		}
		
	}
	
	public Boolean check_user() {
		if(manager.user_num !=1) {
			return false;
		}else {
			return true;
		}
	}
	
	public void return_main(int ID,Client_socket user) {
		
		manager.return_main(ID,user);
		
	}
	
//	public void send_data() {
	//	export.send_data();
	//}
	
	public Boolean start_request() {
		int ptr = 0;
		for(LobbyModelUser tmp :lobby_server.getLobbyModelUser()) {
			if(!(tmp.getStz_ready().equals("1"))) {
				return false;
			}
			ptr++;
		}
		
		if(ptr == 4) {
			return true;
		}
		return false;
	}
	
	public synchronized void start_game(ScheduledExecutorService service) {
		
		export = new DataExportToAllClient(manager.client_list,service);
		//export.send_data();
	}
	
	
	
}
