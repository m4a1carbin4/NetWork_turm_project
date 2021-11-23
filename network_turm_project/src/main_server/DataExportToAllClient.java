package main_server;

import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import game_calculator.GameCalculator;

public class DataExportToAllClient extends Thread{ 
	
	private HashMap<Integer,Client_socket> client_list = null;
	private Vector<Client_socket> clientList = null;
	private Gson gson = new Gson();
	private GameCalculator gameCalculator = null;
	private ScheduledExecutorService service;

	public DataExportToAllClient(HashMap<Integer,Client_socket> c_list,ScheduledExecutorService service) {
		this.client_list = c_list;
		
		Vector<Client_socket> ptr = new Vector<Client_socket>();
		
		for(Client_socket i : client_list.values()) {
			ptr.add(i);
		}
		
		this.clientList = ptr;
		this.gameCalculator = new GameCalculator();
		this.service = service;
		export_start();
	}
	
	public synchronized void export_start() {
		for(Client_socket tmp : client_list.values()) {
			getcal(tmp);
		}
		Vector<Integer> ptr = new Vector<Integer>();
		
		for(Integer i : client_list.keySet()) {
			ptr.add(i);
		}
		
		startDataToReadyClient(ptr);
		
	}
	
	public synchronized void getcal(Client_socket user) {
		user.game_cal_set(gameCalculator);
	}

	public synchronized void startDataToReadyClient(Vector<Integer> readyClientList) {
		
		gameCalculator.getGameModelForCalculator().setSize(0);
		for (int i = 0; i < readyClientList.size(); i++) {
			GameModel readyClient = new GameModel();
			readyClient.setPlayer(new Player());
			readyClient.getPlayer().setStz_username(readyClientList.get(i));
			readyClient.getPlayer().setCurHp(100);

			Vector<Bullet> bulletList = new Vector<Bullet>();
			readyClient.setBulletList(bulletList);

			gameCalculator.getGameModelForCalculator().add(readyClient);
		}
		
		for (Client_socket client : client_list.values()) {				
			client.Game_start("start");
		}

		// run() 0초 후에 34ms마다 실행
		service = Executors.newSingleThreadScheduledExecutor();
		service.scheduleAtFixedRate(this, 0, 70, TimeUnit.MILLISECONDS);

	}

	public void lobbyDataExportToAllClient(String jSonData) {
		
		for (Client_socket client : client_list.values()) {				
			try {
				client.dataoutputstream.writeUTF(jSonData);
			} catch (IOException e) {
				System.out.println("dataoutput stream error in game_lobby");
			}
		}
	}

	// 모든 클라이언트에게 (그림 그리는데 필요한 모든)데이터 전송하는 스레드 , period : 34ms

	public void run() {
		int num = 4;
		gameCalculator.calPlayerHitByBullet(); // 피격 판정 함수
		GameModelList gameModelList = new GameModelList(gameCalculator.getGameModelForCalculator());

		for (Client_socket client : client_list.values()) {
			String tmp = gson.toJson(gameModelList);
			
			client.Game_model_msg(tmp);
			System.out.println(tmp);
		}

		for (int i = 0; i < gameModelList.getGameModelList().size(); i++) {
			if (gameModelList.getGameModelList().get(i).getPlayer().getCurHp() <= 0) {
				for (int j = 0; j < clientList.size(); j++) {
					if (gameModelList.getGameModelList().get(i).getPlayer().getStz_username()
							== clientList.get(j).ID) {
						clientList.remove(j);
						num--;
					}
				}
			}
		}
		
		if(clientList.isEmpty()) {
			service.shutdown();
		}
	}

}
