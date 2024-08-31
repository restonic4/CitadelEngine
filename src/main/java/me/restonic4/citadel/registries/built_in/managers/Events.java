package me.restonic4.citadel.registries.built_in.managers;

import me.restonic4.citadel.registries.AbstractRegistryInitializer;
import me.restonic4.citadel.registries.AssetLocation;
import me.restonic4.citadel.registries.Registries;
import me.restonic4.citadel.registries.Registry;
import me.restonic4.citadel.registries.built_in.types.Event;
import me.restonic4.citadel.util.CitadelConstants;

public class Events extends AbstractRegistryInitializer {
    public static Event SCENE_LOADED;

    @Override
    public void register() {
        SCENE_LOADED = Registry.register(Registries.EVENT, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "scene_loaded"), new Event());

        /*SCENE_LOADED.listen((a, b) -> {

        });*/
    }
}
