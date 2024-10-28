package com.restonic4.citadel.registries.built_in.managers;

import com.restonic4.citadel.registries.AbstractRegistryInitializer;
import com.restonic4.citadel.registries.AssetLocation;
import com.restonic4.citadel.registries.Registries;
import com.restonic4.citadel.registries.Registry;
import com.restonic4.citadel.util.CitadelConstants;
import com.restonic4.citadel.world.object.components.*;

public class Components extends AbstractRegistryInitializer {
    public static DebugComponent DEBUG;
    public static ModelRendererComponent MODEL_RENDERER;
    public static LightComponent LIGHT;
    public static CameraComponent CAMERA;
    public static ColliderComponent COLLIDER;
    public static RigidBodyComponent RIGID_BODY;

    @Override
    public void register() {
        DEBUG = (DebugComponent) Registry.register(Registries.COMPONENT, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "debug"), new DebugComponent().setSerializerID("db"));
        MODEL_RENDERER = (ModelRendererComponent) Registry.register(Registries.COMPONENT, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "model_renderer"), new ModelRendererComponent().setSerializerID("mr"));
        LIGHT = (LightComponent) Registry.register(Registries.COMPONENT, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "light"), new LightComponent().setSerializerID("l"));
        CAMERA = (CameraComponent) Registry.register(Registries.COMPONENT, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "camera"), new CameraComponent().setSerializerID("cam"));
        COLLIDER = (ColliderComponent) Registry.register(Registries.COMPONENT, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "collider"), new ColliderComponent().setSerializerID("c"));
        RIGID_BODY = (RigidBodyComponent) Registry.register(Registries.COMPONENT, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "rigid_body"), new RigidBodyComponent().setSerializerID("rb"));
    }
}
