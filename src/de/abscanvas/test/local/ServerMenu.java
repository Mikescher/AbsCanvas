package de.abscanvas.test.local;

import java.net.InetAddress;
import java.net.UnknownHostException;

import de.abscanvas.Screen;
import de.abscanvas.input.MouseButtons;
import de.abscanvas.network.NetworkScanner;
import de.abscanvas.ui.gui.GUIMenu;
import de.abscanvas.ui.gui.elements.GUIButton;
import de.abscanvas.ui.gui.elements.GUIProgressbar;
import de.abscanvas.ui.gui.elements.GUIServerList;
import de.abscanvas.ui.listener.ButtonListener;

public class ServerMenu extends GUIMenu implements ButtonListener {
	private final int BTN_ADD = 1;
	private final int BTN_CANCEL = 2;

	GUIServerList s;
	GUIProgressbar p;
	GUIButton b1;
	GUIButton b2;

	public ServerMenu(Screen owner) {
		super(owner);
		create();
	}

	private void create() {
		setImage(GameArt.titles[1]);

		b2 = new GUIButton(getOwner().getScreenWidth() - 134, getOwner().getScreenHeight() - 30, 128, 24, GameArt.buttons[2], BTN_CANCEL, this);
		addElement(b2);
		b2.addListener(this);

		b1 = new GUIButton(6, getOwner().getScreenHeight() - 30, 128, 24, GameArt.buttons[3], BTN_ADD, this);
		addElement(b1);
		b1.addListener(this);

		p = new GUIProgressbar(140, getOwner().getScreenHeight() - 30, 230, 24, this);
		p.setMaximum(3000);
		addElement(p);

		s = new GUIServerList(32, 32, getOwner().getScreenWidth() - 64, 300, 0, "1.3.37", this);
		addElement(s);

		try {
			s.addServer(999, "Servername", InetAddress.getByName("127.0.0.1"), 81, "Gamename", "1.3.37", 2, 32, true);
		} catch (UnknownHostException e) {
			//nocode
		}

		addRandomServer();
		addRandomServer();
		addRandomServer();
		addRandomServer();
		addRandomServer();
		addRandomServer();
		addRandomServer();
		addRandomServer();
		addRandomServer();
		addRandomServer();
		addRandomServer();
	}

	@Override
	public void tick() {
		super.tick();
		p.stepIt();
	}

	@Override
	public void buttonPressed(MouseButtons mouse, int id) {
		if (id == BTN_CANCEL) {
			getOwner().popMenu();
		} else if (id == BTN_ADD) {
			s.clear();

			byte[] idf = { 100, 23 };
			NetworkScanner sn = new NetworkScanner(s, null, idf);

			long k = System.currentTimeMillis();
			sn.scan("localhost");
			sn.scan("mikescher.com");
			System.out.println("Dura: --> " + (System.currentTimeMillis() - k));
		}
	}

	private void addRandomServer() {
		try {
			s.addServer((int) (Math.random() * 400), "Servername " + (int) (Math.random() * 100), InetAddress.getByName((int) Math.rint(Math.random() * 256) + "." + (int) Math.rint(Math.random() * 256) + "."
					+ (int) Math.rint(Math.random() * 256) + "." + (int) Math.rint(Math.random() * 256)), (int) (Math.random() * 600), "Gamename" + (int) (Math.random() * 100) + ":" + (int) (Math.random() * 100),
					(int) Math.rint(Math.random() * 9) + "." + (int) Math.rint(Math.random() * 9) + "." + (int) Math.rint(Math.random() * 9) + "." + (int) Math.rint(Math.random() * 9), (int) Math.rint(Math
							.random() * 99), (int) Math.rint(Math.random() * 512), Math.random() > 0.5);
		} catch (UnknownHostException e) {
			//nocode
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
