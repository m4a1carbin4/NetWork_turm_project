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
import javax.swing.SwingConstants;

import org.json.simple.JSONObject;

import GUI.ImagedButton9;
import GUI.ImagedLabel9;
import GUI.ListPanel;
import Json_Controller.Json_Controller;


public class MainFrame_re extends JFrame implements ActionListener, Runnable {
	//
	Container container = getContentPane();
	
	private JTextField identify;
	private JPasswordField password;
	
	private Chat chat;
	
	private ListPanel room_list;
	private ListPanel player_list;

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
	    
	    this.datainput = datainput;
		this.dataoutput = dataoutput;
	    initialize(1);
	    start();
	    valid = true;
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

		    var point = getLocation();
		    chat = new Chat(this, point.x + main_size[0] - 440, point.y + main_size[1] - 350, 400, 300);
		    chat.setVisible(false);
		    chat.setAlwaysOnTop(true);
		    //container.add(chat);

		    var exitbtn = new ImagedButton9(this, "Exit", "gui/imagedbutton9_red/button");
		    exitbtn.setBounds(main_size[0] - 130, main_size[1] - 120, 60, 30);
		    exitbtn.setVisible(true);
		    //container.add(exitbtn);
		    
		    var chatbtn = new ImagedButton9(this, "Chat", "gui/imagedbutton9/button", "OpenChat");
		    chatbtn.setBounds(main_size[0] - 200, main_size[1] - 120, 60, 30);
		    chatbtn.setVisible(true);
		    container.add(chatbtn);
		    
		    var room_list_label = new ImagedLabel9("Room List");
		    room_list_label.setTImage("gui/imagedlabel/label", true);
		    room_list_label.setBounds(50, 50, 100, 30);
		    room_list_label.setHorizontalAlignment(SwingConstants.CENTER);
		    container.add(room_list_label);
		    
		    String[] room_names = {"Room Name", "Participant Number", "Status"};
		    room_list = new ListPanel(this, room_names, 15);
		    room_list.setBounds(50, 90, 600, 520);
		    container.add(room_list);
		    
		    var player_list_label = new ImagedLabel9("Player List");
		    player_list_label.setTImage("gui/imagedlabel/label", true);
		    player_list_label.setBounds(670, 50, 100, 30);
		    player_list_label.setHorizontalAlignment(SwingConstants.CENTER);
		    container.add(player_list_label);
		    
		    String[] plist_attr = {"Player List"};
		    player_list = new ListPanel(this, plist_attr, 15);
		    player_list.setBounds(670, 90, 280, 520);
		    container.add(player_list);
		    
		    var room_create = new ImagedButton9(this, "Create Room", "gui/imagedbutton9/button", "Room");
		    room_create.setBounds(50, main_size[1] - 120, 90, 30);
		    room_create.setVisible(true);
		    container.add(room_create);
		    
		    var Groom_create = new ImagedButton9(this, "Create GRoom", "gui/imagedbutton9/button", "GRoom");
		    Groom_create.setBounds(150, main_size[1] - 120, 90, 30);
		    Groom_create.setVisible(true);
		    container.add(Groom_create);
		    
		    var room_join = new ImagedButton9(this, "Join Room", "gui/imagedbutton9/button", "JRoom");
		    room_join.setBounds(50, main_size[1] - 150, 90, 30);
		    room_join.setVisible(true);
		    container.add(room_join);
		    
		    var Groom_join = new ImagedButton9(this, "Join GRoom", "gui/imagedbutton9/button", "JGRoom");
		    Groom_join.setBounds(150, main_size[1] - 150, 90, 30);
		    Groom_join.setVisible(true);
		    container.add(Groom_join);
		    
		    var return_main = new ImagedButton9(this, "Retrun", "gui/imagedbutton9/button", "return");
		    return_main.setBounds(250, main_size[1] - 120, 90, 30);
		    return_main.setVisible(true);
		    container.add(return_main);
		    
			try {
				dataoutput.writeUTF(Json_Controller.wrap("Request.Room_List", ""));
				dataoutput.writeUTF(Json_Controller.wrap("Request.Player_List", ""));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		    
		    container.repaint();
		    
			break;
		case 2: // 
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
				case "MainFrame.Room_List":
					if (Data.length() != 0 && room_list != null) {
						var jobj = Json_Controller.parse(Data);
						if (jobj != null) {
							room_list.deleteAllItem();
							for (var sub_data : jobj.values()) {
								var jobje = Json_Controller.parse((String)sub_data);
								if (jobje != null) {
									String[] spec = new String[3];
									spec[0] = (String)jobje.get("name");
									spec[1] = "( " + String.valueOf((long)jobje.get("num")) + " / 4)";
									spec[2] = (String)jobje.get("status");
									if (spec != null && spec.length == room_list.getAttrSize())
										room_list.addItem(spec, "Lobby.JoinLoom " +  spec[0]);
								}
							}
						}
					}
					break;
					
				case "MainFrame.Player_List":
					if (Data.length() != 0 && player_list != null) {
						String[] spl = Data.split(";");
						if (spl != null) {
							player_list.deleteAllItem();
							for (int i = 0; i < spl.length; i++) {
								String[] spec = {spl[i]};
								player_list.addItem(spec, "Lobby.ShowPlayer " +  spl[i]);
							}
						}
					}
					break;
					
				case "MainFrame.Player_List.ShowData":
					if (Data.length() > 0) {
						var obj = Json_Controller.parse(Data);
						if (obj != null) {
							String text = "";
							if (obj.get("nname") != null)
								text += "Nickname: " + obj.get("nname") + "\n";
							if (obj.get("email") != null)
								text += "E-Mail: " + obj.get("email") + "\n";
							if (obj.get("psite") != null)
								text += "Nickname: " + obj.get("psite") + "\n";
							if (obj.get("con_date") != null)
								text += "Last Connection Date: " + obj.get("con_date") + "\n";
							if (obj.get("win") != null)
								text += "WIN: " + obj.get("win") + "\n";
							if (obj.get("loss") != null)
								text += "LOSS: " + obj.get("loss") + "\n";
							JOptionPane.showMessageDialog(null, text);
						}
					}
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
		
		var args = e.getActionCommand().split(" ");
		if (args != null && args.length >= 2) {
			String context = e.getActionCommand().substring(args[0].length() + 1);
			switch (args[0]) {
			case "Lobby.JoinLoom":
				try {
					dataoutput.writeUTF(Json_Controller.wrap("join_G_Room", context));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				dispose();
				break;
			case "Lobby.ShowPlayer":
				try {
					dataoutput.writeUTF(Json_Controller.wrap("Request.Player_Data", context));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				break;
			}
			return;
		}
		
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
