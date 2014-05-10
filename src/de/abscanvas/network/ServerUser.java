package de.abscanvas.network;

public class ServerUser {
	private String name;
	private final long UID;
	
	public ServerUser(String name, long uid) {
		this.name = name;
		this.UID = uid;
	}

	public long getUID() {
		return UID;
	}

	public String getName() {
		return name;
	}

	public void updateName(String snm) {
		name = snm;
	}
}
