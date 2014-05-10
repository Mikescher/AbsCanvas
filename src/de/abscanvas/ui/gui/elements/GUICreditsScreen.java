package de.abscanvas.ui.gui.elements;

import java.awt.Font;
import java.util.ArrayList;

import de.abscanvas.surface.AbsColor;
import de.abscanvas.surface.Bitmap;
import de.abscanvas.ui.gui.GUIElement;
import de.abscanvas.ui.gui.GUIMenu;

public class GUICreditsScreen extends GUIElement {

	private double position = 0;

	private double speed = 1;
	private boolean center = true;
	private boolean drawBorder = true;
	private int borderColor = AbsColor.WHITE;
	private ArrayList<String> lines = new ArrayList<String>();
	private int backgroundColor = AbsColor.BLACK;
	private int textColor = AbsColor.WHITE;
	private Font font = new Font("Arial", java.awt.Font.PLAIN, 24);

	public GUICreditsScreen(int x, int y, int width, int height, GUIMenu owner) {
		super(x, y, width, height, new Bitmap(1, 1), owner);
	}

	@Override
	public void tick() {
		super.tick();

		repaint();
	}

	@Override
	protected void repaint() {
		ArrayList<String> tL = new ArrayList<String>(lines);

		Bitmap r = new Bitmap(getWidth(), getHeight());
		r.fill(backgroundColor);

		if (drawBorder) {
			r.lineVert(0, 0, getHeight(), borderColor);
			r.lineVert(getWidth() - 1, 0, getHeight(), borderColor);
			r.lineHorz(0, 0, getWidth() - 1, borderColor);
			r.lineHorz(0, getHeight() - 1, getWidth(), borderColor);
		}

		int h = font.getSize() + 3;

		while (tL.size() * h < getHeight()) {
			tL.add("");
		}

		position -= speed;
		if ((position + (tL.size() * h)) < 0) {
			position += (tL.size() * h);
		}

		tL.addAll(tL);

		int x;
		int y;
		for (int i = 0; i < tL.size(); i++) {
			if (center) {
				x = getWidth() / 2 - Bitmap.getStringWidth(font, tL.get(i)) / 2;
			} else {
				x = 5;
			}
			y = (int) Math.round(i * h + position);
			r.drawString(tL.get(i), font, textColor, x, y);
		}
		setImage(r);
	}

	public void setLines(ArrayList<String> lines) {
		this.lines = lines;
	}

	public void addLines(String line) {
		this.lines.add(line);
	}

	public void clearLines() {
		this.lines.clear();
	}

	public ArrayList<String> getLines() {
		return lines;
	}

	public void setBackgroundColor(int backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public int getBackgroundColor() {
		return backgroundColor;
	}

	public void setCenterLines(boolean center) {
		this.center = center;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public double getSpeed() {
		return speed;
	}

	public void setTextColor(int textColor) {
		this.textColor = textColor;
	}

	public int getTextColor() {
		return textColor;
	}

	public void setFont(Font font) {
		this.font = font;
	}

	public Font getFont() {
		return font;
	}

	public void setDrawBorder(boolean drawBorder) {
		this.drawBorder = drawBorder;
	}

	public boolean isDrawBorder() {
		return drawBorder;
	}

	public void setBorderColor(int borderColor) {
		this.borderColor = borderColor;
	}

	public int getBorderColor() {
		return borderColor;
	}
}
