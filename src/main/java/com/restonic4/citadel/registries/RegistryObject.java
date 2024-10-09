package com.restonic4.citadel.registries;

import com.restonic4.citadel.exceptions.RegistryObjectException;

public abstract class RegistryObject {
    private AssetLocation assetLocation;
    private boolean isPopulated = false;

    public AssetLocation getAssetLocation() {
        if (assetLocation == null) {
            throw new RegistryObjectException("This RegistryObject is not populated yet");
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
        this.isPopulated = true;
    }
}
