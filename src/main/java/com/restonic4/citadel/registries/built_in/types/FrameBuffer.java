package com.restonic4.citadel.registries.built_in.types;

import com.restonic4.citadel.core.CitadelLauncher;
import com.restonic4.citadel.core.Window;
import com.restonic4.citadel.registries.RegistryObject;
import com.restonic4.citadel.render.FrameBufferManager;
import com.restonic4.citadel.util.debug.diagnosis.Logger;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.ARBBindlessTexture.*;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL32.glFramebufferTexture;

public class FrameBuffer extends RegistryObject {
    private int width, height;
    private boolean generated;
    private int depthBufferId = -1;
    private int frameBufferId, textureId;
    private long textureHandlerId;
    private boolean useDepthBuffer = false;

    public FrameBuffer() {
        this.width = Window.getInstance().getWidth();
        this.height = Window.getInstance().getHeight();
    }

    public FrameBuffer(boolean useDepthBuffer) {
        this.width = Window.getInstance().getWidth();
        this.height = Window.getInstance().getHeight();
        this.useDepthBuffer = useDepthBuffer;
    }

    public FrameBuffer(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public FrameBuffer(int width, int height, boolean useDepthBuffer) {
        this.width = width;
        this.height = height;
        this.useDepthBuffer = useDepthBuffer;
    }


    public void generate() {
        if (generated) {
            return;
        }

        generated = true;

        GLCapabilities glCapabilities = GL.getCapabilities();

        Logger.log("OpenGL32: " + glCapabilities.OpenGL32);
        Logger.log("FrameBuffer objects: " + glCapabilities.GL_ARB_framebuffer_object);

        frameBufferId = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, frameBufferId);
        glDrawBuffer(GL_COLOR_ATTACHMENT0);

        textureId = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureId);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, (ByteBuffer) null);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);


        // glFramebufferTexture works on NVIDIA RTX 2060 but breaks in Intel(R) HD Graphics 520
        // It might be fixed by using a glFramebufferTexture2D instead.
        // TODO: This should be properly tested.

        //glFramebufferTexture(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, textureId, 0);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, textureId, 0);

        if (useDepthBuffer) {
            depthBufferId = glGenRenderbuffers();
            glBindRenderbuffer(GL_RENDERBUFFER, depthBufferId);
            glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT, width, height);
            glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, depthBufferId);
        }

        if (CitadelLauncher.getInstance().getSettings().shouldGenerateBindlessTextures()) {
            textureHandlerId = glGetTextureHandleARB(textureId);
            glMakeTextureHandleResidentARB(textureHandlerId);
        }

        FrameBufferManager.unbindCurrentFrameBuffer();

        Logger.log("FrameBuffer " + frameBufferId + " generated: " + this.getAssetLocation() +
                " texture handler id: " + textureHandlerId +
                (useDepthBuffer ? " with depth buffer." : " without depth buffer."));
    }

    public void bind() {
       bind(width, height);
    }

    public void bind(int width, int height) {
        if (!generated) {
            generate();
        }

        //glBindTexture(GL_TEXTURE_2D, 0);//To make sure the texture isn't bound
        glBindFramebuffer(GL_FRAMEBUFFER, frameBufferId);
        glViewport(0, 0, width, height);
    }

    public void cleanup() {
        if (!generated) {
            return;
        }

        glDeleteFramebuffers(frameBufferId);
        glDeleteTextures(textureId);

        if (useDepthBuffer) {
            glDeleteRenderbuffers(depthBufferId);
        }

        glMakeTextureHandleNonResidentARB(textureHandlerId);
    }

    public boolean isGenerated() {
        return this.generated;
    }

    public void setGenerated() {
        this.generated = true;
    }

    public int getFrameBufferId() {
        return this.frameBufferId;
    }

    public void setFrameBufferId(int id) {
        this.frameBufferId = id;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getTextureId() {
        return textureId;
    }

    public void setTextureId(int textureId) {
        this.textureId = textureId;
    }

    public long getTextureHandlerId() {
        return textureHandlerId;
    }

    public void setTextureHandlerId(long textureHandlerId) {
        this.textureHandlerId = textureHandlerId;
    }
}
