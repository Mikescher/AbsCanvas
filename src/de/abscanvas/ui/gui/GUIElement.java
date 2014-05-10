package de.abscanvas.ui.gui;

import de.abscanvas.entity.Entity;
import de.abscanvas.input.MouseButtons;
import de.abscanvas.math.BoxBounds;
import de.abscanvas.math.BoxBoundsOwner;
import de.abscanvas.math.MDPoint;
import de.abscanvas.math.MPoint;
import de.abscanvas.surface.Bitmap;
import de.abscanvas.surface.Surface;
import de.abscanvas.ui.gui.GUIMenu;
import de.abscanvas.ui.listener.MouseListener;

public abstract class GUIElement implements MouseListener, BoxBoundsOwner {
	protected MPoint pos;
	protected int width, height;
	protected Bitmap img;
	protected GUIMenu owner;
	protected boolean isHovered = false;
	protected boolean visible = true;

	public GUIElement(int x, int y, int width, int height, Bitmap i, GUIMenu owner) {
		pos = new MPoint(x, y);
		this.width = width;
		this.height = height;
		this.owner = owner;
		img = i;
	}

	public void tick() {
		//doesnt do anything 
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

	protected void setImage(Bitmap i) {
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

	/**
	 * @return the isHovered
	 */
	public boolean isHovered() {
		return isHovered;
	}

	/**
	 * @param isHovered
	 *            the isHovered to set
	 */
	public void setHovered(boolean isHovered) {
		this.isHovered = isHovered;
	}

	@Override
	public void onMousePress(MouseButtons mouse) {
		// Event - override if you need an Event 
	}

	@Override
	public void onMouseRelease(MouseButtons mouse) {
		// Event - override if you need an Event 
	}

	@Override
	public void onMouseHover(MouseButtons mouse) {
		// Event - override if you need an Event 
	}

	@Override
	public void onMouseEnter(MouseButtons mouse) {
		// Event - override if you need an Event 
	}

	@Override
	public void onMouseLeave(MouseButtons mouse) {
		// Event - override if you need an Event 
	}

	@Override
	public void onMouseMove(MouseButtons mouse) {
		// Event - override if you need an Event 
	}

	public boolean isMoused(MDPoint mpos) {
		boolean imX = getX() <= mpos.getX() && getX() + getWidth() >= mpos.getX();
		boolean imY = getY() <= mpos.getY() && getY() + getHeight() >= mpos.getY();
		return imX && imY;
	}

	public BoxBounds getBoxBounds() {
		return new BoxBounds(this, pos.getX(), pos.getY(), pos.getX() + getWidth(), pos.getY() + getHeight());
	}
	
	public GUIMenu getOwner() {
		return owner;
	}

	@Override
	public void handleCollision(Entity entity, double xa, double ya, boolean canPass, boolean isCollider) {
		// Collision-Event - override if you need an Event 
	}

	@Override
	public boolean canPass(Entity e) {
		return false;
	}
}
