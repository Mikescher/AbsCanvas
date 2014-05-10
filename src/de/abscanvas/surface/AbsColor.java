package de.abscanvas.surface;

public class AbsColor {
	public static final int transparent = 0;
	public static final int TRANSPARENT = transparent;
	
	public static final int white = -1;
	public static final int WHITE = white;

	public static final int lightGray = -4144960;
	public static final int LIGHT_GRAY = lightGray;
	
	public static final int darklightgray = -6184543;
	public static final int DARK_LIGHT_GRAY = darklightgray;

	public static final int gray = -8355712;
	public static final int GRAY = gray;

	public static final int darkGray = -12566464;
	public static final int DARK_GRAY = darkGray;

	public static final int black = -16777216;
	public static final int BLACK = black;

	public static final int red =  -65536;
	public static final int RED = red;

	public static final int pink = -20561;
	public static final int PINK = pink;

	public static final int orange = -14336;
	public static final int ORANGE = orange;

	public static final int yellow = -256;
	public static final int YELLOW = yellow;

	public static final int green = -16711936;
	public static final int GREEN = green;

	public static final int magenta = -65281;
	public static final int MAGENTA = magenta;

	public static final int cyan = -16711681;
	public static final int CYAN = cyan;

	public static final int blue = -16776961;
	public static final int BLUE = blue;
	
	public static int getColor(int r, int g, int b) {
		return 0xff000000 | r << 16 | g << 8 | b;
	}
	
	public static int getColor(int r, int g, int b, int a) {
		return 0x00000000 | a << 24 | r << 16 | g << 8 | b;
	}
}
