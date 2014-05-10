package de.abscanvas.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import de.abscanvas.math.ByteUtilities;

public class ClientAdapter extends NetworkAdapter implements Runnable {
	private NetworkListener listener;

	private final InetAddress server_ip;
	private final int server_port;

	private DatagramSocket clientSocket;

	private Thread listenerThread;
	private boolean running = true;
	
	private byte packageCounter = Byte.MIN_VALUE; // for sending

	private byte lastPackageID = Byte.MAX_VALUE; // for recieving
	private long lastReliablePackageID = -1;

	private CommandBuffer buffer;
	private ReliabilityClientInterface reliabilityInterface;

	public ClientAdapter(InetAddress server_ip, int server_port, NetworkListener listener, byte[] identifier) {
		super(identifier);
		this.server_ip = server_ip;
		this.server_port = server_port;
		this.listener = listener;
		buffer = new CommandBuffer(getIdentifier());
		reliabilityInterface = new ReliabilityClientInterface(getIdentifier(), this);
	}

	public void start() {
		try {
			clientSocket = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		}

		listenerThread = new Thread(this);
		listenerThread.start();
	}

	public void stop() {
		running = false;
		clientSocket.close();
	}

	@Override
	public void run() {
		byte[] rData = new byte[NetworkConstants.PACKAGE_SIZE];

		while (running) {
			try {
				DatagramPacket receivePacket = new DatagramPacket(rData, rData.length);

				clientSocket.receive(receivePacket); // WAITING

				if (ByteUtilities.arr2Short(rData, 5) != NetworkConstants.CMD_RELIEABLE_PACKAGE_SEND) {
					byte packageID = rData[4];
					lastPackageID++;
					if (lastPackageID != packageID) {
						listener.onPackageLoss(ByteUtilities.getByteDifference(lastPackageID, packageID), null);
					}
					lastPackageID = packageID;
				}

				(new ClientDecoder(receivePacket, rData, listener, this)).start();
			} catch (SocketException e) {
				running = false;
			} catch (IOException e) {
				e.printStackTrace();
				running = false;
			}
		}
		clientSocket.close();
	}

	public void send(short command) {
		send(command, new byte[0]);
	}

	public void send(short command, byte[] d) {
		byte[] dat = new byte[d.length + 2];
		ByteUtilities.insert(dat, ByteUtilities.short2Arr(command), 0);
		ByteUtilities.insert(dat, d, 2);

		if ((getBuffer().getByteLength() + dat.length + 2) > NetworkConstants.PACKAGE_SIZE) {
			sendBuffer(); // Buffer voll .... senden
		}

		getBuffer().add(dat);
	}

	public void sendBuffer() {
		sendNow(getBuffer());
		getBuffer().reset();
	}

	private void sendNow(CommandBuffer buff) {
		if (buff.isFilled()) {
			sendNow(buff.getFullData(getNewPackageID()));
		}
	}
	
	public void sendNow(short command) {
		sendNow(command, new byte[0]);
	}

	public void sendNow(short command, byte[] d) {
		CommandBuffer b = new CommandBuffer(getIdentifier());

		byte[] dat = new byte[d.length + 2];
		ByteUtilities.insert(dat, ByteUtilities.short2Arr(command), 0);
		ByteUtilities.insert(dat, d, 2);

		b.add(dat);
		sendNow(b);
	}

	public void sendNow(byte[] sendData) {
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, server_ip, server_port);
		try {
			clientSocket.send(sendPacket);
			Thread.sleep(NetworkConstants.SEND_SLEEP);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public CommandBuffer getBuffer() {
		return buffer;
	}
	
	public byte getNewPackageID() {
		byte tmp = packageCounter;
		packageCounter++;
		return tmp;
	}
	
	public byte getActualPackageID() {
		return packageCounter;
	}

	/**
	 * @param ip 
	 * @param port  
	 */
	public boolean recieveReliablePackage(long relUID, InetAddress ip, int port) {
		boolean result = false;

		if (relUID > lastReliablePackageID) {
			lastReliablePackageID = relUID;
			result = true;
		}

		send(NetworkConstants.CMD_RELIEABLE_PACKAGE_CONFIRMED, ByteUtilities.long2Arr(relUID));

		return result;
	}

	public ReliabilityClientInterface getReliabilityInterface() {
		return reliabilityInterface;
	}
}
