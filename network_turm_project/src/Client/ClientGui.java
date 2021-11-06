package Client;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class ClientGui extends JFrame implements ActionListener, Runnable{
	// 클라이언트 화면용
	Container container = getContentPane();
	JTextArea textArea = new JTextArea();
	JScrollPane scrollPane = new JScrollPane(textArea);
	JTextField textField = new JTextField();
	// 통신용
	Socket socket;
	InputStream input = null;
	OutputStream output = null;
	DataInputStream datainput = null;
	DataOutputStream dataoutput = null;
	String str; 		// 채팅 문자열 저장
	
	BufferedReader in = null;
	
	JSONParser parser = new JSONParser();
	JSONObject tmp = null;
	
	public ClientGui(String ip, int port) {
		// frame 기본 설정
		setTitle("챗팅");
		setSize(550, 400);
		setLocation(400, 400);
		init();
		start();
		setVisible(true);
		in = new BufferedReader(new InputStreamReader(System.in));
		// 통신 초기화
		initNet(ip, port);
		System.out.println("ip = " + ip);
	}	
	// 통신 초기화
	private void initNet(String ip, int port) {
		try {
			// 서버에 접속 시도
			socket = new Socket(ip, port);
			// 통신용 input, output 클래스 설정
			input = socket.getInputStream();
			output = socket.getOutputStream();
			
			datainput = new DataInputStream(input);
			dataoutput = new DataOutputStream(output);
			
		} catch (UnknownHostException e) {
			System.out.println("IP 주소가 다릅니다.");
			//e.printStackTrace();
		} catch (IOException e) {
			System.out.println("접속 실패");
			//e.printStackTrace();
		}
		// 쓰레드 구동
		Thread thread = new Thread(this); // run 함수 -> this
		thread.start();
	}
	private void init() {
		container.setLayout(new BorderLayout());
		container.add("Center", scrollPane);
		container.add("South", textField);
	}
	private void start() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		textField.addActionListener(this);
	}
	// 응답 대기
	// -> 서버로부터 응답으로 전달된 문자열을 읽어서, textArea에 출력하기
	
	public JSONObject Json_parser(String data) {
		
		JSONObject json_data = null;
		
		try {
			json_data = (JSONObject) parser.parse(data);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			System.out.println("incoming data error error ");
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
	
	public String Login_maker(String passwd,int ID) {
		
		JSONObject object = new JSONObject();
		
		object.put("ID", ID);
		object.put("passwd", passwd);
		
		System.out.println("new json_login data");
		
		return object.toJSONString();
	}
	
	public String Message_maker(String msg) {
		
		JSONObject object = new JSONObject();
		
		object.put("Type", "Message");
		object.put("Data", msg);
		
		System.out.println("new json_login data");
		
		return object.toJSONString();
	}
	
	public String Make_room(String name) {
		
		JSONObject object = new JSONObject();
		
		object.put("Type", "new_Room");
		object.put("Data", name);
		
		System.out.println("new json_make_room data");
		
		return	object.toJSONString();
	}
	
	public String Join_room(String name) {
		
		JSONObject object = new JSONObject();
		
		object.put("Type", "join_Room");
		object.put("Data", name);
		
		System.out.println("new json_join_room data");
		
		return	object.toJSONString();
	}
	
	@Override
	public void run() {
		
		int ID = -1;
		String passwd = null;
		
		try {
			System.out.println("input ID :");
			ID = Integer.parseInt(in.readLine());
			System.out.println("input passwd :");
			passwd = in.readLine();
		} catch (NumberFormatException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		str = Login_maker(passwd,ID);
		
		str = Json_maker(str,"Login");
		
		try {
			dataoutput.writeUTF(str);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		while(true) {
			try {
				
				str = datainput.readUTF();
				
				JSONObject input_data = Json_parser(str);
				
				String Type = (String) input_data.get("Type");
				String Data = (String) input_data.get("Data");
				
				switch(Type) {
				
					case "Message":
						textArea.append(Data + "\n");
						break;
						
				}
				
				textArea.append(str + "\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// textField의 문자열을 읽어와서 서버로 전송함
		str = textField.getText();
		 
		str = Message_maker(str);
		
		try {
			dataoutput.writeUTF(str);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// textField 초기화
		textField.setText("");
	}
}
