package de.abscanvas.additional.chat;

public interface ChatReciever {
	public void recieveMessage(long broadcaster, String txt, boolean isPriv);
}
