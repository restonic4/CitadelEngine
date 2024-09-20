package com.restonic4.citadel.render;

import com.restonic4.citadel.core.Window;
import com.restonic4.citadel.registries.AssetLocation;
import com.restonic4.citadel.registries.Registries;
import com.restonic4.citadel.registries.Registry;
import com.restonic4.citadel.registries.built_in.types.FrameBuffer;

import java.util.Map;

import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;

public class FrameBufferManager {
    private static final Window window = Window.getInstance();

    public static void bindFrameBuffer(FrameBuffer frameBuffer) {
        frameBuffer.bind(window.getWidth(), window.getHeight());
    }

    public static void unbindCurrentFrameBuffer() {
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        glViewport(0, 0, window.getWidth(), window.getHeight());
    }

    public static void cleanup() {
        Map<AssetLocation, FrameBuffer> frameBufferMap = Registry.getRegistry(Registries.FRAME_BUFFER);
        for (FrameBuffer frameBuffer : frameBufferMap.values()) {
            frameBuffer.cleanup();
        }
    }

    public static void preGenerateFrameBuffers() {
        Map<AssetLocation, FrameBuffer> frameBufferMap = Registry.getRegistry(Registries.FRAME_BUFFER);
        for (FrameBuffer frameBuffer : frameBufferMap.values()) {
            frameBuffer.generate();
        }

        FrameBufferManager.unbindCurrentFrameBuffer();
    }
}
