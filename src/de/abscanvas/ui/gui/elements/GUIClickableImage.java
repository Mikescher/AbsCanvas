package de.abscanvas.ui.gui.elements;

import java.util.ArrayList;
import java.util.List;

import de.abscanvas.input.MouseButtons;
import de.abscanvas.surface.Bitmap;
import de.abscanvas.ui.gui.GUIMenu;
import de.abscanvas.ui.listener.ButtonListener;

public class GUIClickableImage extends GUIImage {
	private List<ButtonListener> listeners;

	private int state = 0; // 0=Normal 1=Hover 2=Pressed 3=disabled
	private final int id;

	public GUIClickableImage(int x, int y, Bitmap n, int id, GUIMenu owner) {
		super(x, y, n, owner);
		this.id = id;
	}

	public void addListener(ButtonListener listener) {
		if (listeners == null) {
			listeners = new ArrayList<ButtonListener>();
		}
		listeners.add(listener);
	}

	@Override
	public void onMousePress(MouseButtons mouse) {
		state = 2;
		if (listeners != null) {
			for (ButtonListener listener : listeners) {
				listener.buttonMouseDown(mouse, id);
			}
		}
	}

	@Override
	public void onMouseRelease(MouseButtons mouse) {
		if (state == 2) {
			if (listeners != null) {
				for (ButtonListener listener : listeners) {
					listener.buttonPressed(mouse, id);
				}
			}
		}
		state = 1;
	}

	@Override
	public void onMouseEnter(MouseButtons mouse) {
		state = 1;
		if (listeners != null) {
			for (ButtonListener listener : listeners) {
				listener.buttonMouseEnter(mouse, id);
			}
		}
	}

	@Override
	public void onMouseLeave(MouseButtons mouse) {
		state = 0;
		if (listeners != null) {
			for (ButtonListener listener : listeners) {
				listener.buttonMouseLeave(mouse, id);
			}
		}
	}
}
