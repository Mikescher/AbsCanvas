package de.abscanvas.output;

import java.util.ArrayList;

import de.abscanvas.level.tile.EmptyTile;
import de.abscanvas.level.tile.Tile;

public class ClassTranslationTable extends TranslationTable {
	private ArrayList<TranslationElement> elemLst = new ArrayList<TranslationElement>();
	
	public void addLink(int id, Class<? extends Tile> c) {
		elemLst.add(new TranslationElement(id, c));
	}
	
	@Override
	public int getID(Tile t) {
		if (t instanceof EmptyTile) {
			return 0;
		}
		
		for (TranslationElement reg : elemLst) {
			if (reg.getC().isAssignableFrom(t.getClass())) {
				return reg.getCID();
			}
		}
		
		return -1;
	}

}
