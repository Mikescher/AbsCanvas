package de.abscanvas.math;

public class MRect {
	public final static int QUADRANT_ERROR 	 	 = -1;
	public final static int QUADRANT_INNER 		 = 0;
	public final static int QUADRANT_TOP 		 = 1;
	public final static int QUADRANT_TOPLEFT 	 = 2;
	public final static int QUADRANT_LEFT 		 = 3;
	public final static int QUADRANT_BOTTOMLEFT  = 4;
	public final static int QUADRANT_BOTTOM 	 = 5;
	public final static int QUADRANT_BOTTOMRIGHT = 6;
	public final static int QUADRANT_RIGHT 		 = 7;
	public final static int QUADRANT_TOPRIGHT 	 = 8;
	
	private MPoint topleft;
	private MPoint bottomright;
	
	public MRect() {
		topleft = new MPoint(0, 0);
		bottomright = new MPoint(0, 0);
	}
	
	public MRect(MRect r) {
		topleft = new MPoint(r.getTopLeft());
		bottomright = new MPoint(r.getBottomRight());
	}
	
	public MRect(int x1, int y1, int x2, int y2) {
		topleft = new MPoint(x1, y1);
		bottomright = new MPoint(x2, y2);
		
		normalize();
	}
	
	public MRect(MPoint pTopleft, MPoint pBottomright) {
		topleft = new MPoint(pTopleft);
		bottomright = new MPoint(pBottomright);
		
		normalize();
	}
	
	private void normalize() {
		if (bottomright.getX() < topleft.getX()) {
			// swap
			int tmp = bottomright.getX();
			bottomright.setX(topleft.getX());
			topleft.setX(tmp);
		}
		
		if (bottomright.getY() < topleft.getY()) {
			// swap
			int tmp = bottomright.getY();
			bottomright.setY(topleft.getY());
			topleft.setY(tmp);
		}
	}
	
	public void setTopLeft(MPoint tl) {
		topleft.set(tl);
		
		normalize();
	}
	
	public void setTopLeft(int x, int y) {
		topleft.set(x, y);
		
		normalize();
	}
	
	public MPoint getTopLeft() {
		return topleft;
	}
	
	public void setBottomRight(MPoint br) {
		bottomright.set(br);
		
		normalize();
	}
	
	public void setBottomRight(int x, int y) {
		bottomright.set(x, y);
		
		normalize();
	}
	
	public MPoint getBottomRight() {
		return bottomright;
	}
	
	public int getWidth() {
		return bottomright.getX() - topleft.getX();
	}
	
	public int getHeight() {
		return bottomright.getY() - topleft.getY();
	}
	
	public int getVolume() {
		return getWidth() * getHeight();
	}
	
	public int getTop() {
		return topleft.getY();
	}
	
	public int getY() {
		return getTop();
	}
	
	public int getBottom() {
		return bottomright.getY();
	}
	
	public int getLeft() {
		return topleft.getX();
	}
	
	public int getX() {
		return getLeft();
	}
	
	public int getRight() {
		return bottomright.getX();
	}
	
	public boolean intersects(int x0, int y0, int x1, int y1) {
		return intersects(new MRect(x0, y0, x1, y1));
	}

	public boolean intersects(MRect or) {
		return ! (this.getLeft() > or.getRight() || or.getLeft() > this.getRight() || this.getTop() > or.getBottom() || or.getTop() > this.getBottom());
	}
	
	public void move(int byX, int byY) {
		topleft.move(byX, byY);
		bottomright.move(byX, byY);
		
		normalize();
	}
	
	public void moveX(int byX) {
		topleft.moveX(byX);
		bottomright.moveX(byX);
		
		normalize();
	}
	
	public void moveY(int byY) {
		topleft.moveY(byY);
		bottomright.moveY(byY);
		
		normalize();
	}
	
	public void grow(int inc) {
		grow(inc, inc);
	}
	
	public void grow(int incX, int incY) {
		topleft.move(-incX, -incY);
		bottomright.move(incX, incY);
		
		normalize();
	}
	
	public void shrink(int dec) {
		shrink(dec, dec);
	}
	
	public void shrink(int decX, int decY) {
		topleft.move(decX, decY);
		bottomright.move(-decX, -decY);
		
		normalize();
	}
	
	@Override
	public boolean equals(Object equalRect) {
		if ((equalRect == null) || !(equalRect instanceof MRect)) {
			return false;
		} else {
			return compare((MRect) equalRect);
		}
	}
	
	public boolean compare(MRect equalRect) {
		return equalRect.getTopLeft().compare(getTopLeft()) && equalRect.getBottomRight().compare(getBottomRight());
	}
	
	public boolean isZero() {
		return getTopLeft().isZero() && getBottomRight().isZero();
	}
	
	public boolean includes(MRect oRect) {
		return oRect.getTop() > getTop() && oRect.getLeft() > getLeft() && oRect.getBottom() < getBottom() && oRect.getRight() < getRight();
	}
	
	public MPoint getCenter() {
		MPoint md = getTopLeft().clone();
		md.move(getWidth() / 2, getHeight() / 2);
		return md;
	}
	
	public String asString() {
		return getTopLeft().asString() + " | " + getBottomRight().asString();
	}
	
	public MRect copy() {
		return new MRect(this);
	}
	
	public void setTop(int t) {
		topleft.setY(t);
		
		normalize();
	}
	
	public void setLeft(int l) {
		topleft.setX(l);
		
		normalize();
	}
	
	public void setBottom(int b) {
		bottomright.setY(b);
		
		normalize();
	}
	
	public void setRight(int r) {
		bottomright.setX(r);
		
		normalize();
	}
	
	public void adjustWithMaxRect(MRect maxRect) {
		if (getTop() < maxRect.getTop()) {
			topleft.setY(maxRect.getTop()); // ohne normalize
		} else if (getTop() > maxRect.getBottom()) {
			topleft.setY(maxRect.getBottom()); // ohne normalize
		}
		
		if (getLeft() < maxRect.getLeft()) {
			topleft.setX(maxRect.getLeft()); // ohne normalize
		} else if (getLeft() > maxRect.getRight()) {
			topleft.setX(maxRect.getRight()); // ohne normalize
		}
		
		if (getBottom() > maxRect.getBottom()) {
			bottomright.setY(maxRect.getBottom()); // ohne normalize
		} else if (getBottom() < maxRect.getTop()) {
			bottomright.setY(maxRect.getTop()); // ohne normalize
		}
		
		if (getRight() > maxRect.getRight()) {
			bottomright.setX(maxRect.getRight()); // ohne normalize
		} else if (getRight() < maxRect.getLeft()) {
			bottomright.setX(maxRect.getLeft()); // ohne normalize
		}
		
		normalize();
	}
	
	public int getQuadrant(MPoint p) {
		int rx;
		int ry;
		
		if (p.getX() < getLeft()) {
			rx = -1;
		} else if (p.getX() > getRight()) {
			rx = 1;
		} else {
			rx = 0;
		}
		
		if (p.getY() < getTop()) {
			ry = -1;
		} else if (p.getY() > getBottom()) {
			ry = 1;
		} else {
			ry = 0;
		}
		
		if (rx == 0 && ry == 0) {
			return QUADRANT_INNER;
		} else if (rx == 0 && ry == -1) {
			return QUADRANT_TOP;
		} else if (rx == -1 && ry == -1) {
			return QUADRANT_TOPLEFT;
		} else if (rx == -1 && ry == 0) {
			return QUADRANT_LEFT;
		} else if (rx == -1 && ry == 1) {
			return QUADRANT_BOTTOMLEFT;
		} else if (rx == 0 && ry == 1) {
			return QUADRANT_BOTTOM;
		} else if (rx == 1 && ry == 1) {
			return QUADRANT_BOTTOMRIGHT;
		} else if (rx == 1 && ry == 0) {
			return QUADRANT_RIGHT;
		} else if (rx == 1 && ry == -1) {
			return QUADRANT_TOPRIGHT;
		} else {
			return QUADRANT_ERROR;
		}
	}
}