package de.abscanvas.ui.listener;

import de.abscanvas.input.MouseButtons;

public interface MouseListener {
	
	public void onMousePress(MouseButtons mouse);
	
	public void onMouseRelease(MouseButtons mouse);
	
	public void onMouseHover(MouseButtons mouse);
	
	public void onMouseEnter(MouseButtons mouse);
	
	public void onMouseLeave(MouseButtons mouse);
	
	public void onMouseMove(MouseButtons mouse);
}
