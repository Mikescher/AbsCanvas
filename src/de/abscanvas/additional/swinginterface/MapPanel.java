package de.abscanvas.additional.swinginterface;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import de.abscanvas.ConsoleScreen;
import de.abscanvas.entity.network.ServerEntity;
import de.abscanvas.level.Level;
import de.abscanvas.level.ServerLevel;

public class MapPanel extends Canvas implements Runnable {
	private static final long serialVersionUID = 8454093419781450441L;

	private boolean running = true;

	private ConsoleScreen screen;

	public MapPanel() {
		(new Thread(this)).start();
	}

	@Override
	public void run() {
		long lastRTime = System.currentTimeMillis();

		while (running) {
			if (screen != null) {
				Level level = screen.getLevel();

				if (level != null) {
					if ((System.currentTimeMillis() - lastRTime) > 33) {
						BufferStrategy bs = getBufferStrategy();
						if (bs == null) {
							createBufferStrategy(3);
							continue;
						}

						Graphics g = bs.getDrawGraphics();

						if (bs != null) {
							bs.show();
						}

						render(g, (ServerLevel) level);

						lastRTime = System.currentTimeMillis();
					}
				}
			}

			Thread.yield();

			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void render(Graphics rendg, ServerLevel level) {
		final int TILE_SIZE = 16;
		final int ENTITY_SIZE = 4;

		int panelW = getWidth();
		int panelH = getHeight();

		int w = level.getWidth() * TILE_SIZE;
		int h = level.getHeight() * TILE_SIZE;
		
		int levelW = level.getWidth() * level.getTileWidth();
		int levelH = level.getHeight() * level.getTileHeight();

		BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) bi.getGraphics();

		g.setBackground(Color.MAGENTA);
		g.clearRect(0, 0, w, h);

		for (int xt = 0; xt < level.getWidth(); xt++) {
			for (int yt = 0; yt < level.getHeight(); yt++) {
				g.setColor(new Color(level.getTile(xt, yt).getMinimapColor()));
				g.fillRect(xt * TILE_SIZE, yt * TILE_SIZE, TILE_SIZE, TILE_SIZE);
			}
		}

		rendg.drawImage(bi, 0, 0, panelW, panelH, null);

		for (int i = 0; i < level.getEntities().size(); i++) {
			ServerEntity e = (ServerEntity) level.getEntities().get(i);
			rendg.setColor(new Color(e.getMinimapColor()));

			int xe = (int) ((e.getPosX() / levelW) * panelW);
			int ye = (int) ((e.getPosY() / levelH) * panelH);

			rendg.fillRect(xe - ENTITY_SIZE/2, ye - ENTITY_SIZE/2, ENTITY_SIZE, ENTITY_SIZE);
		}
	}

	public void setScreen(ConsoleScreen s) {
		this.screen = s;
	}

	public void stop() {
		this.running = false;
	}

	public boolean isRunning() {
		return running;
	}
}
