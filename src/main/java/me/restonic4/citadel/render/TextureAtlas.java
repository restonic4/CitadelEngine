package me.restonic4.citadel.render;

import me.restonic4.citadel.files.FileManager;

import org.lwjgl.stb.STBImageWrite;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class TextureAtlas {
    private List<Texture> textures;
    private int width, height, maxPieceHeight;

    private ByteBuffer bakedAtlas;
    private boolean isBaked = false;

    public TextureAtlas(int textureWidth, int textureHeight) {
        this.textures = new ArrayList<>();
        this.width = width;
        this.height = height;
    }

    public void addTexture(Texture texture) {
        if (!texture.isRaw()) {
            throw new IllegalStateException("Can't add regular textures to an atlas. The texture should be raw.");
        }

        textures.add(texture);
    }

    public boolean canFit(Texture texture) {
        return false;
    }

    public ByteBuffer createTextureAtlas() {
        ByteBuffer atlas = ByteBuffer.allocateDirect(this.width * this.height * 4); // RGBA

        ByteBuffer[] byteBuffers = new ByteBuffer[this.textures.size()];
        int[] widths = new int[this.textures.size()];
        int[] heights = new int[this.textures.size()];

        for (int i = 0; i < this.textures.size(); i++) {
            Texture textureData = this.textures.get(i);

            byteBuffers[i] = textureData.getImageBufferData();
            widths[i] = textureData.getWidth();
            heights[i] = textureData.getHeight();
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

    public boolean isBaked() {
        return this.isBaked;
    }
}