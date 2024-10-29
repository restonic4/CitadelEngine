package com.restonic4.citadel.sound;

import com.restonic4.ClientSide;
import com.restonic4.citadel.util.debug.diagnosis.Logger;
import org.joml.Vector3f;

import static org.lwjgl.openal.AL10.*;

@ClientSide
public class SoundSource {
    private final int sourceId;

    public SoundSource(boolean loop, boolean relative) {
        if (SoundManager.getInstance().getContext() == 0) {
            Logger.logError("Could not create sound sources, the OpenAL context is missing!");

            this.sourceId = 0;

            return;
        }

        this.sourceId = alGenSources();
        alSourcei(sourceId, AL_LOOPING, loop ? AL_TRUE : AL_FALSE);
        alSourcei(sourceId, AL_SOURCE_RELATIVE, relative ? AL_TRUE : AL_FALSE);
    }

    public void cleanup() {
        if (SoundManager.getInstance().getContext() == 0) {
            return;
        }

        stop();
        alDeleteSources(sourceId);
    }

    public boolean isPlaying() {
        if (SoundManager.getInstance().getContext() == 0) {
            return false;
        }

        return alGetSourcei(sourceId, AL_SOURCE_STATE) == AL_PLAYING;
    }

    public void pause() {
        if (SoundManager.getInstance().getContext() == 0) {
            return;
        }

        alSourcePause(sourceId);
    }

    public void play() {
        if (SoundManager.getInstance().getContext() == 0) {
            return;
        }

        alSourcePlay(sourceId);
    }

    public void setBuffer(int bufferId) {
        if (SoundManager.getInstance().getContext() == 0) {
            return;
        }

        stop();
        alSourcei(sourceId, AL_BUFFER, bufferId);
    }

    public void setGain(float gain) {
        if (SoundManager.getInstance().getContext() == 0) {
            return;
        }

        alSourcef(sourceId, AL_GAIN, gain);
    }

    public void setPitch(float pitch) {
        if (SoundManager.getInstance().getContext() == 0) {
            return;
        }

        alSourcef(sourceId, AL_PITCH, pitch);
    }

    public void setPosition(Vector3f position) {
        if (SoundManager.getInstance().getContext() == 0) {
            return;
        }

        alSource3f(sourceId, AL_POSITION, position.x, position.y, position.z);
    }

    public void stop() {
        if (SoundManager.getInstance().getContext() == 0) {
            return;
        }

        alSourceStop(sourceId);
    }
}