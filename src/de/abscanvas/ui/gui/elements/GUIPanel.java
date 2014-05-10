package de.abscanvas.ui.gui.elements;

import java.util.ArrayList;

import de.abscanvas.input.MouseButtons;
import de.abscanvas.math.MDPoint;
import de.abscanvas.math.MPoint;
import de.abscanvas.surface.Bitmap;
import de.abscanvas.surface.Surface;
import de.abscanvas.ui.gui.GUIMenu;
import de.abscanvas.ui.gui.GUIElement;

public class GUIPanel extends GUIElement {
	private ArrayList<GUIElement> elements = new ArrayList<GUIElement>();

	public GUIPanel(int x, int y, int width, int height, GUIMenu owner) {
		super(x, y, width, height, new Bitmap(1, 1), owner);
	}

	@Override
	public void render(Surface surface) {
		if (visible) {
			for (GUIElement e : elements) {
				e.render(surface);
			}
		}
	}

	@Override
	public void tick() {
		if (visible) {
			for (GUIElement elem : elements) {
				if (elem.isVisible()) {
					MouseButtons mb = owner.getOwner().getMouseButtons();

					MPoint mpi = mb.getPosition().clone();
					MDPoint mp = owner.getOwner().screenToCanvas(mpi);

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

		for (GUIElement e : elements) {
			e.tick();
		}
	}

	public void addElement(GUIElement e) {
		elements.add(e);
	}

	public ArrayList<GUIElement> getElements() {
		return elements;
	}

	@Override
	protected void repaint() {
		for (GUIElement e : elements) {
			e.refresh();
		}
	}
}
