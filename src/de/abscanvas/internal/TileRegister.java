package de.abscanvas.internal;

import de.abscanvas.level.tile.Tile;

public class TileRegister {
	private final Class<? extends Tile> c;
	private final short cid;
	
	public TileRegister(Class<? extends Tile> c, short cid) {
		this.c = c;
		this.cid = cid;
	}
	
	public short getClassID() {
		return cid;
	}
	
	public short getCID() {
		return cid;
	}

	public Class<? extends Tile> getC() {
		return c;
	}
	
	public Tile create() {
		return (Tile) ClassHelper.getObject(c);
	}
}
