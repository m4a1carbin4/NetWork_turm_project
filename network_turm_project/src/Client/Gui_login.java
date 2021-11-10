package Client;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JComboBox;
import java.awt.BorderLayout;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JFormattedTextField;
import javax.swing.SwingConstants;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.awt.SystemColor;
import javax.swing.JLabel;


public class Gui_login {

	private JFrame frame;
	private JFormattedTextField textField;
	private JTextField textField_1;
	private JTextField txtAlpha;
	private JTextField textField_3;
	private JTextField textField_4;
	private JButton btnNewButton_1;
	private JButton btnNewButton_2;
	
	Socket socket;
	InputStream input = null;
	OutputStream output = null;
	DataInputStream datainput = null;
	DataOutputStream dataoutput = null;
	
	JSONParser parser = new JSONParser();
	
	String str;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Gui_login window = new Gui_login();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @throws UnknownHostException 
	 */
	public Gui_login() throws UnknownHostException {
		
		InetAddress ia = InetAddress.getLocalHost();
		String ip_str = ia.toString();
		String ip = ip_str.substring(ip_str.indexOf("/") + 1);
		
		initNet(ip,9647);
		initialize();
	}
	
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
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.WHITE);
		frame.setBounds(100, 100, 698, 427);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		textField = new JFormattedTextField();
		textField.setForeground(Color.WHITE);
		textField.setHorizontalAlignment(SwingConstants.CENTER);
		textField.setFont(new Font("굴림", Font.BOLD, 20));
		textField.setText("Login");
		textField.setBackground(Color.GRAY);
		textField.setBounds(58, 30, 101, 33);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		txtAlpha = new JTextField();
		txtAlpha.setForeground(Color.WHITE);
		txtAlpha.setHorizontalAlignment(SwingConstants.CENTER);
		txtAlpha.setFont(new Font("굴림", Font.BOLD, 20));
		txtAlpha.setText("Alpha");
		txtAlpha.setBackground(Color.GRAY);
		txtAlpha.setBounds(582, 62, 76, 33);
		frame.getContentPane().add(txtAlpha);
		txtAlpha.setColumns(10);
		
		JButton btnNewButton = new JButton("Login");
		btnNewButton.setFont(new Font("굴림", Font.BOLD, 16));
		btnNewButton.setBackground(Color.BLACK);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String ID = textField_3.getText();
				String passwd = textField_4.getText();
				
				String Login = Json_maker(Login_maker(passwd,ID),"Login");
				
				try {
					dataoutput.writeUTF(Login);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				try {
					str = datainput.readUTF();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				JSONObject input_data = Json_parser(str);
				
				String Type = (String) input_data.get("Type");
				String Data = (String) input_data.get("Data");
				
				switch(Type) {
				
					case "Login_fail":
						System.out.println("Wrong id or password!");
						break;
					case "Login_clear":
						System.out.println("Login_clear!!");
						//do something in next;
						break;
				}
				
			}
		});
		btnNewButton.setBounds(504, 228, 83, 33);
		frame.getContentPane().add(btnNewButton);
		
		textField_3 = new JTextField();
		textField_3.setBounds(217, 139, 253, 33);
		frame.getContentPane().add(textField_3);
		textField_3.setColumns(10);
		
		textField_4 = new JTextField();
		textField_4.setColumns(10);
		textField_4.setBounds(217, 190, 253, 33);
		frame.getContentPane().add(textField_4);
		
		btnNewButton_1 = new JButton("Click here to register");
		btnNewButton_1.setBounds(394, 291, 196, 23);
		frame.getContentPane().add(btnNewButton_1);
		
		btnNewButton_2 = new JButton("Forgot your password?");
		btnNewButton_2.setFont(new Font("Gulim", Font.PLAIN, 12));
		btnNewButton_2.setBounds(108, 291, 180, 23);
		frame.getContentPane().add(btnNewButton_2);
		
		JLabel lblNewLabel = new JLabel("ID:");
		lblNewLabel.setFont(new Font("굴림", Font.BOLD, 15));
		lblNewLabel.setBounds(171, 148, 43, 15);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Password:");
		lblNewLabel_1.setFont(new Font("굴림", Font.BOLD, 15));
		lblNewLabel_1.setBounds(116, 205, 92, 15);
		frame.getContentPane().add(lblNewLabel_1);
		
		textField_1 = new JTextField();
		textField_1.setBackground(SystemColor.control);
		textField_1.setBounds(43, 62, 615, 289);
		frame.getContentPane().add(textField_1);
		textField_1.setColumns(10);
	}

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
	
	public String Login_maker(String passwd,String ID) {
		
		JSONObject object = new JSONObject();
		
		object.put("ID", ID);
		object.put("passwd", passwd);
		
		System.out.println("new json_login data");
		
		return object.toJSONString();
	}
	
}
