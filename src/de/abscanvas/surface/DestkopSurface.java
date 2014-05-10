package de.abscanvas.surface;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class DestkopSurface extends Bitmap implements Surface {
	private BufferedImage image;
	private int offsetX = 0;
	private int offsetY = 0;

	public DestkopSurface(int w, int h) {
		super(w, h);
		image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		// pointer to pixels of buffered image
		setPixels(((DataBufferInt) image.getRaster().getDataBuffer()).getData());
	}

	@Override
	public void setOffset(double xOffset, double yOffset) {
		offsetX = (int) Math.round(xOffset);
		offsetY = (int) Math.round(yOffset);
	}

	@Override
	public void alphaBlit(AbstractBitmap bitmap, int x, int y, int alpha) {
		super.alphaBlit(bitmap, x, y, alpha);
	}

	@Override
	public void alphaBlit(AbstractBitmap bitmap, double x, double y, int alpha) {
		super.alphaBlit(bitmap, (int) x, (int) y, alpha);
	}

	@Override
	public void blit(AbstractBitmap bitmap, double x, double y) {
		super.blit(bitmap, (int) x + offsetX, (int) y + offsetY);
	}

	@Override
	public void blit(AbstractBitmap bitmap, int x, int y) {
		super.blit(bitmap, x + offsetX, y + offsetY);
	}

	@Override
	public void blit(AbstractBitmap bitmap, int x, int y, int w, int h) {
		super.blit(bitmap, x + offsetX, y + offsetY, w, h);
	}

	@Override
	public void blit(AbstractBitmap bitmap, double x, double y, int w, int h) {
		super.blit(bitmap, (int) (x + offsetX), (int) (y + offsetY), w, h);
	}

	@Override
	public void colorBlit(AbstractBitmap bitmap, double x, double y, int color) {
		super.colorBlit(bitmap, (int) x, (int) y, color);
	}

	@Override
	public void colorBlit(AbstractBitmap bitmap, int x, int y, int color) {
		super.colorBlit(bitmap, x + offsetX, y + offsetY, color);
	}

	@Override
	public void fill(int x, int y, int width, int height, int color) {
		super.fill(x + offsetX, y + offsetY, width, height, color);
	}

	@Override
	public BufferedImage getImage() {
		return image;
	}
}
