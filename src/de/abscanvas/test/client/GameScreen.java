package de.abscanvas.test.client;

import java.awt.event.KeyEvent;

import de.abscanvas.DestkopScreen;

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

//		try {
//			setLevel(new GameLevel(this));
//		} catch (UnknownHostException e) {
//			e.printStackTrace();
//		}
		
		setMenu(new MainMenu(this));

		addToKeyMap(KeyEvent.VK_UP, k.up);
		addToKeyMap(KeyEvent.VK_DOWN, k.down);
		addToKeyMap(KeyEvent.VK_LEFT, k.left);
		addToKeyMap(KeyEvent.VK_RIGHT, k.right);
		
		addToKeyMap(KeyEvent.VK_SHIFT, k.shift);

		addToKeyMap(KeyEvent.VK_W, k.up);
		addToKeyMap(KeyEvent.VK_S, k.down);
		addToKeyMap(KeyEvent.VK_A, k.left);
		addToKeyMap(KeyEvent.VK_D, k.right);
	}

	@Override
	public void onAfterTick() {
		// Event - override this as needed
	}

	@Override
	public void onStop() {
		// This is the end ...
	}
}
