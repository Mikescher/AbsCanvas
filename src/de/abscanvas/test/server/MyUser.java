package de.abscanvas.test.server;

import java.net.InetAddress;

import de.abscanvas.network.NetworkUser;
import de.abscanvas.network.ServerAdapter;

public class MyUser extends NetworkUser{

	public Player player;
	
	public MyUser(String name, InetAddress ip, int port, ServerAdapter k, Player p) {
		super(name, ip, port, k);
		this.player = p;
	}

}
