package de.abscanvas.additional.swinginterface;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import de.abscanvas.ConsoleScreen;
import de.abscanvas.level.ServerLevel;
import de.abscanvas.network.NetworkUser;

public abstract class ServerMapWindow extends StandardServerWindow {
	private static final long serialVersionUID = 462773195990172758L;
	
	protected static final int WIDTH = StandardServerWindow.WIDTH + StandardServerWindow.HEIGHT;
	protected static final int HEIGHT = StandardServerWindow.HEIGHT + 20;
	
	protected JPanel oldPanel;
	
	protected JPanel super_rightPanel;
	protected JPanel mapControlPanel;
	protected MapPanel mapPanel;
	protected JButton[] mapControlButton;
	
	public ServerMapWindow(String title) {
		super(title);
	}
	
	@Override
	protected void createGUI(String title) {
		setTitle(title);
		setLayout(null);
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setMinimumSize(new Dimension(WIDTH, HEIGHT));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);

		setLocationRelativeTo(null);

		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				updateUserList();

				if (userList_selectedUser_UID >= 0) {
					NetworkUser selU = ((ServerLevel) screen.getLevel()).getUser(userList_selectedUser_UID);
					if (selU != null) {
						unfo_ping.setText(selU.getPing() + "");
						unfo_packLoss.setText(selU.getPackageLossCount() + "");
						for (JButton b : userCommandButtons) {
							b.setEnabled(true);
						}
					} else {
						userList_selectedUser_UID = -1;
					}
				} else {
					unfo_ping.setText("");
					unfo_packLoss.setText("");
					for (JButton b : userCommandButtons) {
						b.setEnabled(false);
					}
				}
			}
		}, 5000, 1000);

		oldPanel = new JPanel(new GridLayout(1, 2, 10, 0));
		oldPanel.setBounds(0, 0, StandardServerWindow.WIDTH, StandardServerWindow.HEIGHT);
		super_rightPanel = new JPanel(new BorderLayout(20,20));
		super_rightPanel.setBounds(StandardServerWindow.WIDTH, 0, StandardServerWindow.HEIGHT, StandardServerWindow.HEIGHT - 10);
		
		add(oldPanel);
		add(super_rightPanel);
		
		leftPanel = new JPanel(new FlowLayout());
		rightPanel = new JPanel(new FlowLayout());

		createLeftPanel();
		createRightPanel();
		createSuperRightPanel();

		oldPanel.add(leftPanel);
		oldPanel.add(rightPanel);

		pack();
	}
	
	@Override
	public void setConsoleScreen(ConsoleScreen s) {
		super.setConsoleScreen(s);
		mapPanel.setScreen(s);
	}
	
	protected void createSuperRightPanel() {
		mapControlPanel = new JPanel(new FlowLayout());
		
		mapPanel = new MapPanel();
		
		super_rightPanel.add(mapControlPanel, BorderLayout.PAGE_START);
		super_rightPanel.add(new JPanel(), BorderLayout.LINE_START);
		super_rightPanel.add(new JPanel(), BorderLayout.LINE_END);
		super_rightPanel.add(new JPanel(), BorderLayout.PAGE_END);
		super_rightPanel.add(mapPanel, BorderLayout.CENTER);
		
		mapControlButton = new JButton[5];
		
		for (int i = 0; i < mapControlButton.length; i++) {
			mapControlButton[i] = new JButton("Controll "+(i+1));
			mapControlPanel.add(mapControlButton[i]);
		}
	}
}
