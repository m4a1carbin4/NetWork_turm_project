package main_server;

import java.io.*;
import java.net.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Client_socket extends Thread {

	Socket socket = null;
	
	InputStream input = null;
	
	DataInputStream datainputstream = null;
	
	OutputStream output = null;
	
	DataOutputStream dataoutputstream = null;
	
	JSONParser parser = new JSONParser();
	
	String Threadname;
	
	String str;
	
	Main_server server;
	Client_manager manager;
	Echo echo;
	int ID;
	
	public JSONObject Json_parser(String data) {
		
		JSONObject json_data = null;
		
		try {
			json_data = (JSONObject) parser.parse(data);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			System.out.println("incoming data error. Client_socket : " + Threadname + " error ");
		}
		
		return json_data;
		
	}
	
	public String Json_maker(String data,String type) {
		
		JSONObject object = new JSONObject();
		
		object.put("Type", type);
		object.put("Data", data);
		
		System.out.println("new json data");
		
		return object.toJSONString();
		
	}
	
	public void Fail_login() {
		
		try {
			dataoutputstream.writeUTF(Json_maker("fail","Login_fail"));
		} catch (IOException e) {
			System.out.println("login_fail_system_fail");
		}
	}
	
	public void clear_login() {
		
		try {
			dataoutputstream.writeUTF(Json_maker("clear","Login_clear"));
		} catch (IOException e) {
			System.out.println("login_clear");
		}
	}
	
	public void clear_regiester() {
		
		try {
			dataoutputstream.writeUTF(Json_maker("clear","Regiester_clear"));
		} catch (IOException e) {
			System.out.println("register_clear");
		}
	}
	
	public void fail_regiester() {
		
		try {
			dataoutputstream.writeUTF(Json_maker("fail","Regiester_fail"));
		} catch (IOException e) {
			System.out.println("register_fail");
		}
	}
	
	public void send_message(String input) {
		String str = Json_maker(input,"Message");
		
		try {
			dataoutputstream.writeUTF(str);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("server_echo_message_send_fail");
		}
	}
	
	public Boolean echo_change(Echo echo) {
		this.echo = echo;
		return true;
	}
	
	public Boolean manager_change(Client_manager manager) {
		this.manager = manager;
		return true;
	}

	public Client_socket(Main_server server, Socket socket,Echo echo) {
		
		this.server = server;
		this.socket = socket;
		this.echo = echo;
		this.manager = new Client_manager(server);
		
		Threadname = super.getName();
		ID = -1;
		
		System.out.println("new Socket connect! name : "+Threadname);
		
	}
	
	public void run(){
		
		try {
			
			input = socket.getInputStream();
			output = socket.getOutputStream();
			
			datainputstream = new DataInputStream(input);
			dataoutputstream = new DataOutputStream(output);
			
			while(true) {
				
				str = datainputstream.readUTF();
				
				JSONObject input_data = Json_parser(str);
				
				String Type = (String) input_data.get("Type");
				String Data = (String) input_data.get("Data");
				
				switch(Type) {
				case "Register":
					server.regiester_request(Data, this);
					break;
				case "Login":
					ID = server.Login_request(Data, this);
					break;
				case "Message":
					echo.brodcast_message(Data);
					break;
				/*case "Game":
					break;여전히 구상중인 파트.*/
				case "new_Room":
					server.make_Room(Data, this,ID);
					break;
				case "join_Room":
					server.Join_Room(Data, this,ID);
					break;
				case "return":
					manager.return_main(ID,this);
					break;
				/*case "private_msg":
					break;*/
				default :
					break;
				}
				
			}
			
		}catch (IOException e){
			System.out.println(Threadname + " has been removed!");
			manager.client_leave(ID, this);
		} finally {
			try {
				if (dataoutputstream != null) dataoutputstream.close();
				if (output != null) output.close();
				if (datainputstream != null) datainputstream.close();
				if (input != null) input.close();
				if (socket != null) socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
	}
	
}
