package de.abscanvas.ui.gui;

public class EmptyAnimation extends MenuAnimation{
	public EmptyAnimation(GUIMenu owner) {
		super(owner, 1);
	}

	@Override
	public void onTick(double proc) {
		//empty
	}

	@Override
	public boolean isReady() {
		return started;
	}
}
