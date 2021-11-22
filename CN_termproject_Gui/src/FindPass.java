package Client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class FindPass {

	private JFrame frame;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FindPass window = new FindPass();
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
	public FindPass() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.WHITE);
		frame.setBounds(100, 100, 543, 347);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(46, 40, 437, 227);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
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
			}
		});
		btnNewButton.setFont(new Font("굴림", Font.BOLD, 15));
		btnNewButton.setBounds(328, 194, 97, 23);
		panel.add(btnNewButton);
	}
}