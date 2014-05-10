package de.abscanvas.ui.gui.elements;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import de.abscanvas.input.MouseButtons;
import de.abscanvas.math.MDPoint;
import de.abscanvas.surface.AbsColor;
import de.abscanvas.surface.Bitmap;
import de.abscanvas.ui.gui.GUIElement;
import de.abscanvas.ui.gui.GUIMenu;

public class GUIListBox extends GUIElement {
	private final static int SCROLL_WIDTH = 8;
	
	private int colBackground = AbsColor.WHITE;
	private int colLines = AbsColor.BLACK;
	private int colScrollbarBackground = AbsColor.WHITE;
	private int colScrollbar = AbsColor.RED;
	private int colText = AbsColor.BLACK;

	private int scrollBarWidth = 10;
	private int scrollBarHeight = 20;
	
	private boolean thickLines = false;

	public class ListBoxLine {
		private ArrayList<String> s = new ArrayList<String>();

		public ListBoxLine(ArrayList<String> s0) {
			s.addAll(s0);
		}

		public ListBoxLine(String s1) {
			s.add(s1);
		}

		public ListBoxLine(String s1, String s2) {
			s.add(s1);
			s.add(s2);
		}

		public ListBoxLine(String s1, String s2, String s3) {
			s.add(s1);
			s.add(s2);
			s.add(s3);
		}

		public ListBoxLine(String s1, String s2, String s3, String s4) {
			s.add(s1);
			s.add(s2);
			s.add(s3);
			s.add(s4);
		}

		public String get(int row) {
			if (row <= (s.size() - 1)) {
				return s.get(row);
			} else {
				return "";
			}
		}

		public int rowSize() {
			return s.size();
		}
	}

	private ArrayList<ListBoxLine> lines = new ArrayList<ListBoxLine>();

	private int offset = 0; // zwischen 0 und height

	private Font font = new Font("Arial", 0, 12);

	private boolean isUpdating = false;

	private boolean mouseDownOnScrollbar = false;

	public GUIListBox(int x, int y, int width, int height, GUIMenu owner) {
		super(x, y, width, height, new Bitmap(0, 0), owner);
		repaint();
	}

	@Override
	protected void repaint() {
		// ------------------------------- MATH -------------------------

		int lineHeight = font.getSize() + 3;

		int rowCount = getRowCount();

		int[] rowWidth = new int[rowCount];
		for (int i = 0; i < rowCount; i++) {
			rowWidth[i] = getRowWidth(i);
			rowWidth[i] += 4;
			if (rowWidth[i] < 10) {
				rowWidth[i] = 10;
			}
		}

		int offScroll = offset;
		if (offScroll > (getHeight() - scrollBarWidth)) {
			offScroll = getHeight() - scrollBarWidth;
		}
		if (offScroll < 0) {
			offScroll = 0;
		}
		int linesHeight = (lines.size() * lineHeight + 1);

		int offDraw = (int) (((offset * 1d) / (getHeight() - scrollBarHeight)) * (linesHeight - getHeight()));
		if (offDraw < 0) {
			offDraw = 0;
		}

		// ------------------------------- PAINT -------------------------

		Bitmap b = new Bitmap(getWidth(), getHeight());
		b.fill(colBackground);

		b.rectangle(0, 0, getWidth(), getHeight(), colLines);
		if (thickLines) b.rectangle(1, 1, getWidth() - 2, getHeight() - 2, colLines);
		
		// ------------------------------- LINES -------------------------

		if (rowCount > 0) {
			int ttc = 0;
			for (int i = 0; i < (rowCount-1); i++) {
				ttc += rowWidth[i];
				b.lineVert(ttc, 0, getHeight(), colLines);
				if (thickLines) b.lineVert(ttc + 1, 0, getHeight(), colLines);
				ttc += 1;
			}

			for (int i = 0; i < lines.size(); i++) {
				ListBoxLine l = lines.get(i);
				int y = -offDraw + i * lineHeight;

				int ttl = 2;
				for (int j = 0; j < rowCount; j++) {
					b.drawString(l.get(j), font, colText, ttl, y + 1);

					ttl += rowWidth[j];
				}

				b.lineHorz(0, y, getWidth() - scrollBarWidth, colLines);
				if (thickLines) b.lineHorz(0, y + 1, getWidth() - scrollBarWidth, colLines);
			}
		}
		
		// ------------------------------- SCROLLBAR -------------------------

		b.fill(getWidth() - scrollBarWidth, 1, scrollBarWidth - 1, getHeight() - 2, colScrollbarBackground);
		b.lineVert(getWidth() - scrollBarWidth, 0, getHeight(), colLines);
		if (thickLines) b.lineVert(getWidth() - scrollBarWidth - 1, 0, getHeight(), colLines);

		if (linesHeight >= getHeight()) {
			b.fill(getWidth() - scrollBarWidth + 1, offScroll + 1, scrollBarWidth - 2, scrollBarHeight - 2, colScrollbar);
		} else {
			b.fill(getWidth() - scrollBarWidth + 1, 1, scrollBarWidth - 2, getHeight() - 2, colScrollbar);
		}

		setImage(b);
	}
	
	public void scrollBy(int rot) {
		double linesHeight = (lines.size() * font.getSize() + 4);
		double scrollHeight = getHeight() - scrollBarHeight;
		
		if (linesHeight < getHeight()) {
			setOffset(0);
			return;
		}

		double newTotalOffset = ((offset / scrollHeight) * linesHeight) + rot * SCROLL_WIDTH;

		int newOffset = (int) ((newTotalOffset / linesHeight) * scrollHeight);
		
		if (newOffset<0) {
			newOffset = 0;
		}
		
		if (newOffset > scrollHeight) {
			newOffset = (int) scrollHeight;
		}
		
		setOffset(newOffset);
	}

	@Override
	public void tick() {
		super.tick();

		MouseButtons mb = owner.getOwner().getMouseButtons();
		
		if (mouseDownOnScrollbar) {
			if (!mb.isDown() && !mb.isNextDown()) {
				mouseDownOnScrollbar = false;
			}
			if (mb.isMouseMoving()) {
				MDPoint p = owner.getOwner().screenToCanvas(mb.getPosition());
				p.sub(getPos().asMDPoint());

				setOffset((int) p.getY() - scrollBarHeight / 2);
			}
		}
		
		if (mb.isHovered(this)) {
			int sc = mb.getMouseWheelRotation() * 5;
			if (sc != 0) {
				scrollBy(sc);
			}
		}
	}

	private int getRowWidth(int row) {
		Graphics g = (new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB)).getGraphics();

		int w = 0;
		for (ListBoxLine l : lines) {
			int len = Bitmap.getStringWidth(g, font, l.get(row));
			if (len > w) {
				w = len;
			}
		}
		return w;
	}

	private int getRowCount() {
		int rc = 0;
		for (ListBoxLine lbl : lines) {
			if (lbl.rowSize() > rc) {
				rc = lbl.rowSize();
			}
		}
		return rc;
	}
	
	public void addLine(String s1) {
		lines.add(new ListBoxLine(s1));
		if (!isUpdating) {
			repaint();
		}
	}
	
	public void addLine(String s1, String s2) {
		lines.add(new ListBoxLine(s1, s2));
		if (!isUpdating) {
			repaint();
		}
	}
	
	public void addLine(String s1, String s2, String s3) {
		lines.add(new ListBoxLine(s1, s2, s3));
		if (!isUpdating) {
			repaint();
		}
	}
	
	public void addLine(String s1, String s2, String s3, String s4) {
		lines.add(new ListBoxLine(s1, s2, s3, s4));
		if (!isUpdating) {
			repaint();
		}
	}

	public void addLine(ArrayList<String> s0) {
		lines.add(new ListBoxLine(s0));
		if (!isUpdating) {
			repaint();
		}
	}
	
	public void addLine(ListBoxLine s0) {
		lines.add(s0);
		if (!isUpdating) {
			repaint();
		}
	}

	public void clear() {
		lines.clear();
		repaint();
	}

	public void beginUpdate() {
		isUpdating = true;
	}

	public void endUpdate() {
		isUpdating = false;
		repaint();
	}

	public void setOffset(int o) {
		if (o < 0) {
			offset = 0;
		} else if (o > (getHeight() - scrollBarHeight)) {
			offset = getHeight() - scrollBarHeight;
		} else {
			offset = o;
		}

		repaint();
	}

	public void setColorScheme(int background, int lines, int scrollbar, int scrollbarBackground, int text) {
		colBackground = background;
		colLines = lines;
		colScrollbar = scrollbar;
		colScrollbarBackground = scrollbarBackground;
		colText = text;

		repaint();
	}

	public Font getFont() {
		return font;
	}

	public void setFont(java.awt.Font f) {
		font = f;
		repaint();
	}

	public void setScrollBarSize(int w, int h) {
		scrollBarWidth = w;
		scrollBarHeight = h;

		repaint();
	}

	@Override
	public void onMousePress(MouseButtons mb) {
		MDPoint p = owner.getOwner().screenToCanvas(mb.getPosition());
		p.sub(getPos().asMDPoint());
		if (p.getX() > (getWidth() - scrollBarWidth)) {
			mouseDownOnScrollbar = true;
		}
	}

	public void thickLines(boolean tl) {
		this.thickLines = tl;
	}
}
