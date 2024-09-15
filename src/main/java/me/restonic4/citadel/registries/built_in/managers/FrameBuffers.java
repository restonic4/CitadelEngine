package me.restonic4.citadel.registries.built_in.managers;

import me.restonic4.citadel.registries.AbstractRegistryInitializer;
import me.restonic4.citadel.registries.AssetLocation;
import me.restonic4.citadel.registries.Registries;
import me.restonic4.citadel.registries.Registry;
import me.restonic4.citadel.registries.built_in.types.FrameBuffer;
import me.restonic4.citadel.util.CitadelConstants;

public class FrameBuffers extends AbstractRegistryInitializer {
    public static FrameBuffer TEST;

    @Override
    public void register() {
        TEST = Registry.register(Registries.FRAME_BUFFER, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "test"), new FrameBuffer());
    }
}
