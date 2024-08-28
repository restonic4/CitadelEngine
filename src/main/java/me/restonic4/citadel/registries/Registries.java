package me.restonic4.citadel.registries;

import me.restonic4.game.core.world.sounds.Sound;

import java.util.ArrayList;
import java.util.List;

public class Registries {
    public static final RegistryKey<Sound> SOUND = new RegistryKey<>("sound", "assets/sounds", "ogg");
    public static final RegistryKey<Sound> MUSIC = new RegistryKey<>("music", "assets/sounds/music", "ogg");

    private static final List<RegistryKey<?>> customKeys = new ArrayList<>();

    public static <T extends RegistryObject> RegistryKey<T> registerCustomKey(String id, String rootDirectory, String assetFileExtension) {
        RegistryKey<T> customKey = new RegistryKey<>(id, rootDirectory, assetFileExtension);
        customKeys.add(customKey);

        return customKey;
    }

    public static <T extends RegistryObject> RegistryKey<T> getCustomKey(String id) {
        for (RegistryKey<?> key : customKeys) {
            if (key.getKey().equals(id)) {
                RegistryKey<T> foundKey = (RegistryKey<T>) key;
                return foundKey;
            }
        }

        return null;
    }
}
