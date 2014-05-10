package de.abscanvas.test.server;

import java.awt.Color;

import de.abscanvas.level.Level;
import de.abscanvas.level.tile.Tile;

public class SpawnerTile extends Tile {
	boolean isInit = false;

	@Override
	public void init(Level level, int x, int y) {
		super.init(level, x, y);
		isInit = true;
		setMinimapColor((new Color(255, 40, 40)).getRGB());
	}

	@Override
	public void tick() {
		if (isInit) {
			TestLevel lvl = (TestLevel) getOwner();
			int c = lvl.getEntityCount(Mummy.class);

			if (c < 128) {
				if (Math.random() < 0.08) {
					Mummy nMum = new Mummy();
					nMum.setPos(getPos().getX() * getLevel().getTileWidth() + 16, getPos().getY() * getLevel().getTileHeight() + 16);
					lvl.addEntity(nMum);
				}
			}
		}
	}
}
