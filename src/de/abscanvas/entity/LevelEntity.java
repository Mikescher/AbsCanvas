package de.abscanvas.entity;

import java.util.List;

import de.abscanvas.level.Level;
import de.abscanvas.level.tile.Tile;
import de.abscanvas.math.BoxBounds;
import de.abscanvas.math.BoxBoundsOwner;
import de.abscanvas.math.MDPoint;
import de.abscanvas.math.MPoint;
import de.abscanvas.sound.SoundPlayer;

public abstract class LevelEntity extends Entity {
	private final static double PUSH_SPEED = 1;

	private Level owner;

	private long uid;

	private boolean positionChanged = false;

	private boolean pushOutEnabled = true;

	public final void init(Level level, long uid) {
		this.owner = level;
		this.uid = uid;
		getAnimation().setScreen(level.getOwner());
		onInit(level.getTileWidth(), level.getTileHeight());
	}

	@Override
	public void tick() {
		super.tick();

		if (isBlocked() && isPushOutEnabled()) {
			calculatePushOut();
		}
	}

	private void calculatePushOut() {
		List<BoxBounds> bbs = owner.getClipBoxBounds(this);
		BoxBounds ownbbs = getBoxBounds();

		MDPoint pushDirection = new MDPoint(0, 0);

		for (int i = 0; i < bbs.size(); i++) {
			BoxBounds bb = bbs.get(i);
			if (ownbbs.intersects(bb)) {
				BoxBoundsOwner o = bb.owner;
				if (o != null) {
					if (!o.canPass(this)) {
						pushDirection.add(ownbbs.getMiddle());
						pushDirection.sub(bb.getMiddle());
					}
				}
			}
		}
		
		if (pushDirection.isZero()) {
			pushDirection.add(1, 1);
		}

		pushDirection.setLength(PUSH_SPEED);

		moveUncollisioned(pushDirection.getX(), pushDirection.getY());
	}

	public Tile getTile() {
		return owner.getTile(getRoundedPos());
	}

	private void moveUncollisioned(double xa, double ya) {
		setPos(getPosition().getX() + xa, getPosition().getY() + ya);
	}

	public boolean move(double xa, double ya) {
		List<BoxBounds> bbs = owner.getClipBoxBounds(this);
		BoxBounds ownbbs = getBoxBounds();
		boolean moved = true;
		if (!isRemoved()) {
			moved &= partmove(ownbbs, bbs, xa, 0);
			moved &= partmove(ownbbs, bbs, 0, ya);
		}

		return moved;
	}

	public boolean canMove(double xa, double ya) {
		boolean moved = true;

		List<BoxBounds> bbs = owner.getClipBoxBounds(this);
		BoxBounds ownbbs = getBoxBounds();
		BoxBounds newbb = ownbbs.copy();
		newbb.move(xa, ya);

		if (!isRemoved()) {
			moved &= !checkCollisions(bbs, newbb);
		}

		return moved;
	}

	public boolean isBlocked() {
		boolean blocked = false;

		List<BoxBounds> bbs = owner.getClipBoxBounds(this);
		BoxBounds ownbbs = getBoxBounds();

		if (!isRemoved()) {
			blocked = checkCollisions(bbs, ownbbs);
		}

		return blocked;
	}

	private boolean partmove(BoxBounds ownbbs, List<BoxBounds> bbs, double xa, double ya) {
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

	public Level getOwner() {
		return owner;
	}

	public SoundPlayer getSoundPlayer() {
		return getOwner().getSoundPlayer();
	}

	@Override
	public void setPos(MDPoint p) {
		super.setPos(p);
		checkCollisions();
		setPositionChanged();
	}

	@Override
	public void setPos(MPoint p) {
		super.setPos(p);
		checkCollisions();
		setPositionChanged();
	}

	@Override
	public void setPos(double x, double y) {
		super.setPos(x, y);
		checkCollisions();
		setPositionChanged();
	}

	@Override
	public void addPos(double x, double y) {
		super.addPos(x, y);
		checkCollisions();
		setPositionChanged();
	}

	@Override
	public void addPosX(double x) {
		super.addPosX(x);
		checkCollisions();
		setPositionChanged();
	}

	@Override
	public void addPosY(double y) {
		super.addPosY(y);
		checkCollisions();
		setPositionChanged();
	}

	@Override
	public void setPosX(double x) {
		super.setPosX(x);
		checkCollisions();
		setPositionChanged();
	}

	@Override
	public void setPosY(double y) {
		super.setPosY(y);
		checkCollisions();
		setPositionChanged();
	}

	public long getUID() {
		return uid;
	}

	public void setPositionChanged() {
		positionChanged = true;
	}

	public boolean isPositionChanged(boolean reset) {
		if (positionChanged) {
			positionChanged = !reset;
			return true;
		} else {
			return false;
		}
	}

	public void resetPositionchanged() {
		positionChanged = false;
	}

	public boolean isPushOutEnabled() {
		return pushOutEnabled;
	}

	public void setPushOutEnabled(boolean pen) {
		pushOutEnabled = pen;
	}
}
