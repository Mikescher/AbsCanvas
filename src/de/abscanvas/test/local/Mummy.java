package de.abscanvas.test.local;

import de.abscanvas.entity.Entity;

public class Mummy extends Mob{

	public Mummy(double x, double y) {
		super(x, y, 8);
	}

	@Override
	public void handleCollision(Entity entity, double xa, double ya, boolean canPass, boolean isCollider) {
		//nocode
	}
}
