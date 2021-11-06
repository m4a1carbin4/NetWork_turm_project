package main_server;

import java.util.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Echo {
	
	JSONParser parser = new JSONParser();
	JSONObject tmp = null;

	Client_manager manager = null;
	
	public Echo(Client_manager input) {
		manager = input;
	}
	
	public synchronized void brodcast_message(String str) {
		
		for(Client_socket socket : manager.client_list.values()) {
			
			socket.send_message(str);
			
		}
		
	}
	
}
