package Client;

import java.awt.Toolkit;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Color;

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
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import Json_Controller.Json_Controller;


public class MainFrame extends JFrame implements ActionListener, Runnable {
	//
	Container container = getContentPane();
	
	private JTextField identify;
	private JPasswordField password;
	
	// register
	private JTextField textField_3;
	private JTextField textField_8;
	private JTextField textField_9;
	private JTextField textField_10;
	private JTextField textField_11;
	private JTextField textField_12;
	private JTextField textField_13;
	private JTextField textField_14;
	
	// find password
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	
	private Chat chat;

	private int userid = -1;
	private boolean valid = false;
	
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
		
		// find password 패널 추가
		JPanel findpassPanel = new JPanel();
		findpassPanel.setBounds(46, 40, 437, 227);
		container.add(panel);
		findpassPanel.setLayout(null);
		findpassPanel.setVisible(false);
		
		
		// register 패널 추가 (보였다 안보였다 하기)
		JPanel registerPage = new JPanel();
		registerPage.setBackground(Color.WHITE);
		registerPage.setBounds(0, 0, 859, 612);
		container.add(registerPage);
		registerPage.setLayout(null);
		registerPage.setVisible(false);
		
		textField_3 = new JTextField();
		textField_3.setText("Resister Account");
		textField_3.setHorizontalAlignment(SwingConstants.CENTER);
		textField_3.setForeground(Color.WHITE);
		textField_3.setFont(new Font("굴림", Font.BOLD, 30));
		textField_3.setColumns(10);
		textField_3.setBackground(Color.GRAY);
		textField_3.setBounds(120, 0, 403, 43);
		panel.add(textField_3);
		
		JCheckBox chckbxNewCheckBox_4 = new JCheckBox("Duplicate Check");
		chckbxNewCheckBox_4.setFont(new Font("굴림", Font.PLAIN, 14));
		chckbxNewCheckBox_4.setBackground(Color.LIGHT_GRAY);
		chckbxNewCheckBox_4.setBounds(467, 61, 133, 23);
		panel.add(chckbxNewCheckBox_4);
		
		JLabel lblNewLabel_3 = new JLabel("Username:");
		lblNewLabel_3.setFont(new Font("굴림", Font.BOLD, 15));
		lblNewLabel_3.setBounds(79, 46, 97, 53);
		panel.add(lblNewLabel_3);
		
		JLabel lblPassword_1 = new JLabel("Password:");
		lblPassword_1.setFont(new Font("굴림", Font.BOLD, 15));
		lblPassword_1.setBounds(79, 92, 97, 53);
		panel.add(lblPassword_1);
		
		textField_8 = new JTextField();
		textField_8.setColumns(10);
		textField_8.setBounds(173, 54, 286, 37);
		panel.add(textField_8);
		
		textField_9 = new JTextField();
		textField_9.setColumns(10);
		textField_9.setBounds(173, 100, 286, 37);
		panel.add(textField_9);
		
		JLabel lblPasswordCheck_1 = new JLabel("Password Check:");
		lblPasswordCheck_1.setFont(new Font("굴림", Font.BOLD, 15));
		lblPasswordCheck_1.setBounds(30, 139, 158, 53);
		panel.add(lblPasswordCheck_1);
		
		textField_10 = new JTextField();
		textField_10.setColumns(10);
		textField_10.setBounds(173, 147, 286, 37);
		panel.add(textField_10);
		
		JCheckBox chckbxNewCheckBox_1_1 = new JCheckBox("Agree to provide information");
		chckbxNewCheckBox_1_1.setFont(new Font("굴림", Font.PLAIN, 14));
		chckbxNewCheckBox_1_1.setBackground(Color.LIGHT_GRAY);
		chckbxNewCheckBox_1_1.setBounds(207, 380, 235, 23);
		panel.add(chckbxNewCheckBox_1_1);
		
		JLabel lblNickname_1 = new JLabel("Nickname:");
		lblNickname_1.setFont(new Font("굴림", Font.BOLD, 15));
		lblNickname_1.setBounds(79, 183, 88, 53);
		panel.add(lblNickname_1);
		
		textField_11 = new JTextField();
		textField_11.setColumns(10);
		textField_11.setBounds(173, 191, 286, 37);
		panel.add(textField_11);
		
		JLabel lblEmail_1 = new JLabel("Email:");
		lblEmail_1.setFont(new Font("굴림", Font.BOLD, 15));
		lblEmail_1.setBounds(112, 231, 55, 53);
		panel.add(lblEmail_1);
		
		textField_12 = new JTextField();
		textField_12.setColumns(10);
		textField_12.setBounds(173, 239, 286, 37);
		panel.add(textField_12);
		
		JCheckBox chckbxNewCheckBox_2_1 = new JCheckBox("Duplicate Check");
		chckbxNewCheckBox_2_1.setFont(new Font("굴림", Font.PLAIN, 14));
		chckbxNewCheckBox_2_1.setBackground(Color.LIGHT_GRAY);
		chckbxNewCheckBox_2_1.setBounds(467, 198, 133, 23);
		panel.add(chckbxNewCheckBox_2_1);
		
		JCheckBox chckbxNewCheckBox_3_1 = new JCheckBox("Duplicate Check");
		chckbxNewCheckBox_3_1.setFont(new Font("굴림", Font.PLAIN, 14));
		chckbxNewCheckBox_3_1.setBackground(Color.LIGHT_GRAY);
		chckbxNewCheckBox_3_1.setBounds(467, 246, 133, 23);
		panel.add(chckbxNewCheckBox_3_1);
		
		textField_13 = new JTextField();
		textField_13.setColumns(10);
		textField_13.setBounds(173, 291, 286, 37);
		panel.add(textField_13);
		
		textField_14 = new JTextField();
		textField_14.setColumns(10);
		textField_14.setBounds(173, 333, 286, 37);
		panel.add(textField_14);
		
		JLabel lblNewLabel_2_1 = new JLabel("PSite:");
		lblNewLabel_2_1.setFont(new Font("굴림", Font.BOLD, 15));
		lblNewLabel_2_1.setBounds(114, 347, 46, 15);
		panel.add(lblNewLabel_2_1);
		
		JLabel lblNewLabel_1_1 = new JLabel("Name:");
		lblNewLabel_1_1.setFont(new Font("굴림", Font.BOLD, 15));
		lblNewLabel_1_1.setBounds(112, 302, 57, 15);
		panel.add(lblNewLabel_1_1);
		
		JButton btnNewButton_1 = new JButton("Resister");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String uid = textField_8.getText();
				String pw = textField_9.getText();
				String name = textField_13.getSelectedText();
				String nname = textField_11.getText();
				String email = textField_12.getText();
				String psite = textField_14.getSelectedText();
				
				JSONObject obj = new JSONObject();
				
				obj.put("ID",uid); 
				obj.put("passwd",pw);
				obj.put("Name", name);
				obj.put("NickName",nname); 
				obj.put("EMail",email);
				obj.put("PSite", psite);
				
				JSONObject obj2 = new JSONObject();
				
				obj2.put("Type","Register");
				obj2.put("Data", obj.toJSONString());
				
				registerPage.setVisible(false);
				container.setVisible(true);
				
				try {
					dataoutput.writeUTF(obj2.toJSONString());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnNewButton_1.setFont(new Font("굴림", Font.BOLD, 14));
		btnNewButton_1.setBounds(503, 369, 97, 23);
		panel.add(btnNewButton_1);
		
		// find password
		JLabel lblNewLabel = new JLabel("ID:");
		lblNewLabel.setFont(new Font("굴림", Font.BOLD, 15));
		lblNewLabel.setBounds(104, 34, 29, 37);
		panel.add(lblNewLabel);
		
		JLabel lblEmail = new JLabel("Email:");
		lblEmail.setFont(new Font("굴림", Font.BOLD, 15));
		lblEmail.setBounds(79, 84, 54, 37);
		panel.add(lblEmail);
		
		JLabel lblNickname = new JLabel("NickName:");
		lblNickname.setFont(new Font("굴림", Font.BOLD, 15));
		lblNickname.setBounds(48, 135, 85, 37);
		panel.add(lblNickname);
		
		textField = new JTextField();
		textField.setBounds(133, 34, 196, 29);
		panel.add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(133, 84, 196, 29);
		panel.add(textField_1);
		
		textField_2 = new JTextField();
		textField_2.setColumns(10);
		textField_2.setBounds(133, 135, 196, 29);
		panel.add(textField_2);
		
		JButton btnNewButton = new JButton("Find");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String ID = textField.getText();
				String Email = textField_1.getText();
				String Nname = textField_2.getText();
				
				// db상의 ID,email,nickname과 비교 해서 같으면 비밀번호 출력
				//if(ID.equals(txtID.getText())&&Email.equals(loginPasswordField.getText())
				//		&&Nname.equals(e)))
		{
			//JOptionPane.showMessageDialog(null, "Password: "+txtID.getText());
		}
		findpassPanel.setVisible(false);
		container.setVisible(true);
			}
		});
		btnNewButton.setFont(new Font("굴림", Font.BOLD, 15));
		btnNewButton.setBounds(328, 194, 97, 23);
		panel.add(btnNewButton);
		
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
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				registerPage.setVisible(true);
				container.setVisible(false);
			}
		});
		container.add(btn);
		
		btn = new JButton("Register");
		btn.setBounds(x + 200, y + 80, 180, 20);
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				findpassPanel.setVisible(true);
				container.setVisible(false);
			}
		});
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
		    
		    JButton chatbtn = new JButton("Chat");
		    chatbtn.setBounds(main_size[0] - 80, main_size[1] - 70, 60, 30);
		    chatbtn.addActionListener(this);
		    chatbtn.setVisible(true);
		    container.add(chatbtn);
		    
		    chat = new Chat(this, main_size[0] - 440, main_size[1] - 350, 400, 300);
		    chat.setVisible(false);
		    container.add(chat);
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
		
		while(true) {
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
					break;
				
				case "Message":
					chat.actionPerformed(new ActionEvent(Data, 0, "Receive"));
					break;
						
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().getClass() == JButton.class) {
			if (e.getActionCommand().contentEquals("Login")) {
				Thread thread = new Thread(this); // run 함수 -> this
				thread.start();
			}
			
			if (e.getActionCommand().contentEquals("Chat")) {
				if (this.chat == null)
					return;
				
				if (this.chat.isVisible())
					return;
				
				this.chat.appear();
			}
		}
		
		if (e.getSource().getClass() == JTextField.class) {
			if (e.getActionCommand().contentEquals("SendChat")) {
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
			}
		}
	}
}
