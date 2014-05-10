package de.abscanvas.test.server;

import de.abscanvas.entity.Entity;
import de.abscanvas.level.Level;
import de.abscanvas.level.tile.Tile;
import de.abscanvas.test.client.GameArt;

public class HillTile extends Tile {
    @Override
	public void init(Level level, int x, int y) {
        super.init(level, x, y);
        setMinimapColor(GameArt.hillTileColor);
    }
    
    @Override
	public boolean canPass(Entity e) {
		return false;
	}
}
