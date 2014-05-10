package de.abscanvas.ui.hud.elements;

import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import de.abscanvas.input.MouseButtons;
import de.abscanvas.surface.AbsColor;
import de.abscanvas.surface.Bitmap;
import de.abscanvas.ui.hud.HUD;
import de.abscanvas.ui.hud.HUDElement;
import de.abscanvas.ui.listener.ButtonListener;

public class HUDDynamicButton extends HUDElement{
	private Bitmap[] graphics;
	private Bitmap[] rawGraphics;
	private int state = 0; // 0=Normal 1=Hover 2=Pressed 3=disabled
	
	private int standardX = -1;
	private int standardY = -1;

	private boolean enabled = true;

	private final int id;

	private List<ButtonListener> listeners;
	
	private String text;
	private Font font = new Font("Arial", java.awt.Font.PLAIN, 24);
	private int fontColor[] = new int[4];
	
	public HUDDynamicButton(int x, int y, Bitmap[] i, String s, int id, HUD owner) {
		super(x, y, i[0].getWidth(), i[0].getHeight(), new Bitmap(1,1), owner);
		this.id = id;
		text = s;
		rawGraphics = i;
		fontColor[0] = AbsColor.BLACK;
		fontColor[1] = AbsColor.BLACK;
		fontColor[2] = AbsColor.BLACK;
		fontColor[3] = AbsColor.GRAY;
		
		repaint();
	}
	
	@Override
	protected void repaint() {
		graphics = new Bitmap[4];
		
		for (int i = 0; i < 4; i++) {
			int x;
			int y;
			
			graphics[i] = rawGraphics[i].copy();
			if (standardX >= 0) {
				x = standardX;
			} else {
				x = getWidth()/2 - Bitmap.getStringWidth(font, text)/2;
			}
			if (standardY >= 0) {
				y = standardY;
			} else {
				y = getHeight()/2 - font.getSize()/2;
			}
			graphics[i].drawString(text, font, fontColor[i], x, y);
		}
		
		setImage(graphics[state]);
	}
	
	public int getID() {
		return id;
	}

	@Override
	public void tick() {
		super.tick();
	}

	public void setGraphics(Bitmap[] i) {
		rawGraphics = i;
		repaint();
	}


	public void setEnabled(boolean e) {
		if (e ^ enabled) {
			enabled = e;
			if (e) {
				state = 0;
				setImage(graphics[state]);
			} else {
				state = 3;
				setImage(graphics[state]);
			}
		}
	}

	public void addListener(ButtonListener listener) {
		if (listeners == null) {
			listeners = new ArrayList<ButtonListener>();
		}
		listeners.add(listener);
	}

	@Override
	public void onMousePress(MouseButtons mouse) {
		if (enabled) {
			state = 2;
			setImage(graphics[state]);
			if (listeners != null) {
				for (ButtonListener listener : listeners) {
					listener.buttonMouseDown(mouse, id);
				}
			}
		}
	}

	@Override
	public void onMouseRelease(MouseButtons mouse) {
		if (enabled) {
			if (state == 2) {
				if (listeners != null) {
					for (ButtonListener listener : listeners) {
						listener.buttonPressed(mouse, id);
					}
				}
			}
			if (enabled) {
				state = 1;
				setImage(graphics[state]);
			}
		}
	}

	@Override
	public void onMouseEnter(MouseButtons mouse) {
		if (enabled) {
			state = 1;
			setImage(graphics[state]);
			if (listeners != null) {
				for (ButtonListener listener : listeners) {
					listener.buttonMouseEnter(mouse, id);
				}
			}
		} else {
			if (listeners != null) {
				for (ButtonListener listener : listeners) {
					listener.buttonMouseEnter(mouse, id);
				}
			}
		}
	}

	@Override
	public void onMouseLeave(MouseButtons mouse) {
		if (enabled) {
			state = 0;
			setImage(graphics[state]);
			if (listeners != null) {
				for (ButtonListener listener : listeners) {
					listener.buttonMouseLeave(mouse, id);
				}
			}
		} else {
			if (listeners != null) {
				for (ButtonListener listener : listeners) {
					listener.buttonMouseLeave(mouse, id);
				}
			}
		}
	}

	public void setText(String text) {
		this.text = text;
		repaint();
	}

	public String getText() {
		return text;
	}

	public void setColorSheme(int fontColor, int fontColor_Hover, int fontColor_Pressed, int fontColor_disabled) {
		setFontColor(fontColor, fontColor_Hover, fontColor_Pressed, fontColor_disabled);
	}

	public void setFontColor(int fontColor, int fontColor_Hover, int fontColor_Pressed, int fontColor_disabled) {
		this.fontColor[0] = fontColor;
		this.fontColor[1] = fontColor_Hover;
		this.fontColor[2] = fontColor_Pressed;
		this.fontColor[3] = fontColor_disabled;
		repaint();
	}

	public int[] getFontColor() {
		return fontColor;
	}
	
	public void setFontColor(int fontColor) {
		this.fontColor[0] = fontColor;
		this.fontColor[1] = fontColor;
		this.fontColor[2] = fontColor;
		this.fontColor[3] = fontColor;
		repaint();
	}

	public void setFont(Font font) {
		this.font = font;
		repaint();
	}

	public Font getFont() {
		return font;
	}

	public void setStandardX(int standardX) {
		this.standardX = standardX;
		repaint();
	}

	public void resetStandardX() {
		standardX = -1;
		repaint();
	}

	public void setStandardY(int standardY) {
		this.standardY = standardY;
		repaint();
	}

	public void resetStandardY() {
		standardY = -1;
		repaint();
	}

}
