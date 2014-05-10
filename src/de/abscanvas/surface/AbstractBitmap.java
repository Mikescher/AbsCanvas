package de.abscanvas.surface;

import de.abscanvas.math.MPoint;
import de.abscanvas.math.MRect;

public interface AbstractBitmap {
	public int getWidth();

	public int getHeight();

	public AbstractBitmap copy();

	public int blendPixels(int backgroundColor, int pixelToBlendColor);

	public void blit(AbstractBitmap bitmap, int x, int y);
	public void blit(AbstractBitmap bitmap, MPoint p);

	public void blit(AbstractBitmap bitmap, int x, int y, int width, int height);
	public void blit(AbstractBitmap bitmap, MRect r);

	public void alphaBlit(AbstractBitmap bitmap, int x, int y, int alpha);
	public void alphaBlit(AbstractBitmap bitmap, MPoint p, int alpha);

	public void colorBlit(AbstractBitmap bitmap, int x, int y, int color);
	public void colorBlit(AbstractBitmap bitmap, MPoint p, int color);

	public void alphaFill(int x, int y, int width, int height, int color, int alpha);
	public void alphaFill(MRect r, int color, int alpha);

	public void fill(int x, int y, int width, int height, int color);
	public void fill(MRect r, int color);
	public void fill(int color);
	
	public void clear(int x, int y, int width, int height);
	public void clear(MRect r);
	public void clear();

	public void rectangle(int x, int y, int bw, int bh, int color);
	public void rectangle(MRect r, int color);
	
	public void setPixel(int x, int y, int color);
	public void setPixel(MPoint p, int color);

	public AbstractBitmap shrink();

	public AbstractBitmap scaleBitmap(int width, int height);
	
	public MRect getRect();
	
	public int getPixel(int pos);
	public int[] getPixels();
	public int getPixelSize();
	
	public void setPixel(int pos, int color);
}
