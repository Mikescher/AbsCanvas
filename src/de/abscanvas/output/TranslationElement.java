package de.abscanvas.output;

import de.abscanvas.level.tile.Tile;

public class TranslationElement {
	private final int id;
	private Class<? extends Tile> c;
	
	public TranslationElement(int id, Class<? extends Tile> c) {
		this.id = id;
		this.c = c;
	}

	public Class<? extends Tile> getC() {
		return c;
	}

	public int getCID() {
		return id;
	}
}
