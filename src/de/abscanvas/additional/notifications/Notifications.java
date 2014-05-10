package de.abscanvas.additional.notifications;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import de.abscanvas.Screen;

public class Notifications {
	private final static int LIFETIME_0 = 45; // SLIDE IN
	private final static int LIFETIME_1 = 240; // DISPLAY
	private final static int LIFETIME_2 = 60; // FADE OUT

	private List<Note> notes = new CopyOnWriteArrayList<Note>();

	private Screen owner;

	public Notifications(Screen owner) {
		this.owner = owner;
	}

	public void add(String message) {
		notes.add(new Note(message, LIFETIME_0, LIFETIME_1, LIFETIME_2, notes.size(), this));
	}

	public void render() {
		for (int i = 0; i < notes.size(); i++) {
			Note n = notes.get(i);
			n.render(owner);
		}
	}

	public void tick() {
		for (int i = 0; i < notes.size(); i++) {
			notes.get(i).tick(i);
			if (notes.get(i).isRemoved()) {
				notes.remove(i);
			}
		}
	}

}
