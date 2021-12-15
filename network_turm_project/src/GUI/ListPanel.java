package GUI;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.Comparator;

import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.border.LineBorder;

public class ListPanel extends JPanel implements ActionListener, AdjustmentListener, MouseWheelListener {
	private ImagedButton9[] attribute;
	private ImagedButton9[] item_button;
	private boolean[] item_order;

	private ArrayList<String[]> item_texts;

	JScrollBar scroll;

	ActionListener target;

	private int item_size;

	public void setSize(int w, int t) {
		super.setSize(w, t);
		replaceElements();
	}

	public void setBounds(int x, int y, int w, int t) {
		super.setBounds(x, y, w, t);
		replaceElements();
	}

	public void updateAttr(String[] new_attr, boolean clear) {
		if (attribute != null) {
			for (int i = 0; i < attribute.length; i++)
				remove(attribute[i]);
		}

		attribute = new ImagedButton9[new_attr.length];
		for (int i = 0; i < new_attr.length; i++) {
			attribute[i] = new ImagedButton9(this, new_attr[i], "gui/imagedbutton9/button", "Sort " + i);
			add(attribute[i]);
		}
		
		item_order = new boolean[new_attr.length];
		for (int i = 0; i < new_attr.length; i++)
			item_order[i] = false;

		if (clear) {
			deleteAllItem();
			replaceElements();
		}
	}

	public void deleteAllItem() {
		item_texts.clear();
		Calc();
	}

	public void updateItemSize(int value) {
		if (item_button != null)
			for (int i = 0; i < item_button.length; i++)
				this.remove(item_button[i]);

		item_size = value;

		item_button = new ImagedButton9[item_size];

		for (int i = 0; i < item_size; i++) {
			item_button[i] = new ImagedButton9(this, "", "gui/imagedbutton9/button", "Item " + i);
			this.add(item_button[i]);
		}

		Calc();
		replaceElements();
	}

	public void addItem(String[] text, String command) {
		if (attribute == null)
			return;
		if (text.length != attribute.length)
			return;

		String[] newText = new String[attribute.length + 1];
		for (int i = 0; i < attribute.length; i++)
			newText[i] = text[i];
		newText[attribute.length] = command;
		item_texts.add(newText);

		Calc();
	}
	
	public int getAttrSize() {
		if (attribute == null)
			return 0;
		
		return attribute.length;
	}
	
	public int getItemSize() {
		if (item_texts == null)
			return 0;
		
		return item_texts.size();
	}

	public ListPanel(ActionListener target, String[] att, int item_size) {
		super();
		super.setLayout(null);

		this.setBackground(Color.WHITE);
		setBorder(new LineBorder(Color.BLACK, 1, true));

		this.target = target;

		scroll = new JScrollBar(JScrollBar.VERTICAL);
		scroll.addAdjustmentListener(this);
		add(scroll);

		init();

		updateAttr(att, false);
		updateItemSize(item_size);

		this.addMouseWheelListener(this);
	}

	private void init() {
		item_texts = new ArrayList<String[]>();
	}

	private void replaceElements() {
		if (attribute == null)
			return;

		var d = getSize();

		scroll.setBounds(d.width - 18, 2, 16, d.height - 4);

		int aw = (d.width - 20) / attribute.length;

		int att_tall = 25;
		if (att_tall > d.height / 8)
			att_tall = d.height / 8;

		for (int i = 0; i < attribute.length; i++)
			attribute[i].setBounds(2 + aw * i, 4, aw - 2, att_tall);

		if (item_size == 0)
			return;

		int y = 6 + att_tall;
		int it = (d.height - att_tall - 6) / item_size;

		for (int i = 0; i < item_size; i++) {
			item_button[i].setBounds(2, y, d.width - 22, it - 2);
			y += it;
		}
		repaint();
	}

	class StringArrayComparator implements Comparator<String[]> {
		public final int pos;
		public final boolean order;

		public StringArrayComparator(int pos, boolean order) {
			this.pos = pos;
			this.order = order;
		}

		@Override
		public int compare(String[] a, String[] b) {
			if (order)
				return -1 * a[pos].compareTo(b[pos]);

			return a[pos].compareTo(b[pos]);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (target == null)
			return;

		var spl = e.getActionCommand().split(" ");
		if (spl.length != 2)
			return;

		int id = Integer.parseInt(spl[1]);
		
		switch (spl[0]) {
		case "Sort":
			item_texts.sort(new StringArrayComparator(id, item_order[id]));
			
			if (item_order[id])
				item_order[id] = false;
			else
				item_order[id] = true;
			
			scroll.setValue(0);
			Update();
			break;
		case "Item":
			int value = scroll.getValue();
			if (id + value >= item_texts.size())
				return;
			
			target.actionPerformed(new ActionEvent(e.getSource(), e.getID(), item_texts.get(id + value)[attribute.length]));
			return;
		}
		target.actionPerformed(e);
	}

	private void Calc() {
		int count = item_texts.size() - item_size + 1;

		System.out.println(count);

		if (count <= 0) {
			scroll.setEnabled(false);

			scroll.setValue(0);
			scroll.setMinimum(0);
			scroll.setMaximum(1);
		} else {
			int prev = scroll.getValue();

			scroll.setEnabled(true);

			scroll.setValue(Math.min(prev, count));
			scroll.setMinimum(0);
			scroll.setMaximum(count);
		}

		Update();
	}

	private void Update() {
		if (item_texts.size() == 0)
			for (int i = 0; i < attribute.length; i++)
				attribute[i].setEnabled(false);
		else
			for (int i = 0; i < attribute.length; i++)
				attribute[i].setEnabled(true);
		
		int value = scroll.getValue();
		for (int i = 0; i < item_size; i++) {
			if (i + value >= item_texts.size()) {
				item_button[i].setText("");
				item_button[i].setEnabled(false);
				continue;
			}

			var texts = item_texts.get(i + value);
			String newText = "";
			for (int j = 0; j < attribute.length; j++) {
				if (j > 0)
					newText += "                      |                      ";
				newText += texts[j];
			}
			item_button[i].setText(newText);
			item_button[i].setEnabled(true);
		}
		repaint();
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if (e.getWheelRotation() == 1)
			scroll.setValue(scroll.getValue() + 1);
		else if (e.getWheelRotation() == -1)
			scroll.setValue(scroll.getValue() - 1);
	}

	@Override
	public void adjustmentValueChanged(AdjustmentEvent e) {
		Update();
	}
}
