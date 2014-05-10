package de.abscanvas;

import java.awt.Graphics;

import javax.swing.JFrame;

import de.abscanvas.input.EmptyKeys;
import de.abscanvas.input.Keys;
import de.abscanvas.input.Keys.Key;
import de.abscanvas.input.MouseButtons;
import de.abscanvas.level.Level;
import de.abscanvas.level.ServerLevel;
import de.abscanvas.math.MDPoint;
import de.abscanvas.math.MPoint;
import de.abscanvas.network.ConsoleListener;
import de.abscanvas.sound.EmptySoundPlayer;
import de.abscanvas.sound.SoundPlayer;
import de.abscanvas.surface.Bitmap;
import de.abscanvas.ui.gui.Font;
import de.abscanvas.ui.gui.GUIMenu;
import de.abscanvas.ui.gui.GUIStack;

public abstract class ConsoleScreen implements Screen, Runnable {
	private static double framerate = 60;
	private int fps;
	private long ticktime = 1;

	private GUIStack menuStack = new GUIStack();

	private ServerLevel level;

	private ConsoleListener consoleListener;

	private SoundPlayer soundplayer;

	private MouseButtons mouseButtons = new MouseButtons();

	private MPoint offset = new MPoint(0, 0);

	private Keys keys;

	private boolean firstStart = true;
	private boolean running = false;
	
	public ConsoleScreen() {
		keys = new EmptyKeys();
	}

	@Override
	public double getMillisecondsPerFrame() {
		return 1000d / framerate;
	}

	@Override
	public double MPF() {
		return getMillisecondsPerFrame();
	}

	@Override
	public void start() {
		running = true;
		Thread thread = new Thread(this);
		thread.setPriority(Thread.MAX_PRIORITY);
		thread.start();

		if (firstStart) {
			System.out.println("AbsCanvas Engine v" + getVersionString() + " © by Mike Schwörer (www.mikescher.de)");
			firstStart = false;
		}
	}

	@Override
	public void stop() {
		running = false;
	}

	private void init() {
		soundplayer = new EmptySoundPlayer();

		onInit();
	}

	@Override
	public MPoint getOffset() {
		return offset;
	}

	@Override
	public void setOffset(MPoint offset) {
		this.offset = offset;
	}

	@Override
	public Level getLevel() {
		return level;
	}
	
	@Override
	public long getTickTime() {
		return ticktime;
	}

	@Override
	public void run() {
		long lastTime = System.nanoTime();
		double unprocessed = 0;
		int frames = 0;
		long lastTimer1 = System.currentTimeMillis();
		int toTick = 0;
		int tickCount = 0;

		try {
			init();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		while (running) {
			double nsPerTick = 1000000000.0 / framerate;

			while (unprocessed >= 1) {
				toTick++;
				unprocessed -= 1;
			}

			if (toTick > 0 && toTick < 3) {
				tickCount = 1;
			} else if (toTick > 20) {
				toTick = 20;
			} else {
				tickCount = toTick;
			}

			for (int i = 0; i < tickCount; i++) {
				toTick--;
				tick();
			}

			long now = System.nanoTime();
			unprocessed += (now - lastTime) / nsPerTick;
			ticktime = lastTime - now;
			lastTime = now;

			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (System.currentTimeMillis() - lastTimer1 > 1000) {
				lastTimer1 += 1000;
				fps = frames;
				frames = 0;
			}

		}

	}

	private void tick() {
		if (level != null) {
			level.tick();
		}
		menuStack.tick();

		tickMouseHideTime();

		onAfterTick();
	}

	public void consoleOutput(String s) {
		if (consoleListener == null) {
			System.out.println(s);
		} else {
			consoleListener.output(s);
		}
	}

	private void tickMouseHideTime() {
		// override this (maybe)
	}

	@Override
	public void forceMouseHidden() {
		// override this (maybe)
	}

	@Override
	public String getVersionString() {
		return VERSION;
	}

	@Override
	public void renderOnCustom(Graphics g) {
		// do nothing
	}

	@Override
	public void clearMenus() {
		menuStack.clear();
	}

	@Override
	public void addMenu(GUIMenu menu) {
		menuStack.add(menu);
	}

	@Override
	public void setMenu(GUIMenu menu) {
		menuStack.set(menu);
	}

	@Override
	public void popMenu() {
		menuStack.pop();
	}

	@Override
	public abstract void onAfterTick();

	@Override
	public void setFont(String letters, Bitmap[][] img) {
		// override this (maybe)
	}

	@Override
	public void drawString(String txt, int x, int y) {
		// do nothing
	}

	@Override
	public void drawStringAlpha(String txt, int x, int y, int a) {
		// do nothing
	}

	@Override
	public void drawString(Bitmap s, String txt, int x, int y) {
		// do nothing
	}

	@Override
	public int getFPS() {
		return fps;
	}

	@Override
	public SoundPlayer getSoundPlayer() {
		return soundplayer;
	}

	@Override
	public int getScreenHeight() {
		return 1;
	}

	@Override
	public int getScreenWidth() {
		return 1;
	}

	@Override
	public double getScreenScale() {
		return 1;
	}

	@Override
	public void setScreenScale(double s) {
		// do nothing
	}

	@Override
	public int getTotalScreenHeight() {
		return 1;
	}

	@Override
	public int getTotalScreenWidth() {
		return 1;
	}

	@Override
	public JFrame getOwner() {
		return null;
	}

	@Override
	public abstract void onInit();
	
	@Override
	public abstract void onStop();

	@Override
	public MouseButtons getMouseButtons() {
		return mouseButtons;
	}

	@Override
	public void setLevel(Level l) {
		level = (ServerLevel) l;
	}

	@Override
	public MDPoint screenToCanvas(MPoint p) {
		return new MDPoint(0, 0);
	}

	@Override
	public MPoint canvasToScreen(MDPoint mp) {
		return new MPoint(0, 0);
	}

	@Override
	public GUIStack getMenuStack() {
		return menuStack;
	}

	@Override
	public void addToKeyMap(int keycode, Key key) {
		// do nothing
	}

	@Override
	public Keys getKeys() {
		return keys;
	}

	public ConsoleListener getConsoleListener() {
		return consoleListener;
	}

	public void setConsoleListener(ConsoleListener consoleListener) {
		this.consoleListener = consoleListener;
	}
	
	@Override
	public Font getACFont() {
		return null;
	}
}
