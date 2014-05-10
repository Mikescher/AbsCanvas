package de.abscanvas.test.client;

import de.abscanvas.Screen;
import de.abscanvas.input.MouseButtons;
import de.abscanvas.network.NetworkPingScanner;
import de.abscanvas.network.NetworkScanner;
import de.abscanvas.ui.gui.GUIMenu;
import de.abscanvas.ui.gui.elements.GUIDynamicButton;
import de.abscanvas.ui.gui.elements.GUIEdit;
import de.abscanvas.ui.gui.elements.GUIProgressbar;
import de.abscanvas.ui.gui.elements.GUIServerList;
import de.abscanvas.ui.listener.ButtonListener;
import de.abscanvas.ui.listener.SelectionListener;

public class MainMenu extends GUIMenu implements ButtonListener, SelectionListener, Runnable {
	private final static int ID_SERVERLIST = 1;
	private final static int ID_JOIN = 2;
	private final static int ID_REFRESH = 3;

	public GUIServerList serverlist;
	public GUIProgressbar progressbar;
	
	public GUIEdit edname;

	public GUIDynamicButton join;
	public GUIDynamicButton refresh;
	
	public NetworkPingScanner pingscanner;

	public MainMenu(Screen owner) {
		super(owner);
		create();
	}

	private void create() {
		setImage(GameArt.titles[0]);

		serverlist = new GUIServerList(getOwner().getScreenWidth() / 32, 150, getOwner().getScreenWidth() * 11 / 16, getOwner().getScreenHeight() / 2, ID_SERVERLIST, MainWindow.VERSION, this);
		addElement(serverlist);
		serverlist.addListener(this);
		
		pingscanner = new NetworkPingScanner(serverlist, GameLevel.IDENTIFIER);

		progressbar = new GUIProgressbar(getOwner().getScreenWidth() / 32, 350, getOwner().getScreenWidth() - getOwner().getScreenWidth() / 16, 24, this);
		progressbar.setAnimationSpeed(5);
		addElement(progressbar);

		join = new GUIDynamicButton(375, 150, GameArt.buttons[5], "Join", ID_JOIN, this);
		join.addListener(this);
		join.setEnabled(false);
		addElement(join);

		refresh = new GUIDynamicButton(375, 180, GameArt.buttons[6], "Refresh", ID_REFRESH, this);
		refresh.addListener(this);
		addElement(refresh);
		
		edname = new GUIEdit(375, 210, 128, 24, getOwner().getKeys(), this);
		addElement(edname);
		edname.setText("Guest_" + Math.round(Math.random() * 1000));
	}
	
	@Override
	public void tick() {
		super.tick();
		pingscanner.tick();
	}

	@Override
	public void buttonPressed(MouseButtons mouse, int id) {
		if (id == ID_JOIN) {
			joinSelected();
		} else if (id == ID_REFRESH) {
			(new Thread(this)).start();
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

	@Override
	public void run() {
		refresh.setEnabled(false);
		
		NetworkScanner nscan = new NetworkScanner(serverlist, progressbar, GameLevel.IDENTIFIER);
		nscan.scan("localhost", "semtex.org");
		refresh.setEnabled(true);
	}

	@Override
	public void itemSelected(int ID, int itemIndex) {
		join.setEnabled(itemIndex >= 0);
	}

	@Override
	public void itemDoubleClicked(int ID, int itemIndex) {
		joinSelected();
	}

	private void joinSelected() {
		getOwner().clearMenus();
		getOwner().setLevel(new GameLevel(getOwner(), serverlist.getSelectedServer().getIP(), serverlist.getSelectedServer().getPort(), edname.getText()));
	}

}
