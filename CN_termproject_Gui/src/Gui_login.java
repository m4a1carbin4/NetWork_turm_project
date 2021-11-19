import java.awt.EventQueue;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JComboBox;
import java.awt.BorderLayout;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.DriverManager;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JFormattedTextField;
import javax.swing.SwingConstants;

import com.sun.jdi.connect.spi.Connection;

import java.awt.SystemColor;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import javax.swing.JPasswordField;


public class Gui_login {

	private JFrame frame;
	// login
	private JFormattedTextField textField_login;
	private JTextField textField_loginbox;
	private JTextField txtAlpha;
	private JTextField txtID;
	private JPasswordField loginPasswordField;
	private JButton registerBtn;
	private JButton btnNewButton_2;
	
	//register
	private JTextField txtResisterAccount;
	private JTextField registerTxtUsername;
	private JTextField registerFrame;
	private JTextField registerTxtNickname;
	private JTextField registerTxtEmail;
	private JPasswordField passwordField;
	private JPasswordField passwordCheckField;

	// chat
	private JButton registerBackBtn;
	private JButton chatBtnPrivateChat_1;
	private JTextField chatSerMenu;
	private JButton chatBtnServerChat_1;
	private JTextField chatSerUserBox;
	private JTextField chatSerMessageBox;
	private JTextField chatSerSendMsg;
	private JButton chatSerBtnSend;
	private JButton chatLogoutBtn;
	private JButton chatLogoutBtn_1;
	private JButton chatBtnServerChat_2;
	private JButton chatBtnPrivateChat_2;
	private JTextField chatPriMenu;
	private JTextField chatPriUserBox;
	private JTextField chatPriMessageBox;
	private JTextField chatPriSendMsg;
	private JButton chatPriBtnSend;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		getConnection();
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
	
	// ¼­¹ö ¿¬°á
	public static Connection getConnection() {
		try {
			String driver="";
			String url="";
			String user="";
			String pass="";
			Class.forName(driver);
			Connection con = (Connection) DriverManager.getConnection(url,user,pass);
			System.out.println("The connection Successful");
			return con;
		}catch(Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	/**
	 * Create the application.
	 */
	public Gui_login() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.WHITE);
		frame.setBounds(100, 100, 875, 651);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		
		// JPanel
		//login panel initialize
		JPanel loginPage = new JPanel();
		loginPage.setBackground(Color.WHITE);
		loginPage.setBounds(0, 0, 859, 612);
		frame.getContentPane().add(loginPage);
		loginPage.setLayout(null);
		
		//register panel initialize
		JPanel registerPage = new JPanel();
		registerPage.setBackground(Color.WHITE);
		registerPage.setBounds(0, 0, 859, 612);
		frame.getContentPane().add(registerPage);
		registerPage.setLayout(null);
		
		//SeverChat panel initialize
		JPanel chatPageServerChat = new JPanel();
		chatPageServerChat.setBackground(Color.WHITE);
		chatPageServerChat.setBounds(0, 0, 859, 612);
		frame.getContentPane().add(chatPageServerChat);
		chatPageServerChat.setLayout(null);
		
		//Private panel initialize
		JPanel chatPagePrivateChat = new JPanel();
		chatPagePrivateChat.setBackground(Color.WHITE);
		chatPagePrivateChat.setBounds(0, 0, 859, 612);
		frame.getContentPane().add(chatPagePrivateChat);
		chatPagePrivateChat.setLayout(null);
		
		//hide other panel without login panel
		registerPage.setVisible(false);
		chatPageServerChat.setVisible(false);
		chatPagePrivateChat.setVisible(false);
		
		
		//login panel
		textField_login = new JFormattedTextField();
		textField_login.setBounds(138, 121, 101, 33);
		textField_login.setForeground(Color.WHITE);
		textField_login.setHorizontalAlignment(SwingConstants.CENTER);
		textField_login.setFont(new Font("±¼¸²", Font.BOLD, 20));
		textField_login.setText("Login");
		textField_login.setBackground(Color.GRAY);
		loginPage.add(textField_login);
		textField_login.setColumns(10);
		
		txtAlpha = new JTextField();
		txtAlpha.setBounds(662, 153, 76, 33);
		txtAlpha.setForeground(Color.WHITE);
		txtAlpha.setHorizontalAlignment(SwingConstants.CENTER);
		txtAlpha.setFont(new Font("±¼¸²", Font.BOLD, 20));
		txtAlpha.setText("Alpha");
		txtAlpha.setBackground(Color.GRAY);
		loginPage.add(txtAlpha);
		txtAlpha.setColumns(10);
		
		txtID = new JTextField();
		txtID.setBounds(297, 230, 253, 33);
		loginPage.add(txtID);
		txtID.setColumns(10);
		
		loginPasswordField = new JPasswordField();
		loginPasswordField.setBounds(297, 281, 253, 33);
		loginPage.add(loginPasswordField);
		
		JButton logBtn = new JButton("Login");
		logBtn.setBounds(584, 319, 83, 33);
		logBtn.setFont(new Font("±¼¸²", Font.BOLD, 16));
		logBtn.setBackground(Color.LIGHT_GRAY);
		logBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// ·Î±×ÀÎ Á¤º¸ DB¿¡¼­ ºÒ·¯¿Ã ³»¿ë ÀÓÀÇ·Î ¼³Á¤
				String id="User";
				String pass = "1234";
				
				// txtID=ÅØ½ºÆ®Ã¢¿¡ ÀÔ·ÂµÈ ID, txtPass = ÅØ½ºÆ®Ã¢¿¡ ÀÔ·ÂµÈ Password
				if(id.equals(txtID.getText())&&pass.equals(loginPasswordField.getText())) {
					JOptionPane.showMessageDialog(null, "Welcome "+txtID.getText());
					loginPage.setVisible(false);
					chatPageServerChat.setVisible(true);
				}else {
					JOptionPane.showMessageDialog(null, "Log in failed");
				}
			}
		});
		loginPage.add(logBtn);
		
		registerBtn = new JButton("Click here to register");
		registerBtn.setFont(new Font("±¼¸²", Font.BOLD, 12));
		registerBtn.setBounds(474, 382, 196, 23);
		registerBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				registerPage.setVisible(true);
				loginPage.setVisible(false);
			}
		});
		loginPage.add(registerBtn);
		
		btnNewButton_2 = new JButton("Forgot your password?");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Please contact addministor\n\n "
						+ "or login ID: User, Passworod: 1234");
			}
		});
		btnNewButton_2.setBounds(188, 382, 180, 23);
		btnNewButton_2.setFont(new Font("±¼¸²", Font.BOLD, 12));
		loginPage.add(btnNewButton_2);
		
		JLabel lblNewLabel = new JLabel("ID:");
		lblNewLabel.setBounds(251, 239, 43, 15);
		lblNewLabel.setFont(new Font("±¼¸²", Font.BOLD, 15));
		loginPage.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Password:");
		lblNewLabel_1.setBounds(196, 296, 92, 15);
		lblNewLabel_1.setFont(new Font("±¼¸²", Font.BOLD, 15));
		loginPage.add(lblNewLabel_1);
		
		textField_loginbox = new JTextField();
		textField_loginbox.setBounds(123, 153, 615, 289);
		textField_loginbox.setBackground(SystemColor.control);
		loginPage.add(textField_loginbox);
		textField_loginbox.setColumns(10);
		
		
		// register page
		txtResisterAccount = new JTextField();
		txtResisterAccount.setBounds(252, 23, 276, 41);
		txtResisterAccount.setForeground(Color.WHITE);
		txtResisterAccount.setBackground(Color.GRAY);
		txtResisterAccount.setHorizontalAlignment(SwingConstants.CENTER);
		txtResisterAccount.setFont(new Font("±¼¸²", Font.BOLD, 30));
		txtResisterAccount.setText("Resister Account");
		registerPage.add(txtResisterAccount);
		txtResisterAccount.setColumns(10);
		
		JLabel registerLabelUsername = new JLabel("Username:");
		registerLabelUsername.setBounds(227, 189, 81, 18);
		registerLabelUsername.setFont(new Font("±¼¸²", Font.BOLD, 15));
		registerPage.add(registerLabelUsername);
		
		JLabel registerLabelPassword = new JLabel("Password:");
		registerLabelPassword.setBounds(227, 236, 77, 18);
		registerLabelPassword.setFont(new Font("±¼¸²", Font.BOLD, 15));
		registerPage.add(registerLabelPassword);
		
		registerTxtUsername = new JTextField();
		registerTxtUsername.setBounds(314, 185, 171, 28);
		registerPage.add(registerTxtUsername);
		registerTxtUsername.setColumns(10);
		
		JLabel registerLabelPasswordCheck = new JLabel("Password Check:");
		registerLabelPasswordCheck.setBounds(177, 280, 131, 18);
		registerLabelPasswordCheck.setFont(new Font("±¼¸²", Font.BOLD, 15));
		registerPage.add(registerLabelPasswordCheck);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(314, 232, 171, 28);
		registerPage.add(passwordField);
		
		passwordCheckField = new JPasswordField();
		passwordCheckField.setBounds(314, 277, 171, 28);
		registerPage.add(passwordCheckField);
		
		JCheckBox registerCheckboxAgree = new JCheckBox("Agree to provide information");
		registerCheckboxAgree.setBounds(293, 420, 209, 25);
		registerCheckboxAgree.setBackground(Color.LIGHT_GRAY);
		registerCheckboxAgree.setFont(new Font("±¼¸²", Font.PLAIN, 14));
		registerPage.add(registerCheckboxAgree);
		
		JLabel registerLableNickname = new JLabel("Nickname:");
		registerLableNickname.setBounds(229, 320, 79, 18);
		registerLableNickname.setFont(new Font("±¼¸²", Font.BOLD, 15));
		registerPage.add(registerLableNickname);
		
		registerTxtNickname = new JTextField();
		registerTxtNickname.setBounds(314, 316, 171, 27);
		registerTxtNickname.setColumns(10);
		registerPage.add(registerTxtNickname);
		
		JLabel registerLableEmail = new JLabel("Email:");
		registerLableEmail.setBounds(263, 363, 45, 18);
		registerLableEmail.setFont(new Font("±¼¸²", Font.BOLD, 15));
		registerPage.add(registerLableEmail);
		
		registerTxtEmail = new JTextField();
		registerTxtEmail.setBounds(314, 359, 171, 28);
		registerTxtEmail.setColumns(10);
		registerPage.add(registerTxtEmail);
		
		registerBackBtn = new JButton("Back");
		registerBackBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				registerPage.setVisible(false);
				loginPage.setVisible(true);
			}
		});
		registerBackBtn.setFont(new Font("±¼¸²", Font.BOLD, 14));
		registerBackBtn.setBounds(211, 470, 97, 23);
		registerPage.add(registerBackBtn);
		
		JButton registerBtn_1 = new JButton("Resister");
		registerBtn_1.setBounds(533, 469, 108, 25);
		registerBtn_1.setFont(new Font("±¼¸²", Font.BOLD, 14));
		registerBtn_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean agreeCheckBox=registerCheckboxAgree.isSelected();
				if(agreeCheckBox==false) {
					JOptionPane.showMessageDialog(null, "Please check the Agree to provide information");
				}
				else if(!(passwordField.getText().equals(passwordCheckField.getText()))){
					JOptionPane.showMessageDialog(null, "Please check the password");
				}
				else if((registerTxtUsername.getText().equals(""))||
						(registerTxtEmail.getText().equals(""))||
						(registerTxtNickname.getText().equals(""))) {
					JOptionPane.showMessageDialog(null, "Please enter it without any blanks.");
				}
				
				else {
					JOptionPane.showMessageDialog(null, "Register success");
					registerPage.setVisible(false);
					loginPage.setVisible(true);
				}
				
			}
		});
		registerPage.add(registerBtn_1);
		
		registerFrame = new JTextField();
		registerFrame.setBounds(93, 64, 620, 482);
		registerFrame.setBackground(SystemColor.control);
		registerPage.add(registerFrame);
		registerFrame.setColumns(10);
		
		
		// SeverChat panel
		chatLogoutBtn = new JButton("LogOut");
		chatLogoutBtn.setBackground(Color.LIGHT_GRAY);
		chatLogoutBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chatPageServerChat.setVisible(false);
				loginPage.setVisible(true);
			}
		});
		chatLogoutBtn.setFont(new Font("±¼¸²", Font.BOLD, 13));
		chatLogoutBtn.setBounds(1, 25, 116, 23);
		chatPageServerChat.add(chatLogoutBtn);
		
		chatBtnServerChat_1 = new JButton("Server Chat");
		chatBtnServerChat_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chatPagePrivateChat.setVisible(false);
				chatPageServerChat.setVisible(true);
			}
		});
		chatBtnServerChat_1.setForeground(Color.WHITE);
		chatBtnServerChat_1.setBackground(Color.DARK_GRAY);
		chatBtnServerChat_1.setFont(new Font("±¼¸²", Font.BOLD, 13));
		chatBtnServerChat_1.setBounds(1, 80, 116, 23);
		chatPageServerChat.add(chatBtnServerChat_1);
		
		chatBtnPrivateChat_1 = new JButton("Private Chat");
		chatBtnPrivateChat_1.setBackground(Color.WHITE);
		chatBtnPrivateChat_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chatPagePrivateChat.setVisible(true);
				chatPageServerChat.setVisible(false);
			}
		});
		
		
		//PrivateChat panel
		chatBtnPrivateChat_1.setFont(new Font("±¼¸²", Font.BOLD, 13));
		chatBtnPrivateChat_1.setBounds(1, 131, 116, 23);
		chatPageServerChat.add(chatBtnPrivateChat_1);
		
		chatSerMenu = new JTextField();
		chatSerMenu.setColumns(10);
		chatSerMenu.setBackground(SystemColor.activeCaption);
		chatSerMenu.setBounds(1, 0, 116, 208);
		chatPageServerChat.add(chatSerMenu);
		
		chatSerUserBox = new JTextField();
		chatSerUserBox.setColumns(10);
		chatSerUserBox.setBackground(SystemColor.inactiveCaption);
		chatSerUserBox.setBounds(1, 206, 116, 405);
		chatPageServerChat.add(chatSerUserBox);
		
		chatSerMessageBox = new JTextField();
		chatSerMessageBox.setColumns(10);
		chatSerMessageBox.setBackground(SystemColor.inactiveCaptionBorder);
		chatSerMessageBox.setBounds(216, 43, 512, 420);
		chatPageServerChat.add(chatSerMessageBox);
		
		chatSerSendMsg = new JTextField();
		chatSerSendMsg.setColumns(10);
		chatSerSendMsg.setBounds(248, 505, 373, 36);
		chatPageServerChat.add(chatSerSendMsg);
		
		chatSerBtnSend = new JButton("Send");
		chatSerBtnSend.setFont(new Font("±¼¸²", Font.BOLD, 14));
		chatSerBtnSend.setBounds(631, 511, 97, 23);
		chatPageServerChat.add(chatSerBtnSend);
		chatPageServerChat.setVisible(false);
		
		chatLogoutBtn_1 = new JButton("LogOut");
		chatLogoutBtn_1.setFont(new Font("±¼¸²", Font.BOLD, 13));
		chatLogoutBtn_1.setBackground(Color.LIGHT_GRAY);
		chatLogoutBtn_1.setBounds(1, 25, 116, 23);
		chatPagePrivateChat.add(chatLogoutBtn_1);
		
		chatBtnServerChat_2 = new JButton("Server Chat");
		chatBtnServerChat_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chatPagePrivateChat.setVisible(false);
				chatPageServerChat.setVisible(true);
			}
		});
		chatBtnServerChat_2.setForeground(Color.BLACK);
		chatBtnServerChat_2.setFont(new Font("±¼¸²", Font.BOLD, 13));
		chatBtnServerChat_2.setBackground(Color.WHITE);
		chatBtnServerChat_2.setBounds(1, 80, 116, 23);
		chatPagePrivateChat.add(chatBtnServerChat_2);
		
		chatBtnPrivateChat_2 = new JButton("Private Chat");
		chatBtnPrivateChat_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chatPagePrivateChat.setVisible(true);
				chatPageServerChat.setVisible(false);
			}
		});
		chatBtnPrivateChat_2.setForeground(Color.WHITE);
		chatBtnPrivateChat_2.setBackground(Color.DARK_GRAY);
		chatBtnPrivateChat_2.setFont(new Font("±¼¸²", Font.BOLD, 13));
		chatBtnPrivateChat_2.setBounds(1, 131, 116, 23);
		chatPagePrivateChat.add(chatBtnPrivateChat_2);
		
		chatPriMenu = new JTextField();
		chatPriMenu.setColumns(10);
		chatPriMenu.setBackground(SystemColor.activeCaption);
		chatPriMenu.setBounds(1, 0, 116, 208);
		chatPagePrivateChat.add(chatPriMenu);
		
		chatPriUserBox = new JTextField();
		chatPriUserBox.setColumns(10);
		chatPriUserBox.setBackground(SystemColor.inactiveCaption);
		chatPriUserBox.setBounds(1, 206, 116, 405);
		chatPagePrivateChat.add(chatPriUserBox);
		
		chatPriMessageBox = new JTextField();
		chatPriMessageBox.setColumns(10);
		chatPriMessageBox.setBackground(SystemColor.inactiveCaptionBorder);
		chatPriMessageBox.setBounds(216, 43, 512, 420);
		chatPagePrivateChat.add(chatPriMessageBox);
		
		chatPriSendMsg = new JTextField();
		chatPriSendMsg.setColumns(10);
		chatPriSendMsg.setBounds(248, 505, 373, 36);
		chatPagePrivateChat.add(chatPriSendMsg);
		
		chatPriBtnSend = new JButton("Send");
		chatPriBtnSend.setFont(new Font("±¼¸²", Font.BOLD, 14));
		chatPriBtnSend.setBounds(631, 511, 97, 23);
		chatPagePrivateChat.add(chatPriBtnSend);
	
	}
}
