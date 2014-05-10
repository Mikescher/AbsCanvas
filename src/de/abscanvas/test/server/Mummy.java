package de.abscanvas.test.server;

import de.abscanvas.entity.Entity;
import de.abscanvas.entity.Facing;
import de.abscanvas.entity.network.ServerEntity;
import de.abscanvas.math.MDPoint;
import de.abscanvas.network.NetworkUser;
import de.abscanvas.surface.AbsColor;
import de.abscanvas.test.server.Player;

public class Mummy extends ServerEntity {

	int direc = 0;
	int lastDirecChange = 0;
	int nextDirecChange = 120;

	MDPoint wk = new MDPoint(0, -1);

	public Facing facing = new Facing(8);

	public float speed = 1.5f;

	public long lastStep = System.currentTimeMillis();

	public Mummy() {
		getAnimation().setAnimation(GameArt.lordLard, (700));
		setMinimapColor((AbsColor.getColor(175, 66, 7)));
		direc = (int) (Math.random() * 7);
		calcNewDirec();
	}

	@Override
	public void handleCollision(Entity entity, double xa, double ya, boolean canPass, boolean isCollider) {
		if (entity instanceof Player) {
			remove();
		}
	}

	@Override
	public boolean canPass(Entity e) {
		return true;
	}

	@Override
	public void tick() {
		lastDirecChange++;
		if (lastDirecChange > nextDirecChange) {
			calcNewDirec();
		}

		boolean success = false;
		int counter = 0;

		while (!success && counter < 16) {
			counter++;
			success = canMove(wk.getX(), wk.getY());
			if (success) {
				walk(wk);
			} else {
				calcNewDirec();
			}
		}
	}

	private void calcNewDirec() {
		nextDirecChange = (int) (50 + (Math.random() * 60 - 30));
		lastDirecChange = 0;
		if (Math.random() > 0.5) {
			direc++;
		} else {
			direc--;
		}
		if (direc < 0) {
			direc += 8;
		} else if (direc > 7) {
			direc -= 8;
		}

		int m_x = 0;
		int m_y = 0;

		switch (direc) {
		case 0:
			m_x = 0;
			m_y = -1;
			break;
		case 1:
			m_x = 1;
			m_y = -1;
			break;
		case 2:
			m_x = 1;
			m_y = 0;
			break;
		case 3:
			m_x = 1;
			m_y = 1;
			break;
		case 4:
			m_x = 0;
			m_y = 1;
			break;
		case 5:
			m_x = -1;
			m_y = 1;
			break;
		case 6:
			m_x = -1;
			m_y = 0;
			break;
		case 7:
			m_x = -1;
			m_y = -1;
			break;
		}

		wk.set(m_x, m_y);
	}

	public boolean walk(MDPoint aMov) {
		boolean res = true;

		if (!aMov.isZero()) {
			lastStep = System.currentTimeMillis();

			facing.setVector(aMov);
			getAnimation().setLayer(facing);

			aMov.setLength(speed);

			res = move(aMov.getX(), aMov.getY());
		}
		getAnimation().animate((System.currentTimeMillis() - lastStep) < 250);

		return res;
	}

	@Override
	public boolean isControllableByUser(NetworkUser controllRequest) {
		return false;
	}

	@Override
	public void onAfterClientControlMove(MDPoint move) {
		//nocode
	}
}
