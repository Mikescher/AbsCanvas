package de.abscanvas.ui.gui.elements;

import java.util.ArrayList;
import java.util.List;

import de.abscanvas.input.MouseButtons;
import de.abscanvas.surface.Bitmap;
import de.abscanvas.ui.gui.GUIMenu;
import de.abscanvas.ui.gui.GUIElement;
import de.abscanvas.ui.listener.ButtonListener;

public class GUIButton extends GUIElement {
	private Bitmap[] graphics;
	private int state = 0; // 0=Normal 1=Hover 2=Pressed 3=disabled

	private boolean enabled = true;

	private final int id;

	private List<ButtonListener> listeners;

	public GUIButton(int x, int y, int width, int height, Bitmap[] i, int id, GUIMenu owner) {
		super(x, y, width, height, i[0], owner);
		this.id = id;
		graphics = i;
	}

	public GUIButton(int x, int y, Bitmap[] i, int id, GUIMenu owner) {
		super(x, y, i[0].getWidth(), i[0].getHeight(), i[0], owner);
		this.id = id;
		graphics = i;
	}

	public int getID() {
		return id;
	}

	@Override
	public void tick() {
		super.tick();
	}

	public void setGraphics(Bitmap[] i) {
		graphics = i;
		setImage(graphics[state]);
	}

	public void setEnabled(boolean e) {
		if (e ^ enabled) {
			enabled = e;
			if (e) {
				state = 0;
				setImage(graphics[state]);
			} else {
				state = 3;
				setImage(graphics[state]);
			}
		}
	}

	public void addListener(ButtonListener listener) {
		if (listeners == null) {
			listeners = new ArrayList<ButtonListener>();
		}
		listeners.add(listener);
	}

	@Override
	public void onMousePress(MouseButtons mouse) {
		if (enabled) {
			state = 2;
			setImage(graphics[state]);
			if (listeners != null) {
				for (ButtonListener listener : listeners) {
					listener.buttonMouseDown(mouse, id);
				}
			}
		}
	}

	@Override
	public void onMouseRelease(MouseButtons mouse) {
		if (enabled) {
			if (state == 2) {
				if (listeners != null) {
					for (ButtonListener listener : listeners) {
						listener.buttonPressed(mouse, id);
					}
				}
			}
			if (enabled) {
				state = 1;
				setImage(graphics[state]);
			}
		}
	}

	@Override
	public void onMouseEnter(MouseButtons mouse) {
		if (enabled) {
			state = 1;
			setImage(graphics[state]);
		}

		if (listeners != null) {
			for (ButtonListener listener : listeners) {
				listener.buttonMouseEnter(mouse, id);
			}
		}
	}

	@Override
	public void onMouseLeave(MouseButtons mouse) {
		if (enabled) {
			state = 0;
			setImage(graphics[state]);
		}
		if (listeners != null) {
			for (ButtonListener listener : listeners) {
				listener.buttonMouseLeave(mouse, id);
			}
		}
	}

	@Override
	protected void repaint() {
		// nothing to do
	}
}
