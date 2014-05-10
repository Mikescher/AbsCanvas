package de.abscanvas.test.server;

import de.abscanvas.additional.swinginterface.ServerMapWindow;
import de.abscanvas.level.ServerLevel;
import de.abscanvas.network.NetworkUser;

public class MainWindow extends ServerMapWindow {
	private static final long serialVersionUID = -718994130512593096L;

	TestScreen screen;
	
	public static final String VERSION = "1.0";
	
	public MainWindow() {
		super("AbsCanvas NetworkTest");

		screen = new TestScreen(this, false);
		setConsoleScreen(screen);
		screen.start();
		
		setUserCommandButtonText(1, "Kick");
		setUserCommandButtonText(2, "reset");
	}

	@Override
	public void onInputFieldExecuteClicked() {
		//nocode
	}

	@Override
	public void onUserCommandButton1Clicked(NetworkUser selUser) {
		((ServerLevel) getLevel()).kickUser(selUser);
	}

	@Override
	public void onUserCommandButton2Clicked(NetworkUser selUser) {
		((MyUser)selUser).player.setPos(screen.getLevel().getWidth() * 32 / 2d, screen.getLevel().getHeight() * 32 / 2d);
	}

	@Override
	public void onUserCommandButton3Clicked(NetworkUser selUser) {
		//nocode	
	}

	@Override
	public void onUserCommandButton4Clicked(NetworkUser selUser) {
		//nocode
	}

	@Override
	public void onExecuteButton1Clicked() {
		//nocode
	}

	@Override
	public void onExecuteButton2Clicked() {
		//nocode
	}

	@Override
	public void onExecuteButton3Clicked() {
		//nocode
	}

	@Override
	public void onExecuteButton4Clicked() {
		//nocode
	}

	@Override
	public void onExecuteButton5Clicked() {
		//nocode
	}

	@Override
	public void onExecuteButton6Clicked() {
		//nocode
	}
}
