package de.abscanvas.output;

import de.abscanvas.level.tile.EmptyTile;
import de.abscanvas.level.tile.Tile;

public class InterfaceTranslationTable extends TranslationTable {
	@Override
	public int getID(Tile t) {
		if (t instanceof EmptyTile) {
			return 0;
		}
		
		if (t instanceof VariableIDHolder) {
			return ((VariableIDHolder)t).getActualID();
		}
		
		return -1;
	}

}
