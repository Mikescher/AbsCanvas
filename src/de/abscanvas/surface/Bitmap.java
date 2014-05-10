package de.abscanvas.surface;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

import de.abscanvas.math.MPoint;
import de.abscanvas.math.MRect;

public class Bitmap implements AbstractBitmap {
	private int[] pixels;
	private int w;
	private int h;

	public Bitmap(int w, int h) {
		this.w = w;
		this.h = h;
		pixels = new int[w * h];
		clear();
	}

	public Bitmap(int w, int h, int[] pixels) {
		this.w = w;
		this.h = h;
		this.pixels = pixels;
	}
	
	public void setPixels(int[] px) {
		pixels = px;
	}

	public Bitmap(int[][] pixels2D) {
		w = pixels2D.length;
		if (w > 0) {
			h = pixels2D[0].length;
			pixels = new int[w * h];
			for (int y = 0; y < h; y++) {
				for (int x = 0; x < w; x++) {
					pixels[y * w + x] = pixels2D[x][y];
				}
			}
		} else {
			h = 0;
			pixels = new int[0];
		}
	}

	public void replaceColor(int col, int replace) {
		for (int i = 0; i < w * h; i++) {
			if (pixels[i] == col) {
				pixels[i] = replace;
			}
		}
	}

	public void setImage(BufferedImage i) {
		pixels = ((DataBufferInt) i.getRaster().getDataBuffer()).getData();
	}

	public BufferedImage getAsBufferedImage() {
		BufferedImage result = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);

		for (int x = 0; x < getWidth(); x++) {
			for (int y = 0; y < getHeight(); y++) {
				result.setRGB(x, y, getPixel(x, y));
			}
		}

		return result;
	}

	@Override
	public Bitmap copy() {
		Bitmap rValue = new Bitmap(this.w, this.h);
		rValue.pixels = this.pixels.clone();
		return rValue;
	}

	@Override
	public void fill(int color) {
		Arrays.fill(pixels, color);
	}
	
	@Override
	public void clear(int x, int y, int width, int height) {
		clear(new MRect(x, y, x + width, y + height));
	}

	@Override
	public void clear(MRect r) {
		r.adjustWithMaxRect(getRect());
		
		for (int xx = r.getLeft(); xx < r.getRight(); xx++) {
			for (int yy = r.getTop(); yy < r.getBottom(); yy++) {
				setPixel(xx, yy, AbsColor.TRANSPARENT);
			}
		}
	}

	@Override
	public void clear() {
		fill(AbsColor.TRANSPARENT);
	}

	@Override
	public int blendPixels(int backgroundColor, int pixelToBlendColor) {
		int alpha_blend = (pixelToBlendColor >> 24) & 0xff;

		int alpha_background = 256 - alpha_blend;

		int rr = backgroundColor & 0xff0000;
		int gg = backgroundColor & 0xff00;
		int bb = backgroundColor & 0xff;

		int r = (pixelToBlendColor & 0xff0000);
		int g = (pixelToBlendColor & 0xff00);
		int b = (pixelToBlendColor & 0xff);

		r = ((r * alpha_blend + rr * alpha_background) >> 8) & 0xff0000;
		g = ((g * alpha_blend + gg * alpha_background) >> 8) & 0xff00;
		b = ((b * alpha_blend + bb * alpha_background) >> 8) & 0xff;

		return 0xff000000 | r | g | b;
	}
	
	public int fadeColor(int fadefrom, int fadeto, double perecntage) {
		int r0 = fadefrom & 0xff0000;
		int g0 = fadefrom & 0xff00;
		int b0 = fadefrom & 0xff;
		
		int r1 = fadeto & 0xff0000;
		int g1 = fadeto & 0xff00;
		int b1 = fadeto & 0xff;
		
		int r2 = (int) (r0 + (r1 - r0)*perecntage);
		int g2 = (int) (g0 + (g1 - g0)*perecntage);
		int b2 = (int) (b0 + (b1 - b0)*perecntage);
		
		return 0xff000000 | r2 | g2 | b2;
	}

	@Override
	public void blit(AbstractBitmap bitmap, int x, int y) {
		Bitmap mb = (Bitmap) bitmap;

		if (x == 394 && y == 107) {
			printToConsole();
		}
		
		MRect blitArea = new MRect(x, y, x + mb.getWidth(), y + mb.getHeight());
		blitArea.adjustWithMaxRect(getRect());
		
		int blitWidth = blitArea.getWidth();

		for (int yy = blitArea.getTop(); yy < blitArea.getBottom(); yy++) {
			int tp = yy * w + blitArea.getLeft();
			int sp = (yy - y) * mb.w + (blitArea.getLeft() - x);
			tp -= sp;
			for (int xx = sp; xx < sp + blitWidth; xx++) {
				int col = mb.pixels[xx];
				int alpha = (col >> 24) & 0xff;

				if (alpha == 255) {
					pixels[tp + xx] = col;
				} else if (alpha == 0) {
					// pixels[tp + xx] = pixels[tp + xx]
				} else {
					pixels[tp + xx] = blendPixels(pixels[tp + xx], col);
				}
			}
		}
	}
	
	@Override
	public void blit(AbstractBitmap bitmap, MPoint p) {
		blit(bitmap, p.getX(), p.getY());
	}

	@Override
	public void blit(AbstractBitmap bitmap, int x, int y, int width, int height) {
		Bitmap mb = (Bitmap) bitmap;
		
		MRect blitArea = new MRect(x, y, x + width, y + height);
		blitArea.adjustWithMaxRect(getRect());
		
		int blitWidth = blitArea.getWidth();

		for (int yy = blitArea.getTop(); yy < blitArea.getBottom(); yy++) {
			int tp = yy * w + blitArea.getLeft();
			int sp = (yy - y) * mb.w + (blitArea.getLeft() - x);
			tp -= sp;
			for (int xx = sp; xx < sp + blitWidth; xx++) {
				int col = mb.pixels[xx];
				int alpha = (col >> 24) & 0xff;

				if (alpha == 255) {
					pixels[tp + xx] = col;
				} else {
					pixels[tp + xx] = blendPixels(pixels[tp + xx], col);
				}
			}
		}
	}
	
	@Override
	public void blit(AbstractBitmap bitmap, MRect r) {
		blit(bitmap, r.getX(), r.getY(), r.getWidth(), r.getHeight());
	}

	@Override
	public void alphaBlit(AbstractBitmap bitmap, int x, int y, int alpha) {
		Bitmap mb = (Bitmap) bitmap;
		if (alpha >= 255 || alpha < 0) {
			blit(bitmap, x, y);
			return;
		} else if (alpha == 0) {
			return;
		}

		MRect blitArea = new MRect(x, y, x + mb.getWidth(), y + mb.getHeight());
		blitArea.adjustWithMaxRect(getRect());

		int blitWidth = blitArea.getWidth();

		for (int yy = blitArea.getTop(); yy < blitArea.getBottom(); yy++) {
			int tp = yy * w + blitArea.getLeft();
			int sp = (yy - y) * mb.w + (blitArea.getLeft() - x);
			for (int xx = 0; xx < blitWidth; xx++) {
				int col = mb.pixels[sp + xx];
				if (col < 0) {

					int r = (col & 0xff0000);
					int g = (col & 0xff00);
					int b = (col & 0xff);
					col = (alpha << 24) | r | g | b;
					int color = pixels[tp + xx];
					pixels[tp + xx] = this.blendPixels(color, col);
				}
			}
		}
	}
	
	@Override
	public void alphaBlit(AbstractBitmap bitmap, MPoint p, int alpha) {
		alphaBlit(bitmap, p.getX(), p.getY(), alpha);
	}

	@Override
	public void colorBlit(AbstractBitmap bitmap, int x, int y, int color) {
		Bitmap mb = (Bitmap) bitmap;
		MRect blitArea = new MRect(x, y, x + mb.getWidth(), y + mb.getHeight());
		blitArea.adjustWithMaxRect(getRect());

		int blitWidth = blitArea.getWidth();

		int a2 = (color >> 24) & 0xff;
		int a1 = 256 - a2;

		int rr = color & 0xff0000;
		int gg = color & 0xff00;
		int bb = color & 0xff;

		for (int yy = blitArea.getTop(); yy < blitArea.getBottom(); yy++) {
			int tp = yy * w + blitArea.getLeft();
			int sp = (yy - y) * mb.w + (blitArea.getLeft() - x);
			for (int xx = 0; xx < blitWidth; xx++) {
				int col = ((Bitmap) bitmap).pixels[sp + xx];
				if (col < 0) {
					int r = (col & 0xff0000);
					int g = (col & 0xff00);
					int b = (col & 0xff);

					r = ((r * a1 + rr * a2) >> 8) & 0xff0000;
					g = ((g * a1 + gg * a2) >> 8) & 0xff00;
					b = ((b * a1 + bb * a2) >> 8) & 0xff;
					pixels[tp + xx] = 0xff000000 | r | g | b;
				}
			}
		}
	}
	
	@Override
	public void colorBlit(AbstractBitmap bitmap, MPoint p, int color) {
		colorBlit(bitmap, p.getX(),  p.getY(), color);
	}

	@Override
	public void alphaFill(int x, int y, int width, int height, int color, int alpha) {
		if (alpha == 255) {
			this.fill(x, y, width, height, color);
			return;
		}

		Bitmap bmp = new Bitmap(width, height);
		bmp.fill(0, 0, width, height, color);

		this.alphaBlit(bmp, x, y, alpha);
	}
	
	@Override
	public void alphaFill(MRect r, int color, int alpha) {
		alphaFill(r.getX(), r.getY(), r.getWidth(), r.getHeight(), color, alpha);
	}

	@Override
	public void fill(int x, int y, int width, int height, int color) {
		MRect blitArea = new MRect(x, y, x + width, y + height);
		blitArea.adjustWithMaxRect(getRect());

		int blitWidth = blitArea.getWidth();

		for (int yy = blitArea.getTop(); yy < blitArea.getBottom(); yy++) {
			int tp = yy * w + blitArea.getLeft();
			for (int xx = 0; xx < blitWidth; xx++) {
				pixels[tp + xx] = color;
			}
		}
	}
	
	@Override
	public void fill(MRect r, int color) {
		fill(r.getX(), r.getY(), r.getWidth(), r.getHeight(), color);
	}
	
	@Override
	public MRect getRect() {
		return new MRect(0, 0, w, h);
	}

	@Override
	public void rectangle(int x, int y, int bw, int bh, int color) {
		int x0 = x;
		int x1 = x + bw;
		int y0 = y;
		int y1 = y + bh;
		if (x0 < 0) {
			x0 = 0;
		}
		if (y0 < 0) {
			y0 = 0;
		}
		if (x1 > w) {
			x1 = w;
		}
		if (y1 > h) {
			y1 = h;
		}

		if ((x1 <= x0) || (y1 <= y0)) {
			return;
		}

		for (int yy = y0; yy < y1; yy++) {
			setPixel(x0, yy, color);
			setPixel(x1 - 1, yy, color);
		}

		for (int xx = x0; xx < x1; xx++) {
			setPixel(xx, y0, color);
			setPixel(xx, y1 - 1, color);
		}
	}
	
	@Override
	public void rectangle(MRect r, int color) {
		rectangle(r.getX(), r.getY(), r.getWidth(), r.getHeight(), color);
	}
	
	public void lineHorz(int x, int y, int bw, int color) {
		if (y < 0 || y >= h) {
			return;
		}

		int x0 = x;
		int x1 = x + bw;
		if (x0 < 0) {
			x0 = 0;
		}
		if (x1 > w) {
			x1 = w;
		}

		for (int xx = x0; xx < x1; xx++) {
			setPixel(xx, y, color);
		}
	}
	
	public void lineHorz(MPoint p, int bw, int color) {
		lineHorz(p.getX(), p.getY(), bw, color);
	}

	public void lineVert(int x, int y, int bh, int color) {
		if (x < 0 || x >= w) {
			return;
		}

		int y0 = y;
		int y1 = y + bh;
		if (y0 < 0) {
			y0 = 0;
		}
		if (y1 > h) {
			y1 = h;
		}

		for (int yy = y0; yy < y1; yy++) {
			setPixel(x, yy, color);
		}
	}
	
	public void lineVert(MPoint p, int bh, int color) {
		lineVert(p.getX(), p.getY(), bh, color);
	}

	public void saveToFile(String s) {
		try {
			File outputfile = new File(s);
			ImageIO.write(getAsBufferedImage(), "png", outputfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setPixelTest(int x, int y, int color) {
		if (x >= 0 && y >= 0 && x < w && y < h) {
			pixels[x + y * w] = color;
		}
	}

	@Override
	public void setPixel(int x, int y, int color) {
		pixels[x + y * w] = color;
	}
	
	@Override
	public void setPixel(MPoint p, int color) {
		setPixel(p.getX(), p.getY(), color);
	}

	public void circle(int centerX, int centerY, int radius, int color) {
		int d = 3 - (2 * radius);
		int x = 0;
		int y = radius;

		do {
			setPixelTest(centerX + x, centerY + y, color);
			setPixelTest(centerX + x, centerY - y, color);
			setPixelTest(centerX - x, centerY + y, color);
			setPixelTest(centerX - x, centerY - y, color);
			setPixelTest(centerX + y, centerY + x, color);
			setPixelTest(centerX + y, centerY - x, color);
			setPixelTest(centerX - y, centerY + x, color);
			setPixelTest(centerX - y, centerY - x, color);

			if (d < 0) {
				d = d + (4 * x) + 6;
			} else {
				d = d + 4 * (x - y) + 10;
				y--;
			}
			x++;
		} while (x <= y);
	}

	public void circleFill(int centerX, int centerY, int radius, int color) {
		int d = 3 - (2 * radius);
		int x = 0;
		int y = radius;

		do {
			horizonalLine(centerX + x, centerX - x, centerY + y, color);
			horizonalLine(centerX + x, centerX - x, centerY - y, color);
			horizonalLine(centerX + y, centerX - y, centerY + x, color);
			horizonalLine(centerX + y, centerX - y, centerY - x, color);

			if (d < 0) {
				d = d + (4 * x) + 6;
			} else {
				d = d + 4 * (x - y) + 10;
				y--;
			}
			x++;
		} while (x <= y);
	}

	private void horizonalLine(int x1, int x2, int y, int color) {
		if (x1 > x2) {
			int xx = x1;
			x1 = x2;
			x2 = xx;
		}

		lineHorz(x1, y, x2 - x1, color);
	}

	@Override
	public AbstractBitmap shrink() {
		Bitmap newbmp = new Bitmap(w / 2, h / 2);
		int[] pix = pixels;
		int blarg = 0;
		for (int i = 0; i < pix.length; i++) {
			if (blarg >= newbmp.pixels.length) {
				break;
			}
			if (i % 2 == 0) {
				newbmp.pixels[blarg] = pix[i];
				blarg++;
			}
			if (i % w == 0) {
				i += w;
			}
		}

		return newbmp;
	}

	@Override
	public AbstractBitmap scaleBitmap(int width, int height) { // TODO statt 16 hier Tile.WIDTH / 2 (oder .HEIGHT) -> ???
		Bitmap scaledBitmap = new Bitmap(width, height);

		int scaleRatioWidth = ((w << 16) / width);
		int scaleRatioHeight = ((h << 16) / height);

		int i = 0;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				scaledBitmap.pixels[i++] = pixels[(w * ((y * scaleRatioHeight) >> 16)) + ((x * scaleRatioWidth) >> 16)];
			}
		}

		return scaledBitmap;
	}

	public Bitmap scaleBitmapFloat(int width, int height) {
		BufferedImage bi = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
		BufferedImage bi2 = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Bitmap result = new Bitmap(width, height);

		int[] p = ((DataBufferInt) bi.getRaster().getDataBuffer()).getData();
		for (int i = 0; i < p.length; i++) {
			p[i] = pixels[i];
		}

		bi2.getGraphics().drawImage(bi, 0, 0, width, height, null);

		result.setImage(bi2);

		return result;
	}

	@Override
	public int getWidth() {
		return w;
	}

	@Override
	public int getHeight() {
		return h;
	}

	@Override
	public int getPixel(int pos) {
		return pixels[pos];
	}

	@Override
	public void setPixel(int pos, int color) {
		if (pos >= 0 && pos < pixels.length)
			pixels[pos] = color;
	}

	@Override
	public int getPixelSize() {
		return pixels.length;
	}

	@Override
	public int[] getPixels() {
		return pixels;
	}

	public void printToConsole() {
		for (int i = 0; i < getPixelSize(); i++) {
			int alpha = (getPixel(i) >> 24) & 0xff;

			if (i % getWidth() == 0) {
				System.out.println();
			}

			if (alpha == 255) {
				System.out.print("#");
			} else if (alpha == 0) {
				System.out.print("-");
			} else {
				System.out.print("+");
			}
		}

		System.out.println("");
		System.out.println("");
		System.out.println("");
		System.out.println("");
		System.out.println("");
	}

	public Bitmap rotate(double rad) {
		int width = getWidth();
		int height = getHeight();

		rad = Math.PI * 2 - rad;

		double sin = Math.sin(rad);
		double cos = Math.cos(rad);
		double x0 = 0.5 * (width - 1); // point to rotate about
		double y0 = 0.5 * (height - 1); // center of image

		Bitmap pic2 = new Bitmap(width, height);

		// rotation
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				double a = x - x0;
				double b = y - y0;
				int xx = (int) Math.round(+a * cos - b * sin + x0);
				int yy = (int) Math.round(+a * sin + b * cos + y0);

				// plot pixel (x, y) the same color as (xx, yy) if it's in bounds
				if (xx >= 0 && xx < width && yy >= 0 && yy < height) {
					pic2.setPixel(x, y, getPixel(xx, yy));
				}
			}
		}

		return pic2;
	}

	public int getSign(int i) {
		if (i < 0) {
			return -1;
		}
		return 1;
	}

	public void rotate90() {
		int width = getWidth();
		int height = getHeight();

		int w2 = width / 2;
		int h2 = height / 2;

		Bitmap pic2 = copy();

		fill(0, 0, width, height, 0);

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int xx = (y - h2) + w2;
				int yy = -(x - w2) + h2;

				// plot pixel (x, y) the same color as (xx, yy) if it's in bounds
				if (xx >= 0 && xx < width && yy >= 0 && yy < height) {
					setPixel(x, y, pic2.getPixel(xx, yy));
				}
			}
		}
	}

	public void rotate180() {
		int width = getWidth();
		int height = getHeight();

		int w2 = width / 2;
		int h2 = height / 2;

		Bitmap pic2 = copy();

		fill(0, 0, width, height, 0);

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int yy = -(y - h2) + h2;
				int xx = -(x - w2) + w2;

				// plot pixel (x, y) the same color as (xx, yy) if it's in bounds
				if (xx >= 0 && xx < width && yy >= 0 && yy < height) {
					setPixel(x, y, pic2.getPixel(xx, yy));
				}
			}
		}
	}

	public void rotate90(int mult) {
		if (mult == 0 || mult == 4) {
			return;
		} else if (mult == 2) {
			rotate180();
		} else if (mult > 0 && mult < 4) {
			for (int i = 0; i < mult; i++) {
				rotate90();
			}
		} else if (mult < 4) {
			rotate90(mult + 4);
		} else if (mult > 0) {
			rotate90(mult + 4);
		}
	}

	public int getPixel(int x, int y) {
		return getPixel(y * getWidth() + x);
	}

	public void drawString(String txt, java.awt.Font f, int c, int x, int y) {
		if (txt.isEmpty()) {
			return;
		}

		Color cc = new Color(c);

		BufferedImage bi = new BufferedImage(f.getSize() * txt.length(), f.getSize() * 2, BufferedImage.TYPE_INT_ARGB);
		Graphics g = bi.getGraphics();

		g.setFont(f);
		g.setColor(cc);

		g.drawString(txt, 0, f.getSize());

		Bitmap b = new Bitmap(bi.getWidth(), bi.getHeight());
		b.setImage(bi);

		blit(b, x, y);
	}
	
	public void drawString(String txt, java.awt.Font f, int c, MPoint p) {
		drawString(txt, f, c, p.getX(), p.getY());
	}
	
	public void drawStringCentered(String txt, java.awt.Font f, MRect rect, int c) {
		MPoint p = rect.getCenter();
		p.sub(getStringWidth(f, txt)/2, f.getSize()/2);
		drawString(txt, f, c, p.getX(), p.getY());
	}
	
	public static int getStringWidth(java.awt.Font f, String s) {
		return ((new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB)).getGraphics().getFontMetrics(f)).stringWidth(s);
	}
	
	public static int getStringWidth(Graphics g, java.awt.Font f, String s) {
		return g.getFontMetrics(f).stringWidth(s);
	}
}
