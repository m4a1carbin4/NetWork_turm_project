package main_server;  


import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import org.json.simple.JSONObject;

public class Room_manager {

	HashMap<String,Room> Room_list = null;
	HashMap<String,G_Room> Room_listG = null;
	
	Main_server server = null;
	Client_socket user_tmp = null;
	
	public Room_manager(Main_server server) {
		this.server = server;
		Room_list = new HashMap<String,Room>();
		Room_listG = new HashMap<String,G_Room>();
	}
	
	public Boolean Make_Room(Client_socket user,String Room_name ,int ID) {
		
		if(Room_list.get(Room_name) == null) {
			
			Room new_room = new Room(this,server);
			Room_list.put(Room_name, new_room);
			new_room.add_user(user, ID);
			user.send_message("new_Room has been made.");
			
			return true;
		}else {
			return false;
		}
		
	}
	
	public Boolean Join_Room(Client_socket user,String Room_name,int ID) {
		Room tmp = Room_list.get(Room_name);
		
		if(tmp != null) {
			tmp.add_user(user, ID);
			user.send_message("System : success Join the "+Room_name+".");
			return true;
		}else {
			user.send_message("System : fail Join the "+Room_name+".");
			return false;
		}
	}
	
	public Boolean Make_G_Room(Client_socket user,String Room_name ,int ID) {
		
		if(Room_listG.get(Room_name) == null) {
			
			G_Room new_room = new G_Room(this,server);
			Room_listG.put(Room_name, new_room);
			new_room.add_user(user, ID, user.user_lobby);
			user.Room_clear();
			
			return true;
		}else {
			user.Room_fail();
			return false;
		}
		
	}
	
	public Boolean Join_G_Room(Client_socket user,String Room_name,int ID) {
		G_Room tmp = Room_listG.get(Room_name);
		
		if(tmp != null) {
			tmp.add_user(user, ID,user.user_lobby);
			System.out.println("System : success Join the "+Room_name+".");
			user.Room_clear();
			return true;
		}else {
			System.out.println("System : fail Join the "+Room_name+".");
			user.Room_fail();
			return false;
		}
	}
	
	public void brodcast_lobby(String Room_name) {
		
		G_Room tmp = Room_listG.get(Room_name);
		
		tmp.brodcast_lobby();
		
	}
	
	/*public void send_data(String Room_name) {
		
		G_Room tmp = Room_listG.get(Room_name);
		
		tmp.send_data();
		
	}*/
	
	public void start_request(String Room_name,ScheduledExecutorService service) {
		
		G_Room tmp = Room_listG.get(Room_name);
		
		Boolean result = tmp.start_request();
		
		if(result) {
			tmp.start_game(service);
		}
	}
	
	public Boolean Del_room(String Room_name) {
		
		Room tmp = Room_list.get(Room_name);
		
		if(tmp.check_user()) {
			Room_list.remove(Room_name);
			return true;
		}else {
			return false;
		}
		
	}
	
	
	public Boolean Del_room(String Room_name,int i) {
		
		G_Room tmp = Room_listG.get(Room_name);
		
		if(tmp.check_user()) {
			Room_listG.remove(Room_name);
			return true;
		}else {
			return false;
		}
		
	}
	
}
