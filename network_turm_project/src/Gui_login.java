import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JComboBox;
import java.awt.BorderLayout;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JFormattedTextField;
import javax.swing.SwingConstants;
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
		frame.setBounds(100, 100, 698, 427);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		textField = new JFormattedTextField();
		textField.setForeground(Color.WHITE);
		textField.setHorizontalAlignment(SwingConstants.CENTER);
		textField.setFont(new Font("±¼¸²", Font.BOLD, 20));
		textField.setText("Login");
		textField.setBackground(Color.GRAY);
		textField.setBounds(58, 30, 101, 33);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		txtAlpha = new JTextField();
		txtAlpha.setForeground(Color.WHITE);
		txtAlpha.setHorizontalAlignment(SwingConstants.CENTER);
		txtAlpha.setFont(new Font("±¼¸²", Font.BOLD, 20));
		txtAlpha.setText("Alpha");
		txtAlpha.setBackground(Color.GRAY);
		txtAlpha.setBounds(582, 62, 76, 33);
		frame.getContentPane().add(txtAlpha);
		txtAlpha.setColumns(10);
		
		JButton btnNewButton = new JButton("Login");
		btnNewButton.setFont(new Font("±¼¸²", Font.BOLD, 16));
		btnNewButton.setBackground(Color.BLACK);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
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
		lblNewLabel.setFont(new Font("±¼¸²", Font.BOLD, 15));
		lblNewLabel.setBounds(171, 148, 43, 15);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Password:");
		lblNewLabel_1.setFont(new Font("±¼¸²", Font.BOLD, 15));
		lblNewLabel_1.setBounds(116, 205, 92, 15);
		frame.getContentPane().add(lblNewLabel_1);
		
		textField_1 = new JTextField();
		textField_1.setBackground(SystemColor.control);
		textField_1.setBounds(43, 62, 615, 289);
		frame.getContentPane().add(textField_1);
		textField_1.setColumns(10);
	}

}