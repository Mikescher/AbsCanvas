package de.abscanvas.entity;

import de.abscanvas.surface.AbsColor;
import de.abscanvas.surface.Bitmap;
import de.abscanvas.surface.Surface;

public class DebugRenderer {
	private final Entity e;
	
	public DebugRenderer(Entity ent) {
		e = ent;
	}

	public void render(Surface surface) {
		Bitmap b = new Bitmap(e.getWidth(), e.getHeight());
		b.rectangle(0, 0, e.getRadius().getX()*2, e.getRadius().getY()*2, AbsColor.MAGENTA);
		surface.blit(b, e.getPosX() - e.getRadius().getX(), e.getPosY() - e.getRadius().getY());
	}

}
