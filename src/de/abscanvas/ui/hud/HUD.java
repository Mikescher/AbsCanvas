package de.abscanvas.ui.hud;

import java.util.ArrayList;

import de.abscanvas.entity.Entity;
import de.abscanvas.input.MouseButtons;
import de.abscanvas.level.Level;
import de.abscanvas.math.BoxBounds;
import de.abscanvas.math.BoxBoundsOwner;
import de.abscanvas.math.MDPoint;
import de.abscanvas.math.MPoint;
import de.abscanvas.surface.Bitmap;
import de.abscanvas.surface.Surface;

public abstract class HUD implements BoxBoundsOwner {
	public static final int ALIGN_UPPERLEFT = 1;
	public static final int ALIGN_UPPERRIGHT = 2;
	public static final int ALIGN_BOTTOMLEFT = 3;
	public static final int ALIGN_BOTTOMRIGHT = 4;
	public static final int ALIGN_CENTER = 5;

	private int alignment = 1;
	private Bitmap img = new Bitmap(0, 0);

	private ArrayList<HUDElement> elements = new ArrayList<HUDElement>();

	protected Level owner;

	public HUD(Level owner) {
		this.owner = owner;
	}

	public void render(Surface surface) {
		switch (alignment) {
		case ALIGN_UPPERLEFT:
			surface.blit(img, 0, 0);
			break;
		case ALIGN_UPPERRIGHT:
			surface.blit(img, owner.getOwner().getScreenWidth() - img.getWidth(), 0);
			break;
		case ALIGN_BOTTOMLEFT:
			surface.blit(img, 0, owner.getOwner().getScreenHeight() - img.getHeight());
			break;
		case ALIGN_BOTTOMRIGHT:
			surface.blit(img, owner.getOwner().getScreenWidth() - img.getWidth(), owner.getOwner().getScreenHeight() - img.getHeight());
			break;
		case ALIGN_CENTER:
			surface.blit(img, owner.getOwner().getScreenWidth() / 2 - img.getWidth() / 2, owner.getOwner().getScreenHeight() / 2 - img.getHeight() / 2);
			break;
		}

		for (HUDElement e : elements) {
			e.render(surface);
		}
	}

	public BoxBounds getBoxBounds() {
		return new BoxBounds(this, getPos().getX(), getPos().getY(), getPos().getX() + getWidth(), getPos().getY() + getHeight());
	}

	public MPoint getPos() {
		MPoint p = new MPoint();

		switch (alignment) {
		case ALIGN_UPPERLEFT:
			p.set(0, 0);
			break;
		case ALIGN_UPPERRIGHT:
			p.set(owner.getOwner().getScreenWidth() - img.getWidth(), 0);
			break;
		case ALIGN_BOTTOMLEFT:
			p.set(0, owner.getOwner().getScreenHeight() - img.getHeight());
			break;
		case ALIGN_BOTTOMRIGHT:
			p.set(owner.getOwner().getScreenWidth() - img.getWidth(), owner.getOwner().getScreenHeight() - img.getHeight());
			break;
		case ALIGN_CENTER:
			p.set(owner.getOwner().getScreenWidth() / 2 - img.getWidth() / 2, owner.getOwner().getScreenHeight() / 2 - img.getHeight() / 2);
			break;
		}

		return p;
	}

	public int getWidth() {
		return img.getWidth();
	}

	public int getHeight() {
		return img.getHeight();
	}

	public Level getOwner() {
		return owner;
	}

	public void tick() {
		for (HUDElement elem : elements) {
			if (elem.isVisible()) {
				MouseButtons mb = owner.getOwner().getMouseButtons();

				MPoint mpi = mb.getPosition().clone();
				MDPoint mp = owner.getOwner().screenToCanvas(mpi);

				if (elem.isMoused(mp)) {
					if (mb.isMouseMoving()) {
						elem.onMouseMove(mb);
					}

					if (mb.isPressed()) {
						elem.onMousePress(mb);
					} else if (mb.isReleased()) {
						elem.onMouseRelease(mb);
					} else {
						if (!elem.isHovered()) {
							elem.setHovered(true);
							elem.onMouseEnter(mb);
						} else {
							elem.onMouseHover(mb);
						}
					}
				} else {
					if (elem.isHovered()) {
						elem.setHovered(false);
						elem.onMouseLeave(mb);
					}
				}
			}
		}

		for (HUDElement e : elements) {
			e.tick();
		}
	}

	public void setImage(Bitmap img) {
		this.img = img;
	}

	public void setAlignment(int a) {
		alignment = a;
	}

	public void addElement(HUDElement e) {
		elements.add(e);
	}
	
	public boolean removeElement(HUDElement e) {
		return elements.remove(e);
	}

	public ArrayList<HUDElement> getElements() {
		return elements;
	}

	@Override
	public boolean canPass(Entity e) {
		return true;
	}

	@Override
	public void handleCollision(Entity entity, double xa, double ya, boolean canPass, boolean isCollider) {
		// Event - used to override
	}

}
