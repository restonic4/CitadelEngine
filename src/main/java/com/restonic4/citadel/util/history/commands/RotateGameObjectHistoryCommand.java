package com.restonic4.citadel.util.history.commands;

import com.restonic4.citadel.core.editor.LevelEditor;
import com.restonic4.citadel.util.history.HistoryCommand;
import com.restonic4.citadel.world.object.GameObject;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class RotateGameObjectHistoryCommand implements HistoryCommand {
    private final GameObject gameObject;
    private final Quaternionf newRotation;
    private final Quaternionf oldRotation;

    public RotateGameObjectHistoryCommand(GameObject gameObject, Quaternionf newRotation) {
        this.gameObject = gameObject;
        this.newRotation = newRotation;
        this.oldRotation = new Quaternionf(gameObject.transform.getRotation());
    }

    @Override
    public void execute() {
        LevelEditor.setUnsaved(true);
        this.gameObject.transform.setRotation(newRotation);
    }

    @Override
    public void undo() {
        LevelEditor.setUnsaved(true);
        this.gameObject.transform.setRotation(oldRotation);
    }
}
