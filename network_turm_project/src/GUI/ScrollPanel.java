package GUI;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.HashMap;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollBar;

public class ScrollPanel extends JPanel implements AdjustmentListener, MouseWheelListener {
	private JScrollBar horizontal = null;
	private JScrollBar vertical = null;

	private boolean vshow = false;
	private boolean hshow = false;

	HashMap<JComponent, Rectangle> posList = null;

	private int scroll_value = 1;
	private int adjust_x, adjust_y;
	private int move_w, move_t;

	public ScrollPanel(boolean vertical, boolean horizontal) {
		super();
		super.setLayout(null);
		super.setBackground(Color.WHITE);

		this.horizontal = new JScrollBar(JScrollBar.HORIZONTAL);
		this.vertical = new JScrollBar(JScrollBar.VERTICAL);

		this.horizontal.setVisible(horizontal);
		this.vertical.setVisible(vertical);

		super.add(this.horizontal);
		super.add(this.vertical);

		this.vertical.addAdjustmentListener(this);
		this.horizontal.addAdjustmentListener(this);
		this.addMouseWheelListener(this);

		move_w = move_t = 0;
		
		posList = new HashMap<JComponent, Rectangle>();
	}
	
	public void setBounds(Rectangle d) {
		this.setBounds(d.x, d.y, d.width, d.height);
	}
	
	public void setBounds(int x, int y, int w, int t) {
		super.setBounds(x, y, w, t);

		this.vertical.setBounds(x + w - 16, 0, 16, t - 16);
		this.horizontal.setBounds(0, t - 16, w, 16);
		Calc();
	}
	
	public void setScrollValue(int value) {
		if (value < 1)
			value = 1;
		
		scroll_value = value;
	}
	
	public void updateBounds(JComponent c, Rectangle d) {
		if (!isComponent(c))
			return;
		
		posList.put(c, d);
		Calc();
	}
	
	public void add(JComponent c) {
		super.add(c);
		posList.put(c, c.getBounds());
		Calc();
	}
	
	public void remove(JComponent c) {
		super.remove(c);
		posList.remove(c);
		Calc();
	}
	
	public void removeAll() {
		super.removeAll();
		posList.clear();
		Calc();
	}
	
	private boolean isComponent(JComponent c) {
		var cs = getComponents();
		for (int i = 0; i < cs.length; i++) {
			if (cs[i] == c)
				return true;
		}
		return false;
	}

	private void Calc() {
		if (posList.keySet().size() == 0)
			return;

		int wide, tall;
		int min_x = 0, min_y = 0;
		int max_x = 0, max_y = 0;

		int x = 0, y = 0;

		var iter = posList.keySet().iterator();
		while (iter.hasNext()) {
			var d = posList.get(iter.next());
			x = d.x + d.width;
			y = d.y + d.height;

			if (max_x < x)
				max_x = x;
			if (max_y < y)
				max_y = y;
			if (min_x > x)
				min_x = x;
			if (min_y > y)
				min_y = y;
		}

		wide = max_x - min_x;
		tall = max_y - min_y;

		adjust_x = adjust_y = 0;
		move_w = move_t = 0;
		
		var d = (Rectangle)this.getBounds().clone();
		d.width -= 16;
		d.height -= 16;
		
		int delx = 0, dely = 0;
		
		if (d.width < wide) {
			delx = wide - d.width;
			move_w = delx / d.width * 6;

			if (min_x < 0) {
				adjust_x = -min_x / move_w;
				if (min_x % move_w != 0)
					adjust_x++;
			}
		}

		if (d.height < tall) {
			dely = tall - d.height;
			move_t = dely / d.height * 6;

			if (min_y < 0) {
				adjust_y = -min_y / move_t;
				if (min_y % move_t != 0)
					adjust_y++;
			}
		}

		if (move_w > 0) {
			int max = delx / move_w;
			if (wide % move_w != 0)
				max++;
			
			if (max > 0) {
				horizontal.setVisible(true);
				horizontal.setMinimum(0);
				horizontal.setMaximum(max);
				horizontal.setValue(0);
			}
		}
		else {
			horizontal.setVisible(hshow);
			horizontal.setMinimum(0);
			horizontal.setMaximum(1);
			horizontal.setValue(0);
		}
		
		if (move_t > 0) {
			if (move_w <= 0)
				this.vertical.setBounds(d.width, 0, 16, d.height + 16);
			else
				this.vertical.setBounds(d.width, 0, 16, d.height);
			int max = dely / move_t;
			if (tall % move_t != 0)
				max++;
			
			if (max > 0) {
				vertical.setVisible(true);
				vertical.setMinimum(0);
				vertical.setMaximum(max);
				vertical.setValue(0);
			}
		}
		else {
			vertical.setVisible(vshow);
			vertical.setMinimum(0);
			vertical.setMaximum(1);
			vertical.setValue(0);
		}
		
		replaceComponents();
	}
	
	private void replaceComponents() {
		if (posList.keySet().size() == 0)
			return;
		
		int x = adjust_x + horizontal.getValue();
		int y = adjust_y + vertical.getValue();
		
		var iter = posList.keySet().iterator();
		while (iter.hasNext()) {
			var c = iter.next();
			var d = posList.get(c);
			c.setLocation(d.x - x * move_w, d.y - y * move_t);
		}
		repaint();
	}

	@Override
	public void adjustmentValueChanged(AdjustmentEvent e) {
		replaceComponents();
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if (e.getWheelRotation() == 1)
			vertical.setValue(vertical.getValue() + scroll_value);
		else if (e.getWheelRotation() == -1)
			vertical.setValue(vertical.getValue() - scroll_value);
	}
}
