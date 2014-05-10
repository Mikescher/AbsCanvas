package de.abscanvas.test.client;

import de.abscanvas.additional.chat.HUDChat;
import de.abscanvas.input.MouseButtons;
import de.abscanvas.level.Level;
import de.abscanvas.ui.hud.HUD;
import de.abscanvas.ui.hud.elements.HUDLabel;
import de.abscanvas.ui.hud.elements.HUDNativeLabel;
import de.abscanvas.ui.listener.ButtonListener;

public class GameHUD extends HUD implements ButtonListener{
	public HUDNativeLabel fpslabel;
	public HUDNativeLabel entitielabel;
	public HUDLabel positionLbl;
	public HUDChat chat;
	public HUDMap map;
	
	public GameHUD(Level owner) {
		super(owner);
		create();
	}
	
	private void create() {
		setImage(GameArt.panel);
		
		setAlignment(HUD.ALIGN_BOTTOMRIGHT);
		map = new HUDMap(getOwner().getOwner().getScreenWidth() - 83, getOwner().getOwner().getScreenHeight() - 82, this);
		fpslabel = new HUDNativeLabel(337, 343, "FPS : 0", this);
		entitielabel = new HUDNativeLabel(337, 358, "Ents: : 0", this);
		positionLbl = new HUDLabel(2, 2, "", this);
		positionLbl.setColor(-1);
		positionLbl.setFont(new java.awt.Font("Arial", 0, 10));
		addElement(fpslabel);
		addElement(entitielabel);
		addElement(positionLbl);
		chat = new HUDChat(10, 165, 200, 150,getOwner().getOwner().getKeys(), this);
		addElement(chat);
		addElement(map);
	}

	@Override
	public void buttonPressed(MouseButtons mouse, int id) {
		// Event - override this as needed
	}
	
	@Override
	public void tick() {
		super.tick();
		Player p =	((GameScreen)owner.getOwner()).player;
		if (p != null) {
			positionLbl.setText(p.getRoundedPos().asString());
		}
		
		fpslabel.setText(getOwner().getOwner().getFPS() + " FPS");
	}

	@Override
	public void buttonMouseDown(MouseButtons mouse, int id) {
		// Event - override this as needed
	}

	@Override
	public void buttonMouseEnter(MouseButtons mouse, int id) {
		// Event - override this as needed
	}

	@Override
	public void buttonMouseLeave(MouseButtons mouse, int id) {
		// Event - override this as needed
	}
}
