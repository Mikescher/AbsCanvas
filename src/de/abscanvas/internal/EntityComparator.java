package de.abscanvas.internal;

import java.util.Comparator;

import de.abscanvas.entity.Entity;

public class EntityComparator implements Comparator<Entity> {
	@Override
	public int compare(Entity e0, Entity e1) {
		if (e0.getPosY() < e1.getPosY()) {
			return -1;
		}
		if (e0.getPosY() > e1.getPosY()) {
			return +1;
		}
		if (e0.getPosX() < e1.getPosX()) {
			return -1;
		}
		if (e0.getPosX() > e1.getPosX()) {
			return +1;
		}
		return +1; // unschön , eigentlich 0, dann gibts aber keine ENtities mit gleicher Position
	}
}
