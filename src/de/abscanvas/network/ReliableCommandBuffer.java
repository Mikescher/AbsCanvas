package de.abscanvas.network;

import java.util.ArrayList;

import de.abscanvas.math.ByteUtilities;

public class ReliableCommandBuffer extends ArrayList<byte[]> {
	private static final long serialVersionUID = 1L;
	
	private long startTime;

	public ReliableCommandBuffer(byte[] firstPackage) {
		add(firstPackage); // IDENTIFIER
		add(new byte[1]); // PACKAGE ID
		add(new byte[10]); // ReliableCMD + Reliable UID
		resetStartTime();
	}

	public synchronized int getByteLength() {
		int c = 0;
		for (byte[] b : this) {
			c += b.length;
		}
		return c;
	}
	
	public synchronized byte[] getFullData(byte packageID, long reliableUID) {
		set(1, ByteUtilities.byte2Arr(packageID));
		byte[] relSet = new byte[10];
		ByteUtilities.insert(relSet, ByteUtilities.short2Arr(NetworkConstants.CMD_RELIEABLE_PACKAGE_SEND), 0);
		ByteUtilities.insert(relSet, ByteUtilities.long2Arr(reliableUID), 2);
		set(2, relSet);
		int p = 0;
		byte[] result = new byte[getByteLength() + 2];
		for (int i = 0; i < size(); i++) {
			ByteUtilities.insert(result, get(i), p);
			p += get(i).length;
		}
		ByteUtilities.insert(result, ByteUtilities.short2Arr(NetworkConstants.CMD_BUFFER_END), p);
		return result;
	}
	
	public synchronized void reset() {
		if (isFilled()) {
			removeRange(3, size());
		}
		resetStartTime();
	}
	
	public boolean isFilled() {
		return size() > 3;
	}
	
	public boolean isOverfilled() {
		return (getByteLength()-2) > NetworkConstants.PACKAGE_SIZE;
	}

	public void resetStartTime() {
		this.startTime = System.currentTimeMillis();
	}

	public long getStartTime() {
		return startTime;
	}
	
	public boolean isForceSendByTime() {
		return (System.currentTimeMillis() - getStartTime()) > NetworkConstants.MAX_BUFFER_WAITTIME;
	}
}
