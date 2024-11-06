package com.restonic4.citadel.registries.built_in.managers;

import com.restonic4.citadel.registries.AbstractRegistryInitializer;
import com.restonic4.citadel.registries.AssetLocation;
import com.restonic4.citadel.registries.Registries;
import com.restonic4.citadel.registries.Registry;
import com.restonic4.citadel.registries.built_in.types.LevelEditorAddTemplate;
import com.restonic4.citadel.util.CitadelConstants;

public class LevelEditorAddTemplates extends AbstractRegistryInitializer {
    public static LevelEditorAddTemplate EMPTY;
    public static LevelEditorAddTemplate CUBE;
    public static LevelEditorAddTemplate SUN;

    @Override
    public void register() {
        EMPTY = Registry.register(
                Registries.LEVEL_EDITOR_ADD_TEMPLATE,
                new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "empty"),
                new LevelEditorAddTemplate("Empty")
        );

        CUBE = Registry.register(
                Registries.LEVEL_EDITOR_ADD_TEMPLATE,
                new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "cube"),
                new LevelEditorAddTemplate("Cube")
        );

        SUN = Registry.register(
                Registries.LEVEL_EDITOR_ADD_TEMPLATE,
                new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "sun"),
                new LevelEditorAddTemplate("Sun")
        );
    }
}
