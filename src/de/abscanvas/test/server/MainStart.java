package de.abscanvas.test.server;

public class MainStart {

	public static void main(String[] args) {
		boolean noGUI = false;

		for (String s : args) {
			if (s.equals("-nogui")) {
				noGUI = true;
			}
		}

		if (noGUI) {
			new MainConsole();
		} else {
			new MainWindow();
		}
		
	}
}
