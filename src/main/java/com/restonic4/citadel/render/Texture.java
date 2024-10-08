package com.restonic4.citadel.render;

import com.restonic4.ClientSide;
import com.restonic4.citadel.core.CitadelLauncher;
import com.restonic4.citadel.exceptions.FileException;
import com.restonic4.citadel.files.FileManager;
import com.restonic4.citadel.util.debug.diagnosis.Logger;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.ARBBindlessTexture.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL14.GL_TEXTURE_LOD_BIAS;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.opengl.NVBindlessTexture.glGetTextureHandleNV;
import static org.lwjgl.stb.STBImage.*;

@ClientSide
public class Texture {
    private String filepath;
    private int texID;
    private long texHandleID;
    private ByteBuffer imageBufferData;
    private int width, height;
    private boolean isRaw;

    public Texture(boolean isIcon, String filepath) {
        generate(filepath, isIcon);
    }

    public Texture(String filepath) {
        generate(filepath, false);
    }

    public Texture(String filepath, boolean generateGLStuff) {
        if (generateGLStuff) {
            throw new IllegalStateException("This constructor should not be used if the OpenGL texture generation process is needed. Use the default constructor instead, of send the boolean as false to not generate the OpenGL texture data.");
        }

        this.filepath = FileManager.getOrExtractFile(
                FileManager.toResources(filepath)
        );

        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);

        ByteBuffer image = stbi_load(this.filepath, width, height, channels, 0);

        if (image != null) {
            if (channels.get(0) != 3 && channels.get(0) != 4) {
                throw new FileException("Unknown number of channels '" + channels.get(0) + "'");
            }
            else {
                Logger.logExtra("Raw texture created: " + this.filepath);
            }
        } else {
            throw new FileException("Could not load image '" + this.filepath + "'");
        }

        finishImage(image, width, height, true);
    }

    public Texture(long textureHandlerId) {
        this.texHandleID = textureHandlerId;
    }

    public void generate(String filepath, boolean isIcon) {
        this.filepath = FileManager.getOrExtractFile(
                FileManager.toResources(filepath)
        );

        generateOpenGLData();

        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);

        if (!isIcon) {
            stbi_set_flip_vertically_on_load(true);
        }

        ByteBuffer image = stbi_load(this.filepath, width, height, channels, 0);

        loadImageProperties(image, channels, width, height);

        generateBindlessHandler();

        // Free the memory, we don't want a memory leak, don't we?
        stbi_image_free(image);

        finishImage(image, width, height, false);
    }

    private void generateOpenGLData() {
        // Generate texture on GPU
        texID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texID);

        // Set texture parameters
        // Repeat image in both directions
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

        // When stretching the image, pixelate
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

        // When shrinking an image, pixelate
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
    }

    private void loadImageProperties(ByteBuffer image, IntBuffer channels, IntBuffer width, IntBuffer height) {
        if (image != null) {
            if (channels.get(0) == 3) {
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width.get(0), height.get(0),
                        0, GL_RGB, GL_UNSIGNED_BYTE, image);
            } else if (channels.get(0) == 4) {
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(0), height.get(0),
                        0, GL_RGBA, GL_UNSIGNED_BYTE, image);
            } else {
                throw new FileException("Unknown number of channels '" + channels.get(0) + "'");
            }
        } else {
            throw new FileException("Could not load image '" + this.filepath + "'");
        }

        glGenerateMipmap(GL_TEXTURE_2D);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_LOD_BIAS, 0); // -0.3f
    }

    private void finishImage(ByteBuffer image, IntBuffer width, IntBuffer height, boolean isRaw) {
        this.imageBufferData = image;
        this.width = width.get(0);
        this.height = height.get(0);
        this.isRaw = isRaw;
    }

    private void generateBindlessHandler() {
        if (!CitadelLauncher.getInstance().getSettings().shouldGenerateBindlessTextures()) {
            return;
        }

        texHandleID = glGetTextureHandleARB(texID);

        Logger.logExtra("Texture: " + texID + "; " + "Handle: " + texHandleID);

        glMakeTextureHandleResidentARB(texHandleID);
    }

    public long getBindlessHandle() {
        return texHandleID;
    }

    public void cleanup() {
        glMakeTextureHandleNonResidentARB(texHandleID);
        glDeleteTextures(texID);
    }

    public ByteBuffer getImageBufferData() {
        return this.imageBufferData;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public boolean isRaw() {
        return this.isRaw;
    }

    public int getTextureID() {
        return texID;
    }

    public long getTextureHandleID() {
        return texHandleID;
    }
}
