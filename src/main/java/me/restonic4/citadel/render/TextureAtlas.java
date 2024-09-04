package me.restonic4.citadel.render;

import me.restonic4.citadel.files.FileManager;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImageWrite;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.stb.STBImage.stbi_load;
import static org.lwjgl.stb.STBImage.stbi_image_free;
import static org.lwjgl.stb.STBImage.stbi_set_flip_vertically_on_load;

public class TextureAtlas {
    private int atlasWidth;
    private int atlasHeight;
    private int maxWidth;
    private int maxHeight;
    private int texID;
    private ByteBuffer atlasBuffer;
    private List<TextureRegion> regions;

    public TextureAtlas(int maxWidth, int maxHeight) {
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
        this.atlasWidth = 0;
        this.atlasHeight = 0;
        this.regions = new ArrayList<>();
        this.atlasBuffer = BufferUtils.createByteBuffer(maxWidth * maxHeight * 4); // RGBA
    }

    public TextureRegion addTexture(String filepath) {
        String resourcePath = FileManager.getOrExtractFile(
                FileManager.toResources(filepath)
        );

        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);

        stbi_set_flip_vertically_on_load(true);
        ByteBuffer image = stbi_load(resourcePath, width, height, channels, 4); // Load as RGBA

        if (image == null) {
            throw new RuntimeException("Failed to load texture file: " + resourcePath);
        }

        int imageWidth = width.get(0);
        int imageHeight = height.get(0);

        // Check if the texture fits in the current row
        if (atlasWidth + imageWidth > maxWidth) {
            atlasWidth = 0;
            atlasHeight += imageHeight;
        }

        if (atlasHeight + imageHeight > maxHeight) {
            throw new RuntimeException("Texture atlas is full, cannot add texture: " + resourcePath);
        }

        // Copy image data into the atlas buffer
        for (int y = 0; y < imageHeight; y++) {
            for (int x = 0; x < imageWidth; x++) {
                int atlasIndex = ((atlasHeight + y) * maxWidth + (atlasWidth + x)) * 4;
                int imageIndex = (y * imageWidth + x) * 4;
                atlasBuffer.put(atlasIndex, image.get(imageIndex));
                atlasBuffer.put(atlasIndex + 1, image.get(imageIndex + 1));
                atlasBuffer.put(atlasIndex + 2, image.get(imageIndex + 2));
                atlasBuffer.put(atlasIndex + 3, image.get(imageIndex + 3));
            }
        }

        TextureRegion region = new TextureRegion(atlasWidth, atlasHeight, imageWidth, imageHeight);
        regions.add(region);

        atlasWidth += imageWidth;

        stbi_image_free(image);

        return region;
    }

    public void build() {
        texID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texID);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, maxWidth, maxHeight, 0, GL_RGBA, GL_UNSIGNED_BYTE, atlasBuffer);
        glGenerateMipmap(GL_TEXTURE_2D);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST_MIPMAP_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, texID);
    }

    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public List<TextureRegion> getRegions() {
        return regions;
    }

    public void exportAtlasAsImage(String fileName) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            ByteBuffer imageBuffer = BufferUtils.createByteBuffer(atlasWidth * atlasHeight * 4);

            // Copy the atlasBuffer content to imageBuffer
            for (int y = 0; y < atlasHeight; y++) {
                for (int x = 0; x < atlasWidth; x++) {
                    int atlasIndex = (y * maxWidth + x) * 4;
                    int imageIndex = (y * atlasWidth + x) * 4;
                    imageBuffer.put(imageIndex, atlasBuffer.get(atlasIndex));
                    imageBuffer.put(imageIndex + 1, atlasBuffer.get(atlasIndex + 1));
                    imageBuffer.put(imageIndex + 2, atlasBuffer.get(atlasIndex + 2));
                    imageBuffer.put(imageIndex + 3, atlasBuffer.get(atlasIndex + 3));
                }
            }

            String filePath = FileManager.createDirectory("exports") + "/" + fileName;
            if (!STBImageWrite.stbi_write_png(filePath, atlasWidth, atlasHeight, 4, imageBuffer, atlasWidth * 4)) {
                throw new RuntimeException("Failed to write texture atlas image to " + filePath);
            }

            System.out.println("Atlas exported as " + filePath);
        }
    }

    public static class TextureRegion {
        public final int x, y, width, height;

        public TextureRegion(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
    }
}
