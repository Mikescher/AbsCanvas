package de.abscanvas.ui.listener;

import de.abscanvas.input.MouseButtons;

public interface ButtonListener {
	public void buttonPressed(MouseButtons mouse, int id);
	public void buttonMouseDown(MouseButtons mouse, int id);
	public void buttonMouseEnter(MouseButtons mouse, int id);
	public void buttonMouseLeave(MouseButtons mouse, int id);
}
