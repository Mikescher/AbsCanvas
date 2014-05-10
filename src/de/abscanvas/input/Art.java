package de.abscanvas.input;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import de.abscanvas.Screen;
import de.abscanvas.surface.Bitmap;

public abstract class Art {
	protected static Bitmap[][] cut(String path, int w, int h) {
		return cut(path, w, h, 0, 0);
	}

	protected static Bitmap[][] cut(String path, int w, int h, int bx, int by) {
		try {
			BufferedImage bi = loadImage(path);

			w = bi.getWidth() / w;
			h = bi.getHeight() / h;

			int xTiles = (bi.getWidth() - bx) / w;
			int yTiles = (bi.getHeight() - by) / h;

			Bitmap[][] result = new Bitmap[xTiles][yTiles];

			for (int x = 0; x < xTiles; x++) {
				for (int y = 0; y < yTiles; y++) {
					result[x][y] = new Bitmap(w, h);
					bi.getRGB(bx + x * w, by + y * h, w, h, result[x][y].getPixels(), 0, w);
				}
			}

			return result;
		} catch (IOException e) {
			System.out.println("Could not load File from Ressources: \"" + path + "\" (absCanvas.Art)");
			e.printStackTrace();
		}
		return null;
	}

	protected static Bitmap[][] cutReverse(String path, int w, int h) {
		return cutReverse(path, w, h, 0, 0);
	}

	protected static Bitmap[][] cutReverse(String path, int w, int h, int bx, int by) {
		try {
			BufferedImage bi = loadImage(path);

			w = bi.getWidth() / w;
			h = bi.getHeight() / h;

			int xTiles = (bi.getWidth() - bx) / w;
			int yTiles = (bi.getHeight() - by) / h;

			Bitmap[][] result = new Bitmap[yTiles][xTiles];

			for (int x = 0; x < xTiles; x++) {
				for (int y = 0; y < yTiles; y++) {
					result[y][x] = new Bitmap(w, h);
					bi.getRGB(bx + x * w, by + y * h, w, h, result[y][x].getPixels(), 0, w);
				}
			}

			return result;
		} catch (IOException e) {
			System.out.println("Could not load File from Ressources: \"" + path + "\" (absCanvas.Art)");
			e.printStackTrace();
		}
		return null;
	}

	protected static Bitmap[] cut1D(String string, int w, int h) {
		return cut1D(string, w, h, 0, 0);
	}

	protected static Bitmap[] cut1D(String path, int w, int h, int bx, int by) {
		try {
			BufferedImage bi = loadImage(path);

			w = bi.getWidth() / w;
			h = bi.getHeight() / h;

			int xTiles = (bi.getWidth() - bx) / w;
			int yTiles = (bi.getHeight() - by) / h;

			Bitmap[] result = new Bitmap[xTiles * yTiles];

			for (int y = 0; y < yTiles; y++) {
				for (int x = 0; x < xTiles; x++) {
					result[y * xTiles + x] = new Bitmap(w, h);
					bi.getRGB(bx + x * w, by + y * h, w, h, result[y * xTiles + x].getPixels(), 0, w);
				}
			}

			return result;
		} catch (IOException e) {
			System.out.println("Could not load File from Ressources: \"" + path + "\" (absCanvas.Art)");
			e.printStackTrace();
		}
		return null;
	}

	protected static Bitmap[] cut1DReverse(String string, int w, int h) {
		return cut1DReverse(string, w, h, 0, 0);
	}

	protected static Bitmap[] cut1DReverse(String path, int w, int h, int bx, int by) {
		try {
			BufferedImage bi = loadImage(path);

			w = bi.getWidth() / w;
			h = bi.getHeight() / h;

			int xTiles = (bi.getWidth() - bx) / w;
			int yTiles = (bi.getHeight() - by) / h;

			Bitmap[] result = new Bitmap[yTiles * xTiles];

			for (int x = 0; x < xTiles; x++) {
				for (int y = 0; y < yTiles; y++) {
					result[x * yTiles + y] = new Bitmap(w, h);
					bi.getRGB(bx + x * w, by + y * h, w, h, result[x * yTiles + y].getPixels(), 0, w);
				}
			}

			return result;
		} catch (IOException e) {
			System.out.println("Could not load File from Ressources: \"" + path + "\" (absCanvas.Art)");
			e.printStackTrace();
		}
		return null;
	}

	public static int[][] getColors(Bitmap[][] tiles) {
		int[][] result = new int[tiles.length][tiles[0].length];
		for (int y = 0; y < tiles[0].length; y++) {
			for (int x = 0; x < tiles.length; x++) {
				result[x][y] = getColor(tiles[x][y]);
			}
		}
		return result;
	}

	public static int getColor(Bitmap bitmap) {
		if (bitmap.getPixelSize() == 0) {
			return 0;
		}
		
		
		int r = 0;
		int g = 0;
		int b = 0;
		int a = 0;
		for (int i = 0; i < bitmap.getPixelSize(); i++) {
			int col = bitmap.getPixel(i);
			a = (col >> 24) & 0xff;
			if (a == 255) {
				r += (col >> 16) & 0xff;
				g += (col >> 8) & 0xff;
				b += (col) & 0xff;
			}
		}

		r /= bitmap.getPixelSize();
		g /= bitmap.getPixelSize();
		b /= bitmap.getPixelSize();

		return 0xff000000 | r << 16 | g << 8 | b;
	}

	public static int getColor(int[][] colA) {
		int c = 0;

		int r = 0;
		int g = 0;
		int b = 0;

		for (int x = 0; x < colA.length; x++) {
			for (int y = 0; y < colA[x].length; y++) {
				c++;

				r += (colA[x][y] >> 16) & 0xff;
				g += (colA[x][y] >> 8) & 0xff;
				b += (colA[x][y]) & 0xff;
			}
		}

		r /= c;
		g /= c;
		b /= c;

		return 0xff000000 | r << 16 | g << 8 | b;
	}

	public static Bitmap load(String path) {
		try {
			BufferedImage bi = loadImage(path);

			int w = bi.getWidth();
			int h = bi.getHeight();

			Bitmap result = new Bitmap(w, h);
			bi.getRGB(0, 0, w, h, result.getPixels(), 0, w);

			return result;
		} catch (IOException e) {
			System.out.println("Could not load File from Ressources: \"" + path + "\" (absCanvas.Art)");
			e.printStackTrace();
		}

		return null;
	}

	public static Bitmap load(InputStream r) {
		try {
			BufferedImage bi = ImageIO.read(r);

			int w = bi.getWidth();
			int h = bi.getHeight();

			Bitmap result = new Bitmap(w, h);
			bi.getRGB(0, 0, w, h, result.getPixels(), 0, w);

			return result;
		} catch (IOException e) {
			System.out.println("Could not read Image from Stream (absCanvas.Art)");
			e.printStackTrace();
		}

		return null;
	}

	protected static Bitmap[] cutY(String path, int h) {
		try {
			BufferedImage bi = loadImage(path);

			h = bi.getHeight() / h;

			int yTiles = bi.getHeight() / h;
			int w = bi.getWidth();

			Bitmap[] result = new Bitmap[yTiles];

			for (int y = 0; y < yTiles; y++) {
				result[y] = new Bitmap(w, h);
				bi.getRGB(0, y * h, w, h, result[y].getPixels(), 0, w);
			}

			return result;
		} catch (IOException e) {
			System.out.println("Could not load File from Ressources: \"" + path + "\" (absCanvas.Art)");
			e.printStackTrace();
		}

		return null;
	}

	protected static Bitmap[] cutX(String path, int w) {
		try {
			BufferedImage bi = loadImage(path);

			w = bi.getWidth() / w;

			int xTiles = bi.getWidth() / w;
			int h = bi.getHeight();

			Bitmap[] result = new Bitmap[xTiles];

			for (int x = 0; x < xTiles; x++) {
				result[x] = new Bitmap(w, h);
				bi.getRGB(x * w, 0, w, h, result[x].getPixels(), 0, w);
			}

			return result;
		} catch (IOException e) {
			System.out.println("Could not load File from Ressources: \"" + path + "\" (absCanvas.Art)");
			e.printStackTrace();
		}

		return null;
	}

	protected static BufferedImage loadImage(String string) throws IOException {
		BufferedImage bi = ImageIO.read(Screen.class.getResource(makePathCompatible(string)));
		return bi;
	}

	protected static String makePathCompatible(String fn) {
		fn.replace('\\', '/');
		if (fn.charAt(0) != '/')
			fn = "/" + fn;
		return fn;
	}
}
