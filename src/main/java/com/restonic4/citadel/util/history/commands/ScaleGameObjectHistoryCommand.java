package com.restonic4.citadel.util.history.commands;

import com.restonic4.citadel.core.editor.LevelEditor;
import com.restonic4.citadel.util.history.HistoryCommand;
import com.restonic4.citadel.world.object.GameObject;
import org.joml.Vector3f;

public class ScaleGameObjectHistoryCommand implements HistoryCommand {
    private final GameObject gameObject;
    private final Vector3f newScale;
    private final Vector3f oldScale;

    public ScaleGameObjectHistoryCommand(GameObject gameObject, Vector3f newScale) {
        this.gameObject = gameObject;
        this.newScale = newScale;
        this.oldScale = new Vector3f(gameObject.transform.getScale());
    }

    @Override
    public void execute() {
        LevelEditor.setUnsaved(true);
        this.gameObject.transform.setScale(newScale);
    }

    @Override
    public void undo() {
        LevelEditor.setUnsaved(true);
        this.gameObject.transform.setScale(oldScale);
    }
}
