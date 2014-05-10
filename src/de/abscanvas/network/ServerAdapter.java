package de.abscanvas.network;

import java.io.IOException;
import java.net.BindException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import de.abscanvas.math.ByteUtilities;

public class ServerAdapter extends NetworkAdapter implements Runnable {
	NetworkListener listener;

	private int port;
	private Thread listenerThread;
	private boolean running = true;
	private DatagramSocket serverSocket;

	private double packOutPerSec = 0;
	private long packOutPerSec_startTime = System.currentTimeMillis();
	private int packOutPerSec_count = 0;

	private double packInPerSec = 0;
	private long packInPerSec_startTime = System.currentTimeMillis();
	private int packInPerSec_count = 0;

	public ServerAdapter(int port, NetworkListener listener, byte[] identifier) {
		super(identifier);
		this.port = port;
		this.listener = listener;
	}

	public boolean start() {
		try {
			serverSocket = new DatagramSocket(port);

			listenerThread = new Thread(this);
			listenerThread.start();
		} catch (BindException e) {
			return false;
		} catch (SocketException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public void stop() {
		running = false;
		serverSocket.close();
	}

	@Override
	public void run() {
		byte[] rData = new byte[NetworkConstants.PACKAGE_SIZE];

		while (running) {
			try {
				DatagramPacket receivePacket = new DatagramPacket(rData, rData.length);

				serverSocket.receive(receivePacket); // WAITING

				packInPerSec_count++;
				if ((System.currentTimeMillis() - packInPerSec_startTime) > 1500) { // 10s
					packInPerSec = packInPerSec_count / ((System.currentTimeMillis() - packInPerSec_startTime) / 1000);
					packInPerSec_count = 0;
					packInPerSec_startTime = System.currentTimeMillis();
				}

				(new ServerDecoder(receivePacket, rData, listener, this)).start();
			} catch (SocketException e) {
				running = false;
			} catch (IOException e) {
				e.printStackTrace();
				running = false;
			}
		}
		serverSocket.close();
	}

	public synchronized void send(short command, NetworkUser u) {
		send(command, new byte[0], u);
	}

	public synchronized void send(short command, byte[] d, NetworkUser u) {
		byte[] dat = new byte[d.length + 2];
		ByteUtilities.insert(dat, ByteUtilities.short2Arr(command), 0);
		ByteUtilities.insert(dat, d, 2);

		if ((u.getBuffer().getByteLength() + dat.length + 2) > NetworkConstants.PACKAGE_SIZE) {
			sendBuffer(u); // Buffer voll .... senden
		}

		u.getBuffer().add(dat);
	}

	public synchronized void sendBuffer(NetworkUser u) {
		sendNow(u.getBuffer(), u);
		u.getBuffer().reset();
	}

	private synchronized void sendNow(CommandBuffer buff, NetworkUser u) {
		if (buff.isFilled()) {
			sendNow(buff.getFullData(u.getNewPackageID()), u);
		}
	}

	public synchronized void sendNow(byte[] sendData, NetworkUser u) {
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, u.getIP(), u.getPort());
		try {
			serverSocket.send(sendPacket);

			packOutPerSec_count++;
			if ((System.currentTimeMillis() - packOutPerSec_startTime) > 1500) { // 10s
				packOutPerSec = packOutPerSec_count / ((System.currentTimeMillis() - packOutPerSec_startTime) / 1000);
				packOutPerSec_count = 0;
				packOutPerSec_startTime = System.currentTimeMillis();
			}

			Thread.sleep(NetworkConstants.SEND_SLEEP);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void sendUnconnected(byte[] sendData, InetAddress ip, int port) {
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ip, port);
	
		try {
			serverSocket.send(sendPacket);

			Thread.sleep(NetworkConstants.SEND_SLEEP);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public double getPackageSendPerSec() {
		return packOutPerSec;
	}

	public double getPackageRecievedPerSec() {
		return packInPerSec;
	}
}
