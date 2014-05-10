package de.abscanvas.level.tile;

import java.util.List;

import de.abscanvas.entity.Entity;
import de.abscanvas.input.Art;
import de.abscanvas.level.Level;
import de.abscanvas.math.BoxBounds;
import de.abscanvas.math.BoxBoundsOwner;
import de.abscanvas.math.MPoint;
import de.abscanvas.sound.SoundPlayer;
import de.abscanvas.surface.Bitmap;
import de.abscanvas.surface.Surface;

public class Tile implements BoxBoundsOwner {
	private Level level;
	
	private int x;
	private int y;
	
	private Bitmap img;
	
	private boolean minimapColorCalculated = false;
	private int minimapColor = -1;

	public void init(Level level, int x, int y) {
		this.level = level;
		this.x = x;
		this.y = y;
		onInit();
	}

	public void onInit() {
		//no initialization
	}

	public SoundPlayer getSoundPlayer() {
		return level.getSoundPlayer();
	}

	public MPoint getPos() {
		return new MPoint(x, y);
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public Level getLevel() {
		return level;
	}

	public void render(Surface screen) {
		screen.blit(img, x * level.getTileWidth(), y * level.getTileHeight());
	}

	/**
	 * @param e  
	 */
	public void addClipBoxBounds(List<BoxBounds> list, Entity e) {
		list.add(new BoxBounds(this, x * level.getTileWidth(), y * level.getTileHeight(), (x + 1) * level.getTileWidth(), (y + 1) * level.getTileHeight()));
	}

	public void setImage(Bitmap b) {
		img = b;
	}

	@Override
	public void handleCollision(Entity entity, double xa, double ya, boolean canPass, boolean isCollider) {
		//no collsion Code (you can override this)
	}

	@Override
	public boolean canPass(Entity e) {
		return true;
	}

	public Level getOwner() {
		return level;
	}

	public void tick() {
		// you can override this - if you want
	}

	public void setMinimapColor(int minimapColor) {
		minimapColorCalculated = true;
		this.minimapColor = minimapColor;
	}

	public int getMinimapColor() {
		if (! minimapColorCalculated) {
			setMinimapColor(Art.getColor(img));
		}
		
		return minimapColor;
	}
}
