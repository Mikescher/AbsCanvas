package de.abscanvas.math;

import de.abscanvas.entity.Entity;

public interface BoxBoundsOwner {
	public void handleCollision(Entity entity, double xa, double ya, boolean canPass, boolean isCollider);
	
	public boolean canPass(Entity e);
}
