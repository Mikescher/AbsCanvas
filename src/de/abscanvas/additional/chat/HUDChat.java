package de.abscanvas.additional.chat;

import java.util.ArrayList;

import de.abscanvas.input.Keys;
import de.abscanvas.level.ClientLevel;
import de.abscanvas.math.ByteUtilities;
import de.abscanvas.network.NetworkConstants;
import de.abscanvas.surface.AbsColor;
import de.abscanvas.surface.Bitmap;
import de.abscanvas.ui.hud.HUD;
import de.abscanvas.ui.hud.elements.HUDImage;
import de.abscanvas.ui.hud.elements.HUDPanel;

public class HUDChat extends HUDPanel implements ChatReciever {
	private Keys keys;

	private HUDImage background;
	private ChatEdit edit;
	private ChatLog log;

	private ArrayList<ChatMessage> messages = new ArrayList<ChatMessage>();

	public HUDChat(int x, int y, int width, int height, Keys keys, HUD owner) {
		super(x, y, width, height, owner);
		this.keys = keys;

		if (owner.getOwner() instanceof ClientLevel) {
			((ClientLevel) owner.getOwner()).setChatListener(this);
		} else {
			System.out.println("ERROR: ChatComponent intialized with non-ClientLevel owner");
		}

		create();
	}

	@Override
	public void tick() {
		super.tick();

		while (messages.size() > NetworkConstants.CHAT_HISTORY_SIZE) {
			messages.remove(0);
		}
	}

	private void create() {
		Bitmap n = new Bitmap(getWidth(), getHeight());
		n.fill(0, 0, getWidth(), getHeight(), AbsColor.getColor(0, 0, 0, 150));
		background = new HUDImage(getX(), getY(), n, getOwner());
		addElement(background);

		edit = new ChatEdit(getX() + 5, getY() + getHeight() - 20, getWidth() - 10, 15, keys, this);
		addElement(edit);

		log = new ChatLog(getX() + 5, getY() + 5, getWidth() - 10, getHeight() - 30, this);
		addElement(log);
	}

	public ArrayList<ChatMessage> getMessages() {
		return messages;
	}

	public void addMessage(long broadcaster, String txt, boolean isPriv) {
		messages.add(new ChatMessage(broadcaster, txt, isPriv));
		log.repaint();
	}

	@Override
	public void recieveMessage(long broadcaster, String txt, boolean isPriv) {
		addMessage(broadcaster, txt, isPriv);
	}

	public void sendMessage() {
		if (!edit.getText().isEmpty()) {
			byte[] b = new byte[NetworkConstants.STANDARD_STRING_SIZE];
			b = ByteUtilities.insert(b, ByteUtilities.string2arr(edit.getText()), 0);

			((ClientLevel) getOwner().getOwner()).getNetworkAdapter().send(NetworkConstants.CMD_CHAT_SEND, b);
		}

		edit.clear();
		edit.releaseFocus();
	}
	
	public ChatEdit getEdit() {
		return edit;
	}
	
	public ChatLog getLogComponent() {
		return log;
	}
}
