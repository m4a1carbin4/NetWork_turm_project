package GUI;

import java.awt.event.*;
import javax.swing.*;
import java.awt.*;

public class ImagedButton extends JLabel implements MouseListener, MouseMotionListener {
	Object master = null;

	protected int state = 0;
	private ImageIcon[] image = { null, null, null, null };
	protected String command = null;

	public void paintComponent(Graphics g) {
		int slot = state;
		if (!this.isEnabled())
			slot = 3;

		var img = image[slot];

		if (img != null) {
			var d = this.getBounds();
			g.drawImage(img.getImage(), 0, 0, d.width, d.height, null);
			setOpaque(false);
		}

		super.paintComponent(g);
	}

	public void setImage(String file1, String file2, String file3, String file4, boolean repaint) {
		String path = System.getProperty("user.dir");
		image[0] = new ImageIcon(path + "/" + file1);
		image[1] = new ImageIcon(path + "/" + file2);
		image[2] = new ImageIcon(path + "/" + file3);
		image[3] = new ImageIcon(path + "/" + file4);
		
		if (repaint)
			this.repaint();
	}

	public ImagedButton(Object master, String text, String command) {
		super(text, null, SwingConstants.CENTER);
		
		if (command == null)
			command = text;
		
		this.master = master;
		this.command = command;
		this.addMouseListener(this);
	}

	public ImagedButton(Object master, String text) {
		super(text, null, SwingConstants.CENTER);
		this.master = master;
		this.command = text;
		this.addMouseListener(this);
	}
	
	@Override
	public void setEnabled(boolean value) {
		super.setEnabled(value);
		repaint();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent e) { }

	@Override
	public void mousePressed(MouseEvent e) {
		if (!isEnabled())
			return;

		if (state == 1) {
			state = 2;
			repaint();
		}
	}

	private void ObjectTrigger() {
		if (master == null)
			return;

		if (master instanceof ActionListener)
			((ActionListener) master).actionPerformed(new ActionEvent(this, 0, command));
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (state == 2) {
			if (!isEnabled()) {
				state = 1;
				return;
			}

			ObjectTrigger();
			state = 1;
			repaint();
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		state = 1;
		repaint();
	}

	@Override
	public void mouseExited(MouseEvent e) {
		state = 0;
		repaint();
	}
}