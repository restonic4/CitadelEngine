package com.restonic4.citadel.registries.built_in.types;

import com.restonic4.citadel.registries.RegistryObject;
import com.restonic4.citadel.util.debug.diagnosis.Logger;
import com.restonic4.citadel.world.object.Transform;

public class LevelEditorAddTemplate extends RegistryObject {
    private final String name;

    public LevelEditorAddTemplate(String name) {
        this.name = name;
    }

    public void add(Transform parent) {
        Logger.log(name + " added");
    }

    public String getName() {
        return this.name;
    }
}
