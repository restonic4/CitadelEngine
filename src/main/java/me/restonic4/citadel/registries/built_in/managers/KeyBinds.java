package me.restonic4.citadel.registries.built_in.managers;

import me.restonic4.citadel.registries.AbstractRegistryInitializer;
import me.restonic4.citadel.registries.AssetLocation;
import me.restonic4.citadel.registries.Registries;
import me.restonic4.citadel.registries.Registry;
import me.restonic4.citadel.registries.built_in.types.KeyBind;
import me.restonic4.citadel.util.CitadelConstants;
import org.lwjgl.glfw.GLFW;

public class KeyBinds extends AbstractRegistryInitializer {
    public static KeyBind CRASH;
    public static KeyBind TOGGLE_STATISTICS_GUI;
    public static KeyBind TOGGLE_DEV_GUI;

    @Override
    public void register() {
        CRASH = Registry.register(Registries.KEY_BIND, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "crash"), new KeyBind(GLFW.GLFW_KEY_ESCAPE, GLFW.GLFW_KEY_F9));
        TOGGLE_STATISTICS_GUI = Registry.register(Registries.KEY_BIND, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "toggle_statistics_gui"), new KeyBind(GLFW.GLFW_KEY_ESCAPE, GLFW.GLFW_KEY_F3));
        TOGGLE_DEV_GUI = Registry.register(Registries.KEY_BIND, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "toggle_dev_gui"), new KeyBind(GLFW.GLFW_KEY_ESCAPE, GLFW.GLFW_KEY_F4));
    }
}
