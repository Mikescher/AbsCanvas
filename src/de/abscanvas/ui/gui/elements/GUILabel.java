package de.abscanvas.ui.gui.elements;

import de.abscanvas.surface.AbsColor;
import de.abscanvas.surface.Bitmap;
import de.abscanvas.surface.Surface;
import de.abscanvas.ui.gui.GUIMenu;
import de.abscanvas.ui.gui.GUIElement;

public class GUILabel extends GUIElement {
	private String txt;
	
	private java.awt.Font font;
	private int color = AbsColor.BLACK; // BLACK
	
	private int maxHeight = -1;
	private int maxWidth = -1;
	
	public GUILabel(int x, int y, String txt, GUIMenu owner) {
		super(x, y, 0, 0, new Bitmap(0,0), owner);
		this.txt = txt;
		setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 12));
		
		repaint();
	}
	
	public GUILabel(int x, int y, int width, int height, String txt, GUIMenu owner) {
		super(x, y, 0, 0, new Bitmap(0,0), owner);
		this.txt = txt;
		maxHeight = height;
		maxWidth = width;
		setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 12));
		
		repaint();
	}
	
	public void setText(String text) {
		if (! txt.equals(text)) {
			txt = text;
			repaint();
		}
	}

	public java.awt.Font getFont() {
		return font;
	}

	public void setFont(java.awt.Font f) {
		font = f;
		repaint();
	}

	public int getColor() {
		return color;
	}

	public void setColor(int c) {
		color = c;
		repaint();
	}
	
	public String getText() {
		return txt;
	}
	
	@Override
	protected void repaint() {
		int w = txt.length()*font.getSize();
		int h = (int)(font.getSize() *1.5);
		if (maxHeight>0) {
			h = maxHeight;
		}
		if (maxWidth>0) {
			w = maxWidth;
		}
		if (w <= 0) {
			w = 1;
		}
		
		Bitmap b = new Bitmap(w, h);
		b.drawString(txt, font, color, 1, 1);
		setImage(b);
	}
	
	@Override
	public void render(Surface surface) {
		if (visible) {
			surface.blit(img, pos.getX(), pos.getY());
		}
	}
}
