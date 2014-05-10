package de.abscanvas.ui.hud.elements;

import de.abscanvas.surface.Animation;
import de.abscanvas.surface.Bitmap;
import de.abscanvas.surface.Surface;
import de.abscanvas.ui.hud.HUD;
import de.abscanvas.ui.hud.HUDElement;

public class HUDAnimatedImage extends HUDElement{
	private Animation animation;
	
	public HUDAnimatedImage(int x, int y, Bitmap[][] i, int aniSpeed, HUD owner) {
		super(x, y, i[0][0].getWidth(), i[0][0].getHeight(), i[0][0], owner);
		animation = new Animation(owner.getOwner().getOwner());
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
		// nothing to do here
	}
}
