package de.abscanvas.ui.hud.elements;

import java.util.ArrayList;
import java.util.List;

import de.abscanvas.input.MouseButtons;
import de.abscanvas.ui.hud.HUD;
import de.abscanvas.ui.listener.ButtonListener;

public class HUDLinkLabel extends HUDLabel {
	private final int id;

	private List<ButtonListener> listeners;

	public HUDLinkLabel(int x, int y, String txt, int id, HUD owner) {
		super(x, y, txt, owner);
		this.id = id;
	}

	public HUDLinkLabel(int x, int y, int width, int height, String txt, int id, HUD owner) {
		super(x, y, width, height, txt, owner);
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
		if (listeners != null) {
			for (ButtonListener listener : listeners) {
				listener.buttonMouseDown(mouse, id);
			}
		}
	}

	@Override
	public void onMouseRelease(MouseButtons mouse) {
		if (listeners != null) {
			for (ButtonListener listener : listeners) {
				listener.buttonPressed(mouse, id);
			}
		}
	}

	@Override
	public void onMouseEnter(MouseButtons mouse) {
		if (listeners != null) {
			for (ButtonListener listener : listeners) {
				listener.buttonMouseEnter(mouse, id);
			}
		}
	}

	@Override
	public void onMouseLeave(MouseButtons mouse) {
		if (listeners != null) {
			for (ButtonListener listener : listeners) {
				listener.buttonMouseLeave(mouse, id);
			}
		}
	}
}
