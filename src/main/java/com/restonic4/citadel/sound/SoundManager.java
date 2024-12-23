package com.restonic4.citadel.sound;

import com.restonic4.citadel.util.debug.diagnosis.Logger;
import com.restonic4.citadel.world.object.components.CameraComponent;
import org.joml.*;
import org.lwjgl.openal.*;

import java.nio.*;
import java.util.*;

import static org.lwjgl.openal.AL10.alDistanceModel;
import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class SoundManager {
    private static SoundManager instance;

    private final List<SoundBuffer> soundBufferList;
    private final Map<String, SoundSource> soundSourceMap;
    private long context;
    private long device;
    private SoundListener listener;

    // Cache
    Vector3f at = new Vector3f();
    Vector3f up = new Vector3f();

    public SoundManager() {
        soundBufferList = new ArrayList<>();
        soundSourceMap = new HashMap<>();
    }

    public static SoundManager getInstance() {
        if (SoundManager.instance == null) {
            SoundManager.instance = new SoundManager();
        }
        return SoundManager.instance;
    }

    public void init() {
        Logger.log("Starting the audio engine");

        device = alcOpenDevice((ByteBuffer) null);
        if (device == NULL) {
            throw new IllegalStateException("Failed to open the default OpenAL device.");
        }
        ALCCapabilities deviceCaps = ALC.createCapabilities(device);
        this.context = alcCreateContext(device, (IntBuffer) null);
        if (context == NULL) {
            Logger.logError("Failed to create OpenAL context.");
            return;
        }
        alcMakeContextCurrent(context);
        AL.createCapabilities(deviceCaps);

        setAttenuationModel(AL11.AL_EXPONENT_DISTANCE);
        setListener(new SoundListener(new Vector3f(0, 0, 0)));
    }

    public void addSoundBuffer(SoundBuffer soundBuffer) {
        this.soundBufferList.add(soundBuffer);
    }

    public void addSoundSource(String name, SoundSource soundSource) {
        this.soundSourceMap.put(name, soundSource);
    }

    public void reset() {
        soundSourceMap.values().forEach(SoundSource::cleanup);
        soundSourceMap.clear();
    }

    public void cleanup() {
        Logger.log("Cleaning sound manager");

        reset();

        soundBufferList.forEach(SoundBuffer::cleanup);
        soundBufferList.clear();

        if (context != NULL) {
            alcDestroyContext(context);
        }
        if (device != NULL) {
            alcCloseDevice(device);
        }
    }

    public SoundListener getListener() {
        return this.listener;
    }

    public SoundSource getSoundSource(String name) {
        return this.soundSourceMap.get(name);
    }

    public void playSoundSource(String name) {
        SoundSource soundSource = this.soundSourceMap.get(name);
        if (soundSource != null && !soundSource.isPlaying()) {
            soundSource.play();
        }
    }

    public void removeSoundSource(String name) {
        this.soundSourceMap.remove(name);
    }

    public void setAttenuationModel(int model) {
        alDistanceModel(model);
    }

    public void setListener(SoundListener listener) {
        this.listener = listener;
    }

    public void updateListenerPosition(CameraComponent cameraComponent) {
        Matrix4f viewMatrix = cameraComponent.getViewMatrix();
        listener.setPosition(cameraComponent.gameObject.transform.getPosition());
        viewMatrix.positiveZ(at).negate();
        viewMatrix.positiveY(up);
        listener.setOrientation(at, up);
    }

    public int getSourcesAmount() {
        return this.soundSourceMap.size();
    }

    public long getContext() {
        return this.context;
    }
}
