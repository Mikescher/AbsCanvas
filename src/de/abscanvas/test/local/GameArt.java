package de.abscanvas.test.local;

import java.awt.Color;
import java.awt.Font;

import de.abscanvas.input.Art;
import de.abscanvas.math.MRect;
import de.abscanvas.surface.AbsColor;
import de.abscanvas.surface.Bitmap;

public class GameArt extends Art {
	public static Bitmap[][] allFloorTiles = cut("floortiles.png", 7, 5);
	public static int[][] allFloorTileColors = getColors(allFloorTiles);
	
	public static Bitmap floorTile = allFloorTiles[5][0];
	public static int floorTileColor = allFloorTileColors[5][0];
	
	public static Bitmap hillTile = allFloorTiles[6][0];
	public static int hillTileColor = allFloorTileColors[6][0];
	
	public static Bitmap rockTile = allFloorTiles[1][0];
	public static int rockTileColor = allFloorTileColors[1][0];
	
//	public static Bitmap[][] lordLard = cutReverse("/lord_lard_sheet.png", 6, 16);
	public static Bitmap[][] herrSpeck = cutReverse("herr_von_speck_sheet.png", 6, 16);

	public static Bitmap[][] font = cut("gamfont.png", 30, 2);
	
	public static Bitmap panel = load("panel.png");
	
	public static Bitmap[] button = cutX("spawner.png", 4);
	
	public static Bitmap[] dust = cutX("fx_dust2_12.png", 10);
	
	public static Bitmap[] titles = new Bitmap[2];
	
	public static Bitmap[][] allButtons = cut("buttons.png", 2, 8);
	
	public static Bitmap[][] buttons = new Bitmap[5][4];
	
	public static Bitmap[] img_load = cut1D("img_load.png", 15, 12);
	
	public static Bitmap[] btn_dyn = cutY("btn_underlay.png", 4);
	public static Bitmap[] btn_dyn2 = cutY("btn_underlay2.png", 4);
	
	public static void init() {
		buttons[0][0] = allButtons[0][0];
		buttons[0][1] = allButtons[0][1];
		buttons[0][2] = allButtons[0][0];
		buttons[0][3] = allButtons[0][0];
		
		buttons[1][0] = allButtons[0][2];
		buttons[1][1] = allButtons[0][3];
		buttons[1][2] = allButtons[0][2];
		buttons[1][3] = allButtons[0][2];
		
		buttons[2][0] = allButtons[0][4];
		buttons[2][1] = allButtons[0][5];
		buttons[2][2] = allButtons[0][4];
		buttons[2][3] = allButtons[0][4];
		
		buttons[3][0] = allButtons[1][0];
		buttons[3][1] = allButtons[1][1];
		buttons[3][2] = allButtons[1][0];
		buttons[3][3] = allButtons[1][0];
		
		buttons[4][0] = allButtons[1][2];
		buttons[4][1] = allButtons[1][3];
		buttons[4][2] = allButtons[1][2];
		buttons[4][3] = allButtons[1][2];
		
		titles[0] = load("/title.png");
		titles[1] = load("/title2.png");
		
		titles[0].drawString("Bitch Please!", new Font("Arial", Font.BOLD, 28), Color.WHITE.getRGB(), 5, 5);
		titles[0].drawStringCentered(12 + "", new Font("Arial", 0, 156), new MRect(0, 0, titles[0].getWidth(), titles[0].getHeight()), AbsColor.WHITE);
	}

}
