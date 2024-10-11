package com.restonic4.citadel.util.history.commands;

import com.restonic4.citadel.core.CitadelLauncher;
import com.restonic4.citadel.core.editor.LevelEditor;
import com.restonic4.citadel.files.FileManager;
import com.restonic4.citadel.registries.built_in.managers.ImGuiScreens;
import com.restonic4.citadel.util.history.HistoryCommand;
import com.restonic4.citadel.world.object.GameObject;
import org.joml.Vector3f;

import java.io.IOException;

public class MoveGameObjectHistoryCommand implements HistoryCommand {
    private final GameObject gameObject;
    private final Vector3f newPosition;
    private final Vector3f oldPosition;

    public MoveGameObjectHistoryCommand(GameObject gameObject, Vector3f newPosition) {
        this.gameObject = gameObject;
        this.newPosition = newPosition;
        this.oldPosition = new Vector3f(gameObject.transform.getPosition());
    }

    @Override
    public void execute() {
        LevelEditor.setUnsaved(true);
        this.gameObject.transform.setPosition(newPosition);
    }

    @Override
    public void undo() {
        LevelEditor.setUnsaved(true);
        this.gameObject.transform.setPosition(oldPosition);
    }
}
