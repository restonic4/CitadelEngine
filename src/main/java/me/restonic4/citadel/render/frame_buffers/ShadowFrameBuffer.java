package me.restonic4.citadel.render.frame_buffers;

import me.restonic4.citadel.registries.built_in.types.FrameBuffer;
import me.restonic4.citadel.render.FrameBufferManager;
import me.restonic4.citadel.util.debug.diagnosis.Logger;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.ARBBindlessTexture.glGetTextureHandleARB;
import static org.lwjgl.opengl.ARBBindlessTexture.glMakeTextureHandleResidentARB;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0;
import static org.lwjgl.opengl.GL32.glFramebufferTexture;

public class ShadowFrameBuffer extends FrameBuffer {
    @Override
    public void generate() {
        if (isGenerated()) {
            return;
        }

        setGenerated();

        setFrameBufferId(glGenFramebuffers());
        glBindFramebuffer(GL_FRAMEBUFFER, getFrameBufferId());
        glDrawBuffer(GL_NONE);
        //glReadBuffer(GL_NONE);

        setTextureId(glGenTextures());
        glBindTexture(GL_TEXTURE_2D, getTextureId());
        glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT16, getWidth(), getHeight(), 0, GL_DEPTH_COMPONENT, GL_FLOAT, (ByteBuffer) null);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glFramebufferTexture(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, getTextureId(), 0);

        setTextureHandlerId(glGetTextureHandleARB(getTextureId()));
        glMakeTextureHandleResidentARB(getTextureHandlerId());

        FrameBufferManager.unbindCurrentFrameBuffer();

        Logger.log("ShadowFrameBuffer generated: " + this.getAssetLocation());
    }
}
