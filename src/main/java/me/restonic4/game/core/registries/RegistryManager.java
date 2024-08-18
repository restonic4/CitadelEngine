package me.restonic4.game.core.registries;

import me.restonic4.engine.util.debug.Logger;
import me.restonic4.game.core.world.sounds.Sounds;

import java.util.Objects;

public class RegistryManager {
    public static void registerAll() {
        Logger.log("Starting all the registries");

        Sounds.register();
    }

    public static String getAssetPath(RegistryItem registryItem) {
        RegistryKey<?> registryKey = registryItem.getAssetLocation().getRegistryKey();

        String path;

        if (Objects.equals(registryKey.getKey(), "sound")) {
            path = "sounds/" + registryItem.getAssetLocation().getPath() + ".ogg";
        }
        else {
            path = registryItem.getAssetLocation().getPath();
        }

        return path;
    }
}
