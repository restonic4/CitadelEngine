package me.restonic4.citadel.render.frame_buffers;

import me.restonic4.citadel.exceptions.RenderException;
import me.restonic4.citadel.registries.built_in.types.FrameBuffer;
import me.restonic4.citadel.render.CascadeShadow;
import me.restonic4.citadel.render.FrameBufferManager;
import me.restonic4.citadel.render.ShadowTexture;
import me.restonic4.citadel.util.debug.diagnosis.Logger;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.ARBBindlessTexture.glGetTextureHandleARB;
import static org.lwjgl.opengl.ARBBindlessTexture.glMakeTextureHandleResidentARB;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0;
import static org.lwjgl.opengl.GL32.glFramebufferTexture;

public class ShadowFrameBuffer extends FrameBuffer {
    private ShadowTexture depthMap;

    public ShadowFrameBuffer() {
        super();
    }

    public ShadowFrameBuffer(int size) {
        super(size, size);
    }

    public ShadowFrameBuffer(int width, int height) {
        super(width, height);
    }

    @Override
    public void generate() {
        if (isGenerated()) {
            return;
        }

        setGenerated();

        // FBO creation

        setFrameBufferId(glGenFramebuffers());
        glBindFramebuffer(GL_FRAMEBUFFER, getFrameBufferId());
        glDrawBuffer(GL_NONE);
        glReadBuffer(GL_NONE);

        FrameBufferManager.unbindCurrentFrameBuffer();

        Logger.log("ShadowFrameBuffer " + getFrameBufferId() + " generated: " + this.getAssetLocation());

        // Texture array

        depthMap = new ShadowTexture(CascadeShadow.SHADOW_MAP_CASCADE_COUNT, getWidth(), getHeight(), GL_DEPTH_COMPONENT);
        for (int i = 0; i < depthMap.getIds().length; i++) {
            Logger.log(depthMap.getIds()[i]);
        }
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, depthMap.getIds()[0], 0);
    }

    @Override
    public void cleanup() {
        glDeleteFramebuffers(getFrameBufferId());
        depthMap.cleanup();
    }

    @Override
    public int getTextureId() {
        throw new RenderException("Shadow frame buffers do not contain regular textures");
    }

    @Override
    public long getTextureHandlerId() {
        throw new RenderException("Shadow frame buffers do not contain regular textures");
    }

    public void bindTextures(int start) {
        for (int i = 0; i < CascadeShadow.SHADOW_MAP_CASCADE_COUNT; i++) {
            glActiveTexture(start + i);
            glBindTexture(GL_TEXTURE_2D, depthMap.getIds()[i]);
        }
    }

    public ShadowTexture getDepthMapTexture() {
        return depthMap;
    }
}
