package me.restonic4.citadel.registries.built_in.managers;

import me.restonic4.citadel.registries.AbstractRegistryInitializer;
import me.restonic4.citadel.registries.AssetLocation;
import me.restonic4.citadel.registries.Registries;
import me.restonic4.citadel.registries.Registry;
import me.restonic4.citadel.registries.built_in.types.Sound;
import me.restonic4.citadel.util.CitadelConstants;

public class Sounds extends AbstractRegistryInitializer {
    public static Sound CRASH;

    public void register() {
        CRASH = Registry.register(Registries.SOUND, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "crash"), new Sound());
    }
}
