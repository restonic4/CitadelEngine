package com.restonic4.citadel.registries.built_in.managers;

import com.restonic4.citadel.registries.AbstractRegistryInitializer;
import com.restonic4.citadel.registries.AssetLocation;
import com.restonic4.citadel.registries.Registries;
import com.restonic4.citadel.registries.Registry;
import com.restonic4.citadel.registries.built_in.types.Icon;
import com.restonic4.citadel.registries.built_in.types.subtypes.IconSize;
import com.restonic4.citadel.util.CitadelConstants;

public class Icons extends AbstractRegistryInitializer {
    public static Icon ICON;

    // Files
    public static Icon FOLDER;
    public static Icon RESERVED_FOLDER;
    public static Icon GENERIC_FILE;
    public static Icon IMAGE_FILE;
    public static Icon OBJECT_FILE;
    public static Icon JSON_FILE;
    public static Icon TEXT_FILE;
    public static Icon AUDIO_FILE;
    public static Icon SHADER_FILE;
    public static Icon SCENE_FILE;

    // Scene inspector
    public static Icon GAME_OBJECT;

    @Override
    public void register() {
        ICON = Registry.register(Registries.ICON, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "icon"), new Icon());
        FOLDER = Registry.register(Registries.ICON, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "folder"), new Icon());
        RESERVED_FOLDER = Registry.register(Registries.ICON, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "reserved_folder"), new Icon());
        GENERIC_FILE = Registry.register(Registries.ICON, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "generic_file"), new Icon());
        IMAGE_FILE = Registry.register(Registries.ICON, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "image_file"), new Icon());
        OBJECT_FILE = Registry.register(Registries.ICON, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "object_file"), new Icon());
        JSON_FILE = Registry.register(Registries.ICON, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "json_file"), new Icon());
        TEXT_FILE = Registry.register(Registries.ICON, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "text_file"), new Icon());
        AUDIO_FILE = Registry.register(Registries.ICON, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "audio_file"), new Icon());
        SHADER_FILE = Registry.register(Registries.ICON, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "shader_file"), new Icon());
        SCENE_FILE = Registry.register(Registries.ICON, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "scene_file"), new Icon());
        GAME_OBJECT = Registry.register(Registries.ICON, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "game_object"), new Icon());
    }
}
