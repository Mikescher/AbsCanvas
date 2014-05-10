package de.abscanvas.test.local;

import de.abscanvas.entity.Entity;
import de.abscanvas.entity.Facing;
import de.abscanvas.entity.LevelEntity;
import de.abscanvas.math.MDPoint;

public abstract class Mob extends LevelEntity {
	
	public Facing facing;
	private double speed = 1.0;
	double dir = 0;
	private int maxHealth = 10;
	private int health = maxHealth;
	private boolean isImmortal = false;

	public Mob(double x, double y, int facingDivisions) {
		super();
		facing = new Facing(facingDivisions);
		setPos(x, y);
	}

	public void setStartHealth(int newHealth) {
		maxHealth = health = newHealth;
	}

	public double getSpeed() {
		return speed;
	}
	
	public void setSpeed(double s) {
		speed = s;
	}
	
	public Facing getFacing() {
		return facing;
	}

	public void deltaMove(MDPoint v) {
		super.move(v.getX(), v.getY());
	}

	@Override
	public void tick() {
		super.tick();
		getAnimation().setLayer(facing.get());
		if (health <= 0) {
			die();
			remove();
			return;
		}
	}

	public void slideMove(double xa, double ya) {
		move(xa, ya);
	}

	public void die() {
		//nocode
	}

	/**
	 * @param source  
	 */
	public void hurt(Entity source, int damage) {
		if (isImmortal) {
			return;
		}

		health -= damage;
		if (health < 0) {
			health = 0;
		}
	}
}
