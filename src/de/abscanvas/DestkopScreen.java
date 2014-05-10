package de.abscanvas;

import java.awt.Canvas;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import de.abscanvas.input.InputHandler;
import de.abscanvas.input.Keys;
import de.abscanvas.input.Keys.Key;
import de.abscanvas.input.MouseButtons;
import de.abscanvas.level.Level;
import de.abscanvas.math.MDPoint;
import de.abscanvas.math.MPoint;
import de.abscanvas.sound.PaulscodePlayer;
import de.abscanvas.sound.SoundPlayer;
import de.abscanvas.surface.AbsColor;
import de.abscanvas.surface.Bitmap;
import de.abscanvas.surface.DestkopSurface;
import de.abscanvas.ui.gui.Font;
import de.abscanvas.ui.gui.GUIMenu;
import de.abscanvas.ui.gui.GUIStack;

public abstract class DestkopScreen extends Canvas implements Screen, Runnable, MouseMotionListener, MouseListener, MouseWheelListener {
	private static final long serialVersionUID = 1L;

	private JFrame owner;

	private Font font = new Font();

	private boolean running = false;

	private int screen_height;
	private int screen_width;
	private double screen_scale = 1;

	private static double framerate = 60;
	private int fps;
	private long ticktime = 1;
	
	private GUIStack menuStack = new GUIStack();

	private DestkopSurface surface;
	private Level level;

	private MPoint offset = new MPoint(0, 0);

	private Cursor emptyCursor;

	private SoundPlayer soundplayer;

	private Keys keys;
	private InputHandler inputhandler;
	private MouseButtons mouseButtons = new MouseButtons();
	private boolean mouseHidden = false;
	private int mouseHideTime = 0;
	
	private boolean firstStart = true;

	public DestkopScreen(int width, int height, Keys keys, double scale, JFrame owner) {
		screen_height = height;
		screen_width = width;
		screen_scale = scale;
		this.keys = keys;

		this.owner = owner;

		surface = new DestkopSurface(screen_width, screen_height);

		setPreferredSize(new Dimension(getTotalScreenWidth(), getTotalScreenHeight()));
		setMinimumSize(new Dimension(getTotalScreenWidth(), getTotalScreenHeight()));
		setMaximumSize(new Dimension(getTotalScreenWidth(), getTotalScreenHeight()));

		inputhandler = new InputHandler(keys);
		addKeyListener(inputhandler);
		addMouseMotionListener(this);
		addMouseListener(this);
		addMouseWheelListener(this);
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
	public void mouseDragged(MouseEvent arg0) {
		mouseButtons.setMouseMoved(true);
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		mouseButtons.setMouseMoved(true);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// do nothing
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// do nothing
	}

	@Override
	public void mouseExited(MouseEvent e) {
		mouseButtons.releaseAll();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		mouseButtons.setNextState(e.getButton(), true);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		mouseButtons.setNextState(e.getButton(), false);
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		mouseButtons.setMouseWheelState(e.getWheelRotation());
	}

	@Override
	public void addToKeyMap(int keycode, Key key) {
		inputhandler.put(keycode, key);
	}

	@Override
	public Keys getKeys() {
		return keys;
	}
	
	@Override
	public long getTickTime() {
		return ticktime;
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
		if (soundplayer != null) {
			soundplayer.shutdown();
		}
		
		onStop();
	}

	private void init() {
		try {
			emptyCursor = Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB), new java.awt.Point(0, 0), "empty");
		} catch (RuntimeException e) {
			e.printStackTrace();
		}

		soundplayer = new PaulscodePlayer();

		setFocusTraversalKeysEnabled(false);
		requestFocus();

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
	public void run() {
		long lastTime = System.nanoTime();
		double unprocessed = 0;
		int frames = 0;
		long lastTimer1 = System.currentTimeMillis();
		int toTick = 0;
		int tickCount = 0;
		long lastRenderTime = System.nanoTime();
		int min = 999999999;
		int max = 0;

		try {
			init();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		while (running) {
			if (!this.hasFocus()) {
				keys.release();
			}

			double nsPerTick = 1000000000.0 / framerate;
			boolean shouldRender = false;
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
				shouldRender = true; // render only if tickcount > 0
			}

			BufferStrategy bs = getBufferStrategy();
			if (bs == null) {
				createBufferStrategy(3);
				continue;
			}
			if (shouldRender) {
				frames++;
				Graphics g = bs.getDrawGraphics();

				render(g);

				long renderTime = System.nanoTime();
				int timePassed = (int) (renderTime - lastRenderTime); // Time since last Render in ns
				if (timePassed < min) {
					min = timePassed;
				}
				if (timePassed > max) { // max 1s
					max = timePassed;
				}
				lastRenderTime = renderTime;
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

			if (shouldRender) {
				if (bs != null) {
					bs.show();
				}
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

		keys.tick();

		tickMouseHideTime();

		mouseButtons.tick();

		onAfterTick();
	}

	private void tickMouseHideTime() {
		mouseButtons.setPosition(new MPoint(getMousePosition()));
		if (mouseButtons.isMouseMoving() || mouseButtons.isPressed() || level == null) {
			mouseButtons.setMouseMoved(false);
			mouseHideTime = 0;
			if (mouseHidden) {
				setMouseHidden(false);
			}
		}
		if (mouseHideTime < 120) {
			mouseHideTime++;
			if (mouseHideTime == 120) {
				setMouseHidden(true);
			}
		}
	}

	@Override
	public void forceMouseHidden() {
		mouseHideTime = Integer.MIN_VALUE;
	}

	private void setMouseHidden(boolean b) {
		if (!b) {
			mouseHideTime = 0;
			mouseHidden = false;
			setCursor(null);
		} else {
			setCursor(emptyCursor);
			mouseHidden = true;
		}
	}

	@Override
	public String getVersionString() {
		return VERSION;
	}

	private void render(Graphics g) {
		surface.fill(0, 0, surface.getWidth(), surface.getHeight(), AbsColor.BLACK);

		if (level != null) {
			level.render(surface, offset.getX(), offset.getY());
		}

		menuStack.render(surface);

		//g.fillRect(0, 0, getWidth(), getHeight());
		g.translate((getWidth() - getTotalScreenWidth()) / 2, (getHeight() - getTotalScreenHeight()) / 2);
		g.clipRect(0, 0, getTotalScreenWidth(), getTotalScreenHeight());

		g.drawImage(surface.getImage(), 0, 0, getTotalScreenWidth(), getTotalScreenHeight(), null);
	}

	@Override
	public void renderOnCustom(Graphics g) {
		render(g);
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
		font.setFont(letters, img);
	}

	@Override
	public void drawString(String txt, int x, int y) {
		font.draw(surface, txt, x, y);
	}

	@Override
	public void drawStringAlpha(String txt, int x, int y, int a) {
		font.drawAlpha(surface, txt, x, y, a);
	}

	@Override
	public void drawString(Bitmap s, String txt, int x, int y) {
		font.draw(s, txt, x, y);
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
		return screen_height;
	}

	@Override
	public int getScreenWidth() {
		return screen_width;
	}

	@Override
	public double getScreenScale() {
		return screen_scale;
	}

	@Override
	public void setScreenScale(double s) {
		if (s != screen_scale) {
			screen_scale = s;

			setPreferredSize(new Dimension(getTotalScreenWidth(), getTotalScreenHeight()));
			setMinimumSize(new Dimension(getTotalScreenWidth(), getTotalScreenHeight()));
			setMaximumSize(new Dimension(getTotalScreenWidth(), getTotalScreenHeight()));

			owner.pack();
		}
	}

	@Override
	public int getTotalScreenHeight() {
		return (int) (screen_height * screen_scale);
	}

	@Override
	public int getTotalScreenWidth() {
		return (int) (screen_width * screen_scale);
	}

	@Override
	public JFrame getOwner() {
		return owner;
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
		level = l;
	}

	@Override
	public MDPoint screenToCanvas(MPoint p) {
		double scale = getScreenScale();
		MDPoint mp = p.asMDPoint();
		mp.div(scale);
		return mp;
	}

	@Override
	public MPoint canvasToScreen(MDPoint mp) {
		double scale = getScreenScale();
		MPoint p = mp.roundToMPoint();
		p.mult(scale);
		return p;
	}
	
	@Override
	public GUIStack getMenuStack() {
		return menuStack;
	}
	
	@Override
	public Font getACFont() {
		return font;
	}
}
