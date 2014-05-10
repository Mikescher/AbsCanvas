package de.abscanvas.entity;

import java.util.List;

import de.abscanvas.math.BoxBounds;
import de.abscanvas.math.BoxBoundsOwner;
import de.abscanvas.math.MDPoint;
import de.abscanvas.math.MPoint;
import de.abscanvas.sound.SoundPlayer;
import de.abscanvas.ui.gui.GUIMenu;

public abstract class MenuEntity extends Entity {
	private GUIMenu owner;
	
	public final void init(GUIMenu menu) {
		this.owner = menu;
		getAnimation().setScreen(menu.getOwner());
		onInit(10, 10); // Unknown
	}
	
	protected boolean move(double xa, double ya) {
		List<BoxBounds> bbs = owner.getClipBoxBounds(this);
		BoxBounds ownbbs = getBoxBounds();
		boolean moved = true;
		if (!isRemoved()) {
			moved &= partmove(ownbbs, bbs, xa, 0);
			moved &= partmove(ownbbs, bbs, 0, ya);
		}

		return moved;
	}

	protected boolean partmove(BoxBounds ownbbs, List<BoxBounds> bbs, double xa, double ya) {
		boolean moved = true;
		BoxBounds newbb = ownbbs.copy();
		newbb.move(xa, ya);

		if (xa == 0) {
			moved &= !checkCollisions(bbs, newbb);
		}

		if (ya == 0) {
			moved &= !checkCollisions(bbs, newbb);
		}

		if (moved) {
			addPos(xa, ya);
		}

		return moved;
	}
	
	private void checkCollisions() {
		if (owner != null) {
			checkCollisions(owner.getClipBoxBounds(this), getBoxBounds());
		}
	}

	private boolean checkCollisions(List<BoxBounds> bbs, BoxBounds ownbb) {
		if (isRemoved()) {
			return false;
		}

		boolean collision = false;
		for (int i = 0; i < bbs.size(); i++) {
			BoxBounds bb = bbs.get(i);
			if (ownbb.intersects(bb)) {
				BoxBoundsOwner o = bb.owner;				
				if (o != null) {
					if (o instanceof Entity) {
						if (((Entity) o).isRemoved()) {
							continue;
						}
					}
					
					o.handleCollision(this, getPosX(), getPosY(), o.canPass(this), false);
					
					if (o instanceof Entity && !isRemoved()) {
						this.handleCollision((Entity) o, getPosX(), getPosY(), o.canPass(this), true);
					}

					if (!o.canPass(this)) {
						collision = true;
						break;
					}
				}
			}
		}
		return collision;
	}
	
	public GUIMenu getOwner() {
		return owner;
	}

	public SoundPlayer getSoundPlayer() {
		return getOwner().getOwner().getSoundPlayer();
	}
	
	@Override
	public void setPos(MDPoint p) {
		super.setPos(p);
		checkCollisions();
	}

	@Override
	public void setPos(MPoint p) {
		super.setPos(p);
		checkCollisions();
	}

	@Override
	public void setPos(double x, double y) {
		super.setPos(x,y);
		checkCollisions();
	}

	@Override
	public void addPos(double x, double y) {
		super.addPos(x, y);
		checkCollisions();
	}

	@Override
	public void addPosX(double x) {
		super.addPosX(x);
		checkCollisions();
	}

	@Override
	public void addPosY(double y) {
		super.addPosY(y);
		checkCollisions();
	}

	@Override
	public void setPosX(double x) {
		super.setPosX(x);
		checkCollisions();
	}

	@Override
	public void setPosY(double y) {
		super.setPosY(y);
		checkCollisions();
	}
}
