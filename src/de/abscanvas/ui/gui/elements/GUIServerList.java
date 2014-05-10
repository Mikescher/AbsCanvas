package de.abscanvas.ui.gui.elements;

import java.awt.Font;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import de.abscanvas.input.MouseButtons;
import de.abscanvas.math.MDPoint;
import de.abscanvas.network.NetworkConstants;
import de.abscanvas.surface.AbsColor;
import de.abscanvas.surface.Bitmap;
import de.abscanvas.ui.gui.GUIElement;
import de.abscanvas.ui.gui.GUIMenu;
import de.abscanvas.ui.listener.SelectionListener;

public class GUIServerList extends GUIElement {
	private final static int SCROLL_WIDTH = 16;

	private int colBackground = AbsColor.WHITE;
	private int colElementBackground = AbsColor.LIGHT_GRAY;
	private int colElementSelected = AbsColor.DARK_LIGHT_GRAY;
	private int colLines = AbsColor.BLACK;
	private int colScrollbarBackground = AbsColor.WHITE;
	private int colScrollbar = AbsColor.RED;
	private int colText = AbsColor.BLACK;
	private int colTextError = AbsColor.RED;

	private int colVisualPingOutGrayed = AbsColor.GRAY;
	private int colVisualPingGood = AbsColor.GREEN;
	private int colVisualPingMedium = AbsColor.YELLOW;
	private int colVisualPingBad = AbsColor.RED;

	private int scrollBarWidth = 10;
	private int scrollBarHeight = 20;

	private int lineHeight = 64;
	private int lineGap = 4;

	public class ServerLine {
		private long lastSuccPing;

		private int ping;
		private String servername;
		private InetAddress ip;
		private String gamename;
		private String serverversion;
		private int clientcount;
		private int maxClientCount;
		private int port;
		private boolean canConnect;

		public ServerLine(int ping, String servername, InetAddress ip, int port, String gamename, String serverversion, int clientcount, int maxClientCount, boolean canConnect) {
			setPing(ping);
			setServername(servername);
			setIP(ip);
			setGamename(gamename);
			setServerversion(serverversion);
			setClientcount(clientcount);
			setMaxClientCount(maxClientCount);
			setCanConnect(canConnect);
			setPort(port);
		}

		public void setPing(int ping) {
			lastSuccPing = System.currentTimeMillis();
			this.ping = ping;
		}

		public int getPing() {
			if (System.currentTimeMillis() - lastSuccPing >= 5000) {
				ping = 999;
			}

			if (ping <= 999) {
				return ping;
			}
			return 999;
		}

		public void setServername(String servername) {
			this.servername = servername;
		}

		public String getServername() {
			return servername;
		}

		public void setIP(InetAddress ip) {
			this.ip = ip;
		}

		public String getStringIP() {
			return ip.getHostAddress();
		}

		public void setPort(int port) {
			this.port = port;
		}

		public int getPort() {
			return port;
		}

		public InetAddress getIP() {
			return ip;
		}

		public void setGamename(String gamename) {
			this.gamename = gamename;
		}

		public String getGamename() {
			return gamename;
		}

		public void setServerversion(String serverversion) {
			this.serverversion = serverversion;
		}

		public String getServerversion() {
			return serverversion;
		}

		public void setClientcount(int clientcount) {
			this.clientcount = clientcount;
		}

		public int getClientcount() {
			return clientcount;
		}

		public void setMaxClientCount(int maxClientCount) {
			this.maxClientCount = maxClientCount;
		}

		public int getMaxClientCount() {
			return maxClientCount;
		}

		public void setCanConnect(boolean canConnect) {
			this.canConnect = canConnect;
		}
		
		public boolean canConnectVersion() {
			return getServerversion().equalsIgnoreCase(clientVersion);
		}
		
		public boolean canConnectClientCount() {
			return getClientcount() < getMaxClientCount();
		}
		
		public boolean canConnectPing() {
			return getPing() < 999;
		}
		
		public boolean canConnectServerRule() {
			return canConnect;
		}

		public boolean canConnect() {
			return canConnectPing() && canConnectServerRule() && canConnectVersion() && canConnectClientCount();
		}
	}

	private String clientVersion = "-";

	private ArrayList<ServerLine> serverlist = new ArrayList<ServerLine>();

	private int id = -1;
	private List<SelectionListener> listeners;

	private int selectedServer = -1;

	private int offset = 0; // zwischen 0 und (height-scrollbarHeight)

	private Font minor_font = new Font("Arial", Font.PLAIN, 10);
	private Font small_font = new Font("Arial", Font.PLAIN, 12);
	private Font x_font_bold = new Font("Arial", Font.BOLD, 14);
	private Font big_font = new Font("Arial", Font.PLAIN, 20);

	private boolean isUpdating = false;

	private boolean mouseDownOnScrollbar = false;

	public GUIServerList(int x, int y, int width, int height, int id, String clientVersion, GUIMenu owner) {
		super(x, y, width, height, new Bitmap(0, 0), owner);
		this.clientVersion = clientVersion;
		this.id = id;
		repaint();
	}

	@Override
	public void tick() {
		super.tick();

		MouseButtons mb = owner.getOwner().getMouseButtons();
		MDPoint mp = owner.getOwner().screenToCanvas(mb.getPosition());
		mp.sub(getPos().asMDPoint());

		if (mouseDownOnScrollbar) {
			if (!mb.isDown() && !mb.isNextDown()) {
				mouseDownOnScrollbar = false;
			}
			if (mb.isMouseMoving()) {
				setOffset((int) mp.getY() - scrollBarHeight / 2);
			}
		} else if (mb.isDown()) {
			if (mb.isHovered(this)) {
				boolean succ = false;
				if (mp.getX() > (4) && mp.getX() < (4 + getWidth() - scrollBarWidth - 8)) {
					int linesHeight = (serverlist.size() * (lineHeight + lineGap)) + lineGap;

					int offDraw = (int) (((offset * 1d) / (getHeight() - scrollBarHeight)) * (linesHeight - getHeight()));
					if (offDraw < 0) {
						offDraw = 0;
					}

					for (int i = 0; i < serverlist.size(); i++) {
						int sby = -offDraw + i * (lineHeight + lineGap) + 4;
						int sbh = lineHeight;

						if (mp.getY() > sby && mp.getY() < (sby + sbh)) {
							select(i);
							succ = true;
							break;
						}
					}
				}

				if (!succ) {
					select(-1);
				} else {
					if (mb.isDoubleClick()) {
						if (listeners != null) {
							if (listeners.size() > 0) {
								for (SelectionListener l : listeners) {
									l.itemDoubleClicked(id, getSelectedIndex());
								}
							}
						}
					}
				}
			}

		}

		if (mb.isHovered(this)) {
			int sc = mb.getMouseWheelRotation() * 5;
			if (sc != 0) {
				scrollBy(sc);
			}
		}

		repaint();
	}

	public void select(int index) {
		if (index < -1 || index >= serverlist.size()) {
			return;
		}
		selectedServer = index;

		if (listeners != null) {
			if (listeners.size() > 0) {
				for (SelectionListener l : listeners) {
					l.itemSelected(id, index);
				}
			}
		}
	}

	public void scrollBy(int rot) {
		double linesHeight = (serverlist.size() * (lineHeight + lineGap)) + lineGap;
		double scrollHeight = getHeight() - scrollBarHeight;

		if (linesHeight < getHeight()) {
			setOffset(0);
			return;
		}

		double newTotalOffset = ((offset / scrollHeight) * linesHeight) + rot * SCROLL_WIDTH;

		int newOffset = (int) ((newTotalOffset / linesHeight) * scrollHeight);

		if (newOffset < 0) {
			newOffset = 0;
		}

		if (newOffset > scrollHeight) {
			newOffset = (int) scrollHeight;
		}

		setOffset(newOffset);
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

	@Override
	protected void repaint() {
		if (isUpdating) {
			return;
		}

		int rowCount = serverlist.size();

		int offScroll = offset;
		if (offScroll > (getHeight() - scrollBarWidth)) {
			offScroll = getHeight() - scrollBarWidth;
		}
		if (offScroll < 0) {
			offScroll = 0;
		}
		int linesHeight = (rowCount * (lineHeight + lineGap)) + lineGap;

		int offDraw = (int) (((offset * 1d) / (getHeight() - scrollBarHeight)) * (linesHeight - getHeight()));
		if (offDraw < 0) {
			offDraw = 0;
		}
		
		boolean scrollbarNeeded = linesHeight >= getHeight();
		int realWidth = getWidth();
		if (scrollbarNeeded) {
			realWidth -= scrollBarWidth;
		}

		Bitmap b = createBackground();

		if (serverlist.size() == 0) {
			drawNonServerMessage(b, realWidth);
		} else {
			for (int i = 0; i < serverlist.size(); i++) {
				drawElement(offDraw, b, i, realWidth);
			}
		}

		if (scrollbarNeeded) {
			drawScrollbar(offScroll, linesHeight, b);
		}

		setImage(b);
	}

	private void drawNonServerMessage(Bitmap b, int elWid) {
		b.drawString("No Server found", big_font, AbsColor.LIGHT_GRAY, elWid / 2 - Bitmap.getStringWidth(big_font, "No Server found") / 2, 5);
	}

	private void drawScrollbar(int offScroll, int linesHeight, Bitmap b) {
		b.fill(getWidth() - scrollBarWidth, 1, scrollBarWidth - 1, getHeight() - 2, colScrollbarBackground);
		b.lineVert(getWidth() - scrollBarWidth, 0, getHeight(), colLines);

		if (linesHeight >= getHeight()) {
			b.fill(getWidth() - scrollBarWidth + 1, offScroll + 1, scrollBarWidth - 2, scrollBarHeight - 2, colScrollbar);
		} else {
			b.fill(getWidth() - scrollBarWidth + 1, 1, scrollBarWidth - 2, getHeight() - 2, colScrollbar);
		}
	}

	private void drawElement(int offDraw, Bitmap b, int i, int elWid) {
		// ----------------------- MATH ------------------------

		boolean sel = i == selectedServer;

		ServerLine line = serverlist.get(i);

		int visPing = 0;

		if (line.getPing() <= NetworkConstants.PING_LEVEL_VERYGOOD) {
			visPing = 5;
		} else if (line.getPing() <= NetworkConstants.PING_LEVEL_GOOD) {
			visPing = 4;
		} else if (line.getPing() <= NetworkConstants.PING_LEVEL_MEDIUM) {
			visPing = 3;
		} else if (line.getPing() <= NetworkConstants.PING_LEVEL_BAD) {
			visPing = 2;
		} else if (line.getPing() <= NetworkConstants.PING_LEVEL_VERYBAD) {
			visPing = 1;
		} else {
			visPing = 0;
		}

		int colOnlineDot;
		if (line.canConnect()) {
			colOnlineDot = AbsColor.GREEN;
		} else {
			colOnlineDot = AbsColor.RED;
		}

		int sby = -offDraw + i * (lineHeight + lineGap) + 4;
		int sbx = 4;
		int sbw = elWid - 8;
		int sbh = lineHeight;

		String clientCountString = line.getClientcount() + "/" + line.getMaxClientCount();
		int clientCountX = sbx + sbw - Bitmap.getStringWidth(minor_font, clientCountString) - 3;
		int clientCountY = sby + sbh - minor_font.getSize() - 3;

		int ipX = sbx + sbw - Bitmap.getStringWidth(minor_font, line.getStringIP() + ":" + line.getPort()) - 2;
		int ipY = sby + 1;

		int verX = sbx + 3 + 42 + 3 + Bitmap.getStringWidth(big_font, line.getServername()) + 5;
		int verY = sby + 3 + big_font.getSize() - minor_font.getSize();

		// ----------------------- PAINT -----------------------
		drawElementBackground(b, sel, colOnlineDot, sby, sbx, sbw, sbh);

		b.drawString(line.getServername(), big_font, colText, sbx + 3 + 42 + 3, sby + 7);
		b.drawString(line.getGamename(), small_font, colText, sbx + 3 + 42 + 3, sby + 7 + 42 - small_font.getSize() - 4);

		if (line.canConnectClientCount()) {
			b.drawString(clientCountString, minor_font, colText, clientCountX, clientCountY);
		} else {
			b.drawString(clientCountString, minor_font, colTextError, clientCountX, clientCountY);
		}

		if (line.canConnectVersion()) {
			b.drawString(line.getServerversion(), minor_font, colText, verX, verY);
		} else {
			b.drawString(line.getServerversion(), minor_font, colTextError, verX, verY);
		}
		
		drawVisualPing(b, line, visPing, sby, sbx, sbh, clientCountY);

		b.drawString(line.getStringIP() + ":" + line.getPort(), minor_font, colText, ipX, ipY);
	}

	private void drawElementBackground(Bitmap b, boolean sel, int colOnlineDot, int sby, int sbx, int sbw, int sbh) {
		if (sel) {
			b.fill(sbx, sby, sbw, sbh, colElementSelected);
		} else {
			b.fill(sbx, sby, sbw, sbh, colElementBackground);
		}

		b.rectangle(sbx, sby, sbw, sbh, colLines);
		if (sel) {
			b.rectangle(sbx + 1, sby + 1, sbw - 2, sbh - 2, colLines);
		}
		b.fill(sbx + 3, sby + 3, 42, 42, colElementBackground);
		b.rectangle(sbx + 3, sby + 3, 42, 42, colLines);

		b.circleFill(sbx + 13, sby + 13, 8, colOnlineDot);
		b.circle(sbx + 13, sby + 13, 8, colLines);
	}

	private void drawVisualPing(Bitmap b, ServerLine line, int visPing, int sby, int sbx, int sbh, int clientCountY) {
		int pDelta = 0; // 45

		switch (visPing) {
		case 0:
			b.fill(sbx + pDelta + 3, sby + sbh - 3 - 4, 2, 4, colVisualPingOutGrayed);
			b.fill(sbx + pDelta + 6, sby + sbh - 3 - 6, 2, 6, colVisualPingOutGrayed);
			b.fill(sbx + pDelta + 9, sby + sbh - 3 - 8, 2, 8, colVisualPingOutGrayed);
			b.fill(sbx + pDelta + 12, sby + sbh - 3 - 10, 2, 10, colVisualPingOutGrayed);
			b.fill(sbx + pDelta + 15, sby + sbh - 3 - 12, 2, 12, colVisualPingOutGrayed);
			
			b.drawString("x", x_font_bold, colTextError, sbx + pDelta + 3, clientCountY-4);
			break;
		case 1:
			b.fill(sbx + pDelta + 3, sby + sbh - 3 - 4, 2, 4, colVisualPingBad);
			b.fill(sbx + pDelta + 6, sby + sbh - 3 - 6, 2, 6, colVisualPingOutGrayed);
			b.fill(sbx + pDelta + 9, sby + sbh - 3 - 8, 2, 8, colVisualPingOutGrayed);
			b.fill(sbx + pDelta + 12, sby + sbh - 3 - 10, 2, 10, colVisualPingOutGrayed);
			b.fill(sbx + pDelta + 15, sby + sbh - 3 - 12, 2, 12, colVisualPingOutGrayed);
			break;
		case 2:
			b.fill(sbx + pDelta + 3, sby + sbh - 3 - 4, 2, 4, colVisualPingBad);
			b.fill(sbx + pDelta + 6, sby + sbh - 3 - 6, 2, 6, colVisualPingBad);
			b.fill(sbx + pDelta + 9, sby + sbh - 3 - 8, 2, 8, colVisualPingOutGrayed);
			b.fill(sbx + pDelta + 12, sby + sbh - 3 - 10, 2, 10, colVisualPingOutGrayed);
			b.fill(sbx + pDelta + 15, sby + sbh - 3 - 12, 2, 12, colVisualPingOutGrayed);
			break;
		case 3:
			b.fill(sbx + pDelta + 3, sby + sbh - 3 - 4, 2, 4, colVisualPingMedium);
			b.fill(sbx + pDelta + 6, sby + sbh - 3 - 6, 2, 6, colVisualPingMedium);
			b.fill(sbx + pDelta + 9, sby + sbh - 3 - 8, 2, 8, colVisualPingMedium);
			b.fill(sbx + pDelta + 12, sby + sbh - 3 - 10, 2, 10, colVisualPingOutGrayed);
			b.fill(sbx + pDelta + 15, sby + sbh - 3 - 12, 2, 12, colVisualPingOutGrayed);
			break;
		case 4:
			b.fill(sbx + pDelta + 3, sby + sbh - 3 - 4, 2, 4, colVisualPingGood);
			b.fill(sbx + pDelta + 6, sby + sbh - 3 - 6, 2, 6, colVisualPingGood);
			b.fill(sbx + pDelta + 9, sby + sbh - 3 - 8, 2, 8, colVisualPingGood);
			b.fill(sbx + pDelta + 12, sby + sbh - 3 - 10, 2, 10, colVisualPingGood);
			b.fill(sbx + pDelta + 15, sby + sbh - 3 - 12, 2, 12, colVisualPingGood);
			break;
		case 5:
			b.fill(sbx + pDelta + 3, sby + sbh - 3 - 4, 2, 4, colVisualPingGood);
			b.fill(sbx + pDelta + 6, sby + sbh - 3 - 6, 2, 6, colVisualPingGood);
			b.fill(sbx + pDelta + 9, sby + sbh - 3 - 8, 2, 8, colVisualPingGood);
			b.fill(sbx + pDelta + 12, sby + sbh - 3 - 10, 2, 10, colVisualPingGood);
			b.fill(sbx + pDelta + 15, sby + sbh - 3 - 12, 2, 12, colVisualPingGood);
			break;
		}
		if (visPing>0) {
			b.drawString(line.getPing() + "ms", minor_font, colText, sbx + pDelta + 21, clientCountY);
		} else {
			b.drawString(line.getPing() + "ms", minor_font, colTextError, sbx + pDelta + 21, clientCountY);
		}
		
	}

	private Bitmap createBackground() {
		Bitmap b = new Bitmap(getWidth(), getHeight());
		b.fill(colBackground);

		b.rectangle(0, 0, getWidth(), getHeight(), colLines);
		return b;
	}

	public void setMinorFont(java.awt.Font f) {
		minor_font = f;
		repaint();
	}

	public Font getMinorFont() {
		return minor_font;
	}

	public void setSmallFont(java.awt.Font f) {
		small_font = f;
		repaint();
	}

	public Font getSmallFont() {
		return small_font;
	}

	public void setBigFont(java.awt.Font f) {
		big_font = f;
		repaint();
	}

	public Font getBigFont() {
		return big_font;
	}

	public void setScrollBarSize(int w, int h) {
		scrollBarWidth = w;
		scrollBarHeight = h;

		repaint();
	}

	public void clear() {
		serverlist.clear();
		repaint();
	}

	public void addServer(int ping, String servername, InetAddress ip, int port, String gamename, String serverversion, int clientcount, int maxClientCount, boolean canConnect) {
		ServerLine addL = new ServerLine(ping, servername, ip, port, gamename, serverversion, clientcount, maxClientCount, canConnect);
		serverlist.add(addL);

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

	public void beginUpdate() {
		isUpdating = true;
	}

	public void endUpdate() {
		isUpdating = false;
		repaint();
	}

	public void addListener(SelectionListener listener) {
		if (listeners == null) {
			listeners = new ArrayList<SelectionListener>();
		}
		listeners.add(listener);
	}

	public void setColorScheme(int background, int elementBackground, int background_selected, int lines, int scrollbar, int scrollbarBackground, int text, int errorText) {
		colBackground = background;
		colElementSelected = background_selected;
		colLines = lines;
		colScrollbar = scrollbar;
		colScrollbarBackground = scrollbarBackground;
		colText = text;
		colTextError = errorText;
		colElementBackground = elementBackground;

		repaint();
	}

	public int getSelectedIndex() {
		return selectedServer;
	}

	public ServerLine getSelectedServer() {
		if (selectedServer < 0) {
			return null;
		}

		if (selectedServer >= serverlist.size()) {
			return null;
		}

		return serverlist.get(selectedServer);
	}

	public void setPing(int id, int ping) {
		serverlist.get(id).setPing(ping);

		repaint();
	}

	public boolean isUpdating() {
		return isUpdating;
	}

	public int findServerIndex(InetAddress address, int port) {
		for (int i = 0; i < serverlist.size(); i++) {
			if (serverlist.get(i).getPort() == port && serverlist.get(i).getIP().getHostAddress().equals(address.getHostAddress())) {
				return i;
			}
		}
		return -1;
	}

	public ArrayList<ServerLine> getServer() {
		return serverlist;
	}

	public int getServerCount() {
		return serverlist.size();
	}
}
