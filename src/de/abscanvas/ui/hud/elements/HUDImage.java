package de.abscanvas.ui.hud.elements;

import de.abscanvas.surface.Bitmap;
import de.abscanvas.ui.hud.HUD;
import de.abscanvas.ui.hud.HUDElement;

public class HUDImage extends HUDElement {
	public HUDImage(int x, int y, Bitmap n, HUD owner) {
		super(x, y, n.getWidth(), n.getHeight(), n, owner);
	}
	
	@Override
	public void setImage(Bitmap b) {
		super.setImage(b);
	}

	@Override
	protected void repaint() {
		// nothing to do
	}
}
