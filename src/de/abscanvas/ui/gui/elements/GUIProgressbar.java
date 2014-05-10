package de.abscanvas.ui.gui.elements;

import de.abscanvas.surface.AbsColor;
import de.abscanvas.surface.Bitmap;
import de.abscanvas.ui.gui.GUIElement;
import de.abscanvas.ui.gui.GUIMenu;

public class GUIProgressbar extends GUIElement {
	private int colBackground = AbsColor.WHITE;
	private int colProgress = AbsColor.BLUE;
	private int colProgressAnimation = AbsColor.CYAN;
	private int colLines = AbsColor.BLACK;

	private int position = 0;
	private int maximum = 100;

	private int animPos = 200;
	
	private int animSpeed = 1;

	private boolean isAnim = true;

	public GUIProgressbar(int x, int y, int width, int height, GUIMenu owner) {
		super(x, y, width, height, new Bitmap(0, 0), owner);
		repaint();
	}

	@Override
	public void tick() {
		super.tick();

		if (isAnim()) {
			generateImageAnimation();
		}
	}

	private void generateImageAnimation() {
		int hw = getWidth() / 2;

		animPos+= animSpeed;
		if (animPos > (getWidth() * 2)) {
			animPos -= getWidth();
		}

		Bitmap b = new Bitmap(getWidth(), getHeight());
		b.fill(colProgress);

		generateImageAnimation(animPos, b);
		generateImageAnimation(animPos - 1 * hw, b);
		generateImageAnimation(animPos - 2 * hw, b);
		generateImageAnimation(animPos - 3 * hw, b);
		generateImageAnimation(animPos - 4 * hw, b);
		generateImageAnimation(animPos - 5 * hw, b);
		generateImageAnimation(animPos - 6 * hw, b);

		int progressWidth = (int) (((position * 1d) / maximum) * (getWidth() - 2));
		b.fill(progressWidth + 1, 1, getWidth() - 2 - progressWidth, getHeight() - 2, colBackground);
		b.rectangle(0, 0, getWidth(), getHeight(), colLines);

		setImage(b);
	}

	@Override
	protected void repaint() {
		if (isAnim()) {
			return;
		}

		Bitmap b = new Bitmap(getWidth(), getHeight());
		b.fill(colBackground);
		b.rectangle(0, 0, getWidth(), getHeight(), colLines);

		int progressWidth = (int) (((position * 1d) / maximum) * (getWidth() - 2));

		b.fill(1, 1, progressWidth, getHeight() - 2, colProgress);

		setImage(b);
	}

	private void generateImageAnimation(int apos, Bitmap b) {
		int boxlength = 2;
		int bl = boxlength;

		int ha = getHeight() - 2;
		ha = ha / bl;
		if ((ha % 2) == 0) {
			ha -= 1;
		}

		int os = (getHeight() - ha * bl) / 2;

		int mdl = getHeight() / 2;
		int ap = apos;
		for (int i = 0; i < (ha + 1) / 2; i++) {
			b.fill(ap - bl, mdl - bl / 2, bl, bl, colProgressAnimation);

			for (int j = 0; j < i; j++) {
				int c = j + 1;
				b.fill(ap - bl, mdl - bl / 2 - c * bl, bl, bl, colProgressAnimation);
				b.fill(ap - bl, mdl + bl / 2 + j * bl, bl, bl, colProgressAnimation);
			}

			ap -= bl;
		}

		double wsk = 0.9;

		for (int i = 0; i < ((ha + 1) / 2 - 1); i++) {
			int c = ((ha + 1) / 2 - 1) - i;
			b.fill(ap - bl, os, bl, c * bl, colProgressAnimation);
			b.fill(ap - bl, getHeight() - os - c * bl, bl, c * bl, colProgressAnimation);

			for (int j = 0; j < i; j++) {
				int v = j + 1;

				if (getRandomizationPixel(wsk)) {
					b.fill(ap - bl, mdl - bl / 2 - v * bl, bl, bl, b.fadeColor(colProgress, colProgressAnimation, 2 * wsk - wsk * wsk));
				}
				if (getRandomizationPixel(wsk)) {
					b.fill(ap - bl, mdl + bl / 2 + j * bl, bl, bl, b.fadeColor(colProgress, colProgressAnimation, 2 * wsk - wsk * wsk));
				}
			}
			if (getRandomizationPixel(wsk)) {
				b.fill(ap - bl, mdl - bl / 2, bl, bl, b.fadeColor(colProgress, colProgressAnimation, 2 * wsk - wsk * wsk));
			}

			wsk -= 1d / ((ha - 1) * 4);

			ap -= bl;
		}

		for (int i = 0; i < ((ha + 1) / 2 - 1) * 7; i++) {
			for (int j = 0; j < ha; j++) {
				if (getRandomizationPixel(wsk)) {
					b.fill(ap - bl, os + j * bl, bl, bl, b.fadeColor(colProgress, colProgressAnimation, 2 * wsk - wsk * wsk));
				}
			}

			wsk -= 1d / ((ha - 1) * 4);

			ap -= bl;
		}

	}

	private boolean getRandomizationPixel(double www) {
		return Math.random() < www;
	}

	public int getPosition() {
		return position;
	}

	public int getMaximum() {
		return maximum;
	}

	public void stepBy(int k) {
		setPosition(getPosition() + k);
	}

	public void stepIt() {
		stepBy(1);
	}

	public void setPosition(int p) {
		position = p;
		if (position > getMaximum()) {
			position = getMaximum();
		}

		repaint();
	}

	public void setMaximum(int m) {
		maximum = m;
		if (position > getMaximum()) {
			position = getMaximum();
		}

		repaint();
	}

	public void setAnimation(boolean isAnim) {
		this.isAnim = isAnim;
		if (isAnim()) {
			generateImageAnimation();
		} else {
			repaint();
		}
	}
	
	public void setColorSheme(int cBackground, int cAnimation, int cProgress, int cLines) {
		colBackground = cBackground;
		colLines = cLines;
		colProgress = cProgress;
		colProgressAnimation = cAnimation;
		
		refresh();
	}

	public boolean isAnim() {
		return isAnim;
	}

	public void setAnimationSpeed(int animSpeed) {
		this.animSpeed = animSpeed;
	}
}
