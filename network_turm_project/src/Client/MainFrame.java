package Client;

import java.awt.Toolkit;
import java.awt.BorderLayout;
import java.awt.Component;
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
import java.util.Hashtable;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import GUI.ImagedButton9;
import Json_Controller.Json_Controller;


public class MainFrame extends JFrame implements ActionListener, Runnable {
	//
	Container container = getContentPane();
	
	private JTextField identify;
	private JPasswordField password;
	
	private Chat chat;

	private int userid = -1;
	private boolean valid = false;
	
	private Hashtable<String, String> dataKeeper;
	
	// 통신용
	Socket socket = null;
	InputStream input = null;
	OutputStream output = null;
	DataInputStream datainput = null;
	DataOutputStream dataoutput = null;
	
	BufferedReader in = null;

	private final int[] login_size = {480, 320}; 
	private final int[] main_size = {1024, 768}; 
	
	public MainFrame(String ip, int port) {
		setTitle("Title");
		
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
	    var resolution = Toolkit.getDefaultToolkit().getScreenSize();
	    setLocation((resolution.width - login_size[0]) / 2, (resolution.height - login_size[1]) / 2);
	    setSize(login_size[0], login_size[1]);
	    
	    initialize();
		start();
		
		initNet(ip, port);
		System.out.println("ip = " + ip);
		
		in = new BufferedReader(new InputStreamReader(System.in));
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
			
			valid = true;
			
		} catch (UnknownHostException e) {
			System.out.println("IP 주소가 다릅니다.");
			//e.printStackTrace();
		} catch (IOException e) {
			System.out.println("접속 실패");
			//System.exit(0);
			//e.printStackTrace();
		}
	}
	
	private void initialize() { // Initialize to login screen
		int x = 30;
		int y = 110;
		
		JLabel label = new JLabel("ID");
		label.setBounds(x + 10, y + 10, 100, 20);
		container.add(label);
		
		label = new JLabel("Password");
		label.setBounds(x + 10, y + 40, 100, 20);
		container.add(label);
		
		JPanel panel = new JPanel();
		panel.setBounds(x + 110, y + 10, 170, 70);
		panel.setLayout(null);
		container.add(panel);
		
		identify = new JTextField();
		identify.setBounds(0, 0, 170, 20);
		panel.add(identify);
		
		password = new JPasswordField();
		password.setBounds(0, 30, 170, 20);
		password.setEchoChar('*');
		panel.add(password);
		
		JButton btn = new JButton("Login");
		btn.setBounds(x + 300, y + 10, 80, 50);
		btn.addActionListener(this);
		container.add(btn);
		
		btn = new JButton("Forgot your password?");
		btn.setBounds(x + 10, y + 80, 180, 20);
		container.add(btn);
		
		btn = new JButton("Register");
		btn.setBounds(x + 200, y + 80, 180, 20);
		container.add(btn);
	}
	
	private void clear() {
		container.removeAll();
		revalidate();
		repaint();
	}
	
	private void initialize(int type) {
		clear();
		
		switch (type) {
		case 1:
		    var resolution = Toolkit.getDefaultToolkit().getScreenSize();
		    setLocation((resolution.width - main_size[0]) / 2, (resolution.height - main_size[1]) / 2);
		    setSize(main_size[0], main_size[1]);
		    
		    chat = new Chat(this, main_size[0] - 440, main_size[1] - 350, 400, 300);
		    chat.setVisible(false);
		    container.add(chat);
		    
		    var exitbtn = new ImagedButton9(this, "Exit", "gui/imagedbutton9_red/button");
		    exitbtn.setBounds(main_size[0] - 130, main_size[1] - 120, 60, 30);
		    exitbtn.setVisible(true);
		    container.add(exitbtn);
		    
		    var chatbtn = new ImagedButton9(this, "Chat", "gui/imagedbutton9/button", "OpenChat");
		    chatbtn.setBounds(main_size[0] - 200, main_size[1] - 120, 60, 30);
		    chatbtn.setVisible(true);
		    container.add(chatbtn);
		    
		    var room_create = new ImagedButton9(this, "Create Room", "gui/imagedbutton9/button", "Room");
		    room_create.setBounds(main_size[0] - 300, main_size[1] - 120, 90, 30);
		    room_create.setVisible(true);
		    container.add(room_create);
		    
		    container.repaint();
			break;
		}
	}

	private void start() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(null);
		setVisible(true);
	}
	
	public boolean canLogin() {
		if (this.identify == null || this.password == null)
			return false;
		
		if (identify.getText().length() == 0 || password.getText().length() == 0)
			return false;

		return true;
	}
	
	@Override
	public void run() {
		if (!valid) {
			JOptionPane.showMessageDialog(this, "ERROR:\nThis client is not connected with server!");
			System.out.println("...");
			return;
		}
		
		String str = null;
		
		if (!canLogin())
			return;
		
		Hashtable<String, String> table = new Hashtable<String, String>();
		table.put("ID", identify.getText());
		table.put("passwd", password.getText());
		
		str = Json_Controller.wrap("Login", Json_Controller.make(table));
		
		if (str == null) {
			JOptionPane.showMessageDialog(this, "ERROR:\nInvalid message tried to be sent.");
			return;
		}
		
		try {
			dataoutput.writeUTF(str);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		dataKeeper = new Hashtable<String, String>();
		
		boolean breaker = false;
		while (!breaker) {
			try {
				str = datainput.readUTF();
				
				JSONObject input_data = Json_Controller.parse(str);
				
				String Type = (String)input_data.get("Type");
				String Data = (String)input_data.get("Data");
				
				switch(Type) {
				case "Login_clear":
					userid = Integer.parseInt(Data);
					initialize(1);
					break;
				
				case "Login_fail":
					if (Data == null)
						JOptionPane.showMessageDialog(this, "Login failure for unknown error.");
					else
						JOptionPane.showMessageDialog(this, Data);
					breaker = true;
					break;
				
				case "Message":
					chat.actionPerformed(new ActionEvent(Data, 0, "Receive"));
					break;
				
				default:
					dataKeeper.put(Type, Data);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public String getData(String key) {
		if (dataKeeper == null)
			return null;
		
		var result = dataKeeper.get(key);
		if (result != null) {
			result = "" + result;
			dataKeeper.remove(key);
		}
		return result;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case "Login":
			Thread thread = new Thread(this); // run 함수 -> this
			thread.start();
			break;
		case "OpenChat":
			if (this.chat == null)
				return;
			
			if (this.chat.isVisible())
				return;
			
			this.chat.appear();
			break;
		case "SendChat":
			var text = ((JTextField)e.getSource()).getText();
			
			if (text.length() == 0)
				return;

			((JTextField)e.getSource()).setText("");
			var data = Json_Controller.wrap("Message", text);
			
			System.out.println(data);
			try {
				dataoutput.writeUTF(data);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			break;
		case "Room":
			
			break;
		case "Exit":
			System.exit(0);
			break;
		}
	}
}
