package com.restonic4.citadel.core.editor.templates.add;

import com.restonic4.citadel.registries.built_in.types.LevelEditorAddTemplate;
import com.restonic4.citadel.world.SceneManager;
import com.restonic4.citadel.world.object.GameObject;
import com.restonic4.citadel.world.object.Transform;

public class EmptyLevelEditorAddTemplate extends LevelEditorAddTemplate {
    public EmptyLevelEditorAddTemplate(String name) {
        super(name);
    }

    @Override
    public void add(Transform parent) {
        GameObject gameObject = new GameObject("Empty GameObject", false);
        gameObject.transform.setParent(parent);

        this.executeHistoryCommand(gameObject);
    }
}
