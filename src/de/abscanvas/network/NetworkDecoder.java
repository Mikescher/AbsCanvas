package de.abscanvas.network;

import java.net.DatagramPacket;

public abstract class NetworkDecoder extends Thread{
	protected DatagramPacket packet;
	protected NetworkListener listener;
	protected byte[] data;
	
	protected int cmdCount = 0;
	
	public NetworkDecoder(DatagramPacket p, byte[] d, NetworkListener l) {
		this.packet = p; // TODO ist nur ne Referenz - wird vom anaderen Thread beim nächsten recieve geändert (vllt ???)
		this.listener = l;
		this.data = d.clone();
	}
}
