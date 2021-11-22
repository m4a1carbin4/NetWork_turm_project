package GUI;

import javax.swing.*;
import java.awt.*;

public class ImagedLabel9 extends ImagedLabel {
	private ImageIcon[] image = null;
	private int edge_w = 0, edge_t = 0;

	public void paintComponent(Graphics g) {
		var img = image;

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
		if (image == null)
			image = new ImageIcon[9];
		
		String path = System.getProperty("user.dir");
		
		for (int i = 0; i < 9; i++) {
			String tpath = path + "/" + file;
			
			tpath += "_";

			switch (i / 3) {
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

			switch (i % 3) {
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
			
			tpath += "n.jpg";
			image[i] = new ImageIcon(tpath);
		}
		
		if (repaint)
			this.repaint();
	}
	
	public void setEdgeSize(int w, int t) {
		edge_w = w;
		edge_t = t;
	}

	public ImagedLabel9(String text) {
		super(text);
		this.setEdgeSize(4, 4);
	}
}