package com.restonic4.citadel.registries.built_in.types;

import com.restonic4.citadel.sound.SoundBuffer;
import com.restonic4.citadel.registries.RegistryObject;
import com.restonic4.citadel.sound.SoundManager;
import com.restonic4.citadel.sound.SoundSource;
import com.restonic4.citadel.util.debug.diagnosis.Logger;

public class Sound extends RegistryObject {
    private SoundBuffer soundBuffer;
    private float volume, pitch;

    public Sound() {
        this.volume = 1;
        this.pitch = 1;
    }

    public Sound(float volume) {
        this.volume = volume;
        this.pitch = 1;
    }

    public Sound(float volume, float pitch) {
        this.volume = volume;
        this.pitch = pitch;
    }

    public SoundBuffer getBuffer() {
        if (soundBuffer != null) {
            return soundBuffer;
        }

        soundBuffer = createBuffer();

        return soundBuffer;
    }

    private SoundBuffer createBuffer() {
        SoundBuffer soundBuffer = new SoundBuffer(this.getAssetPath());

        SoundManager soundManager =  SoundManager.getInstance();
        soundManager.addSoundBuffer(soundBuffer);

        return soundBuffer;
    }

    public SoundSource createSource(boolean loop, boolean relative) {
        if (getBuffer() == null) {
            throw new IllegalStateException("Could not create a sound buffer for: " + getAssetLocation());
        }

        SoundManager soundManager = SoundManager.getInstance();

        SoundSource soundSource = new SoundSource(loop, relative);
        soundSource.setBuffer(this.soundBuffer.getBufferId());
        soundSource.setGain(volume);
        soundSource.setPitch(pitch);
        soundManager.addSoundSource(getAssetLocation().toString(), soundSource);

        return soundSource;
    }

    @Override
    public void onPopulate() {
        super.onPopulate();

        Logger.logExtra("Preloading the asset: " + getAssetLocation());

        getBuffer();
    }
}
