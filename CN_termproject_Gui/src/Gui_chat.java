import java.awt.EventQueue;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import java.awt.Color;
import javax.swing.JTextField;
import java.awt.SystemColor;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;

public class Gui_chat {

	private JFrame frame;
	private JTextField textField;
	private JButton btnNewButton;
	private JButton btnPrivateChat;
	private JButton btnServerChat;
	private JTextField textField_1;
	private JTextField textField_3;
	private JButton btnNewButton_1;
	private JTextField textField_6;
	private JTextField textField_10;
	private JTextField txtUser;
	private JTextField textField_11;
	private JTextField textField_12;
	private JTextField textField_13;
	private JTextField textField_14;
	private JTextField textField_2;
	private JTextField textField_4;
	private JTextField textField_5;
	private JTextField textField_9;
	private JTextField textField_8;
	private JTextField textField_7;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Gui_chat window = new Gui_chat();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Gui_chat() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.WHITE);
		ImageIcon human = new ImageIcon(this.getClass().getResource("/humanicon.jpeg"));
		frame.getContentPane().setLayout(null);
		
		btnNewButton = new JButton("Online");
		btnNewButton.setFont(new Font("±¼¸²", Font.BOLD, 13));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnNewButton.setBounds(-1, 43, 117, 23);
		frame.getContentPane().add(btnNewButton);
		
		btnPrivateChat = new JButton("Private Chat");
		btnPrivateChat.setFont(new Font("±¼¸²", Font.BOLD, 13));
		btnPrivateChat.setBounds(-1, 95, 116, 23);
		frame.getContentPane().add(btnPrivateChat);
		
		btnServerChat = new JButton("Server Chat");
		btnServerChat.setFont(new Font("±¼¸²", Font.BOLD, 13));
		btnServerChat.setBounds(-1, 144, 117, 23);
		frame.getContentPane().add(btnServerChat);
		textField = new JTextField();
		textField.setBounds(0, 0, 116, 208);
		textField.setBackground(SystemColor.activeCaption);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		textField_3 = new JTextField();
		textField_3.setBounds(215, 423, 373, 36);
		frame.getContentPane().add(textField_3);
		textField_3.setColumns(10);
		
		btnNewButton_1 = new JButton("Send");
		btnNewButton_1.setFont(new Font("±¼¸²", Font.BOLD, 14));
		btnNewButton_1.setBounds(598, 429, 97, 23);
		frame.getContentPane().add(btnNewButton_1);
		
		textField_6 = new JTextField();
		textField_6.setForeground(SystemColor.window);
		textField_6.setFont(new Font("±¼¸²", Font.BOLD, 13));
		textField_6.setBackground(SystemColor.textHighlight);
		textField_6.setText("message");
		textField_6.setHorizontalAlignment(SwingConstants.CENTER);
		textField_6.setColumns(10);
		textField_6.setBounds(233, 221, 116, 21);
		frame.getContentPane().add(textField_6);
		
		textField_10 = new JTextField();
		textField_10.setBackground(SystemColor.info);
		textField_10.setFont(new Font("±¼¸²", Font.BOLD, 13));
		textField_10.setText("message");
		textField_10.setHorizontalAlignment(SwingConstants.CENTER);
		textField_10.setColumns(10);
		textField_10.setBounds(451, 221, 116, 21);
		frame.getContentPane().add(textField_10);
		
		txtUser = new JTextField();
		txtUser.setHorizontalAlignment(SwingConstants.CENTER);
		txtUser.setText("user");
		txtUser.setBounds(-1, 249, 116, 21);
		frame.getContentPane().add(txtUser);
		txtUser.setColumns(10);
		
		textField_11 = new JTextField();
		textField_11.setText("user");
		textField_11.setHorizontalAlignment(SwingConstants.CENTER);
		textField_11.setColumns(10);
		textField_11.setBounds(-1, 280, 116, 21);
		frame.getContentPane().add(textField_11);
		
		textField_12 = new JTextField();
		textField_12.setText("user");
		textField_12.setHorizontalAlignment(SwingConstants.CENTER);
		textField_12.setColumns(10);
		textField_12.setBounds(0, 311, 116, 21);
		frame.getContentPane().add(textField_12);
		
		textField_13 = new JTextField();
		textField_13.setText("user");
		textField_13.setHorizontalAlignment(SwingConstants.CENTER);
		textField_13.setColumns(10);
		textField_13.setBounds(0, 342, 116, 21);
		frame.getContentPane().add(textField_13);
		
		textField_14 = new JTextField();
		textField_14.setBackground(SystemColor.inactiveCaption);
		textField_14.setBounds(-1, 207, 117, 314);
		frame.getContentPane().add(textField_14);
		textField_14.setColumns(10);
		
		textField_2 = new JTextField();
		textField_2.setText("message");
		textField_2.setHorizontalAlignment(SwingConstants.CENTER);
		textField_2.setForeground(Color.WHITE);
		textField_2.setFont(new Font("±¼¸²", Font.BOLD, 13));
		textField_2.setColumns(10);
		textField_2.setBackground(SystemColor.textHighlight);
		textField_2.setBounds(233, 263, 116, 21);
		frame.getContentPane().add(textField_2);
		
		textField_4 = new JTextField();
		textField_4.setText("message");
		textField_4.setHorizontalAlignment(SwingConstants.CENTER);
		textField_4.setForeground(Color.WHITE);
		textField_4.setFont(new Font("±¼¸²", Font.BOLD, 13));
		textField_4.setColumns(10);
		textField_4.setBackground(SystemColor.textHighlight);
		textField_4.setBounds(233, 307, 116, 21);
		frame.getContentPane().add(textField_4);
		
		textField_5 = new JTextField();
		textField_5.setText("message");
		textField_5.setHorizontalAlignment(SwingConstants.CENTER);
		textField_5.setForeground(Color.WHITE);
		textField_5.setFont(new Font("±¼¸²", Font.BOLD, 13));
		textField_5.setColumns(10);
		textField_5.setBackground(SystemColor.textHighlight);
		textField_5.setBounds(233, 352, 116, 21);
		frame.getContentPane().add(textField_5);
		
		textField_9 = new JTextField();
		textField_9.setText("message");
		textField_9.setHorizontalAlignment(SwingConstants.CENTER);
		textField_9.setFont(new Font("±¼¸²", Font.BOLD, 13));
		textField_9.setColumns(10);
		textField_9.setBackground(SystemColor.info);
		textField_9.setBounds(451, 264, 116, 21);
		frame.getContentPane().add(textField_9);
		
		textField_8 = new JTextField();
		textField_8.setText("message");
		textField_8.setHorizontalAlignment(SwingConstants.CENTER);
		textField_8.setFont(new Font("±¼¸²", Font.BOLD, 13));
		textField_8.setColumns(10);
		textField_8.setBackground(SystemColor.info);
		textField_8.setBounds(451, 311, 116, 21);
		frame.getContentPane().add(textField_8);
		
		textField_7 = new JTextField();
		textField_7.setText("message");
		textField_7.setHorizontalAlignment(SwingConstants.CENTER);
		textField_7.setFont(new Font("±¼¸²", Font.BOLD, 13));
		textField_7.setColumns(10);
		textField_7.setBackground(SystemColor.info);
		textField_7.setBounds(451, 353, 116, 21);
		frame.getContentPane().add(textField_7);
		
		textField_1 = new JTextField();
		textField_1.setBackground(SystemColor.inactiveCaptionBorder);
		textField_1.setBounds(215, 43, 373, 356);
		frame.getContentPane().add(textField_1);
		textField_1.setColumns(10);
		ImageIcon chat = new ImageIcon(this.getClass().getResource("/chaticon.png"));
		frame.setBounds(100, 100, 723, 560);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
