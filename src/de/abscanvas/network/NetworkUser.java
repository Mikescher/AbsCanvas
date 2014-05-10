package de.abscanvas.network;

import java.net.InetAddress;

import de.abscanvas.math.ByteUtilities;

public class NetworkUser {
	private static long uidCounter = 0; // MUSS BEI NULL ANFANGEN !!!
	
	private final InetAddress ip;
	private final int port;
	private final long uid;
	
	private String name  = "";
	
	private int ping = 1; // ping in ms
	private long ping_send_time = 0; // The time ping_request was send
	private boolean isPinging = false; // Ping request send ... but not answered
	
	private byte packageCounter = Byte.MIN_VALUE; // for sending
	
	private byte lastPackageID = Byte.MAX_VALUE;  // for recieving
	private long lastReliablePackageID = -1;
	private int packageLossCount = 0;
	
	private ServerAdapter adapter;
	
	protected CommandBuffer buffer;
	
	private ReliabilityServerInterface reliabilityInterface;
	
	public NetworkUser(String name, InetAddress ip, int port, ServerAdapter k) {
		this.ip = ip;
		this.port = port;
		this.name = name;
		this.uid = uidCounter;
		this.adapter = k;
		buffer = new CommandBuffer(adapter.getIdentifier());
		reliabilityInterface = new ReliabilityServerInterface(adapter.getIdentifier(), adapter, this);
		uidCounter++;
	}

	public InetAddress getIP() {
		return ip;
	}

	public int getPort() {
		return port;
	}

	public long getUID() {
		return uid;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public CommandBuffer getBuffer() {
		return buffer;
	}

	public void sendPing() {
		adapter.send(NetworkConstants.CMD_PING_REQUEST, this);
		if (! isPinging) {
			ping_send_time = System.currentTimeMillis();
		}
		isPinging = true;
	}
	
	public void recievePingAnswer() {
		adapter.send(NetworkConstants.CMD_PING_CONFIRM, this);
		ping = (int) (System.currentTimeMillis() - ping_send_time);
		isPinging = false;
	}
	
	public int getPing() {
		if (isPinging) {
			return Math.max((int) (System.currentTimeMillis() - ping_send_time), ping);
		} else {
			return ping;
		}
	}

	public void recievePackage(byte packageID, NetworkListener listener) {
		lastPackageID++;
		if (lastPackageID != packageID) {
			int diff = ByteUtilities.getByteDifference(lastPackageID, packageID);
			packageLossCount += diff;
			listener.onPackageLoss(diff, this);
		}
		lastPackageID = packageID;
	}

	public int getPackageLossCount() {
		return packageLossCount;
	}
	
	public byte getNewPackageID() {
		byte tmp = packageCounter;
		packageCounter++;
		return tmp;
	}
	
	public byte getActualPackageID() {
		return packageCounter;
	}

	public ReliabilityServerInterface getReliabilityInterface() {
		return reliabilityInterface;
	}

	public boolean recieveReliablePackage(long relUID) {
		boolean result = false;

		if (relUID > lastReliablePackageID) {
			lastReliablePackageID = relUID;
			result =  true;
		}

		adapter.send(NetworkConstants.CMD_RELIEABLE_PACKAGE_CONFIRMED, ByteUtilities.long2Arr(relUID), this);
		
		return result;
	}
}
