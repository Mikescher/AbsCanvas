package de.abscanvas.entity;

import de.abscanvas.surface.Bitmap;

public class MenuEffectEntity extends MenuEntity {
	private int lifeticks = -111;
	
	private final int duration;
	
	public MenuEffectEntity(Bitmap[] a, int duration, int x, int y) {
		super();
		setPos(x, y);
		this.duration = duration;
		getAnimation().setAnimation(a, duration);
		getAnimation().loop(false);
	}
	
	@Override
	public void tick() {
		super.tick();
		if (lifeticks == -111) {
			lifeticks = (int)Math.round(duration/getOwner().getOwner().MPF());
		}
		lifeticks--;
		if (lifeticks <= 0) {
			remove();
		}
	}
	
	@Override
	public void handleCollision(Entity entity, double xa, double ya, boolean canPass, boolean isCollider) {
		//no collision handling
	}
}
