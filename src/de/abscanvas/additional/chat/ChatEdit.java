package de.abscanvas.additional.chat;

import java.awt.event.KeyEvent;

import de.abscanvas.input.Keys;
import de.abscanvas.surface.AbsColor;
import de.abscanvas.surface.Surface;
import de.abscanvas.ui.hud.elements.HUDEdit;

public class ChatEdit extends HUDEdit {
	private HUDChat chat;
	
	public ChatEdit(int x, int y, int width, int height, Keys keys, HUDChat owner) {
		super(x, y, width, height, keys, owner.getOwner());
		chat = owner;
		
		create();
	}

	private void create() {
		setColorScheme(AbsColor.BLACK, AbsColor.BLACK, AbsColor.WHITE);
		setClearOnClick(true);
	}
	
	@Override
	public void tick() {
		super.tick();
		if (keys.isKeyPressed(KeyEvent.VK_ENTER) && isFocused()) {
			chat.sendMessage();
		}
	}
	
	@Override
	public void render(Surface surface) {
		int a = 255;
		if (! isFocused()) {
			a = 50;
		}
		if (visible) {
			surface.alphaBlit(img, pos.getX(), pos.getY(), a);
		}
	}
}
