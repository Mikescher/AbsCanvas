package de.abscanvas.ui.gui;

import java.util.Stack;

import de.abscanvas.surface.Surface;

public class GUIStack {
	private final static int MODE_STANDARD = 0;
	private final static int MODE_TRANSITION = 1;

	private final static int MODIFIER_ADD = 0;
	private final static int MODIFIER_SET = 1;

	private int mode = MODE_STANDARD;
	private int modifier = MODIFIER_ADD;

	private Stack<GUIMenu> menuStack = new Stack<GUIMenu>();
	private GUIMenu paramMenu = null;

	public GUIStack() {

	}

	public void render(Surface s) {
		if (isEmpty()) {
			return;
		}
		
		menuStack.peek().render(s);
	}

	public void tick() {
		if (isEmpty()) {
			return;
		}
		
		if (isTransition()) { 						// Es wird gerade animiert
			tickAnimations();
			
			checkTransitionEnd();
		} else {
			menuStack.peek().tick();				// Alles normal
		}
	}
	
	private void checkTransitionEnd() {
		if (isEmpty()) {
			return;
		}
		
		if (isTransition()) {
			if (isShowAnimationReady()) { 			// Die Fade-In Animation is fertig
				resetShowAnimation();
				setModeStandard();
				resetWaitingMenu();
			} else if (isHideAnimationReady()) { 	// Die Fade-Out Animation is fertig
				resetHideAnimation();
				if (hasWaitingMenu()) { 			// Ein neues Menu will angezeigt werden
					if (isModifier_SET()) {
						clear();
					}

					menuStack.add(paramMenu);
					resetWaitingMenu();
					setModeTransition();
					startShowAnimation();
				} else { 							// Ein altes Menu will angezeigt werden
					menuStack.pop();
					if (!isEmpty()) {
						setModeTransition();
						startShowAnimation();
					} else {
						setModeStandard();
					}
				}
				
				checkTransitionEnd();
			}
		}
	}

	public void finishTransition() {
		if (isEmpty()) {
			return;
		}
		
		if (isTransition()) {
			resetShowAnimation();
			resetHideAnimation();
			
			if (hasWaitingMenu()) {
				if (isModifier_SET()) {
					clear();
				}
				menuStack.add(paramMenu);
			} else {
				menuStack.pop();
			}
			
			resetWaitingMenu();
		}
	}
	
	public void add(GUIMenu m) {
		finishTransition();
		
		if (!isEmpty()) {
			SetWaitingMenu(m);
			startHideAnimation();
		} else {
			menuStack.add(m);
			startShowAnimation();
		}

		setModeTransition();
		setModifierAdd();
		
		checkTransitionEnd();
	}

	public void pop() {
		if (isEmpty()) {
			return;
		}
		
		finishTransition();
		
		menuStack.peek().getHideAnimation().start();
		
		setModeTransition();
		resetWaitingMenu();
		
		checkTransitionEnd();
	}

	public void set(GUIMenu m) {
		finishTransition();
		
		if (!isEmpty()) {
			SetWaitingMenu(m);
			startHideAnimation();
		} else {
			resetWaitingMenu();
			menuStack.add(m);
			startShowAnimation();
		}
		
		setModeTransition();
		setModifierSet();
		
		checkTransitionEnd();
	}

	public void clear() {
		menuStack.clear();
	}

	public Stack<GUIMenu> getStack() {
		return menuStack;
	}
	
	public boolean isEmpty() {
		return menuStack.isEmpty();
	}

	public boolean isTransition() {
		return mode == MODE_TRANSITION;
	}

	public boolean isModifier_ADD() {
		return modifier == MODIFIER_ADD;
	}

	public boolean isModifier_SET() {
		return modifier == MODIFIER_SET;
	}
	
	private void setModeTransition() {
		mode = MODE_TRANSITION;
	}
	
	private void setModeStandard() {
		mode = MODE_STANDARD;
	}
	
	private void setModifierAdd() {
		modifier = MODIFIER_ADD;
	}
	
	private void setModifierSet() {
		modifier = MODIFIER_SET;
	}
	
	private boolean hasWaitingMenu() {
		return paramMenu != null;
	}
	
	private void resetWaitingMenu() {
		paramMenu = null;
	}
	
	private void SetWaitingMenu(GUIMenu m) {
		paramMenu = m;
	}
	
	public int getMenuCount() {
		return menuStack.size() + ((paramMenu == null) ? 0 : 1);
	}

	private void startShowAnimation() {
		menuStack.peek().getShowAnimation().start();
	}
	
	private void startHideAnimation() {
		menuStack.peek().getHideAnimation().start();
	}
	
	private void resetShowAnimation() {
		menuStack.peek().getShowAnimation().reset();
	}
	
	private void resetHideAnimation() {
		menuStack.peek().getHideAnimation().reset();
	}
	
	private boolean isShowAnimationReady() {
		return menuStack.peek().getShowAnimation().isReady();
	}
	
	private boolean isHideAnimationReady() {
		return menuStack.peek().getHideAnimation().isReady();
	}
	
	private void tickAnimations() {
		menuStack.peek().tickAnimations();
	}
}
