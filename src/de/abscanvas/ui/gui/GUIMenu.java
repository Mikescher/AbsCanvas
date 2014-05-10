package de.abscanvas.ui.gui;

import java.util.ArrayList;
import java.util.List;

import de.abscanvas.Screen;
import de.abscanvas.entity.MenuEffectEntity;
import de.abscanvas.entity.MenuEntity;
import de.abscanvas.input.MouseButtons;
import de.abscanvas.math.BoxBounds;
import de.abscanvas.math.MDPoint;
import de.abscanvas.math.MPoint;
import de.abscanvas.surface.Bitmap;
import de.abscanvas.surface.Surface;
import de.abscanvas.ui.gui.GUIElement;

public class GUIMenu {
	public static final int ALIGN_UPPERLEFT = 1;
	public static final int ALIGN_UPPERRIGHT = 2;
	public static final int ALIGN_BOTTOMLEFT = 3;
	public static final int ALIGN_BOTTOMRIGHT = 4;
	public static final int ALIGN_CENTER = 5;

	private int alignment = 1;
	private Bitmap img = new Bitmap(0, 0);

	private MenuAnimation showAnimation;
	private MenuAnimation hideAnimation;

	private ArrayList<GUIElement> elements = new ArrayList<GUIElement>();
	
	private boolean tickEntities = true;
	private List<MenuEntity> entities = new ArrayList<MenuEntity>();
	private Class<? extends MenuEntity>[] paintOrder = null;
	
	private boolean renderElementsOnTop = false;

	private Screen owner;

	public GUIMenu(Screen owner) {
		this.owner = owner;
		showAnimation = new EmptyAnimation(this);
		hideAnimation = new EmptyAnimation(this);
	}

	public void render(Surface surface) {
		renderBackground(surface);
		
		if (renderElementsOnTop) {
			renderEntities(surface);
			renderElements(surface);
		} else  {
			renderElements(surface);
			renderEntities(surface);
		}	
	}

	private void renderEntities(Surface surface) {
		List<MenuEntity> orderedEntities = orderEntities(entities);
		
		for (MenuEntity e : orderedEntities) {
			e.render(surface);
		}
	}

	private void renderElements(Surface surface) {
		for (GUIElement e : elements) {
			e.render(surface);
		}
	}

	private void renderBackground(Surface surface) {
		switch (alignment) {
		case ALIGN_UPPERLEFT:
			surface.blit(img, 0, 0);
			break;
		case ALIGN_UPPERRIGHT:
			surface.blit(img, owner.getScreenWidth() - img.getWidth(), 0);
			break;
		case ALIGN_BOTTOMLEFT:
			surface.blit(img, 0, owner.getScreenHeight() - img.getHeight());
			break;
		case ALIGN_BOTTOMRIGHT:
			surface.blit(img, owner.getScreenWidth() - img.getWidth(), owner.getScreenHeight() - img.getHeight());
			break;
		case ALIGN_CENTER:
			surface.blit(img, owner.getScreenWidth() / 2 - img.getWidth() / 2, owner.getScreenHeight() / 2 - img.getHeight() / 2);
			break;
		}
	}

	public void tickAnimations() {
		showAnimation.tick();
		hideAnimation.tick();
	}

	public void tick() {
		for (GUIElement elem : elements) {
			if (elem.isVisible()) {
				MouseButtons mb = owner.getMouseButtons();

				MPoint mpi = mb.getPosition().clone();
				MDPoint mp = owner.screenToCanvas(mpi);

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

		for (GUIElement e : elements) {
			e.tick();
		}
		
		
		MouseButtons mb = owner.getMouseButtons();
		
		if (tickEntities) {
			for (int i = 0; i < entities.size(); i++) {
				MenuEntity e = entities.get(i);

				if (!e.isRemoved()) {
					MDPoint mp = mb.getPosition().asMDPoint();
					BoxBounds bbs = e.getBoxBounds();

					if (mp.isInRect(bbs)) {
						if (mb.isMouseMoving()) {
							e.onMouseMove(mb);
						}

						if (mb.isPressed()) {
							e.onMousePress(mb);
						} else if (mb.isReleased()) {
							e.onMouseRelease(mb);
						} else {
							if (!e.isHovered()) {
								e.setHovered(true);
								e.onMouseEnter(mb);
							} else {
								e.onMouseHover(mb);
							}
						}
					} else {
						if (e.isHovered()) {
							e.setHovered(false);
							e.onMouseLeave(mb);
						}
					}

					e.tick();
				}
				
				if (e.isRemoved()) {
					entities.remove(i--);
				}
			}
		}
	}
	
	public List<MenuEntity> orderEntities(List<MenuEntity> s) {
		List<MenuEntity> lst = new ArrayList<MenuEntity>();

		if (paintOrder != null) {
			for (Class<? extends MenuEntity> c : paintOrder) {
				for (MenuEntity se : s) {
					if (c.isAssignableFrom(se.getClass())) {
						lst.add(se);
					}
				}
			}
		}

		for (MenuEntity se : s) {
			if (!lst.contains(se)) {
				lst.add(se);
			}
		}

		return lst;
	}
	
	public List<BoxBounds> getClipBoxBounds(MenuEntity e) {
		List<BoxBounds> result = new ArrayList<BoxBounds>();

		for (MenuEntity ee : entities) {
			if (ee != e) {
				result.add(ee.getBoxBounds());
			}
		}

		return result;
	}
	
	public void addEntity(MenuEntity e) {
		e.init(this);
		entities.add(e);
	}
	
	public int getEntityCount(Class<? extends MenuEntity> c) {
		int r = 0;
		for (MenuEntity e : entities) {
			if (c.isAssignableFrom(e.getClass())) {
				r++;
			}
		}
		return r;
	}
	
	public ArrayList<MenuEntity> getAllEntities(Class<? extends MenuEntity> c) {
		ArrayList<MenuEntity> r = new ArrayList<MenuEntity>();
		for (MenuEntity e : entities) {
			if (c.isAssignableFrom(e.getClass())) {
				r.add(e);
			}
		}
		return r;
	}

	public void setPaintOrder(Class<? extends MenuEntity>[] c) {
		paintOrder = c;
	}

	public Screen getOwner() {
		return owner;
	}

	public void setImage(Bitmap img) {
		this.img = img;
	}

	public void setAlignment(int a) {
		alignment = a;
	}

	public void addElement(GUIElement e) {
		elements.add(e);
	}
	
	public boolean removeElement(GUIElement e) {
		return elements.remove(e);
	}

	public ArrayList<GUIElement> getElements() {
		return elements;
	}

	public boolean hasShowAnimation() {
		return showAnimation != null;
	}

	public void setShowAnimation(MenuAnimation a) {
		showAnimation = a;
		showAnimation.reset();
	}

	public void removeShowAnimation() {
		showAnimation = new EmptyAnimation(this);
		showAnimation.reset();
	}

	public MenuAnimation getShowAnimation() {
		return showAnimation;
	}

	public boolean hasHideAnimation() {
		return showAnimation != null;
	}

	public void setHideAnimation(MenuAnimation a) {
		hideAnimation = a;
		a.reset();
	}

	public void removeHideAnimation() {
		hideAnimation = new EmptyAnimation(this);
		hideAnimation.reset();
	}

	public MenuAnimation getHideAnimation() {
		return hideAnimation;
	}
	
	public void addEffect(Bitmap[] a, int duration, int x, int y) {
		MenuEntity e = new MenuEffectEntity(a, duration, x, y);
		addEntity(e);
	}
	
	public void renderElementsOnTop() {
		renderElementsOnTop = true;
	}
	
	public void renderEntitiesOnTop() {
		renderElementsOnTop = false;
	}

	public List<MenuEntity> getEntities() {
		return entities;
	}
}
