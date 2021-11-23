package GUI;

import java.awt.event.*;
import javax.swing.*;
import java.awt.*;

public class ImagedLabel extends JLabel {
	private ImageIcon image = null;

	public void paintComponent(Graphics g) {
		if (image != null) {
			var d = this.getBounds();
			g.drawImage(image.getImage(), 0, 0, d.width, d.height, null);
			setOpaque(false);
		}

		super.paintComponent(g);
	}

	public void setImage(String file, boolean repaint) {
		image = new ImageIcon(System.getProperty("user.dir") + "/" + file);
		
		if (repaint)
			this.repaint();
	}

	public ImagedLabel(String text) {
		super(text, null, SwingConstants.LEADING);
	}
}