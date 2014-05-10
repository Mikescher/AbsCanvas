package de.abscanvas.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

import de.abscanvas.input.Keys.Key;

public class InputHandler implements KeyListener {
	private Map<Integer, Key> mappings = new HashMap<Integer, Key>();
	private Keys keys;

	public InputHandler(Keys keys) {
		this.keys = keys;
	}

	@Override
	public void keyPressed(KeyEvent evt) {
		toggle(evt, true);
	}

	@Override
	public void keyReleased(KeyEvent evt) {
		toggle(evt, false);
	}

	@Override
	public void keyTyped(KeyEvent evt) {
		// Dont do anything
	}
	
	private void toggle(KeyEvent ke, boolean state) {
		Key key = mappings.get(ke.getKeyCode());
		if (key != null) {
			key.nextState = state;
		}
		keys.keyEvent(ke, state);
	}
	
	public void put(int keyevent, Key key) {
		mappings.put(keyevent, key);
	}

}
