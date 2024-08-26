package me.restonic4.engine.registries;

import me.restonic4.engine.util.debug.diagnosis.Logger;
import me.restonic4.game.core.world.sounds.Sounds;

import java.util.Objects;

public class RegistryManager {
    public static void registerAll() {
        Logger.log("Starting all the registries");

        Sounds.register();
    }

    public static String getAssetPath(RegistryObject registryObject) {
        RegistryKey<?> registryKey = registryObject.getAssetLocation().getRegistryKey();

        String path;

        if (Objects.equals(registryKey.getKey(), "sound")) {
            path = "sounds/" + registryObject.getAssetLocation().getPath() + ".ogg";
        }
        else {
            path = registryObject.getAssetLocation().getPath();
        }

        return path;
    }
}
