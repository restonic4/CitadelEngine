package com.restonic4.citadel.core.editor.templates.add;

import com.restonic4.citadel.files.parsers.mesh.MeshLoader;
import com.restonic4.citadel.registries.built_in.types.LevelEditorAddTemplate;
import com.restonic4.citadel.world.SceneManager;
import com.restonic4.citadel.world.object.GameObject;
import com.restonic4.citadel.world.object.Transform;
import com.restonic4.citadel.world.object.components.ModelRendererComponent;

public class CubeLevelEditorAddTemplate extends LevelEditorAddTemplate {
    public CubeLevelEditorAddTemplate(String name) {
        super(name);
    }

    @Override
    public void add(Transform parent) {
        GameObject gameObject = new GameObject("Cube GameObject", false);
        gameObject.addComponent(new ModelRendererComponent(MeshLoader.loadMesh("assets/models/cube.obj")));
        gameObject.transform.setParent(parent);

        SceneManager.getCurrentScene().addGameObject(gameObject);
    }
}
