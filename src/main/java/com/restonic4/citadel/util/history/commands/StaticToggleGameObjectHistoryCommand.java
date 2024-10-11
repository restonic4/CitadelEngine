package com.restonic4.citadel.util.history.commands;

import com.restonic4.citadel.core.editor.LevelEditor;
import com.restonic4.citadel.util.history.HistoryCommand;
import com.restonic4.citadel.world.object.GameObject;
import org.joml.Vector3f;

public class StaticToggleGameObjectHistoryCommand implements HistoryCommand {
    private final GameObject gameObject;
    private final boolean newValue;

    public StaticToggleGameObjectHistoryCommand(GameObject gameObject, boolean newValue) {
        this.gameObject = gameObject;
        this.newValue = newValue;
    }

    @Override
    public void execute() {
        LevelEditor.setUnsaved(true);
        this.gameObject.setStatic(newValue);
    }

    @Override
    public void undo() {
        LevelEditor.setUnsaved(true);
        this.gameObject.setStatic(!newValue);
    }
}
