package de.abscanvas.ui.hud.elements;

import java.util.ArrayList;
import java.util.List;

import de.abscanvas.input.MouseButtons;
import de.abscanvas.surface.Animation;
import de.abscanvas.surface.Bitmap;
import de.abscanvas.surface.Surface;
import de.abscanvas.ui.hud.HUD;
import de.abscanvas.ui.hud.HUDElement;
import de.abscanvas.ui.listener.ButtonListener;

public class HUDAnimatedButton extends HUDElement{
	private int state = 0; // 0=Normal 1=Hover 2=Pressed 3=disabled
	
	private Animation animation;

	private boolean enabled = true;

	private final int id;

	private List<ButtonListener> listeners;

	public HUDAnimatedButton(int x, int y, int width, int height, Bitmap[][] i, int speed, int id, HUD owner) {
		super(x, y, width, height, i[0][0], owner);
		this.id = id;
		animation = new Animation(owner.getOwner().getOwner());
		animation.setAnimation(i, speed);
		animation.setOriginToUpperLeft();
	}

	public HUDAnimatedButton(int x, int y, Bitmap[][] i, int speed, int id, HUD owner) {
		super(x, y, i[0][0].getWidth(), i[0][0].getHeight(), i[0][0], owner);
		this.id = id;
		animation = new Animation(owner.getOwner().getOwner());
		animation.setAnimation(i, speed);
		animation.setOriginToUpperLeft();
	}

	public int getID() {
		return id;
	}
	
	@Override
	public void tick() {
		super.tick();
		animation.tick();
	}
	
	@Override
	public void render(Surface s) {
		if (visible) {
			animation.render(s, pos.getX(), pos.getY());
		}
	}

	public void setGraphics(Bitmap[][] i, int speed) {	
		animation.setAnimation(i, speed);
	}

	public void setEnabled(boolean e) {
		if (e ^ enabled) {
			enabled = e;
			if (e) {
				state = 0;
				animation.setLayer(state);
			} else {
				state = 3;
				animation.setLayer(state);
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
			animation.setLayer(state);
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
			state = 1;
			animation.setLayer(state);
		}
	}

	@Override
	public void onMouseEnter(MouseButtons mouse) {
		if (enabled) {
			state = 1;
			animation.setLayer(state);
			if (listeners != null) {
				for (ButtonListener listener : listeners) {
					listener.buttonMouseEnter(mouse, id);
				}
			}
		} else {
			if (listeners != null) {
				for (ButtonListener listener : listeners) {
					listener.buttonMouseEnter(mouse, id);
				}
			}
		}
	}

	@Override
	public void onMouseLeave(MouseButtons mouse) {
		if (enabled) {
			state = 0;
			animation.setLayer(state);
			if (listeners != null) {
				for (ButtonListener listener : listeners) {
					listener.buttonMouseLeave(mouse, id);
				}
			}
		} else {
			if (listeners != null) {
				for (ButtonListener listener : listeners) {
					listener.buttonMouseLeave(mouse, id);
				}
			}
		}
	}
	
	@Override
	protected void repaint() {
		// nothing to do here
	}
}
