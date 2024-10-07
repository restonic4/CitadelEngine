package com.restonic4.citadel.registries.built_in.managers;

import com.restonic4.citadel.registries.AbstractRegistryInitializer;
import com.restonic4.citadel.registries.AssetLocation;
import com.restonic4.citadel.registries.Registries;
import com.restonic4.citadel.registries.Registry;
import com.restonic4.citadel.registries.built_in.types.KeyBind;
import com.restonic4.citadel.util.CitadelConstants;
import org.lwjgl.glfw.GLFW;

public class KeyBinds extends AbstractRegistryInitializer {
    public static KeyBind CRASH;
    public static KeyBind TOGGLE_STATISTICS_GUI;
    public static KeyBind TOGGLE_DEV_GUI;
    public static KeyBind UNDO;
    public static KeyBind REDO;
    public static KeyBind REDO_ALT;
    public static KeyBind COPY;
    public static KeyBind PASTE;
    public static KeyBind DUPLICATE;
    public static KeyBind DELETE;

    @Override
    public void register() {
        CRASH = Registry.register(Registries.KEY_BIND, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "crash"), new KeyBind(GLFW.GLFW_KEY_ESCAPE, GLFW.GLFW_KEY_F9));
        TOGGLE_STATISTICS_GUI = Registry.register(Registries.KEY_BIND, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "toggle_statistics_gui"), new KeyBind(GLFW.GLFW_KEY_ESCAPE, GLFW.GLFW_KEY_F3));
        TOGGLE_DEV_GUI = Registry.register(Registries.KEY_BIND, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "toggle_dev_gui"), new KeyBind(GLFW.GLFW_KEY_ESCAPE, GLFW.GLFW_KEY_F4));
        UNDO = Registry.register(Registries.KEY_BIND, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "undo"), new KeyBind(GLFW.GLFW_KEY_LEFT_CONTROL, GLFW.GLFW_KEY_Z));
        REDO = Registry.register(Registries.KEY_BIND, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "redo"), new KeyBind(GLFW.GLFW_KEY_LEFT_CONTROL, GLFW.GLFW_KEY_Y));
        REDO_ALT = Registry.register(Registries.KEY_BIND, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "redo_alt"), new KeyBind(GLFW.GLFW_KEY_LEFT_CONTROL, GLFW.GLFW_KEY_LEFT_SHIFT, GLFW.GLFW_KEY_Z));
        COPY = Registry.register(Registries.KEY_BIND, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "copy"), new KeyBind(GLFW.GLFW_KEY_LEFT_CONTROL, GLFW.GLFW_KEY_C));
        PASTE = Registry.register(Registries.KEY_BIND, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "paste"), new KeyBind(GLFW.GLFW_KEY_LEFT_CONTROL, GLFW.GLFW_KEY_V));
        DUPLICATE = Registry.register(Registries.KEY_BIND, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "duplicate"), new KeyBind(GLFW.GLFW_KEY_LEFT_CONTROL, GLFW.GLFW_KEY_D));
        DELETE = Registry.register(Registries.KEY_BIND, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "delete"), new KeyBind(GLFW.GLFW_KEY_DELETE));
    }
}
