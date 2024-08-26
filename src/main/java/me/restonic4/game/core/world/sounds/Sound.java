package me.restonic4.game.core.world.sounds;

import me.restonic4.engine.registries.RegistryManager;
import me.restonic4.engine.sound.SoundBuffer;
import me.restonic4.engine.registries.RegistryObject;
import me.restonic4.engine.sound.SoundManager;
import me.restonic4.engine.sound.SoundSource;
import me.restonic4.engine.util.debug.diagnosis.Logger;

public class Sound extends RegistryObject {
    private SoundBuffer soundBuffer;
    private float volume;

    public Sound(float volume) {
        this.volume = volume;
    }

    public SoundBuffer getBuffer() {
        if (soundBuffer != null) {
            return soundBuffer;
        }

        soundBuffer = createBuffer();

        return soundBuffer;
    }

    private SoundBuffer createBuffer() {
        SoundBuffer soundBuffer = new SoundBuffer(RegistryManager.getAssetPath(this));

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
