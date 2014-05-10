package de.abscanvas;

import java.awt.Graphics;

import de.abscanvas.input.Keys;
import de.abscanvas.input.MouseButtons;
import de.abscanvas.input.Keys.Key;
import de.abscanvas.level.Level;
import de.abscanvas.math.MDPoint;
import de.abscanvas.math.MPoint;
import de.abscanvas.sound.SoundPlayer;
import de.abscanvas.surface.Bitmap;
import de.abscanvas.ui.gui.Font;
import de.abscanvas.ui.gui.GUIMenu;
import de.abscanvas.ui.gui.GUIStack;

public interface Screen {
	public static final String VERSION = "1.72";
	
	public void start();
	public void stop();

	public String getVersionString();
	public Object getOwner();
	
	public int getScreenWidth();
	public int getScreenHeight();
	public int getTotalScreenWidth();
	public int getTotalScreenHeight();
	public void setScreenScale(double i);
	public double getScreenScale();
	
	public void setOffset(MPoint offset);
	public MPoint getOffset();
	
	public MPoint canvasToScreen(MDPoint pt);
	public MDPoint screenToCanvas(MPoint pt);
	
	public void onInit();
	public void onAfterTick();
	public void onStop();
	
	public void setFont(String letters, Bitmap[][] img);
	public Font getACFont();
	
	public void drawStringAlpha(String txt, int x, int y, int a);
	public void drawString(Bitmap s, String txt, int x, int y);
	public void drawString(String text, int x, int y);
	
	public int getFPS();
	public double MPF();
	public long getTickTime();
	public double getMillisecondsPerFrame();
	
	public void setLevel(Level l);
	public Level getLevel();
	
	public SoundPlayer getSoundPlayer();
	
	public MouseButtons getMouseButtons();
	public void forceMouseHidden();
	
	public void renderOnCustom(Graphics g);

	public void addToKeyMap(int keycode, Key key);
	public Keys getKeys();

	public void setMenu(GUIMenu menu);
	public void addMenu(GUIMenu menu);
	public void popMenu();
	public void clearMenus();
	public GUIStack getMenuStack();
}
// TODO UCDetector drüberlaufen lassen und schauen was es zu fixen gibt ...