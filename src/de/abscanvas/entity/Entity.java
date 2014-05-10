package de.abscanvas.entity;

import de.abscanvas.input.MouseButtons;
import de.abscanvas.math.BoxBounds;
import de.abscanvas.math.BoxBoundsOwner;
import de.abscanvas.math.MDPoint;
import de.abscanvas.math.MDRect;
import de.abscanvas.math.MPoint;
import de.abscanvas.surface.Animation;
import de.abscanvas.surface.Surface;
import de.abscanvas.ui.listener.MouseListener;

public abstract class Entity implements BoxBoundsOwner, MouseListener {
	private MDPoint pos = new MDPoint(0, 0);
	private MPoint radius = new MPoint(-1, -1);
	
	private boolean removed = false;
	
	private boolean debug = false;
	
	private int alpha = 255;

	private boolean visible = true;

	private boolean isHovered = false;

	private Animation animation = new Animation();

	private int xto;
	private int yto;

	public Entity() {
	}

	public void setPos(MDPoint p) {
		pos.set(p);
	}
	
	public void setPos(MPoint p) {
		setPos(p.getX(), p.getY());
	}

	public void setPos(double x, double y) {
		pos.set(x, y);
	}

	public void addPos(double x, double y) {
		pos.add(x, y);
	}

	public void addPosX(double x) {
		pos.addX(x);
	}

	public void addPosY(double y) {
		pos.addY(y);
	}

	public void setPosX(double x) {
		pos.setX(x);
	}

	public void setPosY(double y) {
		pos.setY(y);
	}

	public MPoint getRoundedPos() {
		return pos.roundToMPoint();
	}

	public double getPosX() {
		return pos.getX();
	}

	public double getPosY() {
		return pos.getY();
	}
	
	public MDPoint getClonedPos() {
		return pos.clone();
	}

	public void setRadius(int xr, int yr) {
		radius.set(xr, yr);
	}
	
	public void setRadius(int r) {
		radius.set(r, r);
	}

	public void setVisible(boolean v) {
		visible = v;
	}

	public boolean isVisible() {
		return visible;
	}

	public void onInit(int tileW, int tileH) {
		if (radius.isInvalid()) setRadius(tileW/2, tileH/2);
	}
	
	public void tick() {
		animation.tick();
	}

	public boolean intersects(double xx0, double yy0, double xx1, double yy1) {
		return getBoxBounds().intersects(xx0, yy0, xx1, yy1);
	}

	public BoxBounds getBoxBounds() {
		return new BoxBounds(this, pos.getX() - radius.getX(), pos.getY() - radius.getY(), pos.getX() + radius.getX(), pos.getY() + radius.getY());
	}
	
	public MDRect getBoxBoundsRect() {
		return new MDRect(pos.getX() - radius.getX(), pos.getY() - radius.getY(), pos.getX() + radius.getX(), pos.getY() + radius.getY());
	}

	public void render(Surface surface) {
		if (isVisible()) {
			animation.renderAlpha(surface, pos.getX(), pos.getY(), alpha);
			if (isDebugging()) (new DebugRenderer(this)).render(surface);
		}
	}

	public int getWidth() {
		return radius.getX() * 2;
	}

	public int getHeight() {
		return radius.getY() * 2;
	}

	public void remove() {
		removed = true;
	}

	@Override
	public boolean canPass(Entity e) {
		return true;
	}

	public boolean isHovered() {
		return isHovered;
	}

	public void setHovered(boolean isHovered) {
		this.isHovered = isHovered;
	}

	public boolean isRemoved() {
		return removed;
	}

	@Override
	public void onMousePress(MouseButtons mouse) {
		//nocode
	}

	@Override
	public void onMouseMove(MouseButtons mouse) {
		//nocode
	}

	@Override
	public void onMouseRelease(MouseButtons mouse) {
		//nocode
	}

	@Override
	public void onMouseEnter(MouseButtons mouse) {
		//nocode
	}

	@Override
	public void onMouseLeave(MouseButtons mouse) {
		//nocode
	}

	@Override
	public void onMouseHover(MouseButtons mouse) {
		//nocode
	}

	public MDPoint getPosition() {
		return pos;
	}

	public MPoint getRadius() {
		return radius;
	}

	public int getXto() {
		return xto;
	}

	public int getYto() {
		return yto;
	}

	public void setXto(int otx) {
		xto = otx;
	}

	public void setYto(int oty) {
		yto = oty;
	}
	
	public int getAlpha() {
		return alpha;
	}

	public void setAlpha(int alpha) {
		this.alpha = alpha;
	}
	
	public Animation getAnimation() {
		return animation;
	}

	public boolean isDebugging() {
		return debug;
	}

	public void setDebugging(boolean debug) {
		this.debug = debug;
	}
}
