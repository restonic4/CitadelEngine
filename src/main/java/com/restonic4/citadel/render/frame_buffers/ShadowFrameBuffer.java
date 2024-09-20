package com.restonic4.citadel.render.frame_buffers;

import com.restonic4.citadel.exceptions.RenderException;
import com.restonic4.citadel.registries.built_in.types.FrameBuffer;
import com.restonic4.citadel.render.shadows.CascadeShadow;
import com.restonic4.citadel.render.FrameBufferManager;
import com.restonic4.citadel.render.shadows.ShadowTexture;
import com.restonic4.citadel.util.debug.diagnosis.Logger;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

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

        // Create the depth map textures
        depthMap = new ShadowTexture(CascadeShadow.SHADOW_MAP_CASCADE_COUNT, getWidth(), getHeight(), GL_DEPTH_COMPONENT);

        // Attach the depth map texture to the FBO
        glBindFramebuffer(GL_FRAMEBUFFER, getFrameBufferId());
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, depthMap.getIds()[0], 0);

        // Set only depth
        glDrawBuffer(GL_NONE);
        glReadBuffer(GL_NONE);

        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            throw new RenderException("Could not create FrameBuffer");
        }

        FrameBufferManager.unbindCurrentFrameBuffer();

        Logger.log("ShadowFrameBuffer " + getFrameBufferId() + " generated: " + this.getAssetLocation());
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
