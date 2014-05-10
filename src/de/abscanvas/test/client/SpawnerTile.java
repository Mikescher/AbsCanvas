package de.abscanvas.test.client;

import java.awt.Color;

import de.abscanvas.level.Level;
import de.abscanvas.level.tile.Tile;

public class SpawnerTile extends Tile{
    @Override
	public void init(Level level, int x, int y) {
        super.init(level, x, y);
        setImage(GameArt.spawnerTile);
        setMinimapColor((new Color(255, 40, 40)).getRGB());
    }
}
