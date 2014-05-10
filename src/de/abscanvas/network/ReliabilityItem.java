package de.abscanvas.network;

public class ReliabilityItem {
	private long uid;
	
	private byte[] data;
	
	public ReliabilityItem(byte[] data, long uid) {
		this.uid = uid;
		this.data = data;
	}
	
	public synchronized int getByteLength() {
		return data.length;
	}

	public byte[] getData() {
		return data;
	}

	public long getReliableUID() {
		return uid;
	}
}
