package de.abscanvas.ui.gui;

public abstract class MenuAnimation {
	protected final double mpf;
	protected double process = 0;
	protected double ticks;
	protected GUIMenu owner;

	protected boolean started = false;

	public MenuAnimation(GUIMenu owner, int ticks) {
		this.owner = owner;
		this.ticks = ticks;
		this.mpf = owner.getOwner().MPF();
	}

	public void tick() {
		if (started) {
			process += 1 / ticks;
			if (process > 1) {
				process = 1;
			} else {
				onTick(process);
			}
		}
	}
	
	public abstract void onTick(double proc);

	public boolean isReady() {
		return process >= 1 && started;
	}

	public void reset() {
		process = 0;
		started = false;
	}

	public void start() {
		reset();
		started = true;
		
		onTick(0);
	}

	public boolean isStarted() {
		return started;
	}
}
