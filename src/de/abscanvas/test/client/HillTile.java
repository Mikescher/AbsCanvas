package de.abscanvas.test.client;

import de.abscanvas.entity.Entity;
import de.abscanvas.level.Level;
import de.abscanvas.level.tile.Tile;

public class HillTile extends Tile {

    @Override
	public void init(Level level, int x, int y) {
        super.init(level, x, y);
        setImage(GameArt.hillTile);
        setMinimapColor(GameArt.hillTileColor);
    }
    
    @Override
	public boolean canPass(Entity e) {
		return false;
	}
}
