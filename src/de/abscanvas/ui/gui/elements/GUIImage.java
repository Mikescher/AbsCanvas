package de.abscanvas.ui.gui.elements;

import de.abscanvas.surface.Bitmap;
import de.abscanvas.ui.gui.GUIMenu;
import de.abscanvas.ui.gui.GUIElement;

public class GUIImage extends GUIElement {
	public GUIImage(int x, int y, Bitmap n, GUIMenu owner) {
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
