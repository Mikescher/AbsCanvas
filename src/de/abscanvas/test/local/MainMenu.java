package de.abscanvas.test.local;

import java.awt.Font;

import de.abscanvas.Screen;
import de.abscanvas.input.MouseButtons;
import de.abscanvas.level.levelIO.LIODefinitions;
import de.abscanvas.level.levelIO.LIOMethod;
import de.abscanvas.level.levelIO.LIOParsingException;
import de.abscanvas.level.levelIO.LIOReadOnlyMethod;
import de.abscanvas.level.levelIO.LevelReader;
import de.abscanvas.level.levelIO.LevelReaderIterator;
import de.abscanvas.level.levelIO.LevelWriter;
import de.abscanvas.surface.Bitmap;
import de.abscanvas.ui.gui.GUIMenu;
import de.abscanvas.ui.gui.elements.GUIAnimatedImage;
import de.abscanvas.ui.gui.elements.GUIButton;
import de.abscanvas.ui.gui.elements.GUIDynamicButton;
import de.abscanvas.ui.listener.ButtonListener;

public class MainMenu extends GUIMenu implements ButtonListener {
	private final int BTN_START = 1;
	private final int BTN_HOST = 2;
	private final int BTN_JOIN = 3;
	private final int BTN_JOIN2 = 4;
	private final int BTN_GEN = 5;

	public MainMenu(Screen owner) {
		super(owner);
		create();
	}

	private void create() {
		setImage(GameArt.titles[0]);
		renderElementsOnTop();

		GUIButton b;
		GUIDynamicButton d;
		GUIAnimatedImage i;

		b = new GUIButton(getOwner().getScreenWidth() / 2 - 64, getOwner().getScreenHeight() / 2 - 40, 128, 24, GameArt.buttons[0], BTN_START, this);
		addElement(b);
		b.addListener(this);
		b = new GUIButton(getOwner().getScreenWidth() / 2 - 64, getOwner().getScreenHeight() / 2 - 00, 128, 24, GameArt.buttons[1], BTN_HOST, this);
		b.addListener(this);
		addElement(b);
		b = new GUIButton(getOwner().getScreenWidth() / 2 - 64, getOwner().getScreenHeight() / 2 + 40, 128, 24, GameArt.buttons[4], BTN_JOIN, this);
		b.addListener(this);
		addElement(b);
		b = new GUIButton(getOwner().getScreenWidth() / 2 - 64, getOwner().getScreenHeight() / 2 + 80, 128, 24, GameArt.buttons[3], BTN_JOIN2, this);
		b.addListener(this);
		addElement(b);
		d = new GUIDynamicButton(getOwner().getScreenWidth() / 2 - 64, getOwner().getScreenHeight() / 2 + 115, GameArt.btn_dyn, "Gen LVL File", BTN_GEN, this);
		d.addListener(this);
		d.setFontColor(-1);
		d.setFont(new Font("Arial bold", 0, 28));
		addElement(d);
		Bitmap[][] btb = new Bitmap[1][GameArt.img_load.length];
		btb[0] = GameArt.img_load;
		i = new GUIAnimatedImage(0, getOwner().getScreenHeight() - 96, btb, 1000, this);
		addElement(i);

		for (int ii = 0; ii < 20; ii++) {
			addEntity(new MenuWalker(Math.random() * (getOwner().getScreenWidth() - 40) + 20, Math.random() * (getOwner().getScreenHeight() - 40) + 20));
		}

	}

	@Override
	public void buttonPressed(MouseButtons mouse, int id) {
		if (id == BTN_START) {
			getOwner().addMenu(new NameMenu(getOwner()));
		} else if (id == BTN_HOST) {
			getOwner().setScreenScale(3 - getOwner().getScreenScale());
		} else if (id == BTN_JOIN) {
			for (int i = 0; i < 100000000; i++) {
				Math.random();
			}
		} else if (id == BTN_JOIN2) {
			getOwner().addMenu(new ServerMenu(getOwner()));
		} else if (id == BTN_GEN) {
			createLevelFile();
		}
	}

	private void createLevelFile() {
		// ----------------------------------------------------------------------------------------------------------------------
		// ---------------------------------------------------     WRITER     ---------------------------------------------------
		// ----------------------------------------------------------------------------------------------------------------------
		
		LevelWriter w = new LevelWriter();

		w.setProgrammName("absCanv Test");

		w.addMethodDefinition("addSomething", 1);
		w.addMethodDefinition("setPlayerCount", 2);

		w.addConstantDefinition("PLAYER_NEUTRAL", -1);
		w.addConstantDefinition("PLAYER_HUMAN", 0);
		w.addConstantDefinition("PLAYER_KI_1", 1);
		w.addConstantDefinition("PLAYER_KI_2", 2);
		w.addConstantDefinition("PLAYER_KI_3", 3);

		w.addMethod(new LIOMethod(2).addIntegerParameter(2, false));

		w.addMethod(new LIOMethod(1).addIntegerParameter(352).addIntegerParameter(512).addIntegerParameter(1));

		w.addMethod(new LIOMethod(1).addIntegerParameter(1056).addIntegerParameter(512).addIntegerParameter(1));

//			try {
//				TextFileUtils.writeTextFile("C:/generated-Level-File.txt", w.getFileString());
//			} catch (IOException e) {}
		
		String unparsed = w.getFileString();
		
		System.out.println(unparsed);
		
		// ----------------------------------------------------------------------------------------------------------------------
		// ---------------------------------------------------     READER     ---------------------------------------------------
		// ----------------------------------------------------------------------------------------------------------------------
		
		LevelReader r = new LevelReader();
		
		LevelReaderIterator it;
		try {
			it = r.parse(unparsed, "absCanv Test");
		} catch (LIOParsingException e) {
			e.printStackTrace();
			return;
		}
		
		while(it.hasMoreElements()) {
			LIOReadOnlyMethod rom = it.nextElement();
			
			System.out.print(rom.getMethodname() + " -> ");
			
			for (int i = 0; i < rom.getParameterCount(); i++) {
				switch(rom.getParameterType(i)) {
				case LIODefinitions.TYPE_STRING:
					System.out.print(rom.getParameter_s(i) + " | ");
					break;
				case LIODefinitions.TYPE_INT:
					System.out.print(rom.getParameter_i(i) + " | ");
					break;
				case LIODefinitions.TYPE_DOUBLE:
					System.out.print(rom.getParameter_d(i) + " | ");
					break;
				case LIODefinitions.TYPE_BOOLEAN:
					System.out.print(rom.getParameter_b(i) + " | ");
					break;
				}
			}
			
			System.out.print("\n");
		}
	}

	@Override
	public void buttonMouseDown(MouseButtons mouse, int id) {
		//nocode
	}

	@Override
	public void buttonMouseEnter(MouseButtons mouse, int id) {
		//nocode
	}

	@Override
	public void buttonMouseLeave(MouseButtons mouse, int id) {
		//nocode
	}
}
