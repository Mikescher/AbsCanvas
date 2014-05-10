package de.abscanvas.test.local;

import java.awt.event.KeyEvent;

import de.abscanvas.DestkopScreen;
import de.abscanvas.entity.Entity;

public class GameScreen extends DestkopScreen {
	private static final long serialVersionUID = 2L;

	public String f_letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ   " + "0123456789-.!?/%$\\=*+,;:()&#\"'";
	
	public Player player;
	
	private GameKeys k;

	public GameScreen(int width, int height, MainWindow owner) {
		super(width, height, new GameKeys(), 2, owner);
		k = (GameKeys) getKeys();
	}

	@Override
	public void onInit() {
		GameArt.init();

		setFont(f_letters, GameArt.font);

		addMenu(new MainMenu(this));
//		createLevel();

		addToKeyMap(KeyEvent.VK_SHIFT, k.shift);
		
		addToKeyMap(KeyEvent.VK_UP, k.up);
		addToKeyMap(KeyEvent.VK_DOWN, k.down);
		addToKeyMap(KeyEvent.VK_LEFT, k.left);
		addToKeyMap(KeyEvent.VK_RIGHT, k.right);

		addToKeyMap(KeyEvent.VK_W, k.up);
		addToKeyMap(KeyEvent.VK_S, k.down);
		addToKeyMap(KeyEvent.VK_A, k.left);
		addToKeyMap(KeyEvent.VK_D, k.right);

//		getSoundPlayer().startBackgroundSound("/Background 1.ogg", "B_GROUND");
	}

	private void createHUD() {
		getLevel().setHUD(new GameHUD(getLevel()));
	}

	public void createLevel() {
		setLevel(new GameLevel(this));

		createHUD();
	}

	@Override
	public void onAfterTick() {
		if (getLevel() != null) {
			if (((GameHUD) getLevel().getHUD()).fpslabel != null) {
				((GameHUD) getLevel().getHUD()).fpslabel.setText("FPS: " + getFPS());
			}
			if (((GameHUD) getLevel().getHUD()).entitielabel != null) {
				((GameHUD) getLevel().getHUD()).entitielabel.setText("Ents: " + getLevel().getEntities().size());
			}
		}
	}

	public Entity getPlayer() {
		return player;
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		
	}
}