package de.abscanvas.ui.gui.elements;

import java.util.ArrayList;
import java.util.List;

import de.abscanvas.input.MouseButtons;
import de.abscanvas.surface.Bitmap;
import de.abscanvas.ui.gui.GUIMenu;
import de.abscanvas.ui.gui.GUIElement;
import de.abscanvas.ui.listener.ButtonListener;
import de.abscanvas.ui.listener.CheckBoxChangeListener;

public class GUICheckbox extends GUIElement{
	private boolean checked = false;
	private Bitmap[] graphics; // 0 = unchecked  |  1 = hover  |  2 = checked
	
	private List<CheckBoxChangeListener> changeListeners;
	private List<ButtonListener> clickListeners;
	
	private final int id;
	
	public GUICheckbox(int x, int y, Bitmap[] i, int id, GUIMenu owner) {
		super(x, y, i[0].getWidth(), i[0].getHeight(), i[0], owner);
		this.id = id;
		graphics = i;
	}

	public boolean isChecked() {
		return checked;
	}
	
	public void setChecked(boolean ck) {
		setChecked(ck, null);
	}
	
	public void setChecked(boolean ck, MouseButtons sender) {
		if (checked ^ ck) {
			checked = ck;
			if(checked) {
				setImage(graphics[2]);
			} else {
				setImage(graphics[0]);
			}
			
			if (changeListeners != null) {
				for (CheckBoxChangeListener listener : changeListeners) {
					listener.checkBoxChanged(sender, id, checked);
				}
			}
		}
	}
	
	@Override
	public void onMousePress(MouseButtons mouse) {
		setChecked(! isChecked(), mouse);
		
		if (clickListeners != null) {
			for (ButtonListener listener : clickListeners) {
				listener.buttonMouseDown(mouse, id);
			}
		}
	}
	
	@Override
	public void onMouseRelease(MouseButtons mouse) {
		if (clickListeners != null) {
			for (ButtonListener listener : clickListeners) {
				listener.buttonPressed(mouse, id);
			}
		}
	}

	@Override
	public void onMouseEnter(MouseButtons mouse) {
		if (! isChecked()) {
			setImage(graphics[1]);
		}
		
		if (clickListeners != null) {
			for (ButtonListener listener : clickListeners) {
				listener.buttonMouseEnter(mouse, id);
			}
		}
	}

	@Override
	public void onMouseLeave(MouseButtons mouse) {
		if (! isChecked()) {
			setImage(graphics[0]);
		} else {
			setImage(graphics[2]);
		}
		
		if (clickListeners != null) {
			for (ButtonListener listener : clickListeners) {
				listener.buttonMouseLeave(mouse, id);
			}
		}
	}
	
	@Override
	protected void repaint() {
		// nothing to do here
	}
	
	public void addButtonListener(ButtonListener listener) {
		if (clickListeners == null) {
			clickListeners = new ArrayList<>();
		}
		clickListeners.add(listener);
	}
	
	public void addChangeListener(CheckBoxChangeListener listener) {
		if (changeListeners == null) {
			changeListeners = new ArrayList<>();
		}
		changeListeners.add(listener);
	}
	
	public int getID() {
		return id;
	}
}
