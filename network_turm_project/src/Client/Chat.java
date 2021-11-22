package Client;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;

import GUI.ImagedButton;
import GUI.ImagedButton9;
import GUI.ScrollPanel;
import Json_Controller.Json_Controller;

import javax.swing.JTextField;

public class Chat extends JPanel implements ActionListener, KeyListener {
	private final Object parent;
	
	private JPanel listPanel = null;
	private JTextArea display = null;
	
	public final int x, y, w, t;
	
	private boolean valid = false;
	
	private boolean thread_breaker = false;
	private ArrayList<Thread> thread_list = null;
	
	private ArrayList<String> playerList = null;
	
	public Chat(Object parent, int x, int y, int w, int t) {
		super();
		
		this.parent = parent;
		this.x = x;
		this.y = y;
		this.w = w;
		this.t = t;
		
		this.setBackground(Color.WHITE);
		
		setBounds(x, y, w, t);
		
		valid = true;
		initialize();
	}
	
	public void initialize() {
		thread_list = new ArrayList<Thread>();
		
		setLayout(null);
		
		setBorder(new LineBorder(Color.BLACK, 1, true));
		
		constructLPanel();
		
		var exitbtn = new ImagedButton9(this, "X", "gui/imagedbutton9/button", "exit");
		exitbtn.setBounds(this.w - 31, 1, 30, 30);
		add(exitbtn);
		
		var input = new JTextField();
		input.setBounds(120, this.t - 30, this.w - 120, 30);
		input.addKeyListener(this);
		add(input);
		
		var chatting = new ChattingDialog(32);
		chatting.setBounds(125, 40, this.w - 130, this.t - 73);
		chatting.setBorder(new LineBorder(Color.BLACK, 1, true));
		add(chatting);

		List<Hashtable<String, String>> a = new ArrayList<Hashtable<String, String>>();
		Hashtable<String, String> t = new Hashtable<String, String>();
		for (int i = 0; i < 16; i++) {
			t.put("from", "Index " + i);
			
			String f = "";
			for (int j = 0; j < i; j++)
				f += "Repeating text test ";
			
			t.put("what", f);
			a.add((Hashtable<String, String>)t.clone());
		}
		
		chatting.readContent(a);
		
		t.put("from", "Whow");
		t.put("what", "This is the last message appended");
		chatting.append(t);
		
		/*
		 * display = new JTextArea(); display.setEditable(false);
		 * 
		 * var scroll = new JScrollPane(display); scroll.setBounds(125, 40, this.w -
		 * 130, this.t - 71); scroll.setBorder(null); add(scroll);
		 */
	}
	
	public void appear() {
		breakThread();
		
		var th = new Thread(new app_task(this, 0.2f));
		th.setDaemon(true);
		th.start();
		thread_list.add(th);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String[] args = e.getActionCommand().split(" ");
		
		String command = null;
		if (args.length < 2)
			command = e.getActionCommand();
		else
			command = args[0];
		
		switch (command) {
		case "exit":
			out();
			break;
		case "Receive":
			if (display == null)
				return;

			display.append((String)e.getSource() + "\n");
			break;
		case "Channel":
			var cs = listPanel.getComponents();
			for (int i = 0; i < cs.length; i++) {
				if (cs[i] instanceof ScrollPanel) {
					cs = ((ScrollPanel)cs[i]).getSubComponents();
					for (i = 0; i < cs.length; i++) {
						if (cs[i] instanceof ImagedButton9) {
							var imgbtn = (ImagedButton9)cs[i];
							if (!args[1].contentEquals(imgbtn.getText()))
								imgbtn.setEnabled(true);
						}
					}
					break;
				}
			}
			((ImagedButton9)e.getSource()).setEnabled(false);
			break;
		case "Add":
			constructChatlist();
			break;
		case "Whisper":
			int i;
			for (i = 0; i < playerList.size(); i++)
				if (playerList.get(i).contentEquals(args[1]))
					break;
				
			if (i == playerList.size())
				playerList.add(args[1]);
		case "Back":
			constructLPanel();
			break;
		}
	}
	
	public void setComponentVisible(boolean value) {
		for (var c : this.getComponents()) {
			c.setVisible(value);
		}
	}
	
	private void constructLPanel() {
		if (listPanel == null) {
			listPanel = new JPanel();
			listPanel.setBorder(new LineBorder(Color.BLACK, 1, true));
			listPanel.setBackground(Color.WHITE);
			listPanel.setBounds(0, 0, 120, this.t);
			listPanel.setLayout(null);
			add(listPanel);
		}
		
		listPanel.removeAll();
		
		if (playerList == null)
			playerList = new ArrayList<String>();
		
		var scpanel = new ScrollPanel(false, false);
		scpanel.setBounds(0, 0, 120, this.t - 50);
		scpanel.setBorder(new LineBorder(Color.BLACK, 1, true));
		scpanel.setScrollValue(3);
		listPanel.add(scpanel);

		int y = 10;
		var all9 = new ImagedButton9(this, "ALL", "gui/imagedbutton9/button", "Channel #HERE");
		all9.setBounds(16, y, 80, 30);
		all9.setVisible(true);
		all9.setEnabled(false);
		scpanel.add(all9);
		
		var iter = playerList.iterator();
		while (iter.hasNext()) {
			y += 40;
			
			var name = iter.next();
			all9 = new ImagedButton9(this, name, "gui/imagedbutton9/button", "Channel " + name);
			all9.setBounds(16, y, 80, 30);
			all9.setVisible(true);
			scpanel.add(all9);
		}
		
		var addbtn = new ImagedButton9(this, "New Chat", "gui/imagedbutton9/button", "Add");
		addbtn.setBounds(20, this.t - 41, 80, 30);
		listPanel.add(addbtn);
		
		listPanel.repaint();
	}
	
	private void constructChatlist() {
		if (listPanel == null)
			return;
		
		if (this.parent == null)
			return;
		
		listPanel.removeAll();
		
		MainFrame mainFrame = null;
		try {
			mainFrame = (MainFrame)this.parent;
			mainFrame.dataoutput.writeUTF(Json_Controller.wrap("lobby_list", null));
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}
		
		var wait = new JLabel("Loading...", JLabel.CENTER);
		wait.setBounds(20, 50, 80, 30);
		listPanel.add(wait);

		listPanel.repaint();
		
		String result = null;
		while ((result = mainFrame.getData("Player_List")) == null) {};

		listPanel.remove(wait);
		
		var prev = new ImagedButton9(this, "Back", "gui/imagedbutton9/button");
		prev.setBounds(20, this.t - 41, 80, 30);
		listPanel.add(prev);
		
		var scpanel = new ScrollPanel(false, false);
		scpanel.setBounds(0, 0, 120, this.t - 50);
		scpanel.setBorder(new LineBorder(Color.BLACK, 1, true));
		scpanel.setScrollValue(3);
		listPanel.add(scpanel);
		
		if (result.length() == 0) {
			var none = new JLabel("There is no user.");
			none.setBounds(10, 10, 100, 30);
			scpanel.add(none);
			listPanel.repaint();
			return;
		}

		var names = result.split(";");

		int y = 10;
		for (int i = 0; i < names.length; i++) {
			var all9 = new ImagedButton9(this, names[i], "gui/imagedbutton9/button", "Whisper " + names[i]);
			all9.setBounds(16, y, 80, 30);
			all9.setVisible(true);
			
			int j;
			for (j = 0; j < playerList.size(); j++)
				if (playerList.get(j).contentEquals(names[i]))
					break;
			
			all9.setEnabled(j == playerList.size());
			scpanel.add(all9);
			
			y += 40;
		}
		
		listPanel.repaint();
	}
	
	private void breakThread() {
		thread_breaker = true;
		while (true) {
			var it = thread_list.iterator();
			while (it.hasNext()) {
				if (it.next().isAlive())
					break;
			}
			if (!it.hasNext())
				break;
			try { wait(10); } catch (Exception e) {}
		}
		thread_list.clear();
		thread_breaker = false;
	}

	private class out_task implements Runnable {
		final Chat target;
		final long time;
		final float task_time;
		out_task(Chat p, float ttime) { target = p; time = System.currentTimeMillis(); task_time = ttime; }
		public void run() {
			target.setComponentVisible(false);
			while (true) {
				if (target.thread_breaker || !target.valid)
					return;
				
				if (task_time == 0.0f)
					break;
				
				float passed = (float)(System.currentTimeMillis() - time) / 1000.0f;
				passed /= task_time;
				
				if (passed > 1.0f)
					break;
				
				int ww = (int)((float)target.w * passed);
				int tt = (int)((float)target.t * passed);
				target.setBounds(target.x + ww, target.y + tt, target.w - ww, target.t - tt);
				
				try { Thread.sleep(10);} catch (Exception e) { e.printStackTrace(); }
			}
			target.setVisible(false);
		}
	}

	private class app_task implements Runnable {
		final Chat target;
		final long time;
		final float task_time;
		app_task(Chat p, float ttime) { target = p; time = System.currentTimeMillis(); task_time = ttime; }
		public void run() {
			target.setComponentVisible(false);
			while (true) {
				if (target.thread_breaker || !target.valid)
					return;
				
				if (task_time == 0.0f)
					break;
				
				float passed = (float)(System.currentTimeMillis() - time) / 1000.0f;
				passed /= task_time;
				
				if (passed > 1.0f)
					break;
				
				passed = 1.0f - passed;
				
				int ww = (int)((float)target.w * passed);
				int tt = (int)((float)target.t * passed);
				target.setBounds(target.x + ww, target.y + tt, target.w - ww, target.t - tt);
				
				if (!target.isVisible())
					target.setVisible(true);
				
				try { Thread.sleep(10);} catch (Exception e) { break; }
			}
			target.setBounds(target.x, target.y, target.w, target.t);
			target.setComponentVisible(true);
		}
	}
	
	public void out() {
		breakThread();
		
		var th = new Thread(new out_task(this, 0.2f));
		th.setDaemon(true);
		th.start();
		thread_list.add(th);
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			((MainFrame)this.parent).actionPerformed(new ActionEvent(e.getSource(), 0, "SendChat"));
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}
}
