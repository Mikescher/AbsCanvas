package de.abscanvas.ui.gui.elements;

import de.abscanvas.surface.Bitmap;
import de.abscanvas.surface.Surface;
import de.abscanvas.ui.gui.GUIMenu;
import de.abscanvas.ui.gui.GUIElement;

public class GUINativeLabel extends GUIElement {
	private String txt = "";

	public GUINativeLabel(int x, int y, String txt, GUIMenu owner) {
		super(x, y, 0, 0, new Bitmap(0, 0), owner);
		setText(txt);
	}

	public void setText(String text) {
		if (!txt.equals(text)) {
			txt = text;
			
			setWidth(owner.getOwner().getACFont().getStringWidth(txt));
			setHeight(owner.getOwner().getACFont().getCharHeight());
		}
	}

	public String getText() {
		return txt;
	}

	@Override
	public void render(Surface surface) {
		if (visible) {
			owner.getOwner().drawString(getText(), getX(), getY());
		}
	}
	
	@Override
	protected void repaint() {
		// nothing to do here
	}
}
