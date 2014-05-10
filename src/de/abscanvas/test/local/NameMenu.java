package de.abscanvas.test.local;

import java.awt.Color;

import de.abscanvas.Screen;
import de.abscanvas.input.MouseButtons;
import de.abscanvas.surface.AbsColor;
import de.abscanvas.ui.gui.GUIMenu;
import de.abscanvas.ui.gui.elements.GUIButton;
import de.abscanvas.ui.gui.elements.GUIDynamicButton;
import de.abscanvas.ui.gui.elements.GUIEdit;
import de.abscanvas.ui.gui.elements.GUIListBox;
import de.abscanvas.ui.listener.ButtonListener;

public class NameMenu extends GUIMenu implements ButtonListener{
	private final int BTN_START = 1;
	private final int BTN_CANCEL = 2;
	private final int BTN_SKIP = 4;
	private final int BTN_DISABLE = 3;
	
	public GUIDynamicButton disBtn;

	public NameMenu(Screen owner) {
		super(owner);
		create();
	}
	
	private void create() {
		setImage(GameArt.titles[0]);
		
		GUIButton b;
		GUIEdit e;
		GUIListBox l;
		
		b = new GUIButton(getOwner().getScreenWidth()/2 - 64, getOwner().getScreenHeight()/2 + 00, 128, 24, GameArt.buttons[0], BTN_START, this);
		addElement(b);
		b.addListener(this);
		
		disBtn = new GUIDynamicButton(getOwner().getScreenWidth()/2 - 200, getOwner().getScreenHeight()/2 + 00, GameArt.btn_dyn2, "Click ME", BTN_DISABLE, this);
		disBtn.setFontColor(AbsColor.WHITE);
		addElement(disBtn);
		disBtn.addListener(this);
		
		b = new GUIButton(getOwner().getScreenWidth()/2 - 64, getOwner().getScreenHeight()/2 + 80, 128, 24, GameArt.buttons[2], BTN_CANCEL, this);
		addElement(b);
		b.addListener(this);
		
		b = new GUIButton(getOwner().getScreenWidth()/2 - 64, getOwner().getScreenHeight()/2 + 40, 128, 24, GameArt.buttons[1], BTN_SKIP, this);
		addElement(b);
		b.addListener(this);
		
		e = new GUIEdit(getOwner().getScreenWidth()/2 - 64, getOwner().getScreenHeight()/2 - 40, 128, 24, getOwner().getKeys(), this);
		addElement(e);
		e.setText("Playername");
		e.drawBorders(true);
		e.setClearOnFirstClick();
		
		l = new GUIListBox(getOwner().getScreenWidth()/2 +96, getOwner().getScreenHeight()/2 - 40, 128, 192, this);
		addElement(l);
		l.beginUpdate();
		for (int i = 0; i < 4; i++) {
			l.addLine("Hello", "Moto");
			l.addLine("Number", "481516");
			l.addLine("Third", "Line");
			l.addLine("Master", "Deceptor");
			l.addLine("Not a", "Line");
			l.addLine("Scuba", "Diver");
		}
		l.endUpdate();
		l.setColorScheme(0, Color.WHITE.getRGB(), Color.BLACK.getRGB(), 0, Color.YELLOW.getRGB());
	
	}

	@Override
	public void buttonPressed(MouseButtons mouse, int id) {
		if (id == BTN_START) {
			getOwner().clearMenus();
			((GameScreen) getOwner()).createLevel();
		} else if (id == BTN_CANCEL) {
			getOwner().popMenu();
		} else if (id == BTN_DISABLE) {
			disBtn.setEnabled(false);
		} else if (id == BTN_SKIP) {
			getOwner().popMenu();
			getOwner().addMenu(new NameMenu(getOwner()));
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
