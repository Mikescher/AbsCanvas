package de.abscanvas.test.local;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

import de.abscanvas.DestkopScreen;

public class MainWindow extends JFrame {
	private static final long serialVersionUID = 1485217693822338944L;

	public static final int GAME_WIDTH = 512;
	public static final int GAME_HIGHT = GAME_WIDTH * 3 / 4;

	public static final String GAME_TITLE = "AbsCanvas   --TEST--";

	private DestkopScreen screen;

	public MainWindow() {
		initGUI();

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				screen.stop();
				System.exit(0);
			}
		});
	}

	private void initGUI() {
		JPanel panel = new JPanel(new BorderLayout());
		screen = new GameScreen(GAME_WIDTH, GAME_HIGHT, this);
		panel.add(screen, BorderLayout.CENTER);
		setContentPane(panel);
		setResizable(false);
		pack();
		setTitle(GAME_TITLE);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

		screen.start();
	}

	public static void main(String[] args) {
		new MainWindow();
	}
}
