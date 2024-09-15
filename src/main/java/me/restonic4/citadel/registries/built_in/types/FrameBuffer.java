package me.restonic4.citadel.registries.built_in.types;

import me.restonic4.citadel.core.Window;
import me.restonic4.citadel.registries.RegistryObject;
import me.restonic4.citadel.render.FrameBufferManager;
import me.restonic4.citadel.util.debug.diagnosis.Logger;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.ARBBindlessTexture.*;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL32.glFramebufferTexture;

public class FrameBuffer extends RegistryObject {
    private int width, height;
    private boolean generated;
    private int frameBufferId, textureId;
    private long textureHandlerId;

    public FrameBuffer() {
        this.width = Window.getInstance().getWidth();
        this.height = Window.getInstance().getHeight();
    }

    public FrameBuffer(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void generate() {
        if (generated) {
            return;
        }

        generated = true;

        frameBufferId = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, frameBufferId);
        glDrawBuffer(GL_COLOR_ATTACHMENT0);

        textureId = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureId);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, (ByteBuffer) null);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glFramebufferTexture(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, textureId, 0);

        textureHandlerId = glGetTextureHandleARB(textureId);
        glMakeTextureHandleResidentARB(textureHandlerId);

        FrameBufferManager.unbindCurrentFrameBuffer();

        Logger.log("FrameBuffer generated: " + this.getAssetLocation());
    }

    public void bind() {
       bind(width, height);
    }

    public void bind(int width, int height) {
        if (!generated) {
            generate();
        }

        glBindTexture(GL_TEXTURE_2D, 0);//To make sure the texture isn't bound
        glBindFramebuffer(GL_FRAMEBUFFER, frameBufferId);
        glViewport(0, 0, width, height);
    }

    public void cleanup() {
        if (!generated) {
            return;
        }

        glDeleteFramebuffers(frameBufferId);
        glDeleteTextures(textureId);
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
