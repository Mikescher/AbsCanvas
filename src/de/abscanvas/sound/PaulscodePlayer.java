package de.abscanvas.sound;

import java.net.URL;
import java.util.*;

import paulscode.sound.*;
import paulscode.sound.codecs.*;
import paulscode.sound.libraries.LibraryJavaSound;

public class PaulscodePlayer implements SoundPlayer {
	private final Class<? extends Library> libraryType;
	private SoundSystem soundSystem;
	private boolean oggPlaybackSupport = true;
	private boolean wavPlaybackSupport = true;
	private boolean muted = false;
	
	private Set<String> loaded = new TreeSet<String>();

	private static final int MAX_SOURCES_PER_SOUND = 5;

	public PaulscodePlayer() {
		libraryType = LibraryJavaSound.class;

		try {
			SoundSystemConfig.setCodec("ogg", CodecJOrbis.class);
		} catch (SoundSystemException ex) {
			oggPlaybackSupport = false;
		}

		try {
			SoundSystemConfig.setCodec("wav", CodecWav.class);
		} catch (SoundSystemException ex) {
			wavPlaybackSupport = false;
		}

		try {
			soundSystem = new SoundSystem(libraryType);
		} catch (SoundSystemException ex) {
			soundSystem = null;
		}
	}

	private boolean hasOggPlaybackSupport() {
		return oggPlaybackSupport && soundSystem != null;
	}

	private boolean hasWavPlaybackSupport() {
		return wavPlaybackSupport && soundSystem != null;
	}

	@Override
	public boolean isPlaying(String sourceName) {
		if (hasOggPlaybackSupport()) {
			return soundSystem.playing(sourceName);
		}
		return false;
	}

	@Override
	public boolean startBackgroundSound(String path, String id) {
		return startBackgroundSound(path, id, true);
	}

	@Override
	public boolean startBackgroundSound(String path, String id, boolean loop) {
		URL res = PaulscodePlayer.class.getResource(path);

		if (res == null) {
			return false;
		}

		String extension = res.getPath().substring(res.getPath().lastIndexOf(".") + 1, res.getPath().length());
		extension = extension.toLowerCase();

		if (isMuted()) {
			return false;
		}

		if (extension.equals("ogg")) {
			if (!hasOggPlaybackSupport()) {
				return false;
			}
		} else if (extension.equals("wav")) {
			if (!hasWavPlaybackSupport()) {
				return false;
			}
		} else {
			return false;
		}

		soundSystem.backgroundMusic(id, res, path, loop);

		return true;
	}

	@Override
	public void stopBackgroundMusic(String id) {
		if (hasOggPlaybackSupport()) {
			soundSystem.stop(id);
		}
	}

	@Override
	public void pause(String id) {
		soundSystem.pause(id);
	}

	@Override
	public void resume(String id) {
		soundSystem.play(id);
	}

	@Override
	public void stop(String id) {
		soundSystem.stop(id);
	}

	@Override
	public void setListenerPosition(float x, float y) {
		soundSystem.setListenerPosition(x, y, 50);
	}

	@Override
	public boolean playSound(String sourceName) {
		return playSound(sourceName, soundSystem.getListenerData().position.x, soundSystem.getListenerData().position.y);
	}

	@Override
	public boolean playSound(String sourceName, float x, float y) {
		return playSound(sourceName, x, y, false);
	}

	@Override
	public boolean playSound(String sourceName, float x, float y, boolean blocking) {
		return playSound(sourceName, x, y, blocking, 0);
	}

	private boolean playSound(String sourceName, float x, float y, boolean blocking, int index) {
		if (index < MAX_SOURCES_PER_SOUND && !isMuted() && hasWavPlaybackSupport()) {
			String indexedSourceName = sourceName + index;
			if (!loaded.contains(indexedSourceName)) {
				soundSystem.newSource(false, indexedSourceName, PaulscodePlayer.class.getResource(sourceName), sourceName, false, x, y, 0, SoundSystemConfig.ATTENUATION_ROLLOFF,
						SoundSystemConfig.getDefaultRolloff());
				loaded.add(indexedSourceName);

			} else if (isPlaying(indexedSourceName)) {
				if (blocking) {
					return false;
				}

				// Source already playing, create new source for same sound effect.
				return playSound(sourceName, x, y, false, index + 1);
			} else {
				soundSystem.stop(indexedSourceName);
			}

			soundSystem.setPriority(indexedSourceName, false);
			soundSystem.setPosition(indexedSourceName, x, y, 0);
			soundSystem.setAttenuation(indexedSourceName, SoundSystemConfig.ATTENUATION_ROLLOFF);
			soundSystem.setDistOrRoll(indexedSourceName, SoundSystemConfig.getDefaultRolloff());
			soundSystem.setPitch(indexedSourceName, 1.0f);
			soundSystem.play(indexedSourceName);
			return true;
		}

		return false;
	}

	@Override
	public void shutdown() {
		if (soundSystem != null) {
			soundSystem.cleanup();
		}
	}

	@Override
	public boolean isMuted() {
		return muted;
	}

	@Override
	public void setMuted(boolean muted) {
		this.muted = muted;
	}
}