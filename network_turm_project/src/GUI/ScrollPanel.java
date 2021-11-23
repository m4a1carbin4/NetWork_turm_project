package GUI;

import java.awt.Color;
import java.awt.Component;
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
	protected JScrollBar horizontal = null;
	protected JScrollBar vertical = null;
	
	private JPanel mainPanel = null;

	protected boolean vshow = false;
	protected boolean hshow = false;

	private int scroll_value = 1;
	protected int org_x, org_y;
	protected int adjust_x, adjust_y;
	protected int move_w, move_t;

	public ScrollPanel(boolean vertical, boolean horizontal) {
		super();
		super.setLayout(null);
		super.setBackground(Color.WHITE);

		this.horizontal = new JScrollBar(JScrollBar.HORIZONTAL);
		this.vertical = new JScrollBar(JScrollBar.VERTICAL);

		this.horizontal.setVisible(horizontal);
		this.vertical.setVisible(vertical);
		
		mainPanel = new JPanel();
		mainPanel.setLayout(null);
		mainPanel.setBackground(Color.WHITE);

		super.add(this.horizontal);
		super.add(this.vertical);
		super.add(mainPanel);

		this.vertical.addAdjustmentListener(this);
		this.horizontal.addAdjustmentListener(this);
		this.addMouseWheelListener(this);

		org_x = org_y = move_w = move_t = 0;
	}
	
	public int getSubComponentCount() {
		return mainPanel.getComponentCount();
	}
	
	public Component[] getSubComponents() {
		return mainPanel.getComponents();
	}
	
	public void setBounds(Rectangle d) {
		this.setBounds(d.x, d.y, d.width, d.height);
	}
	
	public void setBounds(int x, int y, int w, int t) {
		super.setBounds(x, y, w, t);

		this.vertical.setBounds(x + w - 16, 0, 16, t - 16);
		this.horizontal.setBounds(0, t - 16, w, 16);
		mainPanel.setBounds(x + 4, y + 4, w - 4, t - 4);
		org_x = 4;
		org_y = 4;
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
		
		Calc();
	}
	
	public void add(JComponent c) {
		mainPanel.add(c);
		Calc();
	}
	
	public void add(JComponent c, boolean calc) {
		mainPanel.add(c);
		
		if (calc)
			Calc();
	}
	
	public void remove(JComponent c) {
		mainPanel.remove(c);
		Calc();
	}
	
	public void removeAll() {
		mainPanel.removeAll();
		Calc();
	}
	
	private boolean isComponent(JComponent c) {
		var cs = getSubComponents();
		for (int i = 0; i < cs.length; i++) {
			if (cs[i] == c)
				return true;
		}
		return false;
	}

	protected void Calc() {
		if (getSubComponentCount() == 0)
			return;

		int wide, tall;
		int min_x = 0, min_y = 0;
		int max_x = 0, max_y = 0;

		int x = 0, y = 0;

		var cs = getSubComponents();
		for (int i = 0; i < cs.length; i++) {
			if (!cs[i].isVisible())
				continue;
			
			var d = cs[i].getBounds();
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
		d.width -= 8;
		d.height -= 8;
		
		int maxx = 0, maxy = 0;
		
		if (d.width < wide) {
			maxx = wide / d.width + 1;

			if (min_x < 0) {
				adjust_x = -min_x / move_w;
				if (min_x % move_w != 0)
					adjust_x++;
			}
		}

		if (d.height < tall) {
			maxy = tall / d.height + 1;

			if (min_y < 0) {
				adjust_y = -min_y / move_t;
				if (min_y % move_t != 0)
					adjust_y++;
			}
		}

		if (maxx > 0) {
			move_w = (int)((double)wide / maxx);
			
			if (maxx > 0) {
				horizontal.setVisible(true);
				horizontal.setMinimum(0);
				horizontal.setMaximum(maxx);
				horizontal.setValue(0);
			}
		}
		else {
			horizontal.setVisible(hshow);
			horizontal.setMinimum(0);
			horizontal.setMaximum(1);
			horizontal.setValue(0);
		}
		
		if (maxy > 0) {
			move_t = (int)((double)tall / maxy);
			if (maxx <= 0)
				this.vertical.setBounds(d.width - 8, 0, 16, d.height + 16);
			else
				this.vertical.setBounds(d.width - 8, 0, 16, d.height);
			
			vertical.setVisible(true);
			vertical.setMinimum(0);
			vertical.setMaximum(maxy);
			vertical.setValue(0);
		}
		else {
			vertical.setVisible(vshow);
			vertical.setMinimum(0);
			vertical.setMaximum(1);
			vertical.setValue(0);
		}
		
		mainPanel.setBounds(org_x, org_y, wide, tall);
		replaceComponents();
	}
	
	private void replaceComponents() {
		if (getSubComponentCount() == 0)
			return;

		int x = adjust_x + horizontal.getValue();
		int y = adjust_y + vertical.getValue();
		mainPanel.setLocation(org_x - x * move_w, org_y - y * move_t);
		
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
