package me.restonic4.citadel.registries;

import me.restonic4.citadel.util.debug.diagnosis.Logger;
import me.restonic4.game.core.world.sounds.Sounds;

import java.util.ArrayList;
import java.util.List;

public class RegistryManager {
    private static List<AbstractRegistryInitializer> builtInRegistries = new ArrayList<>();
    private static List<AbstractRegistryInitializer> customRegistries = new ArrayList<>();

    public static void registerBuiltIn() {
        Logger.log("Starting all the built-in registries");

        for (AbstractRegistryInitializer abstractRegistryInitializer : builtInRegistries) {
            abstractRegistryInitializer.register();
        }
    }

    public static void registerCustom() {
        Logger.log("Starting all the custom registries");

        for (AbstractRegistryInitializer abstractRegistryInitializer : customRegistries) {
            abstractRegistryInitializer.register();
        }
    }

    public static void registerBuiltInRegistrySet(AbstractRegistryInitializer abstractRegistryInitializer) {
        builtInRegistries.add(abstractRegistryInitializer);
    }

    public static void registerRegistrySet(AbstractRegistryInitializer abstractRegistryInitializer) {
        customRegistries.add(abstractRegistryInitializer);
    }
}
