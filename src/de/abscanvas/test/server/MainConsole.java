package de.abscanvas.test.server;

import java.io.Console;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.abscanvas.network.ConsoleListener;

public class MainConsole implements ConsoleListener {
	TestScreen screen;

	public MainConsole() {
		screen = new TestScreen(this, true);
		screen.setConsoleListener(this);
		screen.start();

		Console console = System.console();
		String k = "";
		if (console != null) {
			while (!k.toLowerCase().equals("stop")) {
				k = console.readLine();
			}
			output("STOP SERVER");
			System.exit(0);
		}
	}

	@Override
	public void output(String s) {
		s = s.trim();
		if (!s.isEmpty()) {
			s = "[" + (new SimpleDateFormat("HH:mm:ss")).format(new Date()) + "] " + s;
		}
		
		System.out.println(s);
	}
}
