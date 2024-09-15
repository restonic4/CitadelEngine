package me.restonic4.citadel.registries.built_in.types;

import me.restonic4.citadel.registries.RegistryObject;
import me.restonic4.citadel.util.debug.diagnosis.Logger;

import static org.lwjgl.opengl.ARBBindlessTexture.*;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL30.*;

public class FrameBuffer extends RegistryObject {
    private boolean generated;
    private int frameBufferId, textureId;
    private long textureHandlerId;

    public FrameBuffer() {}

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
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        textureHandlerId = glGetTextureHandleARB(textureId);
        glMakeTextureHandleResidentARB(textureHandlerId);

        Logger.log("FrameBuffer generated: " + this.getAssetLocation());
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
}
