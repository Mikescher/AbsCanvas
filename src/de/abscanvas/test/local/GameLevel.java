package de.abscanvas.test.local;

import de.abscanvas.Screen;
import de.abscanvas.entity.LevelEffectEntity;
import de.abscanvas.entity.LevelEntity;
import de.abscanvas.input.MouseButtons;
import de.abscanvas.level.Level;
import de.abscanvas.level.tile.Tile;
import de.abscanvas.math.MPoint;

public class GameLevel extends Level {
	
	private final static int LVL_TILE_WIDTH = 32;
	private final static int LVL_TILE_HEIGHT = 32;
	
	private final static int LVL_WIDTH = 128 + 1;
	private final static int LVL_HEIGHT = 	128 + 1;

	public GameLevel(Screen o) {
		super(o, LVL_TILE_WIDTH, LVL_TILE_HEIGHT, LVL_WIDTH, LVL_HEIGHT);
		create();
	}
	
	private void create() {
		for (int x = 0; x < getWidth(); x++) {
			for (int y = 0; y < getHeight(); y++) {
				if (Math.random()<0.1) {
					setTile(x, y, new HillTile());
				} else {
					setTile(x, y, new FloorTile());
				}
			}
		}
		
		@SuppressWarnings("unchecked")
		Class<? extends LevelEntity>[] paintOrder = new Class[3];
		
		paintOrder[0] = LevelEffectEntity.class;
		paintOrder[1] = Mummy.class;
		paintOrder[2] = Player.class;
		
		setPaintOrder(paintOrder);
		
		((GameScreen)owner).player = new Player(owner.getKeys(), owner, width * getTileWidth()/2 , height * getTileHeight()/2);
		LevelEntity player = ((GameScreen)owner).player;
		addEntity( player );
		
		MPoint tp = player.getRoundedPos();
		tp.div(getTileWidth());
		
		Tile tile = new RockTile();
		setTile(tp.getX()+0, tp.getY()+0, tile);
		tile = new RockTile();
		setTile(tp.getX()-1, tp.getY()-1, tile);
		tile = new RockTile();
		setTile(tp.getX()-1, tp.getY()+0, tile);
		tile = new RockTile();
		setTile(tp.getX()-1, tp.getY()+1, tile);
		tile = new RockTile();
		setTile(tp.getX()+0, tp.getY()-1, tile);
		tile = new RockTile();
		setTile(tp.getX()+1, tp.getY()-1, tile);
		tile = new RockTile();
		setTile(tp.getX()+1, tp.getY()+0, tile);
		tile = new RockTile();
		setTile(tp.getX()+1, tp.getY()+1, tile);
		tile = new RockTile();
		setTile(tp.getX()+0, tp.getY()+1, tile);
	}

	@Override
	public void tick() {
		super.tick();
	}

	@Override
	public void onTilePressed(MouseButtons mouse) {
		System.out.println("TilePressed");
	}

	@Override
	public void onMouseMove(MouseButtons mouse) {
		//nocode
	}

	@Override
	public void onHUDPressed(MouseButtons mouse) {
		System.out.println("HUDPressed");
	}

	@Override
	public void onPressed(MouseButtons mouse) {
		//nocode
	}
}
