package de.abscanvas.network;

import de.abscanvas.math.ByteUtilities;

public class ReliabilityClientInterface extends ReliabilityInterface{
	private ClientAdapter owner;

	public ReliabilityClientInterface(byte[] identifierpackage, ClientAdapter owner) {
		super(identifierpackage);
		this.owner = owner;
	}

	public void tick() {
		if (itemQueue.size() == 0 && buffer.isFilled() && buffer.isForceSendByTime()) {
			addBufferToSendList();
		}
		
		if (itemQueue.size() > 0) {
			owner.sendNow(itemQueue.get(0).getData());
		}
	}

	@Override
	public synchronized void send(short command) {
		send(command, new byte[0]);
	}

	@Override
	public synchronized void send(short command, byte[] data) {
		byte[] d = new byte[data.length + 2];
		ByteUtilities.insert(d, ByteUtilities.short2Arr(command), 0);
		ByteUtilities.insert(d, data, 2);

		if ((getBuffer().getByteLength() + d.length + 2) > NetworkConstants.PACKAGE_SIZE) {
			addBufferToSendList();
		}

		getBuffer().add(d);
	}
}
