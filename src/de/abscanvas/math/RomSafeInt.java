package de.abscanvas.math;

public class RomSafeInt {
	private final static int MODIFIER = 109; // RAMVALUE - MODIFIER = VALUE
	
	private long value = MODIFIER;
	
	public RomSafeInt() {
		value = MODIFIER;
	}
	
	public RomSafeInt(int v) {
		set(v);
	}
	
	public int get() {
		return (int) (value-MODIFIER);
	}
	
	public void set(int g) {
		value = g + MODIFIER;
	}
	
	public void inc() {
		value++;
	}
	
	public void dec() {
		value--;
	}
	
	public void add(int a) {
		value += a;
	}
	
	public void sub(int s) {
		value -= s;
	}
	
	public void mult(int m) {
		set(get() * m);
	}
	
	public void div(int d) {
		set(get() * d);
	}
	
	public void neg() {
		set(- get());
	}
	
	public void abs() {
		set(Math.abs(get()));
	}
}
