package de.abscanvas.math;

public class MDRect {
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
	
	private MDPoint topleft;
	private MDPoint bottomright;
	
	public MDRect() {
		topleft = new MDPoint(0, 0);
		bottomright = new MDPoint(0, 0);
	}
	
	public MDRect(MDRect r) {
		topleft = new MDPoint(r.getTopLeft());
		bottomright = new MDPoint(r.getBottomRight());
	}
	
	public MDRect(double x1, double y1, double x2, double y2) {
		topleft = new MDPoint(x1, y1);
		bottomright = new MDPoint(x2, y2);
		
		normalize();
	}
	
	public MDRect(MDPoint pTopleft, MDPoint pBottomright) {
		topleft = new MDPoint(pTopleft);
		bottomright = new MDPoint(pBottomright);
		
		normalize();
	}
	
	private void normalize() {
		if (bottomright.getX() < topleft.getX()) {
			// swap
			double tmp = bottomright.getX();
			bottomright.setX(topleft.getX());
			topleft.setX(tmp);
		}
		
		if (bottomright.getY() < topleft.getY()) {
			// swap
			double tmp = bottomright.getY();
			bottomright.setY(topleft.getY());
			topleft.setY(tmp);
		}
	}
	
	public void setTopLeft(MDPoint tl) {
		topleft.set(tl);
		
		normalize();
	}
	
	public void setTopLeft(double x, double y) {
		topleft.set(x, y);
		
		normalize();
	}
	
	public MDPoint getTopLeft() {
		return topleft;
	}
	
	public void setBottomRight(MDPoint br) {
		bottomright.set(br);
		
		normalize();
	}
	
	public void setBottomRight(double x, double y) {
		bottomright.set(x, y);
		
		normalize();
	}
	
	public MDPoint getBottomRight() {
		return bottomright;
	}
	
	public double getWidth() {
		return bottomright.getX() - topleft.getX();
	}
	
	public double getHeight() {
		return bottomright.getY() - topleft.getY();
	}
	
	public double getVolume() {
		return getWidth() * getHeight();
	}
	
	public double getTop() {
		return topleft.getY();
	}
	
	public double getBottom() {
		return bottomright.getY();
	}
	
	public double getLeft() {
		return topleft.getX();
	}
	
	public double getRight() {
		return bottomright.getX();
	}
	
	public boolean intersects(double x0, double y0, double x1, double y1) {
		return intersects(new MDRect(x0, y0, x1, y1));
	}

	public boolean intersects(MDRect or) {
		return ! (this.getLeft() > or.getRight() || or.getLeft() > this.getRight() || this.getTop() > or.getBottom() || or.getTop() > this.getBottom());
	}
	
	public void move(double byX, double byY) {
		topleft.move(byX, byY);
		bottomright.move(byX, byY);
		
		normalize();
	}
	
	public void moveX(double byX) {
		topleft.moveX(byX);
		bottomright.moveX(byX);
		
		normalize();
	}
	
	public void moveY(double byY) {
		topleft.moveY(byY);
		bottomright.moveY(byY);
		
		normalize();
	}
	
	public void grow(double inc) {
		grow(inc, inc);
	}
	
	public void grow(double incX, double incY) {
		topleft.move(-incX, -incY);
		bottomright.move(incX, incY);
		
		normalize();
	}
	
	public void shrink(double dec) {
		shrink(dec, dec);
	}
	
	public void shrink(double decX, double decY) {
		topleft.move(decX, decY);
		bottomright.move(-decX, -decY);
		
		normalize();
	}
	
	@Override
	public boolean equals(Object equalRect) {
		if ((equalRect == null) || !(equalRect instanceof MDRect)) {
			return false;
		} else {
			return compare((MDRect) equalRect);
		}
	}
	
	public boolean compare(MDRect equalRect) {
		return equalRect.getTopLeft().compare(getTopLeft()) && equalRect.getBottomRight().compare(getBottomRight());
	}
	
	public boolean isZero() {
		return getTopLeft().isZero() && getBottomRight().isZero();
	}
	
	public boolean includes(MDRect oRect) {
		return oRect.getTop() > getTop() && oRect.getLeft() > getLeft() && oRect.getBottom() < getBottom() && oRect.getRight() < getRight();
	}
	
	public MDPoint getMiddle() {
		MDPoint md = getTopLeft().clone();
		md.move(getWidth() / 2, getHeight() / 2);
		return md;
	}
	
	public String asString() {
		return getTopLeft().asString() + " | " + getBottomRight().asString();
	}
	
	public MDRect copy() {
		return new MDRect(this);
	}
	
	public void setTop(double t) {
		topleft.setY(t);
		
		normalize();
	}
	
	public void setLeft(double l) {
		topleft.setX(l);
		
		normalize();
	}
	
	public void setBottom(double b) {
		bottomright.setY(b);
		
		normalize();
	}
	
	public void setRight(double r) {
		bottomright.setX(r);
		
		normalize();
	}
	
	public void adjustWithMaxRect(MRect maxRect) {
		if (getTop() < maxRect.getTop()) {
			setTop(maxRect.getTop());
		} else if (getTop() > maxRect.getBottom()) {
			setTop(maxRect.getBottom());
		}
		
		if (getLeft() < maxRect.getLeft()) {
			setLeft(maxRect.getLeft());
		} else if (getLeft() > maxRect.getRight()) {
			setLeft(maxRect.getRight());
		}
		
		if (getBottom() > maxRect.getBottom()) {
			setBottom(maxRect.getBottom());
		} else if (getBottom() < maxRect.getTop()) {
			setBottom(maxRect.getTop());
		}
		
		if (getRight() > maxRect.getRight()) {
			setRight(maxRect.getRight());
		} else if (getRight() < maxRect.getLeft()) {
			setRight(maxRect.getLeft());
		}
		
		normalize();
	}
	
	public int getQuadrant(MDPoint p) {
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