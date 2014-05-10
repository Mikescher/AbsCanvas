package de.abscanvas.ui.gui;

import de.abscanvas.surface.Bitmap;

public class Font {
	private String letters = "";
	private Bitmap[][] images;
	
    public Font() {
    	// Empty constructor is empty
    }	
	
    public int getStringWidth(String s) {
        return s.length() * getCharWidth();
    }
    
    public int getCharHeight() {
        return images[0][0].getHeight();
    }

    public void draw(Bitmap screen, String msg, int x, int y) {
        msg = msg.toUpperCase();
        int length = msg.length();
        for (int i = 0; i < length; i++) {
            int c = letters.indexOf(msg.charAt(i));
            if (c < 0) continue;
            screen.blit(images[c % 29][c / 29], x, y);
            x += getCharWidth();
        }    
    }
    
    public void drawAlpha(Bitmap screen, String msg, int x, int y, int a) {
        msg = msg.toUpperCase();
        int length = msg.length();
        for (int i = 0; i < length; i++) {
            int c = letters.indexOf(msg.charAt(i));
            if (c < 0) continue;
            screen.alphaBlit(images[c % 29][c / 29], x, y, a);
            x += getCharWidth();
        }    
    }
    
    private int getCharWidth() {
    	return images[0][0].getWidth();
    }
    
	public void setFont(String letters, Bitmap[][] img) {
		this.letters = letters;
		this.images = img;
	}
}
