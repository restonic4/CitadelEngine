package com.restonic4.citadel.registries.built_in.managers;

import com.restonic4.citadel.registries.AbstractRegistryInitializer;
import com.restonic4.citadel.registries.AssetLocation;
import com.restonic4.citadel.registries.Registries;
import com.restonic4.citadel.registries.Registry;
import com.restonic4.citadel.render.frame_buffers.GameViewportFrameBuffer;
import com.restonic4.citadel.render.frame_buffers.ShadowFrameBuffer;
import com.restonic4.citadel.util.CitadelConstants;

public class FrameBuffers extends AbstractRegistryInitializer {
    public static ShadowFrameBuffer SHADOWS;
    public static GameViewportFrameBuffer GAME_VIEWPORT;

    @Override
    public void register() {
        SHADOWS = (ShadowFrameBuffer) Registry.register(Registries.FRAME_BUFFER, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "shadows"), new ShadowFrameBuffer(CitadelConstants.SHADOW_RESOLUTION));
        GAME_VIEWPORT = (GameViewportFrameBuffer) Registry.register(Registries.FRAME_BUFFER, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "game_viewport"), new GameViewportFrameBuffer(1920, 1080, true));
    }
}
