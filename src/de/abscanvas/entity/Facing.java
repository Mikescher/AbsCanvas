package de.abscanvas.entity;

import de.abscanvas.math.MDPoint;

public class Facing {
	public static final int F4_NORTH = 0;
	public static final int F4_EAST = 1;
	public static final int F4_SOUTH = 2;
	public static final int F4_WEST = 3;
	
	public static final int F8_NORTH = 0;
	public static final int F8_NORTHEAST = 1;
	public static final int F8_EAST = 2;
	public static final int F8_SOUTHEAST = 3;
	public static final int F8_SOUTH = 4;
	public static final int F8_SOUTHWEST = 5;
	public static final int F8_WEST = 6;
	public static final int F8_NORTHWEST = 7;
	
	private int facing;
	
	private int divisions;
	
	public Facing(int divisions) {
		this.divisions = divisions;
		facing = 0;
	}
	
	public int get() {
		return facing;
	}
	
	public void set(int f) {
		facing = f;
		fixOverflow();
	}
	
	/**
	 * sets the Facing to the Direction of the Vector p (nearly)
	 * 
	 * @param p
	 */
	public void setVector(MDPoint p) {
		int a = (int) p.getAngle();
		a-= 90;
		if (a<0) {
			a+=360;
		}
		
		facing = (int) ((a*divisions*1f)/360);
	}
	
	public int getDivisions() {
		return divisions;
	}
	
	public void setDivisions(int d) {
		divisions = d;
	}
	
	public void rotateCW(int angle) {
		facing += divisions * (angle/360f);
		fixOverflow();
	}
	
	public void rotateCCW(int angle) {
		facing -= divisions * (angle/360f);
		fixOverflow();
	}
	
	public void rotate90CW() {
		rotateCW(90);
	}
	
	public void rotate90CCW() {
		rotateCCW(90);
	}
	
	public void rotate45CW() {
		rotateCW(45);
	}
	
	public void rotate45CCW() {
		rotateCCW(90);
	}
	
	private void fixOverflow() {
		while(facing <0) {
			facing += divisions;
		}
		while(facing >=divisions) {
			facing -= divisions;
		}
	}
}
