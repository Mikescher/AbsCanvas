package de.abscanvas.output;

import de.abscanvas.level.Level;
import de.abscanvas.level.ServerLevel;
import de.abscanvas.level.tile.Tile;

public class ServerCreationTable extends CreationTable {
	private ServerLevel sl;
	
	public ServerCreationTable(ServerLevel sl) {
		this.sl = sl;
	}
	
	@Override
	public Tile createTile(int tid) {	
		return (sl.findTileRegister((short) tid)).create();
	}

	@Override
	public Level createLevel(int w, int h) {
		return null;
	}

}
