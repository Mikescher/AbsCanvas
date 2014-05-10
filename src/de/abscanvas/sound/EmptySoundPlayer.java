package de.abscanvas.sound;

public class EmptySoundPlayer implements SoundPlayer{

	@Override
	public boolean isPlaying(String sourceName) {
		return true;
	}

	@Override
	public boolean startBackgroundSound(String path, String id) {
		return true;
	}

	@Override
	public boolean startBackgroundSound(String path, String id, boolean loop) {
		return true;
	}

	@Override
	public void stopBackgroundMusic(String id) {
		//its empty - as you can see...
	}

	@Override
	public void pause(String id) {	
		//its empty - as you can see...
	}

	@Override
	public void resume(String id) {
		//its empty - as you can see...
	}

	@Override
	public void stop(String id) {
		//its empty - as you can see...	
	}

	@Override
	public void setListenerPosition(float x, float y) {
		//its empty - as you can see...
	}

	@Override
	public boolean playSound(String sourceName) {
		return false;
	}

	@Override
	public boolean playSound(String sourceName, float x, float y) {
		return true;
	}

	@Override
	public boolean playSound(String sourceName, float x, float y, boolean blocking) {
		return true;
	}

	@Override
	public void shutdown() {
		//its empty - as you can see...
	}

	@Override
	public boolean isMuted() {
		return true;
	}

	@Override
	public void setMuted(boolean muted) {
		//its empty - as you can see...
	}

}
