package de.abscanvas.ui.hud.elements;

import de.abscanvas.surface.Animation;
import de.abscanvas.surface.Bitmap;
import de.abscanvas.surface.Surface;
import de.abscanvas.ui.hud.HUD;
import de.abscanvas.ui.hud.HUDElement;

public class HUDElementAnimation extends HUDElement{
	protected Animation animation;
	
	public HUDElementAnimation(int x, int y, int width, int height, Bitmap[][] i, int speed, HUD owner) {
		super(x, y, width, height, i[0][0], owner);
		animation = new Animation(owner.getOwner().getOwner());
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
		// nothing to do here
	}
}
