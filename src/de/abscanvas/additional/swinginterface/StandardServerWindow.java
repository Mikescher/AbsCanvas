package de.abscanvas.additional.swinginterface;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import de.abscanvas.ConsoleScreen;
import de.abscanvas.level.Level;
import de.abscanvas.level.ServerLevel;
import de.abscanvas.network.ConsoleListener;
import de.abscanvas.network.NetworkUser;

public abstract class StandardServerWindow extends JFrame implements ConsoleListener {
	protected static final long serialVersionUID = -718994130512593096L;

	protected static final int WIDTH = 800;
	protected static final int HEIGHT = 540;

	protected ConsoleScreen screen = null;

	protected Timer timer = new Timer();

	protected JPanel leftPanel;
	protected JPanel rightPanel;

	protected JTextArea serverlog;
	protected JScrollPane serverlogPanel;
	protected JPanel inputPanel;
	protected JPanel chatPanel;
	protected JTextField input;
	protected JButton inputExecute;
	protected JTextField chat_input;
	protected JButton chat_send;

	protected JPanel infoPanel;
	protected JTextField nfo_connUser;
	protected JTextField nfo_ticktime;
	protected JTextField nfo_packCount;
	protected JTextField nfo_packOutPerSec;
	protected JTextField nfo_packInPerSec;
	protected JPanel executePanel;
	protected JButton[] executeButtons = new JButton[6];
	protected long userList_selectedUser_UID = -1;
	protected JTable userlist;
	protected JScrollPane userlistPanel;
	protected JPanel userInfoPanel;
	protected JTextField unfo_ping;
	protected JTextField unfo_packLoss;
	protected DefaultTableModel userListModel;
	protected JPanel userCommandPanel;
	protected JButton[] userCommandButtons = new JButton[4];
	
	public StandardServerWindow(String title) {
		super();
		createGUI(title);
		setVisible(true);
	}

	protected void createGUI(String title) {
		setTitle(title);
		setLayout(new GridLayout(1, 2, 10, 0));
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

		leftPanel = new JPanel(new FlowLayout());
		rightPanel = new JPanel(new FlowLayout());

		createLeftPanel();
		createRightPanel();

		add(leftPanel);
		add(rightPanel);

		pack();
	}
	
	protected void updateUserList() {
		int sRow = userlist.getSelectedRow();
		if (sRow >= 0) {
			userList_selectedUser_UID = (Long) userListModel.getValueAt(sRow, 0);
		} else {
			userList_selectedUser_UID = -1;
		}
		if (screen != null) {
			if (screen.getLevel() != null) {
				nfo_packOutPerSec.setText(((ServerLevel) screen.getLevel()).getPackageRecievedPerSec() + "");
				nfo_packOutPerSec.setText(((ServerLevel) screen.getLevel()).getPackageSendPerSec() + "");
			}
		}
		
	}

	protected void createRightPanel() {
		createInfoPanel();
		createExecutePanel();
		createUserList();
		createUserInfoPanel();
		createUserCommandPanel();
	}

	protected void createUserCommandPanel() {
		userCommandPanel = new JPanel(new GridLayout(0, 4, 6, 6));
		userCommandPanel.setPreferredSize(new Dimension(WIDTH / 2 - 20, 23));

		userCommandButtons[0] = new JButton("cmd 1");
		userCommandPanel.add(userCommandButtons[0]);
		userCommandButtons[0].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (userlist.getSelectedRow() >= 0) {
					onUserCommandButton1Clicked(((ServerLevel) screen.getLevel()).getUser(userList_selectedUser_UID));
				}
			}
		});

		userCommandButtons[1] = new JButton("cmd 2");
		userCommandPanel.add(userCommandButtons[1]);
		userCommandButtons[1].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (userlist.getSelectedRow() >= 0) {
					onUserCommandButton2Clicked(((ServerLevel) screen.getLevel()).getUser(userList_selectedUser_UID));
				}
			}
		});

		userCommandButtons[2] = new JButton("cmd 3");
		userCommandPanel.add(userCommandButtons[2]);
		userCommandButtons[2].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (userlist.getSelectedRow() >= 0) {
					onUserCommandButton3Clicked(((ServerLevel) screen.getLevel()).getUser(userList_selectedUser_UID));
				}
			}
		});

		userCommandButtons[3] = new JButton("cmd 4");
		userCommandPanel.add(userCommandButtons[3]);
		userCommandButtons[3].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (userlist.getSelectedRow() >= 0) {
					onUserCommandButton4Clicked(((ServerLevel) screen.getLevel()).getUser(userList_selectedUser_UID));
				}
			}
		});
		rightPanel.add(userCommandPanel);
	}

	protected void createUserList() {
		userListModel = new DefaultTableModel();
		userlist = new JTable(userListModel);
		ListSelectionModel selectionModel = new DefaultListSelectionModel();
		selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		userlist.setSelectionModel(selectionModel);
		userListModel.addColumn("ID");
		userListModel.addColumn("Name");
		userListModel.addColumn("IP");
		userListModel.addColumn("Port");

		userlistPanel = new JScrollPane(userlist);
		userlistPanel.setPreferredSize(new Dimension(WIDTH / 2 - 20, HEIGHT / 3));

		userlist.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					userList_selectedUser_UID = e.getFirstIndex();
				}
			}
		});

		rightPanel.add(userlistPanel);
	}

	protected void createUserInfoPanel() {
		userInfoPanel = new JPanel(new GridLayout(0, 4, 6, 3));
		userInfoPanel.setPreferredSize(new Dimension(WIDTH / 2 - 20, 1 * 23));

		rightPanel.add(userInfoPanel);

		userInfoPanel.add(new JLabel("Ping:"));
		unfo_ping = new JTextField("0");
		unfo_ping.setEditable(false);
		userInfoPanel.add(unfo_ping);

		userInfoPanel.add(new JLabel("Package Loss:"));
		unfo_packLoss = new JTextField("0");
		unfo_packLoss.setEditable(false);
		userInfoPanel.add(unfo_packLoss);
	}

	protected void createInfoPanel() {
		infoPanel = new JPanel(new GridLayout(0, 2, 6, 3));
		infoPanel.setPreferredSize(new Dimension(WIDTH / 2 - 20, 5 * 23));

		rightPanel.add(infoPanel);

		infoPanel.add(new JLabel("Connected User:"));
		nfo_connUser = new JTextField("0");
		nfo_connUser.setEditable(false);
		infoPanel.add(nfo_connUser);

		infoPanel.add(new JLabel("Time per Render:"));
		nfo_ticktime = new JTextField("0");
		nfo_ticktime.setEditable(false);
		infoPanel.add(nfo_ticktime);

		infoPanel.add(new JLabel("Package Count:"));
		nfo_packCount = new JTextField("0");
		nfo_packCount.setEditable(false);
		infoPanel.add(nfo_packCount);

		infoPanel.add(new JLabel("Packages Send per Second:"));
		nfo_packOutPerSec = new JTextField("0");
		nfo_packOutPerSec.setEditable(false);
		infoPanel.add(nfo_packOutPerSec);

		infoPanel.add(new JLabel("Packages Recieved per Second:"));
		nfo_packInPerSec = new JTextField("0");
		nfo_packInPerSec.setEditable(false);
		infoPanel.add(nfo_packInPerSec);
	}

	protected void createExecutePanel() {
		executePanel = new JPanel(new GridLayout(0, 3, 6, 6));
		executePanel.setPreferredSize(new Dimension(WIDTH / 2 - 20, 3 * 23));
		rightPanel.add(executePanel);

		executeButtons[0] = new JButton("Command 1");
		executePanel.add(executeButtons[0]);
		executeButtons[0].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				onExecuteButton1Clicked();
			}
		});

		executeButtons[1] = new JButton("Command 2");
		executePanel.add(executeButtons[1]);
		executeButtons[1].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				onExecuteButton2Clicked();
			}
		});

		executeButtons[2] = new JButton("Command 3");
		executePanel.add(executeButtons[2]);
		executeButtons[2].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				onExecuteButton3Clicked();
			}
		});

		executeButtons[3] = new JButton("Command 4");
		executePanel.add(executeButtons[3]);
		executeButtons[3].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				onExecuteButton4Clicked();
			}
		});

		executeButtons[4] = new JButton("Command 5");
		executePanel.add(executeButtons[4]);
		executeButtons[4].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				onExecuteButton5Clicked();
			}
		});

		executeButtons[5] = new JButton("Command 6");
		executePanel.add(executeButtons[5]);
		executeButtons[5].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				onExecuteButton6Clicked();
			}
		});
	}

	protected void createLeftPanel() {
		serverlog = new JTextArea();
		serverlog.setLineWrap(false);
		serverlog.setEditable(false);
		serverlog.setBackground(Color.BLACK);
		serverlog.setForeground(Color.GREEN);

		serverlogPanel = new JScrollPane(serverlog);
		serverlogPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		serverlogPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		serverlogPanel.setPreferredSize(new Dimension(WIDTH / 2 - 20, HEIGHT - 95));

		leftPanel.add(serverlogPanel);

		inputPanel = new JPanel(new FlowLayout());
		chatPanel = new JPanel(new FlowLayout());

		leftPanel.add(inputPanel);
		leftPanel.add(chatPanel);

		input = new JTextField();
		input.setPreferredSize(new Dimension(WIDTH / 3 - 10, 23));
		inputExecute = new JButton("Execute");
		inputExecute.setPreferredSize(new Dimension(WIDTH / 6 - 15, 23));
		inputExecute.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				onInputFieldExecuteClicked();
			}
		});

		chat_input = new JTextField();
		chat_input.setPreferredSize(new Dimension(WIDTH / 3 - 10, 23));
		chat_send = new JButton("Send");
		chat_send.setPreferredSize(new Dimension(WIDTH / 6 - 15, 23));
		chat_send.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				((ServerLevel)screen.getLevel()).sendServerChatMessage(chat_input.getText());
				chat_input.setText("");
			}
		});
		
		inputPanel.add(input);
		inputPanel.add(inputExecute);
		
		chatPanel.add(chat_input);
		chatPanel.add(chat_send);
	}

	@Override
	public void output(String s) {
		s = s.trim();
		if (!s.isEmpty()) {
			s = "[" + (new SimpleDateFormat("HH:mm:ss")).format(new Date()) + "] " + s;
		}
		if (serverlog.getText().isEmpty()) {
			serverlog.append(s);
		} else {
			serverlog.append("\n" + s);
		}

		serverlog.setCaretPosition(serverlog.getDocument().getLength());
	}

	public String getInputFieldText() {
		return input.getText();
	}

	public void setExecuteButtonText(int btnID, String text) {
		executeButtons[btnID - 1].setText(text);
	}

	public void setUserCommandButtonText(int btnID, String text) {
		userCommandButtons[btnID - 1].setText(text);
	}

	public void setConsoleScreen(ConsoleScreen s) {
		screen = s;
		screen.setConsoleListener(this);
	}

	public ConsoleScreen getScreen() {
		return screen;
	}

	public Level getLevel() {
		return screen.getLevel();
	}

	public void addUser(NetworkUser u, int userSize) {
		userListModel.addRow(new Object[] { u.getUID(), u.getName(), u.getIP().getHostAddress(), u.getPort() + "" });
		nfo_connUser.setText(userSize + "");
	}

	public void resetUserList() {
		userListModel.setNumRows(0);
	}

	public abstract void onInputFieldExecuteClicked();

	public abstract void onUserCommandButton1Clicked(NetworkUser selUser);

	public abstract void onUserCommandButton2Clicked(NetworkUser selUser);

	public abstract void onUserCommandButton3Clicked(NetworkUser selUser);

	public abstract void onUserCommandButton4Clicked(NetworkUser selUser);

	public abstract void onExecuteButton1Clicked();

	public abstract void onExecuteButton2Clicked();

	public abstract void onExecuteButton3Clicked();

	public abstract void onExecuteButton4Clicked();

	public abstract void onExecuteButton5Clicked();

	public abstract void onExecuteButton6Clicked();
}
