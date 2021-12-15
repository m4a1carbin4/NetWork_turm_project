package Client;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import GUI.ImagedButton9;
import GUI.ScrollPanel;
import Json_Controller.Json_Controller;

import javax.swing.JTextField;

public class Chat extends JDialog implements ActionListener, KeyListener {
	private final Object parent;
	
	private JPanel listPanel = null;
	private ChattingDialog display = null;
	
	private ArrayList<String> roomList = null;
	
	public Chat(Object parent, int x, int y, int w, int t) {
		super();
		
		this.parent = parent;
		
		setBounds(x, y, w, t);
		initialize();
	}
	
	public void initialize() {
		setLayout(null);
		setUndecorated(true);
		setBackground(Color.WHITE);
		
		//setBorder(new LineBorder(Color.BLACK, 1, true));
		
		constructLPanel();
		
		var rect = getBounds();
		
		var exitbtn = new ImagedButton9(this, "X", "gui/imagedbutton9/button", "exit");
		exitbtn.setBounds(rect.width - 31, 1, 30, 30);
		add(exitbtn);
		
		var input = new JTextField();
		input.setBounds(120, rect.height - 30, rect.width - 120, 30);
		input.addKeyListener(this);
		add(input);
		
		display = new ChattingDialog(32);
		display.setBounds(120, 32, rect.width - 121, rect.height - 62);
		display.setBorder(new LineBorder(Color.BLACK, 1, true));
		add(display);
	}
	
	public void appear() {
		var main = (MainFrame)this.parent;
		var d = main.getBounds();
		setLocation(d.x + d.width - 440, d.y + d.height - 350);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String[] args = e.getActionCommand().split(" ");
		
		String command = null;
		String data = null;
		if (args.length < 2) {
			command = e.getActionCommand();
			data = "";
		}
		else {
			command = args[0];
			data = e.getActionCommand().substring(args[0].length() + 1);
		}
		
		switch (command) {
		case "exit":
			out();
			break;
		case "Receive":
			if (display == null)
				return;

			var t = new Hashtable<String, String>();
			
			String[] spl = ((String)e.getSource()).split(":");
			System.out.println(spl[0] + spl[1]);
			
			t.put("from", spl[0]);
			t.put("what", spl[1]);
			
			display.append(t);
			break;
		case "Channel":
			if (isChannel(data))
			{
				System.out.println("??");
				return;
			}
			
			var cs = listPanel.getComponents();
			for (int i = 0; i < cs.length; i++) {
				if (cs[i] instanceof ScrollPanel) {
					cs = ((ScrollPanel)cs[i]).getSubComponents();
					for (i = 0; i < cs.length; i++) {
						if (cs[i] instanceof ImagedButton9) {
							var imgbtn = (ImagedButton9)cs[i];
							if (!data.contentEquals(imgbtn.getText()))
								imgbtn.setEnabled(true);
						}
					}
					break;
				}
			}
			
			if (e.getSource() instanceof ImagedButton9)
				((ImagedButton9)e.getSource()).setEnabled(false);

			String data2 = null;
			
			if (data.contentEquals("#HERE"))
				data2 = Json_Controller.wrap("return","return_request");
			else
				data2 = Json_Controller.wrap("join_Room_Ninfo", data);
			
			System.out.println("test: " + data2);
			try {
				((MainFrame)parent).dataoutput.writeUTF(data2);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			break;
		case "AddChannel":
			if (!hasChannel(data)) {
				this.roomList.add(data);
				this.constructLPanel();
			}
			break;
		}
	}
	
	public boolean hasChannel(String data) {
		if (roomList == null)
			return false;
		
		for (int i = 0; i < roomList.size(); i++)
			if (data.contentEquals(roomList.get(i)))
				return true;
		
		return false;
	}
	
	public boolean isChannel(String data) {
		var cs = listPanel.getComponents();
		for (int i = 0; i < cs.length; i++) {
			if (cs[i] instanceof ScrollPanel) {
				cs = ((ScrollPanel)cs[i]).getSubComponents();
				for (i = 0; i < cs.length; i++) {
					if (cs[i] instanceof ImagedButton9) {
						var imgbtn = (ImagedButton9)cs[i];
						if (!imgbtn.isEnabled() && data.contentEquals(imgbtn.getText()))
							return true;
					}
				}
				return false;
			}
		}
		
		return false;
	}
	
	public void setComponentVisible(boolean value) {
		for (var c : this.getComponents()) {
			c.setVisible(value);
		}
	}
	
	private void constructLPanel() {
		var rect = getBounds();
		if (listPanel == null) {
			listPanel = new JPanel();
			listPanel.setBorder(new LineBorder(Color.BLACK, 1, true));
			listPanel.setBackground(Color.WHITE);
			listPanel.setBounds(0, 0, 120, rect.height);
			listPanel.setLayout(null);
			add(listPanel);
		}
		
		listPanel.removeAll();
		
		if (roomList == null)
			roomList = new ArrayList<String>();
		
		var scpanel = new ScrollPanel(false, false);
		scpanel.setBounds(0, 0, 120, rect.height - 50);
		scpanel.setBorder(new LineBorder(Color.BLACK, 1, true));
		scpanel.setScrollValue(3);
		listPanel.add(scpanel);

		int y = 10;
		var all9 = new ImagedButton9(this, "ALL", "gui/imagedbutton9/button", "Channel #HERE");
		all9.setBounds(16, y, 80, 30);
		all9.setVisible(true);
		all9.setEnabled(false);
		scpanel.add(all9);
		
		var iter = roomList.iterator();
		while (iter.hasNext()) {
			y += 40;
			
			var name = iter.next();
			all9 = new ImagedButton9(this, name, "gui/imagedbutton9/button", "Channel " + name);
			all9.setBounds(16, y, 80, 30);
			all9.setVisible(true);
			scpanel.add(all9);
		}
		
		var addbtn = new ImagedButton9(this, "Room List", "gui/imagedbutton9/button", "Add");
		addbtn.setBounds(20, rect.height - 41, 80, 30);
		addbtn.setEnabled(false);
		listPanel.add(addbtn);
		
		listPanel.repaint();
	}
	
	public void out() {
		setVisible(false);
	}

	@Override
	public void keyTyped(KeyEvent e) { }

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			((ActionListener) this.parent).actionPerformed(new ActionEvent(e.getSource(), 0, "SendChat"));
		}
	}

	@Override
	public void keyReleased(KeyEvent e) { }
}
