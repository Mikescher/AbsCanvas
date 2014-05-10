package de.abscanvas.network;

import java.net.DatagramPacket;
import java.net.InetAddress;

import de.abscanvas.math.ByteUtilities;

public class ClientDecoder extends NetworkDecoder {
	private ClientAdapter owner;

	private byte packageID;

	public ClientDecoder(DatagramPacket p, byte[] data, NetworkListener l, ClientAdapter a) {
		super(p, data, l);
		owner = a;
	}

	@Override
	public void run() {
		if (data[0] == owner.getIdentifier()[0] && data[1] == owner.getIdentifier()[1] && data[2] == owner.getIdentifier()[2] && data[3] == owner.getIdentifier()[3]) {
			packageID = data[4];
			byte[] dater = ByteUtilities.removeFirst(data, 5);
			recieveCMD(dater, packet.getAddress(), packet.getPort(), true);
		} else {
			listener.failRecieveCommand(data, packet.getAddress(), packet.getPort());
		}
	}

	private void recieveCMD(byte[] d, InetAddress ip, int port, boolean first) {
		int command = ByteUtilities.arr2Short(d, 0);
		if (command == NetworkConstants.CMD_BUFFER_END) {
			return;
		} else if (command == NetworkConstants.CMD_RELIEABLE_PACKAGE_SEND && first) {
			d = ByteUtilities.removeFirst(d, 2);
			long relUID = ByteUtilities.arr2Long(d, 0);
			d = ByteUtilities.removeFirst(d, 8);

			boolean is_new = owner.recieveReliablePackage(relUID, ip, port);
			cmdCount++;
			if (is_new) {
				recieveCMD(d, ip, port, false);
			}
		} else {
			if (d.length > 1) {
				d = ByteUtilities.removeFirst(d, 2);

				int length = listener.recieveCommand(packageID, command, d, ip, port);
				cmdCount++;

				d = ByteUtilities.removeFirst(d, length);

				recieveCMD(d, ip, port, false);
			} else {
				System.out.println("ERROR - Package overflow");
			}
		}
	}
}
