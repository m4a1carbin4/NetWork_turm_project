package Client;

import java.util.List;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.border.LineBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import GUI.ScrollPanel;

public class ChattingDialog extends ScrollPanel {
	private final int max_content = 100;
	private JTextPane[] preservedText = null; 
	
	List<Hashtable<String, String>> contextList = null;

	public ChattingDialog(int preserve) {
		super(false, false);
		
		if (preserve > max_content)
			preserve = max_content;

		var line = new LineBorder(Color.BLACK, 1, true);
		
		preservedText = new JTextPane[100];
		for (int i = 0; i < preserve; i++) {
			preservedText[i] = new JTextPane();
			preservedText[i].setVisible(false);
			preservedText[i].setBorder(line);
			add(preservedText[i], false);
		}
		
		for (int i = preserve; i < max_content; i++) {
			preservedText[i] = null;
		}
	}

	public void readContent(List<Hashtable<String, String>> value) {
		if (value == null)
			return;

		contextList = value.subList(0, value.size());
		load();
	}
	
	private void load() {
		if (contextList == null)
			return;
		
		int size = contextList.size();
		if (size == 0)
			return;
		
		if (size > max_content) {
			contextList = contextList.subList(size - max_content, size);
			size = max_content;
		}
		
		var line = new LineBorder(Color.BLACK, 1, true);

		int y = 5;
		int w = this.getWidth() / 5 * 4;
		
		for (int i = 1; i <= size; i++) {
			int id = size - i;
			var m = contextList.get(id);
			
			String sender = m.get("from");
			//String receiver = m.get("to");
			String text = m.get("what");

			if (sender == null)
				sender = "?";
			if (text == null)
				continue;
			
			id = i - 1;
			
			if (preservedText[id] == null) {
				preservedText[id] = new JTextPane();
				preservedText[id].setBorder(line);
				preservedText[id].setVisible(false);
				add(preservedText[id], false);
			}
			
			preservedText[id].setText("");
			
			int fsize = preservedText[id].getFont().getSize();
			int tall = 6 * (fsize + 1);

			appendToPane(preservedText[id], "Message from ", Color.BLACK);
			appendToPane(preservedText[id], sender + "\n\n", Color.MAGENTA);
			appendToPane(preservedText[id], text, Color.BLACK);
			preservedText[id].setBounds(5, y, w, tall);
			preservedText[id].setVisible(true);
			y += tall + 10;
		}
		
		for (int i = contextList.size(); i < max_content; i++) {
			if (preservedText[i] == null)
				break;
			
			preservedText[i].setVisible(false);
		}
		Calc();
	}
	
	public void append(Hashtable<String, String> value) {
		if (value == null)
			return;
		
		if (contextList == null)
			contextList = new ArrayList<Hashtable<String, String>>();

		contextList.add(0, (Hashtable<String, String>)value.clone());
		load();
	}
	
	protected void Calc() {
		super.Calc();
		vertical.setValue(vertical.getMaximum());
		horizontal.setValue(horizontal.getMaximum());
	}
	
	private String breakLines(String str, int width, int size) {
		String result = "";
		String[] spl = str.split(" ");
		
		int w = 0;
		for (int i = 0; i < spl.length; i++) {
			int ww = size * spl[i].length();

			if (w + ww > width) {
				result += '\n';
				w = ww;
				ww = 0;
			}
			result += spl[i];
			if (w + ww == width) {
				w = 0;
				result += '\n';
			}
			else if (w + ww + size <= width) {
				w += size;
				result += ' ';
			}
		}
		
		return result;
	}
	
	private void appendToPane(JTextPane tp, String msg, Color c)
    {
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

        aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_LEFT);

        int len = tp.getDocument().getLength();
        tp.setCaretPosition(len);
        tp.setCharacterAttributes(aset, false);
        tp.replaceSelection(msg);
    }
}
