package me.restonic4.citadel.render;

import me.restonic4.citadel.exceptions.FileException;
import me.restonic4.citadel.files.FileManager;
import me.restonic4.citadel.util.debug.diagnosis.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;
import org.lwjgl.stb.STBImageWrite;
import org.lwjgl.system.MemoryStack;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.stb.STBImage.stbi_load;
import static org.lwjgl.stb.STBImage.stbi_image_free;
import static org.lwjgl.stb.STBImage.stbi_set_flip_vertically_on_load;

/*public class TextureAtlas {
    private int maxWidth;
    private int maxHeight;

    private int atlasWidth;
    private int atlasHeight;

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

    public void addTexture(String filepath) {
        String resourcePath = FileManager.getOrExtractFile(
                FileManager.toResources(filepath)
        );

        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);

        stbi_set_flip_vertically_on_load(true);
        ByteBuffer image = stbi_load(resourcePath, width, height, channels, 0);
        if (image == null) {
            throw new RuntimeException("Failed to load texture file: " + resourcePath);
        }

        int texWidth = width.get(0);
        int texHeight = height.get(0);

        if (atlasWidth + texWidth > maxWidth) {
            if (atlasHeight + texHeight > maxHeight) {
                throw new FileException("Full atlas");
            }

            atlasHeight += texHeight;
        }
        else {
            atlasWidth += texWidth;
        }

        stbi_image_free(image);

        atlasBuffer = image;

        //return region;
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

    public void saveImage(String filePath) {
        String fileExport = FileManager.createDirectory("exports") + "/" + filePath;

        // Guardar la imagen en formato PNG utilizando STBImageWrite
        boolean success = STBImageWrite.stbi_write_png(
                fileExport, // La ruta donde se guardará la imagen
                atlasWidth,      // Ancho de la imagen
                atlasHeight,     // Alto de la imagen
                4,          // Número de canales (4 para RGBA)
                atlasBuffer, // Buffer que contiene los datos de la imagen
                atlasWidth * 4   // Stride: el número de bytes por fila (ancho * número de canales)
        );

        if (!success) {
            throw new RuntimeException("Error al guardar la imagen PNG en: " + fileExport);
        } else {
            System.out.println("Imagen guardada en: " + fileExport);
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
}*/

public class TextureAtlas {
    private List<TextureData> textures;
    private int width, height;

    private ByteBuffer bakedAtlas;

    public TextureAtlas(int width, int height) {
        this.textures = new ArrayList<>();
        this.width = width;
        this.height = height;
    }

    public ByteBuffer loadTexture(String filepath) {
        String resourcePath = FileManager.getOrExtractFile(
                FileManager.toResources(filepath)
        );

        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);

        STBImage.stbi_set_flip_vertically_on_load(true);
        ByteBuffer image = STBImage.stbi_load(resourcePath, width, height, channels, 4); // 4 para RGBA
        if (image == null) {
            throw new RuntimeException("Failed to load texture: " + STBImage.stbi_failure_reason());
        }

        textures.add(new TextureData(image, width.get(0), height.get(0)));

        return image;
    }

    public ByteBuffer createTextureAtlas() {
        ByteBuffer atlas = ByteBuffer.allocateDirect(this.width * this.height * 4); // RGBA

        ByteBuffer[] byteBuffers = new ByteBuffer[this.textures.size()];
        int[] widths = new int[this.textures.size()];
        int[] heights = new int[this.textures.size()];

        for (int i = 0; i < this.textures.size(); i++) {
            TextureData textureData = this.textures.get(i);

            byteBuffers[i] = textureData.buffer;
            widths[i] = textureData.width;
            heights[i] = textureData.height;
        }

        int currentX = 0;
        int currentY = 0;
        int maxRowHeight = 0;

        for (int i = 0; i < byteBuffers.length; i++) {
            ByteBuffer texture = byteBuffers[i];
            int width = widths[i];
            int height = heights[i];

            String text = (currentX + width) + " > " + this.width + "; " + (currentY + height) + " > " + this.height;
            System.out.println(text);

            // Verifica que no sobrepase los límites del atlas
            if (currentX + width > this.width || currentY + height > this.height) {
                throw new RuntimeException("Texture atlas overflow: not enough space to place texture.");
            }

            // Copia los píxeles de la textura al atlas
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int srcIndex = (y * width + x) * 4; // RGBA
                    int dstIndex = ((currentY + y) * this.width + (currentX + x)) * 4;

                    // Verifica si los índices están dentro de los límites
                    if (srcIndex >= texture.capacity()) {
                        throw new RuntimeException("Attempting to read outside the bounds of the texture buffer.");
                    }
                    if (dstIndex >= atlas.capacity()) {
                        throw new RuntimeException("Attempting to write outside the bounds of the atlas buffer.");
                    }

                    atlas.put(dstIndex, texture.get(srcIndex));     // R
                    atlas.put(dstIndex + 1, texture.get(srcIndex + 1)); // G
                    atlas.put(dstIndex + 2, texture.get(srcIndex + 2)); // B
                    atlas.put(dstIndex + 3, texture.get(srcIndex + 3)); // A
                }
            }

            currentX += width;
            maxRowHeight = Math.max(maxRowHeight, height);

            if (currentX + width > this.width) {
                currentX = 0;
                currentY += maxRowHeight;
                maxRowHeight = 0;
            }
        }

        this.bakedAtlas = atlas;
        return atlas;
    }

    public void saveAtlasToPNG(String filepath) {
        String fileExport = FileManager.createDirectory("exports") + "/" + filepath;

        STBImageWrite.stbi_write_png(fileExport, this.width, this.height, 4, this.bakedAtlas, this.width * 4); // 4 para RGBA
    }

    public class TextureData {
        private ByteBuffer buffer;
        private int width, height;

        public TextureData(ByteBuffer buffer, int width, int height) {
            this.buffer = buffer;
            this.width = width;
            this.height = height;
        }
    }
}