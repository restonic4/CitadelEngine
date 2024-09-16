package me.restonic4.citadel.registries.built_in.managers;

import me.restonic4.citadel.registries.AbstractRegistryInitializer;
import me.restonic4.citadel.registries.AssetLocation;
import me.restonic4.citadel.registries.Registries;
import me.restonic4.citadel.registries.Registry;
import me.restonic4.citadel.registries.built_in.types.FrameBuffer;
import me.restonic4.citadel.render.frame_buffers.ShadowFrameBuffer;
import me.restonic4.citadel.util.CitadelConstants;

public class FrameBuffers extends AbstractRegistryInitializer {
    public static ShadowFrameBuffer SHADOWS;

    @Override
    public void register() {
        SHADOWS = (ShadowFrameBuffer) Registry.register(Registries.FRAME_BUFFER, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "shadows"), new ShadowFrameBuffer(CitadelConstants.SHADOW_RESOLUTION));
        //SHADOWS = Registry.register(Registries.FRAME_BUFFER, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "shadows"), new FrameBuffer(CitadelConstants.SHADOW_RESOLUTION, CitadelConstants.SHADOW_RESOLUTION));
    }
}
