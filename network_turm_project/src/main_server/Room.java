package main_server;


import java.util.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import org.json.simple.JSONObject;

public class Room {

	Main_server Orgin = null;
	Room_manager R_manager = null;
	Client_manager manager = null;
	
	public Room(Room_manager R_manager, Main_server orgin) {
		this.R_manager = R_manager;
		Orgin = orgin;
		manager = new Client_manager(this,orgin);
	}
	
	public Boolean add_user(Client_socket user,int user_id) {
		
		if(manager.client_access(user_id, user)) {
			manager.Echo_change(user);
			return true;
		}else {
			return false;
		}
		
	}
	
	public Boolean Leave_user(Client_socket user,int user_id) {
		
		if(manager.client_leave(user_id, user)) {
			manager.return_main(user_id, user);
			return true;
		}else {
			return false;
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
	
	
	
}
