package Client;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;
import javax.swing.JTextField;

public class Chat extends JPanel implements ActionListener, KeyListener {
	private final Object parent;
	
	private JTextArea display = null;
	
	private final int numline;
	public final int x, y, w, t;
	
	private boolean valid = false;
	
	private boolean thread_breaker = false;
	private ArrayList<Thread> thread_list = null;
	
	public Chat(Object parent, int x, int y, int w, int t) {
		super();
		
		this.parent = parent;
		this.x = x;
		this.y = y;
		this.w = w;
		this.t = t;
		
		this.setBackground(Color.WHITE);
		
		setBounds(x, y, w, t);
		numline = t / 30;
		
		valid = true;
		initialize();
	}
	
	public void initialize() {
		thread_list = new ArrayList<Thread>();
		
		setLayout(null);
		
		var line = new LineBorder(Color.BLACK, 1, true);
		setBorder(line);
		
		var listPanel = new JPanel();
		listPanel.setBounds(0, 0, 120, this.t);
		listPanel.setBorder(line);
		listPanel.setBackground(Color.WHITE);
		listPanel.setLayout(null);
		add(listPanel);
		
		int count = (int)((double)numline / 1.2);
		int y = 10;
		int t = (this.t - 20) / count;
		
		for (int i = 0; i < count; i++) {
			var label = new JLabel("Channel " + (i + 1));
			label.setBounds(10, y + 5, 110, t - 10);
			label.setVisible(true);
			y += t;
			listPanel.add(label);
		}
		
		var exitbtn = new JButton("-");
		exitbtn.setActionCommand("exit");
		exitbtn.addActionListener(this);
		exitbtn.setBounds(this.w - 30, 0, 30, 30);
		add(exitbtn);
		
		var input = new JTextField();
		input.setBounds(120, this.t - 30, this.w - 121, 30);
		input.addKeyListener(this);
		add(input);
		
		display = new JTextArea();
		display.setEditable(false);
		
		var scroll = new JScrollPane(display);
		scroll.setBounds(125, 40, this.w - 130, this.t - 71);
		scroll.setBorder(null);
		add(scroll);
	}
	
	public void setComponentVisible(boolean value) {
		for (var c : this.getComponents()) {
			c.setVisible(value);
		}
	}
	
	public void breakThread() {
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
	
	public void out() {
		breakThread();
		
		var th = new Thread(new out_task(this, 0.2f));
		th.setDaemon(true);
		th.start();
		thread_list.add(th);
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
	
	public void appear() {
		breakThread();
		
		var th = new Thread(new app_task(this, 0.2f));
		th.setDaemon(true);
		th.start();
		thread_list.add(th);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case "exit":
			out();
			break;
		case "Receive":
			if (display == null)
				return;

			display.append((String)e.getSource() + "\n");
			break;
		}
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
