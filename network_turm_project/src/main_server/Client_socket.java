package main_server;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Vector;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;

import game_calculator.GameCalculator;

public class Client_socket extends Thread {

	Socket socket = null;
	
	InputStream input = null;
	
	DataInputStream datainputstream = null;
	
	OutputStream output = null;
	
	DataOutputStream dataoutputstream = null;
	
	JSONParser parser = new JSONParser();
	
	GameCalculator gameCalculator = null;
	
	private ScheduledExecutorService service;
	
	String Threadname;
	
	String str;
	
	Main_server server;
	Client_manager manager;
	Echo echo;
	DataExportToAllClient game_echo;
	String G_name_ptr;
	
	LobbyModelUser user_lobby = null;
	
	private StringBuilder jsonData = new StringBuilder();
	
	private Gson gson = new Gson();
	
	int ID = -1;
	
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
	
	public void Fail_login(String reason) {
		
		try {
			dataoutputstream.writeUTF(Json_maker(reason, "Login_fail"));
		} catch (IOException e) {
			System.out.println("login_fail_system_fail");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void clear_login(int id) {
		
		try {
			dataoutputstream.writeUTF(Json_maker(String.valueOf(id),"Login_clear"));
			int WIN = JDBC.WIN_check(ID);
			int LOSE = JDBC.LOSE_check(ID);
			user_lobby = new LobbyModelUser(ID, WIN,LOSE,"0","on");
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
	
	public void Room_clear() {
		String str = Json_maker("clear","R_clear");
		
		try {
			dataoutputstream.writeUTF(str);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("server_Room_clear_send_fail");
		}
	}
	
	public void Room_fail() {
		String str = Json_maker("fail","R_fail");
		
		try {
			dataoutputstream.writeUTF(str);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("server_Room_clear_send_fail");
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
	
	public void Game_model_msg(String input) {
		String str = Json_maker(input,"Game");
		
		try {
			dataoutputstream.writeUTF(str);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("server_Game_model_msg_send_fail");
		}
	}
	
	public void Game_start(String input) {
		String str = Json_maker(input,"Game_start");
		
		try {
			dataoutputstream.writeUTF(str);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("server_Game_start_send_fail");
		}
	}
	
	public void lobby_echo(String input) {
		String str = Json_maker(input,"lobby_echo");
		
		try {
			dataoutputstream.writeUTF(str);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("server_Lobby_data_send_fail");
		}
	}
	
	public void send_lobbyuser() {
		Gson gson = new Gson();
		
		String tmp = gson.toJson(user_lobby);
		
		String str = Json_maker(tmp,"lobby_echo");
		
		try {
			dataoutputstream.writeUTF(str);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("server_Lobbyuser_send_fail");
		}
	}
	
	public Boolean echo_change(Echo echo) {
		this.echo = echo;
		return true;
	}
	
	public Boolean echo_change(DataExportToAllClient echo) {
		this.echo = null;
		this.game_echo = echo;
		game_echo.getcal(this);
		return true;
	}
	
	public Boolean manager_change(Client_manager manager) {
		this.manager = manager;
		return true;
	}
	
	public void game_cal_set(GameCalculator input) {
		gameCalculator = input;
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
				case "new_Room":
					server.make_Room(Data, this,ID);
					break;
				case "join_Room":
					server.Join_Room(Data, this,ID);
					break;
				case "new_G_Room":
					server.G_make_Room(Data, this,ID);
					G_name_ptr = Data;
					break;
				case "join_G_Room":
					server.G_Join_Room(Data, this,ID);
					G_name_ptr = Data;
					break;
				case "return":
					manager.return_main(ID,this);
					break;
				case "Game_start_request":
					 server.R_manager.start_request(G_name_ptr,service);
					break;
				/*case "private_msg":
					break;*/
				case "Game":
					
					GameModel gameModel = gson.fromJson(Data, GameModel.class);

					for (int i = 0; i < gameCalculator.getGameModelForCalculator().size(); i++) {
						if (gameCalculator.getGameModelForCalculator().get(i).getPlayer().getStz_username()
								== ID) {
							gameCalculator.getGameModelForCalculator().get(i).getPlayer()
									.setAngle(gameModel.getPlayer().getAngle());
							gameCalculator.getGameModelForCalculator().get(i).getPlayer()
									.setStz_username(gameModel.getPlayer().getStz_username());
							gameCalculator.getGameModelForCalculator().get(i).getPlayer()
									.setX(gameModel.getPlayer().getX());
							gameCalculator.getGameModelForCalculator().get(i).getPlayer()
									.setY(gameModel.getPlayer().getY());
							gameCalculator.getGameModelForCalculator().get(i)
									.setBulletList(gameModel.getBulletList());
						}
					}
					
					//server.R_manager.send_data(G_name_ptr);
					
					break;	
					
				case "lobby":
					this.send_lobbyuser();
					server.R_manager.brodcast_lobby(G_name_ptr);
					break;
				case "ready":
					user_lobby.setStz_ready(Data);
					server.R_manager.brodcast_lobby(G_name_ptr);
					server.R_manager.start_request(G_name_ptr,service);
					break;
					
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
