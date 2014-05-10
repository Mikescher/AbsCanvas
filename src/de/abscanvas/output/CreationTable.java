package de.abscanvas.output;

import de.abscanvas.level.Level;
import de.abscanvas.level.tile.Tile;

public abstract class CreationTable {
	public abstract Tile createTile(int tid);
	
	public abstract Level createLevel(int w, int h);
}
