package de.abscanvas.ui.hud.elements;

import de.abscanvas.surface.Bitmap;
import de.abscanvas.surface.Surface;
import de.abscanvas.ui.hud.HUD;
import de.abscanvas.ui.hud.HUDElement;

public class HUDNativeLabel extends HUDElement {
	private String txt = "";

	public HUDNativeLabel(int x, int y, String txt, HUD owner) {
		super(x, y, 0, 0, new Bitmap(0, 0), owner);
		setText(txt);
	}

	public void setText(String text) {
		if (!txt.equals(text)) {
			txt = text;
			
			setWidth(owner.getOwner().getOwner().getACFont().getStringWidth(txt));
			setHeight(owner.getOwner().getOwner().getACFont().getCharHeight());
		}
	}

	public String getText() {
		return txt;
	}

	@Override
	public void render(Surface surface) {
		if (visible) {
			owner.getOwner().getOwner().drawString(getText(), getX(), getY());
		}
	}
	
	@Override
	protected void repaint() {
		// nothing to do here
	}
}
