package de.abscanvas.math;

public class BoxBounds extends MDRect{
	public BoxBoundsOwner owner;

	public BoxBounds(BoxBoundsOwner owner, double x0, double y0, double x1, double y1) {
		super(x0, y0, x1, y1);
		this.owner = owner;
	}
	
	public BoxBounds(BoxBounds bb) {
		super(bb);
		owner = bb.getOwner();
	}
	
	@Override
	public BoxBounds copy() {
		return new BoxBounds(this);
	}
	
	public BoxBoundsOwner getOwner() {
		return owner;
	}
}
