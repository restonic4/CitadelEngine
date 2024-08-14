package me.restonic4.game.core.registries;

import me.restonic4.game.core.AssetLocation;
import me.restonic4.game.core.exceptions.RegistryItemException;

public abstract class RegistryItem {
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
}
