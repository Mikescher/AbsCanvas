package de.abscanvas.ui.hud.elements;

import java.util.ArrayList;

import de.abscanvas.input.MouseButtons;
import de.abscanvas.math.MDPoint;
import de.abscanvas.math.MPoint;
import de.abscanvas.surface.Bitmap;
import de.abscanvas.surface.Surface;
import de.abscanvas.ui.hud.HUD;
import de.abscanvas.ui.hud.HUDElement;

public class HUDPanel extends HUDElement {
	private ArrayList<HUDElement> elements = new ArrayList<HUDElement>();

	public HUDPanel(int x, int y, int width, int height, HUD owner) {
		super(x, y, width, height, new Bitmap(1, 1), owner);
	}

	@Override
	public void render(Surface surface) {
		if (visible) {
			for (HUDElement e : elements) {
				e.render(surface);
			}
		}
	}

	@Override
	public void tick() {
		if (visible) {
			for (HUDElement elem : elements) {
				if (elem.isVisible()) {
					MouseButtons mb = owner.getOwner().getOwner().getMouseButtons();

					MPoint mpi = mb.getPosition().clone();
					MDPoint mp = owner.getOwner().getOwner().screenToCanvas(mpi);

					if (elem.isMoused(mp)) {
						if (mb.isMouseMoving()) {
							elem.onMouseMove(mb);
						}

						if (mb.isPressed()) {
							elem.onMousePress(mb);
						} else if (mb.isReleased()) {
							elem.onMouseRelease(mb);
						} else {
							if (!elem.isHovered()) {
								elem.setHovered(true);
								elem.onMouseEnter(mb);
							} else {
								elem.onMouseHover(mb);
							}
						}
					} else {
						if (elem.isHovered()) {
							elem.setHovered(false);
							elem.onMouseLeave(mb);
						}
					}
				}
			}
		}

		for (HUDElement e : elements) {
			e.tick();
		}
	}

	public void addElement(HUDElement e) {
		elements.add(e);
	}

	public ArrayList<HUDElement> getElements() {
		return elements;
	}
	
	@Override
	protected void repaint() {
		for (HUDElement e : elements) {
			e.refresh();
		}
	}
}
