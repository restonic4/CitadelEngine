package com.restonic4.citadel.util.history.commands;

import com.restonic4.citadel.core.editor.LevelEditor;
import com.restonic4.citadel.util.history.HistoryCommand;
import com.restonic4.citadel.world.object.GameObject;

public class RenameGameObjectHistoryCommand implements HistoryCommand {
    private final GameObject gameObject;
    private final String newName;
    private String oldName;

    public RenameGameObjectHistoryCommand(GameObject gameObject, String newName) {
        this.gameObject = gameObject;
        this.newName = newName;
        this.oldName = gameObject.getName();
    }

    @Override
    public void execute() {
        LevelEditor.setUnsaved(true);
        this.oldName = gameObject.getName();
        gameObject.setName(newName);
    }

    @Override
    public void undo() {
        LevelEditor.setUnsaved(true);
        gameObject.setName(oldName);
    }
}
