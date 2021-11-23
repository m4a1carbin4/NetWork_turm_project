package Client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;

import org.json.simple.JSONObject;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.awt.event.ActionEvent;
import Json_Controller.Json_Controller;

public class FindPass {

	private JFrame frame;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	
	private DataOutputStream dataoutput = null;
	private DataInputStream datainput = null;

	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {
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
	}*/

	/**
	 * Create the application.
	 */
	public FindPass(DataOutputStream dataoutput, DataInputStream datainput) {
		this.dataoutput = dataoutput;
		this.datainput = datainput;
		initialize();
		frame.setVisible(true);
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
		lblNewLabel.setFont(new Font("±¼¸²", Font.BOLD, 15));
		lblNewLabel.setBounds(104, 34, 29, 37);
		panel.add(lblNewLabel);
		
		JLabel lblEmail = new JLabel("Email:");
		lblEmail.setFont(new Font("±¼¸²", Font.BOLD, 15));
		lblEmail.setBounds(79, 84, 54, 37);
		panel.add(lblEmail);
		
		JLabel lblNickname = new JLabel("NickName:");
		lblNickname.setFont(new Font("±¼¸²", Font.BOLD, 15));
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
				
				JSONObject ptr = new JSONObject();
				JSONObject ptr2 = new JSONObject();
				
				ptr.put("ID", ID);
				ptr.put("Email", Email);
				ptr.put("Nname", Nname);
				
				String send = Json_Controller.wrap("find_passwd", ptr.toJSONString());
				
				try {
					dataoutput.writeUTF(send);
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				
				String input = null;
				
				try {
					input = datainput.readUTF();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				ptr2 = Json_Controller.parse(input);
				
				String Type = (String)ptr2.get("Type");
				String Data = (String)ptr2.get("Data");
				
				if(Type.equals("send_passwd")){
					JOptionPane.showMessageDialog(null, "Password:  " + Data);
					frame.setVisible(false);
				}else if(Type.equals("fail_passwd")){
					JOptionPane.showMessageDialog(null, "Password_find_fail ");
					frame.setVisible(false);
				}

			}
		});
		btnNewButton.setFont(new Font("±¼¸²", Font.BOLD, 15));
		btnNewButton.setBounds(328, 194, 97, 23);
		panel.add(btnNewButton);
	}
}
