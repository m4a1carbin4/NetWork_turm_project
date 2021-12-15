package main_server;

import java.util.*;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.Socket;


public class Client_manager{

	Main_server Home = null;
	Room room = null;
	G_Room groom = null;
	HashMap<Integer,Client_socket> client_list = null;	
	Socket socket;
	Echo echo;
	int user_num = 0;
	
	JSONParser parser = new JSONParser();
	JSONObject tmp = null;
	
	public Client_manager(Main_server home) {
		
		Home = home;
		echo = new Echo(this);
		client_list = new HashMap<Integer,Client_socket>();
		
	}
	
	public Client_manager(Room room ,Main_server home) {
		
		Home = home;
		this.room = room;
		echo = new Echo(this);
		client_list = new HashMap<Integer,Client_socket>();
		
	}
	
	public Client_manager(G_Room g_Room, Main_server orgin) {
		Home = orgin;
		this.groom = g_Room;
		client_list = new HashMap<Integer,Client_socket>();
	}

	public Boolean client_access(String data,Client_socket user) {
		
		try {
			tmp = (JSONObject) parser.parse(data);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			System.out.println("parsing_error");
		}
		
		client_list.put(((Long)tmp.get("ID")).intValue(), user);
		user.manager = this;
		user_num +=1;
		return true;
	}
	
	public Boolean client_access(int ID ,Client_socket user) {
		
		client_list.put(ID, user);
		user.manager = this;
		user_num ++;
		return true;
	}
	
	public Boolean client_leave(int ID, Client_socket user) {
		
		if(Home.remove_client(user)) {
			client_list.remove(user.ID);
			user_num --;
			
			return true;
		}else {
			return false;
		}
		
		
	}
	
	public Boolean Echo_change(Client_socket user) {
		user.echo_change(echo);
		return true;
	}
	
	public Client_socket find_user(int ID) {
		Client_socket user = client_list.get(ID);
		return user;
	}
	
	public void return_main(int ID,Client_socket user) {
		user.echo = user.server.manager.echo;
		user.server.manager.client_access(ID, user);
		client_list.remove(ID);
		user.return_clear();
	}
	
}
