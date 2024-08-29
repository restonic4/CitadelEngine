package me.restonic4.citadel.registries;

import me.restonic4.citadel.exceptions.RegistryItemException;

public abstract class RegistryObject {
    private AssetLocation assetLocation;

    public AssetLocation getAssetLocation() {
        if (assetLocation == null) {
            throw new RegistryItemException("This RegistryItem is not populated yet");
        }

        return this.assetLocation;
    }

    public void setAssetLocation(AssetLocation assetLocation) {
        this.assetLocation = assetLocation;
    }

    public String getAssetPath() {
        RegistryKey<?> registryKey = this.getAssetLocation().getRegistryKey();

        return registryKey.getRootDirectory() + "/" + this.getAssetLocation().getPath() + "." + registryKey.getAssetFileExtension();
    }

    //Gets called when the game registers the item
    public void onPopulate() {
        return;
    }
}
