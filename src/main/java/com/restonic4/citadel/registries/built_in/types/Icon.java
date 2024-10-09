package com.restonic4.citadel.registries.built_in.types;

import com.restonic4.citadel.core.CitadelLauncher;
import com.restonic4.citadel.registries.RegistryKey;
import com.restonic4.citadel.registries.RegistryObject;
import com.restonic4.citadel.registries.built_in.types.subtypes.IconSize;
import com.restonic4.citadel.render.Texture;
import com.restonic4.citadel.util.FlagFactory;
import imgui.ImGui;

import java.util.HashMap;
import java.util.Map;

public class Icon extends RegistryObject {
    private final Map<Integer, Texture> textures = new HashMap<>();

    private void generateTexture(RegistryKey<?> registryKey, IconSize iconSize) {
        textures.put(
                iconSize.getFlag(),
                new Texture(
                        true,
                        registryKey.getRootDirectory() + "/" + this.getAssetLocation().getPath() + "/" + iconSize.getPixelSize() + "." + registryKey.getAssetFileExtension()
                )
        );
    }

    public int getTextureID(int sizeFlag) {
        Texture texture = textures.get(sizeFlag);

        if (texture == null) {
            IconSize iconSize = IconSize.values()[Integer.numberOfTrailingZeros(sizeFlag)];
            generateTexture(this.getAssetLocation().getRegistryKey(), iconSize);
            texture = textures.get(sizeFlag);
        }

        if (texture == null) {
            throw new IllegalStateException("Texture ID for size flag " + sizeFlag + " is not available.");
        }

        return texture.getTextureID();
    }

    public void renderImGui(IconSize iconSize) {
        int textureID = getTextureID(iconSize.getFlag());
        ImGui.image(textureID, iconSize.getPixelSize(), iconSize.getPixelSize());
    }
}
