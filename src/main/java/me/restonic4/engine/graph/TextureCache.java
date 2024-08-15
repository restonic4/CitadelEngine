package me.restonic4.engine.graph;

import me.restonic4.engine.util.Constants;

import java.util.*;

public class TextureCache {
    private Map<String, Texture> textureMap;

    public TextureCache() {
        textureMap = new HashMap<>();
        textureMap.put(Constants.DEFAULT_TEXTURE, new Texture(Constants.DEFAULT_TEXTURE));
    }

    public void cleanup() {
        textureMap.values().forEach(Texture::cleanup);
    }

    public Texture createTexture(String texturePath) {
        return textureMap.computeIfAbsent(texturePath, Texture::new);
    }

    public Collection<Texture> getAll() {
        return textureMap.values();
    }

    public Texture getTexture(String texturePath) {
        Texture texture = null;
        if (texturePath != null) {
            texture = textureMap.get(texturePath);
        }
        if (texture == null) {
            texture = textureMap.get(Constants.DEFAULT_TEXTURE);
        }
        return texture;
    }
}
