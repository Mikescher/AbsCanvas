package de.abscanvas.input;

import java.util.ArrayList;
import java.util.List;

import de.abscanvas.Screen;
import de.abscanvas.entity.Entity;
import de.abscanvas.level.Level;
import de.abscanvas.math.MDPoint;
import de.abscanvas.math.MPoint;
import de.abscanvas.ui.gui.GUIMenu;
import de.abscanvas.ui.gui.GUIElement;
import de.abscanvas.ui.hud.HUD;
import de.abscanvas.ui.hud.HUDElement;
import de.abscanvas.ui.hud.elements.HUDPanel;

public class MouseButtons {
	protected boolean mouseMoved = false;

	private boolean[][] prevHistory = new boolean[15][4]; // 15*16.6666 = 250ms = DoubleClickDuration
	private boolean[] prevState = new boolean[4];
	private boolean[] currentState = new boolean[4];
	private boolean[] tempState = new boolean[4];

	private int currentMouseWheel = 0;

	private MPoint pos = new MPoint();

	// ---------------------------------------------------------------------------------
	// ------------- BUTTON-GETTER ---------------------------------------------------
	// ---------------------------------------------------------------------------------

	public boolean isDown(int button) {
		return currentState[button];
	}

	public boolean isNextDown(int button) {
		return tempState[button];
	}

	public boolean isPressed(int button) {
		return !prevState[button] && currentState[button];
	}

	public boolean isReleased(int button) {
		return prevState[button] && !currentState[button];
	}

	public boolean isDoubleClick(int button) {
		return (getClickCountInHistory(button) > 0) && isPressed(button);
	}

	// ---------------------------------------------------------------------------------
	// ------------- TICKING ---------------------------------------------------------
	// ---------------------------------------------------------------------------------

	public void tick() {
		for (int i = 0; i < currentState.length; i++) {
			for (int j = 0; j < prevHistory.length - 1; j++) {
				prevHistory[j][i] = prevHistory[j + 1][i];
			}
			prevHistory[prevHistory.length - 1][i] = prevState[i];

			prevState[i] = currentState[i];
			currentState[i] = tempState[i];
		}
		currentMouseWheel = 0;
	}

	public void releaseAll() {
		for (int i = 0; i < tempState.length; i++) {
			tempState[i] = false;
		}
	}

	// ---------------------------------------------------------------------------------
	// ------------- FULL-GETTER -----------------------------------------------------
	// ---------------------------------------------------------------------------------

	public boolean isDown() {
		return isDown(0) || isDown(1) || isDown(2) || isDown(3);
	}

	public boolean isNextDown() {
		return isNextDown(0) || isNextDown(1) || isNextDown(2) || isNextDown(3);
	}

	public boolean isPressed() {
		return isPressed(0) || isPressed(1) || isPressed(2) || isPressed(3);
	}

	public boolean isReleased() {
		return isReleased(0) || isReleased(1) || isReleased(2) || isReleased(3);
	}

	public boolean isDoubleClick() {
		return isDoubleClick(0) || isDoubleClick(1) || isDoubleClick(2) || isDoubleClick(3);
	}

	// ---------------------------------------------------------------------------------
	// ------------- SETTER ----------------------------------------------------------
	// ---------------------------------------------------------------------------------

	public void setNextState(int button, boolean value) {
		tempState[button] = value;
	}

	public void setMouseWheelState(int rot) {
		currentMouseWheel += rot;
	}

	public void setPosition(MPoint mousePosition) {
		if (mousePosition.isValid()) {
			pos.set(mousePosition);
		}
	}

	public void setMouseMoved(boolean b) {
		mouseMoved = b;
	}

	// -------------------------------------------------------------------------------
	// -------------- POSITION -------------------------------------------------------
	// ---------------------------------------------------------------------------------

	public int getX() {
		return pos.getX();
	}

	public int getY() {
		return pos.getY();
	}

	public MPoint getPosition() {
		return new MPoint(pos);
	}

	public int getMouseWheelRotation() {
		return currentMouseWheel;
	}

	public boolean isMouseMoving() {
		return mouseMoved;
	}

	// ---------------------------------------------------------------------------------
	// -------------- ELEMENTS ---------------------------------------------------------
	// ---------------------------------------------------------------------------------

	public ArrayList<Entity> getHoveredEntities(Level l) {
		MPoint pi = pos.clone();
		MDPoint p = l.getOwner().screenToCanvas(pi);
		p.add(l.getOwner().getOffset().asMDPoint());

		ArrayList<Entity> al = new ArrayList<Entity>();
		for (Entity e : l.getEntities()) {
			if (p.isInRect(e.getBoxBounds())) {
				al.add(e);
			}
		}

		return al;
	}

	public ArrayList<GUIElement> getHoveredGUIElements(GUIMenu l) {
		MPoint pi = pos.clone();
		MDPoint p = l.getOwner().screenToCanvas(pi);

		ArrayList<GUIElement> al = new ArrayList<GUIElement>();
		for (GUIElement e : l.getElements()) {
			
			if (p.isInRect(e.getBoxBounds()) && e.isVisible()) {
				al.add(e);
			}
		}

		return al;
	}

	public ArrayList<HUDElement> getHoveredHUDElements(HUD l) {
		if (l == null) {
			return new ArrayList<HUDElement>();
		} else {
			return getHoveredHUDElements(l.getElements(), l.getOwner().getOwner());
		}
	}

	public ArrayList<HUDElement> getHoveredHUDElements(List<HUDElement> els, Screen s) {
		MPoint pi = pos.clone();
		MDPoint p = s.screenToCanvas(pi);

		ArrayList<HUDElement> al = new ArrayList<HUDElement>();

		for (HUDElement e : els) {
			if (e.isVisible()) {
				if (e instanceof HUDPanel) {
					al.addAll(getHoveredHUDElements(((HUDPanel) e).getElements(), s));
				} else if (p.isInRect(e.getBoxBounds())) {
					al.add(e);
				}
			}
		}

		return al;
	}

	public boolean isHover(HUD hud) {
		if (hud != null) {
			MPoint pi = pos.clone();
			MDPoint p = hud.getOwner().getOwner().screenToCanvas(pi);

			if (p.isInRect(hud.getBoxBounds())) {
				return true;
			}
		}
		return false;
	}

	public boolean isHovered(GUIElement el) {
		if (el != null) {
			MPoint pi = pos.clone();
			MDPoint p = el.getOwner().getOwner().screenToCanvas(pi);

			if (p.isInRect(el.getBoxBounds())) {
				return true;
			}
		}
		return false;
	}

	public boolean isHovered(HUDElement el) {
		if (el != null) {
			MPoint pi = pos.clone();
			MDPoint p = el.getOwner().getOwner().getOwner().screenToCanvas(pi);

			if (p.isInRect(el.getBoxBounds())) {
				return true;
			}
		}
		return false;
	}

	private int getClickCountInHistory(int button) {
		int c = 0;
		for (int i = 0; i < prevHistory.length; i++) {
			if (prevHistory[i][button]) {
				c++;
			}
		}

		return c;
	}
}
