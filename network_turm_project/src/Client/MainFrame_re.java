package Client;

import java.awt.Toolkit;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import java.net.Socket;
import java.util.Hashtable;

import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.json.simple.JSONObject;

import GUI.ImagedButton9;
import Json_Controller.Json_Controller;


public class MainFrame_re extends JFrame implements ActionListener, Runnable {
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
	
	public MainFrame_re(DataOutputStream dataoutput,DataInputStream datainput) {
		setTitle("Title");
		
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
	    var resolution = Toolkit.getDefaultToolkit().getScreenSize();
	    setLocation((resolution.width - login_size[0]) / 2, (resolution.height - login_size[1]) / 2);
	    setSize(login_size[0], login_size[1]);
	    
	    initialize(1);
	    start();
	    valid = true;
	    this.datainput = datainput;
		this.dataoutput = dataoutput;
		System.out.println("status = " + "return");
		
		in = new BufferedReader(new InputStreamReader(System.in));
		
		Thread thread = new Thread(this); // run 함수 -> this
		thread.start();
		
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
		    
		    var Groom_create = new ImagedButton9(this, "Create GRoom", "gui/imagedbutton9/button", "GRoom");
		    Groom_create.setBounds(main_size[0] - 400, main_size[1] - 120, 90, 30);
		    Groom_create.setVisible(true);
		    container.add(Groom_create);
		    
		    var room_join = new ImagedButton9(this, "Join Room", "gui/imagedbutton9/button", "JRoom");
		    room_join.setBounds(main_size[0] - 300, main_size[1] - 150, 90, 30);
		    room_join.setVisible(true);
		    container.add(room_join);
		    
		    var Groom_join = new ImagedButton9(this, "Join GRoom", "gui/imagedbutton9/button", "JGRoom");
		    Groom_join.setBounds(main_size[0] - 400, main_size[1] - 150, 90, 30);
		    Groom_join.setVisible(true);
		    container.add(Groom_join);
		    
		    var return_main = new ImagedButton9(this, "Retrun", "gui/imagedbutton9/button", "return");
		    return_main.setBounds(main_size[0] - 500, main_size[1] - 120, 90, 30);
		    return_main.setVisible(true);
		    container.add(return_main);
		    
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
		String str = null;
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
					
				case "R_clear":
					System.out.println(Data);
					break;
					
				case "R_fail":
					JOptionPane.showMessageDialog(this, "Room make fail.");
					break;
					
				case "R_G_clear":
					System.out.println(Data);
					new LobbyFrame(datainput, dataoutput,userid);
					dispose();
					return;
					
				case "R_G_fail":
					JOptionPane.showMessageDialog(this, "Room make fail.");
					break;
				
				case "return_clear":
					System.out.println(Data);
					break;
					
				case "return_fail":
					JOptionPane.showMessageDialog(this, "return fail.");
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
			
			String room_name=JOptionPane.showInputDialog("룸 이름 입력.");
			
			var data1 = Json_Controller.wrap("new_Room", room_name);
			
			System.out.println(data1);
			try {
				dataoutput.writeUTF(data1);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
	
			break;
			
		case "GRoom":
			String room_name1=JOptionPane.showInputDialog("룸 이름 입력.");
			
			var data11 = Json_Controller.wrap("new_G_Room", room_name1);
			
			System.out.println(data11);
			try {
				dataoutput.writeUTF(data11);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			break;
		case "JRoom":
			
			String room_name2=JOptionPane.showInputDialog("룸 이름 입력.");
			
			var data2 = Json_Controller.wrap("join_Room", room_name2);
			
			System.out.println(data2);
			try {
				dataoutput.writeUTF(data2);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			break;
			
		case "JGRoom":
			String room_name3=JOptionPane.showInputDialog("룸 이름 입력.");
			
			var data22 = Json_Controller.wrap("join_G_Room", room_name3);
			
			System.out.println(data22);
			try {
				dataoutput.writeUTF(data22);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			dispose();
			break;
		case "return":
			var return_key = Json_Controller.wrap("return","return_request");
			
			System.out.println(return_key);
			try {
				dataoutput.writeUTF(return_key);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			break;
		case "Exit":
			System.exit(0);
			break;
		}
		
	}
}
