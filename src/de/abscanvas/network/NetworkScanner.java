package de.abscanvas.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import de.abscanvas.math.ByteUtilities;
import de.abscanvas.ui.gui.elements.GUIProgressbar;
import de.abscanvas.ui.gui.elements.GUIServerList;

public class NetworkScanner implements Runnable {
	private byte[] sendData;

	private GUIServerList serverlist;
	private GUIProgressbar progressbar;

	private DatagramSocket socket;
	private Thread listenerThread;
	private boolean running = true;

	private long startTime = 0;

	public NetworkScanner(GUIServerList sl, GUIProgressbar p, byte[] programIdentifier) {
		serverlist = sl;
		progressbar = p;
		if (progressbar == null) {
			progressbar = new GUIProgressbar(0, 0, 10, 10, sl.getOwner());
		}

		sendData = new byte[9];
		sendData[0] = NetworkConstants.ABSCANVAS_IDENTIFIER[0];
		sendData[1] = NetworkConstants.ABSCANVAS_IDENTIFIER[1];
		sendData[2] = programIdentifier[0];
		sendData[3] = programIdentifier[1];
		sendData[4] = ByteUtilities.ZERO;
		sendData[5] = ByteUtilities.short2Arr(NetworkConstants.CMD_SERVERINFORMATION_REQUEST)[0];
		sendData[6] = ByteUtilities.short2Arr(NetworkConstants.CMD_SERVERINFORMATION_REQUEST)[1];
		sendData[7] = ByteUtilities.short2Arr(NetworkConstants.CMD_BUFFER_END)[0];
		sendData[8] = ByteUtilities.short2Arr(NetworkConstants.CMD_BUFFER_END)[1];

		try {
			socket = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		byte[] rData = new byte[NetworkConstants.PACKAGE_SIZE];

		startTime = System.currentTimeMillis();

		while (running) {
			try {
				if (System.currentTimeMillis() - startTime > NetworkConstants.NETWORKSCANNER_TIME_OUT) {
					running = false;
				}

				DatagramPacket receivePacket = new DatagramPacket(rData, rData.length);

				socket.receive(receivePacket); // WAITING

				if (rData[0] == NetworkConstants.ABSCANVAS_IDENTIFIER[0] && rData[1] == NetworkConstants.ABSCANVAS_IDENTIFIER[1]) {
					@SuppressWarnings("unused")
					byte[] gameID = ByteUtilities.copy(rData, 2, 2);
					short cmd = ByteUtilities.arr2Short(rData, 4);

					if (cmd == NetworkConstants.CMD_SERVERINFORMATION_ANSWER) {
						boolean canConnectNewUser = ByteUtilities.arr2Boolean(rData, 6);
						int userCount = ByteUtilities.arr2Int(rData, 7);
						int maxUserCount = ByteUtilities.arr2Int(rData, 11);

						String serverName = ByteUtilities.extractString(rData, 15, NetworkConstants.SMALL_STRING_SIZE);
						String gameName = ByteUtilities.extractString(rData, 47, NetworkConstants.SMALL_STRING_SIZE);
						String versNmr = ByteUtilities.extractString(rData, 79, NetworkConstants.SMALL_STRING_SIZE);

						while (serverlist.isUpdating()) {
							Thread.yield();
						}
						serverlist.beginUpdate();

						serverlist.addServer(999, serverName, receivePacket.getAddress(), receivePacket.getPort(), gameName, versNmr, userCount, maxUserCount, canConnectNewUser);

						serverlist.endUpdate();
					}
				}
			} catch (SocketException e) {
				running = false;
			} catch (IOException e) {
				e.printStackTrace();
				running = false;
			}
		}
	}

	public void scan(String ipAddr) {
		InetAddress ip = null;
		try {
			ip = InetAddress.getByName(ipAddr);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		listenerThread = new Thread(this);
		listenerThread.start();
		
		serverlist.clear();

		progressbar.setMaximum(NetworkConstants.MAX_PORTSCAN_NUMBER - NetworkConstants.MIN_PORTSCAN_NUMBER);

		for (int i = NetworkConstants.MIN_PORTSCAN_NUMBER; i <= NetworkConstants.MAX_PORTSCAN_NUMBER; i++) {
			progressbar.setPosition(i - NetworkConstants.MIN_PORTSCAN_NUMBER);
			scan(ip, i);
		}
		
		progressbar.setPosition(0);
	}
	
	public void scan(String ipAddr1, String ipAddr2) {
		InetAddress ip1 = null;
		InetAddress ip2 = null;
		try {
			ip1 = InetAddress.getByName(ipAddr1);
			ip2 = InetAddress.getByName(ipAddr2);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		
		listenerThread = new Thread(this);
		listenerThread.start();
		
		serverlist.clear();

		int md = (NetworkConstants.MAX_PORTSCAN_NUMBER - NetworkConstants.MIN_PORTSCAN_NUMBER);
		
		progressbar.setMaximum(md * 2);

		for (int i = NetworkConstants.MIN_PORTSCAN_NUMBER; i <= NetworkConstants.MAX_PORTSCAN_NUMBER; i++) {
			progressbar.setPosition(i - NetworkConstants.MIN_PORTSCAN_NUMBER);
			scan(ip1, i);
		}
		
		for (int i = NetworkConstants.MIN_PORTSCAN_NUMBER; i <= NetworkConstants.MAX_PORTSCAN_NUMBER; i++) {
			progressbar.setPosition(md + (i - NetworkConstants.MIN_PORTSCAN_NUMBER));
			scan(ip2, i);
		}
		
		progressbar.setPosition(0);
	}

	private void scan(InetAddress ip, int port) {
		try {
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ip, port);
			socket.send(sendPacket);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			Thread.sleep(NetworkConstants.SEND_SLEEP * 3);
		} catch (InterruptedException e) {
			//this is so senseles - fucking try-catch - forcing -.-
		}
	}
}
