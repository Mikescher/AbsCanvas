package de.abscanvas.ui.gui.elements;

import de.abscanvas.surface.Animation;
import de.abscanvas.surface.Bitmap;
import de.abscanvas.surface.Surface;
import de.abscanvas.ui.gui.GUIMenu;
import de.abscanvas.ui.gui.GUIElement;

public class GUIElementAnimation extends GUIElement{

	protected Animation animation;
	
	public GUIElementAnimation(int x, int y, int width, int height, Bitmap[][] i, int speed, GUIMenu owner) {
		super(x, y, width, height, i[0][0], owner);
		animation = new Animation(owner.getOwner());
		animation.setAnimation(i, speed);
		animation.setOriginToUpperLeft();
	}
	
	@Override
	public void render(Surface surface) {
		if (visible) {
			animation.render(surface, pos.getX(), pos.getY());
		}	
	}
	
	@Override
	public void tick() {
		animation.tick();
	}
	
	@Override
	protected void repaint() {
		// nothing to do
	}
}
