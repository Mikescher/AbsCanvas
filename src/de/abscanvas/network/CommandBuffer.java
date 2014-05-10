package de.abscanvas.network;

import java.util.ArrayList;

import de.abscanvas.math.ByteUtilities;

public class CommandBuffer extends ArrayList<byte[]> {
	private static final long serialVersionUID = 1L;

	private long startTime;

	public CommandBuffer(byte[] firstPackage) {
		add(firstPackage); // IDENTIFIER
		add(new byte[1]); // PACKAGE ID
		resetStartTime();
	}

	public synchronized int getByteLength() {
		int c = 0;
		int l = this.size();
		for (int i = 0; i < l; i++) {
			c += get(i).length;
		}
		return c;
	}

	public synchronized byte[] getFullData(byte packageID) {
		set(1, ByteUtilities.byte2Arr(packageID));
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
			removeRange(2, size());
		}
		resetStartTime();
	}

	public boolean isFilled() {
		return size() > 2;
	}

	public boolean isOverfilled() {
		return getByteLength() > NetworkConstants.PACKAGE_SIZE;
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
