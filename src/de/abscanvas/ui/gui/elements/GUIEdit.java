package de.abscanvas.ui.gui.elements;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import de.abscanvas.input.Keys;
import de.abscanvas.input.MouseButtons;
import de.abscanvas.surface.AbsColor;
import de.abscanvas.surface.Bitmap;
import de.abscanvas.ui.gui.GUIMenu;
import de.abscanvas.ui.gui.GUIElement;

public class GUIEdit extends GUIElement {
	private final static int BLINK_TIME = 31;
	
	public final static int ACCEPT_ONLYLETTER = 1;
	public final static int ACCEPT_ONLYNUMBERS = 2;
	public final static int ACCEPT_LETTERANDSPACE = 3;
	public final static int ACCEPT_NUMBERANDSPACE = 4;
	public final static int ACCEPT_NUMBERANDMINUS = 5;
	public final static int ACCEPT_NUMBERANDLETTER = 6;
	public final static int ACCEPT_LETTERANDNUMBERANDSPACE = 7;

	private java.awt.Font font;
	
	private int textColor = AbsColor.BLACK;
	private int borderColor = AbsColor.BLACK;
	private int backgroundColor = AbsColor.WHITE;

	private String text = "";

	private Keys keys;

	private boolean focused = false;

	private int maxLength = -1;

	private boolean drawBorder = true;
	private boolean doubleThickBorder = false;
	private boolean doubleThickCursor = false;

	private long blinkCounter = 0;
	private boolean actualBlink = true;
	
	private boolean acceptLetter = true;
	private boolean acceptSpace = true;
	private boolean acceptNumbers = true;
	private boolean acceptMinus = false;

	private boolean clearOnFirstClick = false;
	private boolean clearOnEveryClick = false;
	private boolean firstClick = true;

	private Bitmap[] graphics; // 0 = blinkCursor | 1 = ohne blinkCursor

	public GUIEdit(int x, int y, int width, int height, Keys keys, GUIMenu owner) {
		super(x, y, width, height, new Bitmap(1, 1), owner);
		this.keys = keys;
		font = new java.awt.Font("Arial", java.awt.Font.PLAIN, 12); // no setter, cause no repaint

		graphics = new Bitmap[2];

		repaint();
	}

	@Override
	public void onMousePress(MouseButtons mouse) {
		requestFocus();
	}

	public void setMaxLength(int i) {
		maxLength = i;
	}

	public void requestFocus() {
		setFocused(true);

		firstClick = false;
	}

	public void clear() {
		text = "";
		repaint();
	}

	public void setClearOnFirstClick() {
		clearOnFirstClick = true;
	}

	public void setClearOnClick(boolean b) {
		clearOnEveryClick = b;
	}

	@Override
	public void tick() {
		MouseButtons mb = getOwner().getOwner().getMouseButtons();
		if (mb.isPressed()) {
			if (!mb.isHovered(this)) {
				setFocused(false);
			}
		}

		if (isFocused()) {
			blinkCounter--;
			if (blinkCounter <= 0) {
				if (actualBlink) {
					blink(false);
				} else {
					blink(true);
				}
			}

			for (int i = 0; i < keys.getPressedKeys().size(); i++) {
				int k = keys.getPressedKeys().get(i).getKeyCode();
				if (isAcceptableLetter(k)) {
					setText(getText() + keys.getPressedKeys().get(i).getKeyChar());
					blink(true);
				} else if (k == KeyEvent.VK_BACK_SPACE) {
					if (text.length() >= 1) {
						setText(text.substring(0, text.length() - 1));
						blink(true);
					}
				}
			}
		}
	}
	
	private boolean isAcceptableLetter(int k) {
		return (Character.isLetter(k) && acceptLetter) || (Character.isSpaceChar(k) && acceptSpace) || (Character.isDigit(k) && acceptNumbers) || ((k == KeyEvent.VK_MINUS) && acceptMinus);
	}
	
	public void setAcceptanceMode(int m) {
		switch(m) {
		case ACCEPT_ONLYLETTER:
			acceptLetter = true;
			acceptNumbers = false;
			acceptSpace = false;
			break;
		case ACCEPT_ONLYNUMBERS:
			acceptLetter = false;
			acceptNumbers = true;
			acceptSpace = false;
			break;
		case ACCEPT_NUMBERANDMINUS:
			acceptLetter = false;
			acceptNumbers = true;
			acceptSpace = false;
			acceptMinus = true;
			break;
		case ACCEPT_LETTERANDSPACE:
			acceptLetter = true;
			acceptNumbers = false;
			acceptSpace = true;
			break;
		case ACCEPT_NUMBERANDSPACE:
			acceptLetter = false;
			acceptNumbers = true;
			acceptSpace = true;
			break;
		case ACCEPT_NUMBERANDLETTER:
			acceptLetter = true;
			acceptNumbers = true;
			acceptSpace = false;
			break;
		case ACCEPT_LETTERANDNUMBERANDSPACE:
			acceptLetter = true;
			acceptNumbers = true;
			acceptSpace = true;
			break;
		}
	}

	private void blink(boolean b) {
		if (b) {
			actualBlink = true;
			setImage(graphics[1]);
		} else {
			actualBlink = false;
			setImage(graphics[0]);
		}
		blinkCounter = BLINK_TIME;
	}

	public boolean isFocused() {
		return focused;
	}

	public void setFocused(boolean fc) {
		if (!focused && fc) {
			if (clearOnFirstClick && firstClick || clearOnEveryClick) {
				clear();
			}

			focused = fc;
			blink(fc);

			onFocusRecieve();
		} else if (focused && !fc) {
			focused = fc;
			blink(fc);

			onFocusLoose();
		}
	}

	public String getText() {
		return text;
	}

	public void setText(String txt) {
		text = txt;
		if (text.length() > maxLength && maxLength != -1) {
			text = text.substring(0, maxLength);
		}
		repaint();
	}

	public java.awt.Font getFont() {
		return font;
	}

	public void setFont(java.awt.Font f) {
		font = f;
		repaint();
	}

	public int getTextColor() {
		return textColor;
	}

	public int getBackgroundColor() {
		return backgroundColor;
	}

	public int getBorderColor() {
		return borderColor;
	}

	public void setColorScheme(int textC, int borderC, int backgroundC) {
		textColor = textC;
		backgroundColor = backgroundC;
		borderColor = borderC;

		repaint();
	}

	@Override
	protected void repaint() {
		int h = font.getSize();
		
		graphics[0] = new Bitmap(getWidth(), getHeight());
		graphics[1] = new Bitmap(getWidth(), getHeight());

		BufferedImage bi = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
		BufferedImage bi2 = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics g = bi.getGraphics();
		Graphics g2 = bi2.getGraphics();

		int w = g.getFontMetrics(font).stringWidth(getText());

		if (drawBorder) {
			g.setColor(new Color(backgroundColor));
			g.fillRect(0, 0, getWidth(), getHeight());
			g.setColor(new Color(borderColor));
			g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
			if (doubleThickBorder) {
				g.drawRect(1, 1, getWidth() - 2, getHeight() - 2);
			}
		}

		g.setColor(new Color(textColor));

		g.setFont(font);
		if (w > (getWidth() - 2)) {
			g.drawString(getText(), -(w - getWidth()) - 4, getHeight() / 2 + h / 2);
		} else {
			g.drawString(getText(), 2, getHeight() / 2 + h / 2);
		}

		g2.drawImage(bi, 0, 0, null);
		g2.setColor(new Color(textColor));

		int cursorX;
		
		if (w == 0) {
			cursorX = 5;
		} else if (w > (getWidth() - 2)) {
			cursorX = getWidth() - 3;
		} else {
			cursorX = w + 2;
		}
		
		g2.drawLine(cursorX, (getHeight() - h) / 2, cursorX, (getHeight() - h) / 2 + h);
		if (doubleThickCursor) {
			g2.drawLine(cursorX+1, (getHeight() - h) / 2, cursorX+1, (getHeight() - h) / 2 + h);
		}

		graphics[0].setImage(bi);

		graphics[1].setImage(bi2);

		setImage(graphics[0]);
	}

	public void drawBorders(boolean b) {
		drawBorder = b;

		repaint();
	}

	protected void onFocusRecieve() {
		// used to override 
	}

	protected void onFocusLoose() {
		//used to override
	}
	
	public void doubleThickCursor(boolean dtc) {
		this.doubleThickCursor = dtc;
	}

	public void doubleThickBorder(boolean dtb) {
		this.doubleThickBorder = dtb;
	}
}