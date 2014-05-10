package de.abscanvas.network;

import java.net.InetAddress;

public interface NetworkListener {
	public int recieveCommand(byte packageID, int commandID, byte[] data, InetAddress ip, int port);
	
	public void recievePackage(byte[] data, byte packageID, InetAddress ip, int port);
	
	public void failRecieveCommand(byte[] data, InetAddress ip, int port);
	
	public void onPackageLoss(int lossCount, NetworkUser u);

	public NetworkUser getServerUser(InetAddress ip, int port);
}
