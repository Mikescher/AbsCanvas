package de.abscanvas.ui.hud;

import de.abscanvas.entity.Entity;
import de.abscanvas.input.MouseButtons;
import de.abscanvas.math.BoxBounds;
import de.abscanvas.math.BoxBoundsOwner;
import de.abscanvas.math.MDPoint;
import de.abscanvas.math.MPoint;
import de.abscanvas.surface.Bitmap;
import de.abscanvas.surface.Surface;
import de.abscanvas.ui.listener.MouseListener;

public abstract class HUDElement implements MouseListener, BoxBoundsOwner {
	protected MPoint pos;
	protected int width, height;
	protected Bitmap img;
	protected HUD owner;
	protected boolean isHovered = false;

	protected boolean visible = true;

	public HUDElement(int x, int y, int width, int height, Bitmap i, HUD owner) {
		pos = new MPoint(x, y);
		this.width = width;
		this.height = height;
		this.owner = owner;
		img = i;
	}

	public void tick() {
		//Does nothing on tick
	}

	public void setVisible(boolean v) {
		visible = v;
	}

	public boolean isVisible() {
		return visible;
	}

	public void render(Surface surface) {
		if (visible) {
			surface.blit(img, pos.getX(), pos.getY());
		}
	}

	public int getX() {
		return pos.getX();
	}

	public int getY() {
		return pos.getY();
	}

	public MPoint getPos() {
		return pos;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Bitmap getImage() {
		return img;
	}
	
	public HUD getOwner() {
		return owner;
	}

	public void setImage(Bitmap i) {
		if (i != img) {
			img = i;
		}
	}
	
	public void setPos(MPoint p) {
		pos.set(p);
		
		refresh();
	}
	
	public void setWidth(int w) {
		width = w;
		
		refresh();
	}
	
	public void setHeight(int h) {
		height = h;
		
		refresh();
	}
	
	protected abstract void repaint();
	
	public void refresh() {
		repaint();
	}

	public void setPos(int x, int y) {
		pos.set(x, y);
	}

	public boolean isHovered() {
		return isHovered;
	}

	public void setHovered(boolean isHovered) {
		this.isHovered = isHovered;
	}

	@Override
	public void onMousePress(MouseButtons mouse) {
		// Event - used to override
	}

	@Override
	public void onMouseMove(MouseButtons mouse) {
		// Event - used to override
	}

	@Override
	public void onMouseRelease(MouseButtons mouse) {
		// Event - used to override
	}

	@Override
	public void onMouseEnter(MouseButtons mouse) {
		// Event - used to override
	}

	@Override
	public void onMouseLeave(MouseButtons mouse) {
		// Event - used to override
	}

	@Override
	public void onMouseHover(MouseButtons mouse) {
		// Event - used to override
	}

	public boolean isMoused(MDPoint mpos) {
		boolean imX = getX() <= mpos.getX() && getX() + getWidth() >= mpos.getX();
		boolean imY = getY() <= mpos.getY() && getY() + getHeight() >= mpos.getY();
		return imX && imY;
	}

	public BoxBounds getBoxBounds() {
		return new BoxBounds(this, pos.getX(), pos.getY(), pos.getX() + getWidth(), pos.getY() + getHeight());
	}

	@Override
	public void handleCollision(Entity entity, double xa, double ya, boolean canPass, boolean isCollider) {
		// Collision-Event - used to override
	}

	@Override
	public boolean canPass(Entity e) {
		return false;
	}
}
