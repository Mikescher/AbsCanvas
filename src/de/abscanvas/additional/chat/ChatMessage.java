package de.abscanvas.additional.chat;

import java.text.SimpleDateFormat;
import java.util.Date;

import de.abscanvas.level.ClientLevel;

public class ChatMessage {
	private String text;
	private Date recieveTime;
	private long broadcaster; // NetworkUserUID;
	private boolean isPrivateMessage;
	
	public ChatMessage(long broadcaster, String text, boolean isPrivateMessage) {
		this.broadcaster = broadcaster;
		this.text = text;
		this.isPrivateMessage = isPrivateMessage;
		recieveTime = new Date();
	}

	public String getText() {
		return text;
	}

	public Date getRecieveTime() {
		return recieveTime;
	}

	public long getBroadcaster() {
		return broadcaster;
	}

	public boolean isPrivateMessage() {
		return isPrivateMessage;
	}

	public String getMessage(HUDChat c) {
		String s = (new SimpleDateFormat("HH:mm:ss")).format(recieveTime) + " [" + ((ClientLevel) (c.getOwner().getOwner())).getServersideUsername(broadcaster) + "]";
	
		if (isPrivateMessage()) {
			s += "->[" + ((ClientLevel) (c.getOwner().getOwner())).getUsername() + "]";
		}

		s += ": " + text;

		return s;
	}
	
	public String getMessagePart_1() {
		String s = (new SimpleDateFormat("HH:mm:ss")).format(recieveTime) + " ";
		return s;
	}
	
	public String getMessagePart_2(HUDChat c) {
		String s = "[" + ((ClientLevel) (c.getOwner().getOwner())).getServersideUsername(broadcaster) + "]";
	
		if (isPrivateMessage()) {
			s += "->[" + ((ClientLevel) (c.getOwner().getOwner())).getUsername() + "]";
		}

		s += ": ";

		return s;
	}
	
	public String getMessagePart_3() {
		String s = text;

		return s;
	}
}
