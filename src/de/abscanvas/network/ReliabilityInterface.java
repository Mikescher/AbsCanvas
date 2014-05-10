package de.abscanvas.network;

import java.util.ArrayList;

import de.abscanvas.math.ByteUtilities;

public abstract class ReliabilityInterface {
	private long uidCounter = 0;

	protected ReliableCommandBuffer buffer;

	protected ArrayList<ReliabilityItem> itemQueue;
	
	public ReliabilityInterface(byte[] identifierpackage) {
		buffer = new ReliableCommandBuffer(identifierpackage);
		itemQueue = new ArrayList<ReliabilityItem>();
	}
	
	public abstract void send(short command);
	public abstract void send(short command, byte[] data);

	protected synchronized void addBufferToSendList() {
		if (buffer.isFilled()) {
			itemQueue.add(new ReliabilityItem(buffer.getFullData(ByteUtilities.ZERO, uidCounter), uidCounter));
			buffer.reset();
			uidCounter++;
		}
	}
	
	public synchronized void recieveReliablePackageConfirmation(long reliableUID) {
		for (int i = 0; i < itemQueue.size(); i++) {
			if (itemQueue.get(i).getReliableUID() == reliableUID) {
				itemQueue.remove(i);
				return;
			}
		}
	}
	
	protected ReliableCommandBuffer getBuffer() {
		return buffer;
	}
}
