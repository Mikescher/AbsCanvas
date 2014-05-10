package de.abscanvas.test.server;

import de.abscanvas.entity.Entity;
import de.abscanvas.entity.Facing;
import de.abscanvas.entity.network.ServerEntity;
import de.abscanvas.input.MouseButtons;
import de.abscanvas.math.MDPoint;
import de.abscanvas.network.NetworkUser;
import de.abscanvas.surface.AbsColor;

public class Player extends ServerEntity {
	public Facing facing = new Facing(8);
	
	public Player() {
		getAnimation().setAnimation(GameArt.herrSpeck, (700));
		setMinimapColor((AbsColor.getColor(0, 106, 255)));
	}

	@Override
	public boolean canPass(Entity e) {
		return false;
	}

	@Override
	public void handleCollision(Entity entity, double xa, double ya, boolean canPass, boolean isCollider) {
		//nocode
	}

	@Override
	public void onInit(int tileW, int tileH) {
		super.onInit(tileH, tileH);
	}

	@Override
	public void onMousePress(MouseButtons mouse) {
		//nocode
	}

	@Override
	public boolean isControllableByUser(NetworkUser controllRequest) {
		return true;
	}

	@Override
	public void onAfterClientControlMove(MDPoint move) {
		facing.setVector(move);
		getAnimation().setLayer(facing);
	}
	
}
