package Client;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import org.json.simple.JSONObject;

import com.google.gson.Gson;

import Client.GameFrame;
import Client.LobbyModel;
import Client.LobbyModelUser;
import Json_Controller.Json_Controller;

public class LobbyFrame extends JFrame implements Runnable {

	private DataInputStream reader;
	private DataOutputStream writer;
	private int username;
	
	private LobbyModelUser lobbyModelUser;
	private Vector<LobbyModelUser> lobbyModelUserList;
	private LobbyModel sendLobbyModel;
	
	private LobbyModel receiveLobbyModel;
	
	private JPanel contentPane;
	private JPanel panelRank;
	private JLabel labelRank;
	private JScrollPane scrollPaneRank;
	private JPanel panelList;
	private JLabel labelList;
	private JScrollPane scrollPaneList;
	private JButton btnReady, btnRefresh;
	private DefaultTableModel tableModelRank, tableModelList;
	private JTable tableRank, tableList;
	private DefaultTableCellRenderer tScheduleCellRendererRank, tScheduleCellRendererList;
	private JLabel centerCharacter;
	
	private String readyState = "0";

	public LobbyFrame(DataInputStream r, DataOutputStream w, int username) {
		this.reader = r;
		this.writer = w;
		this.username = username;
		
		GameFrame Game = null;

		Color myColor = new Color(200, 190, 230);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 1100, 800);
		setLocationRelativeTo(null);
		setResizable(false);

		contentPane = new JPanel();
		contentPane.setBorder(new LineBorder(Color.BLACK, 5));
		contentPane.setBackground(myColor);

		setContentPane(contentPane);
		contentPane.setLayout(null);

		panelRank = new JPanel();
		panelRank.setBounds(50, 50, 350, 670);
		panelRank.setBackground(new Color(100, 0, 100));
		panelRank.setLayout(null);
		contentPane.add(panelRank);

		labelRank = new JLabel("RANKING");
		labelRank.setBounds(0, 0, panelRank.getWidth(), 50);
		labelRank.setFont(new Font("Arial Black", Font.BOLD, 30));
		labelRank.setForeground(Color.ORANGE);
		labelRank.setHorizontalAlignment(SwingConstants.CENTER);
		panelRank.add(labelRank);

		panelList = new JPanel();
		panelList.setBounds(770, 50, 270, 536);
		panelList.setBackground(new Color(100, 0, 100));
		contentPane.add(panelList);
		panelList.setLayout(null);

		labelList = new JLabel("USER LIST");
		labelList.setBounds(0, 0, panelList.getWidth(), 50);
		labelList.setHorizontalAlignment(SwingConstants.CENTER);
		labelList.setForeground(Color.ORANGE);
		labelList.setFont(new Font("Arial Black", Font.BOLD, 30));
		panelList.add(labelList);

		ImageIcon ready = new ImageIcon("images/lobby/btnReady.png");
		btnReady = new JButton(ready);
		btnReady.setBounds(770, 622, 270, 100);
		btnReady.setContentAreaFilled(false);
		btnReady.setBorderPainted(false);
		btnReady.setFocusPainted(false);
		btnReady.setCursor(new Cursor(Cursor.HAND_CURSOR));
		contentPane.add(btnReady);
		
		ImageIcon character = new ImageIcon("images/lobby/lobbycharacter.png");
		centerCharacter = new JLabel(character);
		centerCharacter.setBounds(433, 93, 310, 492);
		contentPane.add(centerCharacter);



		Thread t1 = new Thread(this);
		t1.start();

//===============레디 버튼=======================================
		btnReady.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(readyState.equals("0")) {
					readyState = "1";
				}else {
					readyState = "0";
				}
				
				lobbyModelUser.setStz_ready(readyState);
				
				String ptr = Json_Controller.wrap("ready", readyState);

				// 3. 만들어진 json을 서버로 보내기
				try {
					writer.writeUTF(ptr);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		setVisible(true);
	}
	
	@Override
	public void run() {
		// 로비 프레임에 진입하자마자 서버로 진입사실을 알림 (LobbyModel 객체 JSON으로 전송)
		GameFrame Game = null;
		// 2. 해당 객체를 GSON 라이브러리를 이용하여 JSON으로 만들기
		Gson gson = new Gson();
		// 3. 만들어진 json을 서버로 보내기
		
		String ptr1 = Json_Controller.wrap("lobby", "in");
		
		try {
			writer.writeUTF(ptr1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String ptr2 = null;
		
		try {
			ptr2 = reader.readUTF();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		JSONObject tmp = Json_Controller.parse(ptr2);
		String data = (String)tmp.get("Data");
		
		lobbyModelUser = gson.fromJson(data, LobbyModelUser.class);
		

		// 4. 서버로부터 응답 받기 (데이터베이스에 있는 모든 회원의 username, rating, log_state 정보)
		while (true) {
			try {
				String jsonData = reader.readUTF();
//				System.out.println(jsonData);
				
				JSONObject obj = Json_Controller.parse(jsonData);
				String Type = (String)obj.get("Type");
				String gson_data = (String) obj.get("Data");
				
				if (Type != null && Type.equals("Game_start")) {
					setVisible(false);
					Game = new GameFrame(writer, reader, username);
					break;
				} else {
					receiveLobbyModel = gson.fromJson(gson_data, LobbyModel.class);

					// LobbyModel 객체 이용해서 화면에 정보 뿌려주기 (Ranking, userlist)
					Vector<LobbyModelUser> lobbyModelUserList = receiveLobbyModel.getLobbyModelUser();

					// =============UserList=========================
					// 리스트 테이블 열의 이름 벡터
					Vector<String> list = new Vector<>();
					list.add("Level");
					list.add("UserName");
					list.add("Ready");

					// tableModel에 전체 행 넣기
					tableModelList = new DefaultTableModel(list, 0) {
						public boolean isCellEditable(int i, int c) {
							return false;
						}
					};

					for (int i = 0; i < lobbyModelUserList.size(); i++) {

						if (lobbyModelUserList.get(i).getStz_logstate().equals("on")) {
							Vector<Object> row = new Vector<>();
							row.addElement(lobbyModelUserList.get(i).getWin());
							row.addElement(lobbyModelUserList.get(i).getStz_username());
							if( lobbyModelUserList.get(i).getStz_ready().equals("0") ) {
								row.addElement("X");
							}else {
								row.addElement("O");
							}
//							row.addElement(lobbyModelUserList.get(i).getStz_ready());

							tableModelList.addRow(row);

						}

					}

					// 테이블 모델을 JTable에 넣기
					tableList = new JTable(tableModelList);
					tableList.getTableHeader().setReorderingAllowed(false); // 이동 불가
					tableList.getTableHeader().setResizingAllowed(false); // 크기 조절 불가
					tableList.setFont(new Font("돋움", Font.PLAIN, 20));
					tableList.setRowHeight(50);

					// 테이블 가운데 정렬
					// DefaultTableCellHeaderRenderer 생성 (가운데 정렬을 위한)
					tScheduleCellRendererList = new DefaultTableCellRenderer();
					// DefaultTableCellHeaderRenderer의 정렬을 가운데 정렬로 지정
					tScheduleCellRendererList.setHorizontalAlignment(SwingConstants.CENTER);
					// 정렬할 테이블의 ColumnModel을 가져옴
					TableColumnModel tcmScheduleList = tableList.getColumnModel();
					// 반복문을 이용하여 테이블을 가운데 정렬로 지정
					for (int i = 0; i < tcmScheduleList.getColumnCount(); i++) {
						tcmScheduleList.getColumn(i).setCellRenderer(tScheduleCellRendererList);
					}

					// JTable에 스크롤달아서 add하기
					scrollPaneList = new JScrollPane(tableList);
					scrollPaneList.setBounds(25, 60, 220, 450);
					panelList.add(scrollPaneList);
				}

			} catch (Exception e1) {

				e1.printStackTrace();
			}
		}
	}
	
//	public static void main(String[] args) {
//		new LobbyFrame(null, null, null);
//	}
}
	