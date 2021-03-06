package de.abscanvas.test.local;

import de.abscanvas.level.Level;
import de.abscanvas.level.tile.Tile;

public class FloorTile extends Tile {
    @Override
	public void init(Level level, int x, int y) {
        super.init(level, x, y);
        setImage(GameArt.floorTile);
        setMinimapColor(GameArt.floorTileColor);
    }
}
