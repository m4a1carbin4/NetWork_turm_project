package Client;

import java.awt.EventQueue;
import javax.swing.JFrame;
import java.awt.FlowLayout;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.SystemColor;
import java.awt.Font;
import javax.swing.SwingConstants;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.awt.event.ActionEvent;

public class Gui_register {

	private JFrame frame1;
	private JTextField txtResisterAccount;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;
	private JTextField textField_6;
	private JTextField textField_7;
	
	private DataOutputStream dataoutput = null;
	private DataInputStream datainput = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Gui_register window = new Gui_register();
					window.frame1.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Gui_register() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame1 = new JFrame();
		frame1.getContentPane().setBackground(Color.WHITE);
		frame1.setBounds(100, 100, 712, 548);
		frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame1.getContentPane().setLayout(null);
		
		txtResisterAccount = new JTextField();
		txtResisterAccount.setForeground(Color.WHITE);
		txtResisterAccount.setBackground(Color.GRAY);
		txtResisterAccount.setHorizontalAlignment(SwingConstants.CENTER);
		txtResisterAccount.setFont(new Font("굴림", Font.BOLD, 30));
		txtResisterAccount.setText("Resister Account");
		txtResisterAccount.setBounds(151, 21, 403, 43);
		frame1.getContentPane().add(txtResisterAccount);
		txtResisterAccount.setColumns(10);
		
		JButton btnNewButton = new JButton("Resister");
		btnNewButton.setFont(new Font("굴림", Font.BOLD, 14));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String uid = textField.getText();
				String pw = textField_1.getText();
				String name = textField_6.getSelectedText();
				String nname = textField_4.getText();
				String email = textField_5.getText();
				String psite = textField_7.getSelectedText();
				
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
				
				try {
					dataoutput.writeUTF(obj2.toJSONString());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		btnNewButton.setBounds(530, 416, 97, 23);
		frame1.getContentPane().add(btnNewButton);
		
		JCheckBox chckbxNewCheckBox = new JCheckBox("Duplicate Check");
		chckbxNewCheckBox.setBackground(Color.LIGHT_GRAY);
		chckbxNewCheckBox.setFont(new Font("굴림", Font.PLAIN, 14));
		chckbxNewCheckBox.setBounds(498, 124, 133, 23);
		frame1.getContentPane().add(chckbxNewCheckBox);
		
		JLabel lblNewLabel = new JLabel("Username:");
		lblNewLabel.setFont(new Font("굴림", Font.BOLD, 15));
		lblNewLabel.setBounds(110, 109, 97, 53);
		frame1.getContentPane().add(lblNewLabel);
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setFont(new Font("굴림", Font.BOLD, 15));
		lblPassword.setBounds(110, 155, 97, 53);
		frame1.getContentPane().add(lblPassword);
		
		textField = new JTextField();
		textField.setBounds(204, 117, 286, 37);
		frame1.getContentPane().add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(204, 163, 286, 37);
		frame1.getContentPane().add(textField_1);
		
		JLabel lblPasswordCheck = new JLabel("Password Check:");
		lblPasswordCheck.setFont(new Font("굴림", Font.BOLD, 15));
		lblPasswordCheck.setBounds(61, 202, 158, 53);
		frame1.getContentPane().add(lblPasswordCheck);
		
		textField_2 = new JTextField();
		textField_2.setColumns(10);
		textField_2.setBounds(204, 210, 286, 37);
		frame1.getContentPane().add(textField_2);
		
		JCheckBox chckbxNewCheckBox_1 = new JCheckBox("Agree to provide information");
		chckbxNewCheckBox_1.setBackground(Color.LIGHT_GRAY);
		chckbxNewCheckBox_1.setFont(new Font("굴림", Font.PLAIN, 14));
		chckbxNewCheckBox_1.setBounds(238, 367, 235, 23);
		frame1.getContentPane().add(chckbxNewCheckBox_1);
		
		JLabel lblNickname = new JLabel("Nickname:");
		lblNickname.setFont(new Font("굴림", Font.BOLD, 15));
		lblNickname.setBounds(110, 246, 88, 53);
		frame1.getContentPane().add(lblNickname);
		
		textField_4 = new JTextField();
		textField_4.setColumns(10);
		textField_4.setBounds(204, 254, 286, 37);
		frame1.getContentPane().add(textField_4);
		
		JLabel lblEmail = new JLabel("Email:");
		lblEmail.setFont(new Font("굴림", Font.BOLD, 15));
		lblEmail.setBounds(143, 294, 55, 53);
		frame1.getContentPane().add(lblEmail);
		
		textField_5 = new JTextField();
		textField_5.setColumns(10);
		textField_5.setBounds(204, 302, 286, 37);
		frame1.getContentPane().add(textField_5);
		
		JCheckBox chckbxNewCheckBox_2 = new JCheckBox("Duplicate Check");
		chckbxNewCheckBox_2.setFont(new Font("굴림", Font.PLAIN, 14));
		chckbxNewCheckBox_2.setBackground(Color.LIGHT_GRAY);
		chckbxNewCheckBox_2.setBounds(498, 261, 133, 23);
		frame1.getContentPane().add(chckbxNewCheckBox_2);
		
		JCheckBox chckbxNewCheckBox_3 = new JCheckBox("Duplicate Check");
		chckbxNewCheckBox_3.setFont(new Font("굴림", Font.PLAIN, 14));
		chckbxNewCheckBox_3.setBackground(Color.LIGHT_GRAY);
		chckbxNewCheckBox_3.setBounds(498, 309, 133, 23);
		frame1.getContentPane().add(chckbxNewCheckBox_3);
		
		textField_3 = new JTextField();
		textField_3.setBackground(SystemColor.control);
		textField_3.setBounds(32, 64, 636, 413);
		frame1.getContentPane().add(textField_3);
		textField_3.setColumns(10);
		
		textField_6 = new JTextField();
		textField_6.setBounds(204, 354, 286, 37);
		frame1.getContentPane().add(textField_6);
		textField_6.setColumns(10);
		
		textField_7 = new JTextField();
		textField_7.setColumns(10);
		textField_7.setBounds(204, 396, 286, 37);
		frame1.getContentPane().add(textField_7);
		
		JLabel lblNewLabel_2 = new JLabel("PSite:");
		lblNewLabel_2.setFont(new Font("굴림", Font.BOLD, 15));
		lblNewLabel_2.setBounds(145, 410, 46, 15);
		frame1.getContentPane().add(lblNewLabel_2);
		
		JLabel lblNewLabel_1 = new JLabel("Name:");
		lblNewLabel_1.setFont(new Font("굴림", Font.BOLD, 15));
		lblNewLabel_1.setBounds(143, 365, 57, 15);
		frame1.getContentPane().add(lblNewLabel_1);
		
		
		//JDBC 파트와 연동 위해서 사용자 이름 그리고 개인 사이트 정보도 받을수 있게 내용추가 부탁드림.
		//현재 서버단에서 중복체크 기능 지원이 없기에 이 부분 관련 처리는 따로 제가 처리하겠습니다.
	}
	
}