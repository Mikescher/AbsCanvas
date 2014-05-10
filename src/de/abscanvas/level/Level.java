package de.abscanvas.level;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import de.abscanvas.Screen;
import de.abscanvas.entity.LevelEffectEntity;
import de.abscanvas.entity.LevelEntity;
import de.abscanvas.input.MouseButtons;
import de.abscanvas.internal.EntityComparator;
import de.abscanvas.level.tile.*;
import de.abscanvas.math.BoxBounds;
import de.abscanvas.math.MDPoint;
import de.abscanvas.math.MPoint;
import de.abscanvas.sound.SoundPlayer;
import de.abscanvas.surface.Bitmap;
import de.abscanvas.surface.Surface;
import de.abscanvas.ui.hud.HUD;

public abstract class Level {
	public static final int ALIGN_UPPERLEFT = 1;
	public static final int ALIGN_UPPERRIGHT = 2;
	public static final int ALIGN_BOTTOMLEFT = 3;
	public static final int ALIGN_BOTTOMRIGHT = 4;
	public static final int ALIGN_CENTER = 5;

	protected int width, height;
	
	private final int tile_width;
	private final int tile_height;

	protected Screen owner;

	protected HUD hud = null;

	protected Tile[][] tiles;

	private int background_alignement = 1;
	private Bitmap background_img = new Bitmap(0, 0);

	protected boolean tickTiles = true;
	protected boolean tickEntities = true;
	protected boolean tickHUD = true;

	private long uidCounter = 0;

	protected List<LevelEntity>[][] entityMap;
	protected List<LevelEntity> entities = new ArrayList<LevelEntity>();

	protected Class<? extends LevelEntity>[] paintOrder = null;

	@SuppressWarnings("unchecked")
	public Level(Screen o, int tile_w, int tile_h, int width, int height) {
		this.width = width;
		this.height = height;
		
		this.tile_width = tile_w;
		this.tile_height = tile_h;
		
		this.owner = o;

		tiles = new Tile[width][height];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				Tile tile = new EmptyTile();

				setTile(x, y, tile);
			}
		}

		entityMap = new List[width][height];
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				entityMap[x][y] = new ArrayList<LevelEntity>();
			}
		}
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void setTile(int x, int y, Tile tile) {
		tiles[x][y] = tile;
		tile.init(this, x, y);
	}

	public Tile getTile(int x, int y) {
		if (x < 0 || y < 0 || x >= width || y >= height) {
			return null;
		}

		return tiles[x][y];
	}

	public Tile getTile(MPoint pos) {
		if (pos.getX() < 0 || pos.getY() < 0) {
			return null;
		}

		int x = pos.getX() / getTileWidth();
		int y = pos.getY() / getTileHeight();
		return getTile(x, y);
	}

	protected MDPoint screenToCanvas(MPoint p) {
		MPoint offset = getOwner().getOffset();
		double scale = getOwner().getScreenScale();
		MDPoint mp = p.asMDPoint();
		mp.div(scale);
		mp.add(offset.getX(), offset.getY());
		return mp;
	}

	public void tick() {
		if (tickTiles) {
			for (Tile[] td : tiles) {
				for (Tile t : td) {
					if (t == null) {
						continue;
					}
					t.tick();
				}
			}
		}

		MouseButtons mb = owner.getMouseButtons();

		if (tickEntities) {
			for (int i = 0; i < entities.size(); i++) {
				LevelEntity e = entities.get(i);

				if (e == null) {
					continue;
				}

				if (!e.isRemoved()) {
					MPoint mpi = mb.getPosition().clone();
					MDPoint mp = screenToCanvas(mpi);

					BoxBounds bbs = e.getBoxBounds();

					if (mp.isInRect(bbs)) {
						if (mb.isMouseMoving()) {
							e.onMouseMove(mb);
						}

						if (mb.isPressed()) {
							e.onMousePress(mb);
						} else if (mb.isReleased()) {
							e.onMouseRelease(mb);
						} else {
							if (!e.isHovered()) {
								e.setHovered(true);
								e.onMouseEnter(mb);
							} else {
								e.onMouseHover(mb);
							}
						}
					} else {
						if (e.isHovered()) {
							e.setHovered(false);
							e.onMouseLeave(mb);
						}
					}

					e.tick();

					int xtn = (int) (e.getPosX() - e.getRadius().getX()) / getTileWidth();
					int ytn = (int) (e.getPosY() - e.getRadius().getY()) / getTileHeight();
					if (xtn != e.getXto() || ytn != e.getYto()) {
						removeFromEntityMap(e);
						insertToEntityMap(e);
					}
				}
				if (e.isRemoved()) {
					entities.remove(i--);
					removeFromEntityMap(e);
				}
			}
		}

		if (tickHUD) {
			if (hud != null) {
				hud.tick();
			}
		}

		if (mb.isMouseMoving()) {
			onMouseMove(mb);
		}

		if (mb.isPressed()) {
			onPressed(mb);
			if (mb.getHoveredEntities(this).isEmpty() && mb.getHoveredHUDElements(hud).isEmpty()) {
				if (mb.isHover(hud)) {
					onHUDPressed(mb);
				} else {
					onTilePressed(mb);
				}
			}
		}
	}

	public abstract void onTilePressed(MouseButtons mouse);

	public abstract void onHUDPressed(MouseButtons mouse);

	public abstract void onPressed(MouseButtons mouse);

	public abstract void onMouseMove(MouseButtons mouse);

	public Screen getOwner() {
		return owner;
	}

	public SoundPlayer getSoundPlayer() {
		return getOwner().getSoundPlayer();
	}

	public List<LevelEntity> orderEntities(Set<LevelEntity> s) {
		List<LevelEntity> lst = new ArrayList<LevelEntity>();

		if (paintOrder != null) {
			for (Class<? extends LevelEntity> c : paintOrder) {
				for (LevelEntity se : s) {
					if (c.isAssignableFrom(se.getClass())) {
						lst.add(se);
					}
				}
			}
		}

		for (LevelEntity se : s) {
			if (!lst.contains(se)) {
				lst.add(se);
			}
		}

		return lst;
	}

	public void render(Surface surface, int xScroll, int yScroll) {
		int x0 = xScroll / getTileWidth();
		int y0 = yScroll / getTileHeight();
		int x1 = (xScroll + surface.getWidth()) / getTileWidth();
		int y1 = (yScroll + surface.getHeight()) / getTileHeight();

		Set<LevelEntity> visibleEntities = getEntities(xScroll - getTileWidth(), yScroll - getTileHeight(), xScroll + surface.getWidth() + getTileWidth(), yScroll + surface.getHeight() + getTileHeight());

		List<LevelEntity> orderedEntities = orderEntities(visibleEntities);

		if (x0 < 0) {
			x0 = 0;
		}
		if (x1 > (width - 1)) {
			x1 = width - 1;
		}
		if (y0 < 0) {
			y0 = 0;
		}
		if (y1 > (height - 1)) {
			y1 = height - 1;
		}
		
		renderBackgroundImage(surface);

		surface.setOffset(-xScroll, -yScroll);

		for (int y = y0; y <= y1; y++) {
			for (int x = x0; x <= x1; x++) {
				tiles[x][y].render(surface);
			}
		}

		for (LevelEntity e : orderedEntities) {
			e.render(surface);
		}

		surface.setOffset(0, 0);

		if (hud != null) {
			hud.render(surface);
		}
	}
	
	private void renderBackgroundImage(Surface surface) {
		int al_r = width * getTileWidth() - background_img.getWidth();
		int al_b = height * getTileHeight() - background_img.getHeight();
		int ac_y = (height * getTileHeight())/2 - background_img.getHeight()/2;
		int ac_x = (width * getTileWidth())/2 - background_img.getWidth()/2;
		
		switch(background_alignement) {
		case ALIGN_BOTTOMLEFT:
			surface.blit(background_img, 0, al_b);
			break;
		case ALIGN_BOTTOMRIGHT:
			surface.blit(background_img, al_r, al_b);
			break;
		case ALIGN_CENTER:
			surface.blit(background_img, ac_x, ac_y);
			break;
		case ALIGN_UPPERLEFT:
			surface.blit(background_img, 0, 0);
			break;
		case ALIGN_UPPERRIGHT:
			surface.blit(background_img, al_r, 0);
			break;
		}
		
	}

	public List<BoxBounds> getClipBoxBounds(LevelEntity e) {
		List<BoxBounds> result = new ArrayList<BoxBounds>();
		BoxBounds bb = e.getBoxBounds().copy();
		bb.grow(getTileWidth());

		int x0 = (int) (bb.getLeft() / getTileWidth());
		int x1 = (int) (bb.getRight() / getTileWidth());
		int y0 = (int) (bb.getTop() / getTileHeight());
		int y1 = (int) (bb.getBottom() / getTileHeight());

		result.add(new BoxBounds(null, 0, 0, 0, height * getTileHeight()));
		result.add(new BoxBounds(null, 0, 0, width * getTileWidth(), 0));
		result.add(new BoxBounds(null, width * getTileWidth(), 0, width * getTileWidth(), height * getTileHeight()));
		result.add(new BoxBounds(null, 0, height * getTileHeight(), width * getTileWidth(), height * getTileHeight()));

		for (int y = y0; y <= y1; y++) {
			if (y < 0 || y >= height) {
				continue;
			}
			for (int x = x0; x <= x1; x++) {
				if (x < 0 || x >= width)
					continue;
				tiles[x][y].addClipBoxBounds(result, e);
			}
		}

		Set<LevelEntity> visibleEntities = getEntities(bb);
		for (LevelEntity ee : visibleEntities) {
			if (ee != e) {
				result.add(ee.getBoxBounds());
			}
		}

		return result;
	}

	public Set<LevelEntity> getEntities(BoxBounds bb) {
		return getEntities(bb.getLeft(), bb.getTop(), bb.getRight(), bb.getBottom());
	}

	public Set<LevelEntity> getEntities(double xx0, double yy0, double xx1, double yy1) {
		int x0 = (int) (xx0) / getTileWidth();
		int x1 = (int) (xx1) / getTileWidth();
		int y0 = (int) (yy0) / getTileHeight();
		int y1 = (int) (yy1) / getTileHeight();

		Set<LevelEntity> result = new TreeSet<LevelEntity>(new EntityComparator());

		for (int y = y0; y <= y1; y++) {
			if (y < 0 || y >= height) {
				continue;
			}

			for (int x = x0; x <= x1; x++) {
				if (x < 0 || x >= width) {
					continue;
				}

				List<LevelEntity> entities = entityMap[x][y];
				for (int i = 0; i < entities.size(); i++) {
					LevelEntity e = entities.get(i);
					if (e.isRemoved()) {
						continue;
					}
					if (e.intersects(xx0, yy0, xx1, yy1)) {
						result.add(e);
					}
				}
			}
		}

		return result;
	}

	public Set<LevelEntity> getEntities(BoxBounds bb, Class<? extends LevelEntity> c) {
		return getEntities(bb.getLeft(), bb.getTop(), bb.getRight(), bb.getBottom(), c);
	}

	public Set<LevelEntity> getEntities(double xx0, double yy0, double xx1, double yy1, Class<? extends LevelEntity> c) {
		int x0 = (int) (xx0) / getTileWidth();
		int x1 = (int) (xx1) / getTileWidth();
		int y0 = (int) (yy0) / getTileHeight();
		int y1 = (int) (yy1) / getTileHeight();

		Set<LevelEntity> result = new TreeSet<LevelEntity>(new EntityComparator());

		for (int y = y0; y <= y1; y++) {
			if (y < 0 || y >= height)
				continue;
			for (int x = x0; x <= x1; x++) {
				if (x < 0 || x >= width)
					continue;
				List<LevelEntity> entities = entityMap[x][y];
				for (int i = 0; i < entities.size(); i++) {
					LevelEntity e = entities.get(i);
					if (!c.isInstance(e))
						continue;
					if (e.isRemoved())
						continue;
					if (e.intersects(xx0, yy0, xx1, yy1)) {
						result.add(e);
					}
				}
			}
		}

		return result;
	}

	public void addEntity(LevelEntity e) {
		uidCounter++;
		addEntity(e, uidCounter);
	}

	public void addEntity(LevelEntity e, long uid) {
		e.init(this, uid);
		entities.add(e);
		insertToEntityMap(e);
	}

	public void insertToEntityMap(LevelEntity e) {
		e.setXto((int) (e.getPosX() - e.getRadius().getX()) / getTileWidth());
		e.setYto((int) (e.getPosY() - e.getRadius().getY()) / getTileHeight());

		int x1 = e.getXto() + (e.getRadius().getX() * 2 + 1) / getTileWidth();
		int y1 = e.getYto() + (e.getRadius().getY() * 2 + 1) / getTileHeight();

		for (int y = e.getYto(); y <= y1; y++) {
			if (y < 0 || y >= height)
				continue;
			for (int x = e.getXto(); x <= x1; x++) {
				if (x < 0 || x >= width)
					continue;
				entityMap[x][y].add(e);
			}
		}
	}

	public void removeFromEntityMap(LevelEntity e) {
		int x1 = e.getXto() + (e.getRadius().getX() * 2 + 1) / getTileWidth();
		int y1 = e.getYto() + (e.getRadius().getY() * 2 + 1) / getTileHeight();

		for (int y = e.getYto(); y <= y1; y++) {
			if (y < 0 || y >= height)
				continue;
			for (int x = e.getXto(); x <= x1; x++) {
				if (x < 0 || x >= width)
					continue;
				entityMap[x][y].remove(e);
			}
		}
	}

	public int getEntityCount(Class<? extends LevelEntity> c) {
		int r = 0;

		for (int i = 0; i < entities.size(); i++) {
			LevelEntity e = entities.get(i);

			if (c.isAssignableFrom(e.getClass())) {
				r++;
			}
		}
		return r;
	}

	public LevelEntity getEntity(long uid) {
		for (int i = 0; i < entities.size(); i++) {
			LevelEntity e = entities.get(i);

			if (e.getUID() == uid) {
				return e;
			}
		}
		return null;
	}

	public boolean removeEntity(long uid) {
		for (int i = 0; i < entities.size(); i++) {
			if (entities.get(i).getUID() == uid) {
				entities.get(i).remove();
				return true;
			}
		}
		return false;
	}

	public ArrayList<LevelEntity> getAllEntities(Class<? extends LevelEntity> c) {
		ArrayList<LevelEntity> r = new ArrayList<LevelEntity>();
		for (int i = 0; i < entities.size(); i++) {
			LevelEntity e = entities.get(i);

			if (c.isAssignableFrom(e.getClass())) {
				r.add(e);
			}
		}
		return r;
	}

	public void setPaintOrder(Class<? extends LevelEntity>[] c) {
		paintOrder = c;
	}

	public HUD getHUD() {
		return hud;
	}

	public void setHUD(HUD h) {
		hud = h;
	}

	public void removeHUD() {
		hud = null;
	}

	public void addEffect(Bitmap[] a, int duration, int x, int y) {
		LevelEntity e = new LevelEffectEntity(a, duration, x, y);
		addEntity(e);
	}

	public List<LevelEntity> getEntities() {
		return entities;
	}

	public Bitmap getBackgroundImage() {
		return background_img;
	}

	public void setBackgroundImage(Bitmap background_img) {
		this.background_img = background_img;
	}

	public int getBackgroundAlignement() {
		return background_alignement;
	}

	public void setBackgroundAlignement(int background_alignement) {
		this.background_alignement = background_alignement;
	}

	public int getTileWidth() {
		return tile_width;
	}
	
	public int getTileHeight() {
		return tile_height;
	}
	
	public int getTW() {
		return getTileWidth();
	}
	
	public int getTH() {
		return getTileHeight();
	}
}
