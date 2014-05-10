package de.abscanvas.test.client;

import de.abscanvas.entity.Entity;
import de.abscanvas.entity.network.ClientEntity;
import de.abscanvas.surface.AbsColor;
import de.abscanvas.surface.Surface;

public class Mummy extends ClientEntity {

	public Mummy() {
		getAnimation().setAnimation(GameArt.lordLard, (700));
		setMinimapColor((AbsColor.getColor(175, 66, 7)));
	}
	
	@Override
	public void render(Surface surface) {
		getAnimation().render(surface, getPosX(), getPosY() - 9);
	}

	@Override
	public void handleCollision(Entity entity, double xa, double ya, boolean canPass, boolean isCollider) {
		//nocode
	}
}
