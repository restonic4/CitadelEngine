package me.restonic4.citadel.registries;

import me.restonic4.citadel.util.debug.diagnosis.Logger;
import me.restonic4.game.core.world.sounds.Sounds;

public class RegistryManager {
    public static void registerAll() {
        Logger.log("Starting all the registries");

        Sounds.register();
    }

    public static String getAssetPath(RegistryObject registryObject) {
        RegistryKey<?> registryKey = registryObject.getAssetLocation().getRegistryKey();

        return registryKey.getRootDirectory() + "/" + registryObject.getAssetLocation().getPath() + "." + registryKey.getAssetFileExtension();
    }
}
