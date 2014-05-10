package de.abscanvas.level.tile;

import de.abscanvas.surface.Bitmap;

public class EmptyTile extends Tile {
	public EmptyTile() {
		setImage(new Bitmap(0, 0));
	}
}
