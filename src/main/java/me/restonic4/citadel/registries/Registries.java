package me.restonic4.citadel.registries;

import me.restonic4.citadel.registries.types.ProfilerStat;
import me.restonic4.citadel.registries.types.Sound;

import java.util.ArrayList;
import java.util.List;

public class Registries {
    // List of built-in keys for you to use, yippie!

    public static final RegistryKey<Sound> SOUND = new RegistryKey<>("sound", "assets/sounds", "ogg");
    public static final RegistryKey<Sound> MUSIC = new RegistryKey<>("music", "assets/sounds/music", "ogg");
    public static final RegistryKey<ProfilerStat> PROFILER_STAT = new RegistryKey<>("profiler_stat");

    // Custom key system, where you can register your own keys

    private static final List<RegistryKey<?>> customKeys = new ArrayList<>();

    public static <T extends RegistryObject> RegistryKey<T> registerCustomKey(String id) {
        return registerCustomKey(id, null, null);
    }

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
