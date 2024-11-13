package com.restonic4.citadel.core.editor.templates.add;

import com.restonic4.citadel.registries.built_in.types.LevelEditorAddTemplate;
import com.restonic4.citadel.world.SceneManager;
import com.restonic4.citadel.world.object.GameObject;

public class EmptyLevelEditorAddTemplate extends LevelEditorAddTemplate {
    public EmptyLevelEditorAddTemplate(String name) {
        super(name);
    }

    @Override
    public void add() {
        GameObject gameObject = new GameObject("Empty GameObject", false);
        SceneManager.getCurrentScene().addGameObject(gameObject);
    }
}
