package de.abscanvas.test.client;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import de.abscanvas.entity.network.ClientEntity;
import de.abscanvas.level.Level;
import de.abscanvas.surface.Bitmap;
import de.abscanvas.ui.hud.HUD;
import de.abscanvas.ui.hud.elements.HUDImage;

public class HUDMap extends HUDImage {

	int redCounter = 480;

	Bitmap b = new Bitmap(64, 64);
	BufferedImage i = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);

	public HUDMap(int x, int y, HUD owner) {
		super(x, y, new Bitmap(64, 64), owner);
		generateImage();
	}

	private void generateImage() {
		redCounter--;
		if (redCounter < 0) {
			redCounter = 480;

			rendermap(i.getGraphics(), getOwner().getOwner());

			b.setImage(i);
		}
		
		Bitmap n = b.copy();
		
		renderEnt(n, getOwner().getOwner());

		setImage(n);
	}

	@Override
	public void tick() {
		super.tick();
		generateImage();
	}
	
	public void renderEnt(Bitmap b, Level level) {
		int panelW = 64;
		int panelH = 64;

		int levelW = level.getWidth() * level.getTileWidth();
		int levelH = level.getHeight() * level.getTileWidth();
		
		for (int i = 0; i < level.getEntities().size(); i++) {
			ClientEntity e = (ClientEntity) level.getEntities().get(i);

			int xe = (int) ((e.getPosX() / levelW) * panelW);
			int ye = (int) ((e.getPosY() / levelH) * panelH);
			
			b.setPixel(xe, ye, e.getMinimapColor());
		}
	}

	public void rendermap(Graphics rendg, Level level) {
		final int TILE_SIZE = 16;

		int panelW = 64;
		int panelH = 64;

		int w = level.getWidth() * TILE_SIZE;
		int h = level.getHeight() * TILE_SIZE;
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
	}

}
