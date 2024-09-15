package me.restonic4.citadel.registries.built_in.managers;

import me.restonic4.citadel.registries.AbstractRegistryInitializer;
import me.restonic4.citadel.registries.AssetLocation;
import me.restonic4.citadel.registries.Registries;
import me.restonic4.citadel.registries.Registry;
import me.restonic4.citadel.render.Shader;
import me.restonic4.citadel.util.CitadelConstants;

public class Shaders extends AbstractRegistryInitializer {
    public static Shader MAIN;
    public static Shader SHADOWS;

    @Override
    public void register() {
        MAIN = Registry.register(Registries.SHADER, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "main"), new Shader());
        SHADOWS = Registry.register(Registries.SHADER, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "shadows"), new Shader());
    }
}
