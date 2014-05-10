package de.abscanvas.test.local;

import de.abscanvas.input.MouseButtons;
import de.abscanvas.level.Level;
import de.abscanvas.ui.hud.HUD;
import de.abscanvas.ui.hud.elements.HUDButton;
import de.abscanvas.ui.hud.elements.HUDLabel;
import de.abscanvas.ui.hud.elements.HUDNativeLabel;
import de.abscanvas.ui.listener.ButtonListener;

public class GameHUD extends HUD implements ButtonListener{
	public HUDNativeLabel fpslabel;
	public HUDNativeLabel entitielabel;
	public HUDLabel positionLbl;
	public HUDButton hudButton;
	
	private final int BTN_MAGIC = 666;
	
	public GameHUD(Level owner) {
		super(owner);
		create();
	}
	
	private void create() {
		setImage(GameArt.panel);
		
		setAlignment(HUD.ALIGN_BOTTOMRIGHT);
		hudButton = new HUDButton(445, 313, GameArt.button[0].getWidth(), GameArt.button[0].getHeight(), GameArt.button, BTN_MAGIC, this);
		fpslabel = new HUDNativeLabel(337, 343, "FPS : 0", this);
		entitielabel = new HUDNativeLabel(337, 358, "Ents: : 0", this);
		positionLbl = new HUDLabel(2, 2, "", this);
		positionLbl.setColor(-1);
		positionLbl.setFont(new java.awt.Font("Arial", 0, 10));
		addElement(hudButton);
		addElement(fpslabel);
		addElement(entitielabel);
		addElement(positionLbl);
		hudButton.addListener(this);
	}

	@Override
	public void buttonPressed(MouseButtons mouse, int id) {
		if (id == BTN_MAGIC) {
			((GameScreen)owner.getOwner()).player.resetPosition();
			System.out.println(owner.getOwner().getKeys().getKeyHistory());
			
		}
	}
	
	@Override
	public void tick() {
		super.tick();
		positionLbl.setText(((GameScreen)owner.getOwner()).getPlayer().getRoundedPos().asString());
	}

	@Override
	public void buttonMouseDown(MouseButtons mouse, int id) {
		//nocode
	}

	@Override
	public void buttonMouseEnter(MouseButtons mouse, int id) {
		//nocode
	}

	@Override
	public void buttonMouseLeave(MouseButtons mouse, int id) {
		//nocode
	}
}
