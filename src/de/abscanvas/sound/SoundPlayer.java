package de.abscanvas.sound;

public interface SoundPlayer {
	public boolean isPlaying(String sourceName);

	public boolean startBackgroundSound(String path, String id);

	public boolean startBackgroundSound(String path, String id, boolean loop);

	public void stopBackgroundMusic(String id);

	public void pause(String id);

	public void resume(String id);

	public void stop(String id);

	public void setListenerPosition(float x, float y);

	public boolean playSound(String sourceName);

	public boolean playSound(String sourceName, float x, float y);

	public boolean playSound(String sourceName, float x, float y, boolean blocking);

	public void shutdown();

	public boolean isMuted();

	public void setMuted(boolean muted);
}
