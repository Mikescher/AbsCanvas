package de.abscanvas.test.server;

import de.abscanvas.ConsoleScreen;
import de.abscanvas.network.ConsoleListener;

public class TestScreen extends ConsoleScreen {
	private final static byte[] IDENTIFIER = {100, 23};
	
	public boolean isConsole = false;
	protected ConsoleListener owner;
	
	public TestScreen(ConsoleListener ownr, boolean isC) {
		super();
		owner = ownr;
		isConsole = isC;
	}
	
	@Override
	public void onAfterTick() {
		//nocode
	}

	@Override
	public void onInit() {
		setLevel(new TestLevel(this, 8764, IDENTIFIER));
	}

	@Override
	public void onStop() {
		//nocode
	}

}
