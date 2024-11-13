package com.restonic4.citadel.util.history.commands;

import com.restonic4.citadel.core.editor.LevelEditor;
import com.restonic4.citadel.util.history.HistoryCommand;
import com.restonic4.citadel.world.Scene;
import com.restonic4.citadel.world.object.GameObject;

public class AddGameObjectHistoryCommand implements HistoryCommand {
    private final Scene scene;
    private final GameObject gameObject;

    public AddGameObjectHistoryCommand(Scene scene, GameObject gameObject) {
        this.scene = scene;
        this.gameObject = gameObject;
    }

    @Override
    public void execute() {
        LevelEditor.setUnsaved(true);
        this.scene.addGameObject(this.gameObject);
    }

    @Override
    public void undo() {
        LevelEditor.setUnsaved(true);
        this.scene.removeGameObject(this.gameObject);
    }
}
