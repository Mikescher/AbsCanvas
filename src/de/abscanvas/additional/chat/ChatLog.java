package de.abscanvas.additional.chat;

import de.abscanvas.surface.AbsColor;
import de.abscanvas.surface.Bitmap;
import de.abscanvas.ui.hud.HUDElement;

public class ChatLog extends HUDElement {
	private HUDChat chat;
	private java.awt.Font font;
	private int textColor = AbsColor.WHITE;
	private int nameColor = AbsColor.getColor(255, 0, 220);
	private int clockColor = AbsColor.getColor(0, 255, 255);

	private int lineCount;
	private int lineGap;

	public ChatLog(int x, int y, int width, int height, HUDChat owner) {
		super(x, y, width, height, new Bitmap(0, 0), owner.getOwner());

		chat = owner;
		setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 10));

		lineCount = getHeight() / (font.getSize() + 2);
		lineGap = (getHeight() - (lineCount * font.getSize())) / (lineCount - 1);

		generateImage();
	}

	private void generateImage() {
		Bitmap b = new Bitmap(getWidth(), getHeight());

		int lineY = (font.getSize() + lineGap) * lineCount;

		for (int i = chat.getMessages().size() - 1; i >= 0; i--) {
			ChatMessage m = chat.getMessages().get(i);
			String cm0 = m.getMessage(chat);
			String cm1 = m.getMessagePart_1();
			String cm2 = m.getMessagePart_2(chat);
			String cm3 = m.getMessagePart_3();

			int lines = getLineCount(cm0);
			
			lineY -= font.getSize() * lines;
			lineY -= lineGap * lines;
			
			drawStringLineBreak(b, lineY, cm1 + cm2 + cm3, textColor);
			drawStringLineBreak(b, lineY, cm1 + cm2, nameColor);
			drawStringLineBreak(b, lineY, cm1, clockColor);
		}

		setImage(b);
	}

	private int getLineCount(String s) {
		int k = 1;

		while ((Bitmap.getStringWidth(font, s) > (getWidth() - 5))) {
			k++;
			s = s.substring(getMaxStringLength(s), s.length());
		}
		
		return k;
	}

	private void drawStringLineBreak(Bitmap b, int lineY, String cm, int col) {
		if (Bitmap.getStringWidth(font, cm) <= (getWidth() - 5)) {
			b.drawString(cm, font, col, 2, lineY);
			return;
		} else {
			int sw = getMaxStringLength(cm);
			String ln = cm.substring(0, sw);
			String ol = cm.substring(sw, cm.length());

			b.drawString(ln, font, col, 2, lineY);

			lineY += font.getSize() + lineGap;

			drawStringLineBreak(b, lineY, ol, col);
		}
	}

	private int getMaxStringLength(String s) {
		String ts = "";
		for (int i = 0; i < s.length(); i++) {
			if (Bitmap.getStringWidth(font, ts + s.charAt(i)) > (getWidth() - 5)) {
				break;
			}

			ts += s.charAt(i);
		}

		int tmpl = ts.length();

		while (!ts.endsWith(" ") && ts.length() > 0) {
			ts = ts.substring(0, ts.length() - 1);
		}

		if (ts.length() <= 0) {
			return tmpl;
		}

		return ts.length();
	}

	public java.awt.Font getFont() {
		return font;
	}

	public void setFont(java.awt.Font f) {
		font = f;
	}

	@Override
	public void repaint() {
		generateImage();
	}

	public int getLineCount() {
		return lineCount;
	}
}
