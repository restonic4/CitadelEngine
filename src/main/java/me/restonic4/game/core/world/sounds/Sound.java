package me.restonic4.game.core.world.sounds;

import me.restonic4.engine.sound.SoundBuffer;
import me.restonic4.game.core.registries.RegistryItem;

public class Sound extends RegistryItem {
    private SoundBuffer soundBuffer;

    public Sound() {}

    /*public SoundBuffer getBuffer() {
        if (soundBuffer != null) {
            return soundBuffer;
        }

        soundBuffer = createBuffer();

        return soundBuffer;
    }*/

    /*private SoundBuffer createBuffer() {
        SoundBuffer soundBuffer = new SoundBuffer(RegistryManager.getAssetPath(this));

        SoundManager soundManager =  Main.getSoundManager();
        soundManager.addSoundBuffer(soundBuffer);

        return soundBuffer;
    }*/

    /*public SoundSource createSource(boolean loop, boolean relative) {
        if (getBuffer() == null) {
            throw new IllegalStateException("Could not create a sound buffer for: " + getAssetLocation());
        }

        SoundManager soundManager =  Main.getSoundManager();

        SoundSource soundSource = new SoundSource(loop, relative);
        soundSource.setBuffer(this.soundBuffer.getBufferId());
        soundManager.addSoundSource(getAssetLocation().toString(), soundSource);

        return soundSource;
    }*/

    /*@Override
    public void onPopulate() {
        super.onPopulate();

        Logger.logExtra("Preloading the asset: " + getAssetLocation());

        getBuffer();
    }*/
}
