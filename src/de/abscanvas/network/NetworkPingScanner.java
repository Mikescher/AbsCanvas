package de.abscanvas.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import de.abscanvas.math.ByteUtilities;
import de.abscanvas.ui.gui.elements.GUIServerList;
import de.abscanvas.ui.gui.elements.GUIServerList.ServerLine;

public class NetworkPingScanner implements Runnable {
	private final static int PING_GAPS = 1000; // ms

	private GUIServerList serverlist;

	private long lastPing = 1;

	private byte[] sendData;
	private DatagramSocket socket;
	private boolean running = true;

	public NetworkPingScanner(GUIServerList slist, byte[] programIdentifier) {
		serverlist = slist;

		try {
			socket = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		}

		sendData = new byte[9];
		sendData[0] = NetworkConstants.ABSCANVAS_IDENTIFIER[0];
		sendData[1] = NetworkConstants.ABSCANVAS_IDENTIFIER[1];
		sendData[2] = programIdentifier[0];
		sendData[3] = programIdentifier[1];
		sendData[4] = ByteUtilities.ZERO;
		sendData[5] = ByteUtilities.short2Arr(NetworkConstants.CMD_UNCONNECTED_PING_REQUEST)[0];
		sendData[6] = ByteUtilities.short2Arr(NetworkConstants.CMD_UNCONNECTED_PING_REQUEST)[1];
		sendData[7] = ByteUtilities.short2Arr(NetworkConstants.CMD_BUFFER_END)[0];
		sendData[8] = ByteUtilities.short2Arr(NetworkConstants.CMD_BUFFER_END)[1];
		
		(new Thread(this)).start();
	}

	@Override
	public void run() {
		byte[] rData = new byte[NetworkConstants.PACKAGE_SIZE];
		while (running) {
			try {
				DatagramPacket receivePacket = new DatagramPacket(rData, rData.length);

				socket.receive(receivePacket); // WAITING

				if (rData[0] == NetworkConstants.ABSCANVAS_IDENTIFIER[0] && rData[1] == NetworkConstants.ABSCANVAS_IDENTIFIER[1]) {
					@SuppressWarnings("unused")
					byte[] gameID = ByteUtilities.copy(rData, 2, 2);
					short cmd = ByteUtilities.arr2Short(rData, 4);

					if (cmd == NetworkConstants.CMD_UNCONNECTED_PING_ANSWER) {
						int sv = serverlist.findServerIndex(receivePacket.getAddress(), receivePacket.getPort());
						if (sv >= 0) {
							serverlist.setPing(sv, (int) (System.currentTimeMillis() - lastPing));
						}
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

	public void tick() {
		if (!running) {
			return;
		}

		if (System.currentTimeMillis() - lastPing > PING_GAPS) {
			lastPing = System.currentTimeMillis();
			for (int i = 0; i < serverlist.getServerCount(); i++) {
				ServerLine sl = serverlist.getServer().get(i);
				DatagramPacket sp = new DatagramPacket(sendData, sendData.length, sl.getIP(), sl.getPort());
				try {
					socket.send(sp);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void stop() {
		running = false;
	}
}
