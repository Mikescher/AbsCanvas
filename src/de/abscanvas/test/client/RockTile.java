package de.abscanvas.test.client;

import de.abscanvas.level.Level;
import de.abscanvas.level.tile.Tile;

public class RockTile extends Tile {
    @Override
	public void init(Level level, int x, int y) {
        super.init(level, x, y);
        setImage(GameArt.rockTile);
        setMinimapColor(GameArt.rockTileColor);
    }
}
