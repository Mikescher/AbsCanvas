package de.abscanvas.ui.gui.elements;

import de.abscanvas.surface.Animation;
import de.abscanvas.surface.Bitmap;
import de.abscanvas.surface.Surface;
import de.abscanvas.ui.gui.GUIMenu;
import de.abscanvas.ui.gui.GUIElement;

public class GUIAnimatedImage extends GUIElement{
	private Animation animation;
	
	public GUIAnimatedImage(int x, int y, Bitmap[][] i, int aniSpeed, GUIMenu owner) {
		super(x, y, i[0][0].getWidth(), i[0][0].getHeight(), i[0][0], owner);
		animation = new Animation(owner.getOwner());
		animation.setAnimation(i, aniSpeed);
		animation.setOriginToUpperLeft();
	}

	@Override
	public void tick() {
		super.tick();
		animation.tick();
	}
	
	@Override
	public void render(Surface s) {
		if (visible) {
			animation.render(s, pos.getX(), pos.getY());
		}
	}
	
	@Override
	protected void repaint() {
		// nothing to do
	}
}
