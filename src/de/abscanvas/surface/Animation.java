package de.abscanvas.surface;

import de.abscanvas.Screen;
import de.abscanvas.entity.Facing;
import de.abscanvas.entity.FacingListener;

public class Animation {
	private Screen screen;

	private Bitmap[][] anma;

	private int anma_ticks = 0; // Time in Milliseconds for all frames

	private FacingListener changeListener;

	private long tick = 0;
	private boolean animated = false;
	private boolean loop = true;
	private int frame = 0;
	private int layer = 0;

	private boolean originCenter = true;

	public Animation(Screen s) {
		screen = s;
		anma = new Bitmap[1][1];
		anma[0][0] = new Bitmap(0, 0);
	}

	public Animation() {
		screen = null;
		anma = new Bitmap[1][1];
		anma[0][0] = new Bitmap(0, 0);
	}

	public double MPF() {
		if (screen == null) {
			return 16.66666666d; // Not nice but fast and safe :'-(
		} else {
			return screen.MPF();
		}
	}
	
	public void setAnimation(Bitmap[] a) {
		Bitmap[][] b = new Bitmap[a.length][1];
		for (int i = 0; i < a.length; i++) {
			b[i][0] = a[i];
		}
		setAnimation(b, 0);
	}

	public void setAnimation(Bitmap[] a, int speed) {
		Bitmap[][] b = new Bitmap[1][a.length];
		b[0] = a;
		setAnimation(b, speed);
	}

	public void setAnimation(Bitmap a) {
		Bitmap[] b = new Bitmap[1];
		b[0] = a;
		setAnimation(b, 0);
	}

	public void setAnimation(Bitmap[][] a, int speed, boolean updateFrame) {
		animated = a[0].length > 1;

		if (updateFrame) {
			frame = 0;
			layer = 0;
			tick = 0;
		}

		anma_ticks = (int) Math.round(speed / MPF());
		if (anma_ticks <= 0) {
			animated = false;
			anma_ticks = 99999;
		}
		anma = a;

		if (changeListener != null) {
			changeListener.onAnimationChanged();
		}
	}

	public void setAnimation(Bitmap[][] a, int speed) {
		setAnimation(a, speed, true);
	}

	public boolean setLayer(Facing f) {
		return setLayer(f.get());
	}

	public boolean setLayer(int l) {
		if (l == layer) {
			return true; // Do nothing
		}

		if (l < 0 || l >= getLayerCount()) {
			return false;
		}
		layer = l;

		if (changeListener != null) {
			changeListener.onLayerChanged(layer);
		}

		return true;
	}

	public int getLayer() {
		return layer;
	}

	public void setOriginToCenter() {
		originCenter = true;
	}

	public void setOriginToUpperLeft() {
		originCenter = false;
	}

	public void tick() {
		if (animated) {
			tick++;

			if ((tick >= anma_ticks) && !loop) {
				frame = getFrameCount() - 1;
			} else {
				long dlay = (tick % anma_ticks);
				double framedelay = (anma_ticks / (getFrameCount() * 1f));
				frame = (int) (dlay / framedelay);
			}
			if (frame < 0) {
				frame = 0;
			}
			if (frame > (getFrameCount() - 1)) {
				frame = getFrameCount() - 1;
			}
		}

	}

	public int getFrameCount() {
		int i = anma[layer].length - 1;
		while (anma[layer][i] == null) {
			i--;
		}
		return i + 1;
	}

	public int getLayerCount() {
		return anma.length;
	}

	public Bitmap getSprite() {
		return anma[layer][frame];
	}

	public void render(Surface s, double x, double y) {
		renderAlpha(s, x, y, 255);
	}

	public void animate(boolean b) {
		if (! (animated ^ b)) {
			return; // Do nothing
		}

		animated = b;
		if (changeListener != null) {
			changeListener.onAnimatedChanged(animated);
		}
	}

	public void loop(boolean l) {
		loop = l;
	}

	public void setScreen(Screen s) {
		screen = s;
	}

	public void renderAlpha(Surface s, double x, double y, int a) {
		if (originCenter) {
			s.alphaBlit(getSprite(), x - getSprite().getWidth() / 2, y - getSprite().getHeight() / 2, a);
		} else {
			s.alphaBlit(getSprite(), x, y, a);
		}
	}

	public void setFacingListener(FacingListener listener) {
		changeListener = listener;
	}

	public boolean isAnimated() {
		return animated;
	}

	public Bitmap[][] getAllSprite() {
		return anma;
	}
}
