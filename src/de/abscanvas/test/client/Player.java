package de.abscanvas.test.client;

import de.abscanvas.entity.Entity;
import de.abscanvas.entity.Facing;
import de.abscanvas.entity.network.ClientEntity;
import de.abscanvas.input.MouseButtons;
import de.abscanvas.math.MDPoint;
import de.abscanvas.surface.AbsColor;
import de.abscanvas.surface.Surface;

public class Player extends ClientEntity {
	public Facing facing = new Facing(8);

	public int speed = 3;

	public int shootDelay = 0;

	public int steps = 0;

	public MDPoint nextStep = new MDPoint(0, 0);

	public long lastStep = System.currentTimeMillis();

	public boolean plusspeed = false;
	
	public Player() {
		getAnimation().setAnimation(GameArt.herrSpeck, (700));
		setMinimapColor((AbsColor.getColor(0, 106, 255)));
		getAnimation().animate(false);
	}
	
	@Override
	public void tick() {
		super.tick();

		MDPoint aMov = new MDPoint(nextStep);

		if (!aMov.isZero()) {
			lastStep = System.currentTimeMillis();

			walk(aMov);
			nextStep.set(0, 0);
		}

		getAnimation().animate((System.currentTimeMillis() - lastStep) < 250);
	}

	public void walk(MDPoint aMov) {
		facing.setVector(aMov);
		getAnimation().setLayer(facing);

		if (plusspeed) {
			aMov.setLength(speed*2);
		} else {
			aMov.setLength(speed);
		}
		

		move(aMov.getX(), aMov.getY());

		steps++;
	}

	@Override
	public void render(Surface surface) {
		getAnimation().render(surface, getPosX(), getPosY() - 9);
	}

	@Override
	public void handleCollision(Entity entity, double xa, double ya, boolean canPass, boolean isCollider) {
		//nocode
	}

	@Override
	public void onMousePress(MouseButtons mouse) {
		//nocode
	}
	
	@Override
	public boolean canPass(Entity e) {
		return false;
	}
}
