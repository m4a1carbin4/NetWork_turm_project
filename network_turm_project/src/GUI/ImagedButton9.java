package GUI;

import java.awt.event.*;
import javax.swing.*;

import java.awt.*;

public class ImagedButton9 extends ImagedButton {
	Object master = null;

	private Object[] image = { null, null, null, null };
	
	private int edge_w = 0, edge_t = 0;

	public void paintComponent(Graphics g) {
		int slot = state;
		if (!this.isEnabled())
			slot = 3;

		var img = (ImageIcon[])image[slot];

		if (img != null || img.length != 9) {
			var d = this.getBounds();

			int w = img[0].getIconWidth();
			int t = img[0].getIconWidth();
			
			if (edge_w != 0)
				w = edge_w;
			
			if (edge_t != 0)
				t = edge_t;

			if (w >= d.width / 2)
				w = d.width / 5;
			if (t >= d.height / 2)
				t = d.height / 5;
			
			if (w == 0 || t == 0)
				return;

			g.drawImage(img[0].getImage(), 0, 0, w, t, null);
			g.drawImage(img[1].getImage(), w, 0, d.width - w * 2, t, null);
			g.drawImage(img[2].getImage(), d.width - w, 0, w, t, null);
			
			g.drawImage(img[3].getImage(), 0, t, w, d.height - t * 2, null);
			g.drawImage(img[4].getImage(), w, t, d.width - w * 2, d.height - t * 2, null);
			g.drawImage(img[5].getImage(), d.width - w, t, w, d.height - t * 2, null);
			
			g.drawImage(img[6].getImage(), 0, d.height - t, w, t, null);
			g.drawImage(img[7].getImage(), w, d.height - t, d.width - w * 2, t, null);
			g.drawImage(img[8].getImage(), d.width - w, d.height - t, w, t, null);
			setOpaque(false);
		}

		super.paintComponent(g);
	}

	public void setImage(String file, boolean repaint) {
		String path = System.getProperty("user.dir");
		
		for (int i = 0; i < 4; i++) {
			if (image[i] == null)
				image[i] = new ImageIcon[9];
			
			ImageIcon[] holder = (ImageIcon[])image[i];
			
			for (int j = 0; j < 9; j++) {
				String tpath = path + "/" + file;
				
				tpath += "_";

				switch (j / 3) {
				case 0:
					tpath += "t"; // top
					break;
				case 1:
					tpath += "m"; // mid
					break;
				case 2:
					tpath += "b"; // bottom
					break;
				}

				switch (j % 3) {
				case 0:
					tpath += "l"; // left
					break;
				case 1:
					tpath += "c"; // center
					break;
				case 2:
					tpath += "r"; // right
					break;
				}
				
				switch (i) {
				case 0:
					tpath += 'n'; // normal
					break;
				case 1:
					tpath += 'o'; // on
					break;
				case 2:
					tpath += 'c'; // click
					break;
				case 3:
					tpath += 'd'; // disabled
					break;
				}
				
				tpath += ".jpg";
				holder[j] = new ImageIcon(tpath);
			}
		}
		
		if (repaint)
			this.repaint();
	}
	
	public void setEdgeSize(int w, int t) {
		edge_w = w;
		edge_t = t;
	}

	public ImagedButton9(Object master, String text) {
		super(master, text, text);
		this.master = master;
		this.addMouseListener(this);
		this.setEdgeSize(4, 4);
	}

	public ImagedButton9(Object master, String text, String image) {
		super(master, text, text);
		this.master = master;
		this.addMouseListener(this);
		this.setImage(image, false);
		this.setEdgeSize(4, 4);
	}

	public ImagedButton9(Object master, String text, String image, String command) {
		super(master, text, command);
		
		this.master = master;
		this.addMouseListener(this);
		this.setImage(image, false);
		this.setEdgeSize(4, 4);
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