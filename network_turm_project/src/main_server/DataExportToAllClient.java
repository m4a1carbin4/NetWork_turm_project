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
import game_server.Bullet;
import game_server.GameModel;
import game_server.GameModelList;
import game_server.Player;

public class DataExportToAllClient extends Thread { 
	
	private HashMap<Integer,Client_socket> client_list = null;
	private Vector<Client_socket> clientList = null;
	private Gson gson = new Gson();
	private GameCalculator gameCalculator = null;
	private ScheduledExecutorService service;

	public DataExportToAllClient(HashMap<Integer,Client_socket> c_list) {
		this.client_list = c_list;
		this.clientList = (Vector<Client_socket>) c_list.values();
		this.gameCalculator = new GameCalculator();
	}
	
	public void getcal(Client_socket user) {
		user.game_cal_set(gameCalculator);
	}

	public void startDataToReadyClient(Vector<Integer> readyClientList) {
		
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

		for (int readyClientUserName : readyClientList) {
			
			for (Client_socket client : client_list.values()) {
				if (readyClientUserName == client.ID) {
					client.Game_start("game_start_now");
				}
			}
		}

		// run() 0초 후에 34ms마다 실행
		service = Executors.newSingleThreadScheduledExecutor();
		service.scheduleAtFixedRate(this, 0, 34, TimeUnit.MILLISECONDS);

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

	@Override
	public void run() {

		gameCalculator.calPlayerHitByBullet(); // 피격 판정 함수
		GameModelList gameModelList = new GameModelList(gameCalculator.getGameModelForCalculator());

		for (Client_socket client : client_list.values()) {
			client.Game_model_msg(gson.toJson(gameModelList));
		}

		for (int i = 0; i < gameModelList.getGameModelList().size(); i++) {
			if (gameModelList.getGameModelList().get(i).getPlayer().getCurHp() <= 0) {
				for (int j = 0; j < clientList.size(); j++) {
					if (gameModelList.getGameModelList().get(i).getPlayer().getStz_username()
							== clientList.get(j).ID) {
						clientList.remove(j);
					}
				}
			}
		}
	}

}
