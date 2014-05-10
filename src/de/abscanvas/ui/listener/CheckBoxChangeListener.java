package de.abscanvas.ui.listener;

import de.abscanvas.input.MouseButtons;

public interface CheckBoxChangeListener {
	public void checkBoxChanged(MouseButtons sender, int id, boolean state); // sender is null when triggered by method (otherwise its the mousebutton)
}
