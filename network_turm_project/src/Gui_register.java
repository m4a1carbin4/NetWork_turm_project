import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.FlowLayout;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.SystemColor;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Gui_register {

	private JFrame frame;
	private JTextField txtResisterAccount;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Gui_register window = new Gui_register();
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
	public Gui_register() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.WHITE);
		frame.setBounds(100, 100, 712, 548);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		txtResisterAccount = new JTextField();
		txtResisterAccount.setForeground(Color.WHITE);
		txtResisterAccount.setBackground(Color.GRAY);
		txtResisterAccount.setHorizontalAlignment(SwingConstants.CENTER);
		txtResisterAccount.setFont(new Font("±¼¸²", Font.BOLD, 30));
		txtResisterAccount.setText("Resister Account");
		txtResisterAccount.setBounds(151, 21, 403, 43);
		frame.getContentPane().add(txtResisterAccount);
		txtResisterAccount.setColumns(10);
		
		JButton btnNewButton = new JButton("Resister");
		btnNewButton.setFont(new Font("±¼¸²", Font.BOLD, 14));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnNewButton.setBounds(530, 416, 97, 23);
		frame.getContentPane().add(btnNewButton);
		
		JCheckBox chckbxNewCheckBox = new JCheckBox("Duplicate Check");
		chckbxNewCheckBox.setBackground(Color.LIGHT_GRAY);
		chckbxNewCheckBox.setFont(new Font("±¼¸²", Font.PLAIN, 14));
		chckbxNewCheckBox.setBounds(498, 124, 133, 23);
		frame.getContentPane().add(chckbxNewCheckBox);
		
		JLabel lblNewLabel = new JLabel("Username:");
		lblNewLabel.setFont(new Font("±¼¸²", Font.BOLD, 15));
		lblNewLabel.setBounds(110, 109, 97, 53);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setFont(new Font("±¼¸²", Font.BOLD, 15));
		lblPassword.setBounds(110, 155, 97, 53);
		frame.getContentPane().add(lblPassword);
		
		textField = new JTextField();
		textField.setBounds(204, 117, 286, 37);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(204, 163, 286, 37);
		frame.getContentPane().add(textField_1);
		
		JLabel lblPasswordCheck = new JLabel("Password Check:");
		lblPasswordCheck.setFont(new Font("±¼¸²", Font.BOLD, 15));
		lblPasswordCheck.setBounds(61, 202, 158, 53);
		frame.getContentPane().add(lblPasswordCheck);
		
		textField_2 = new JTextField();
		textField_2.setColumns(10);
		textField_2.setBounds(204, 210, 286, 37);
		frame.getContentPane().add(textField_2);
		
		JCheckBox chckbxNewCheckBox_1 = new JCheckBox("Agree to provide information");
		chckbxNewCheckBox_1.setBackground(Color.LIGHT_GRAY);
		chckbxNewCheckBox_1.setFont(new Font("±¼¸²", Font.PLAIN, 14));
		chckbxNewCheckBox_1.setBounds(238, 367, 235, 23);
		frame.getContentPane().add(chckbxNewCheckBox_1);
		
		JLabel lblNickname = new JLabel("Nickname:");
		lblNickname.setFont(new Font("±¼¸²", Font.BOLD, 15));
		lblNickname.setBounds(110, 246, 88, 53);
		frame.getContentPane().add(lblNickname);
		
		textField_4 = new JTextField();
		textField_4.setColumns(10);
		textField_4.setBounds(204, 254, 286, 37);
		frame.getContentPane().add(textField_4);
		
		JLabel lblEmail = new JLabel("Email:");
		lblEmail.setFont(new Font("±¼¸²", Font.BOLD, 15));
		lblEmail.setBounds(143, 294, 55, 53);
		frame.getContentPane().add(lblEmail);
		
		textField_5 = new JTextField();
		textField_5.setColumns(10);
		textField_5.setBounds(204, 302, 286, 37);
		frame.getContentPane().add(textField_5);
		
		JCheckBox chckbxNewCheckBox_2 = new JCheckBox("Duplicate Check");
		chckbxNewCheckBox_2.setFont(new Font("±¼¸²", Font.PLAIN, 14));
		chckbxNewCheckBox_2.setBackground(Color.LIGHT_GRAY);
		chckbxNewCheckBox_2.setBounds(498, 261, 133, 23);
		frame.getContentPane().add(chckbxNewCheckBox_2);
		
		JCheckBox chckbxNewCheckBox_3 = new JCheckBox("Duplicate Check");
		chckbxNewCheckBox_3.setFont(new Font("±¼¸²", Font.PLAIN, 14));
		chckbxNewCheckBox_3.setBackground(Color.LIGHT_GRAY);
		chckbxNewCheckBox_3.setBounds(498, 309, 133, 23);
		frame.getContentPane().add(chckbxNewCheckBox_3);
		
		textField_3 = new JTextField();
		textField_3.setBackground(SystemColor.control);
		textField_3.setBounds(32, 64, 636, 413);
		frame.getContentPane().add(textField_3);
		textField_3.setColumns(10);
	}
}
