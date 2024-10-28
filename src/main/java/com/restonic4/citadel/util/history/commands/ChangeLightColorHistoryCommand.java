package com.restonic4.citadel.util.history.commands;

import com.restonic4.citadel.core.editor.LevelEditor;
import com.restonic4.citadel.util.history.HistoryCommand;
import com.restonic4.citadel.world.object.components.LightComponent;
import org.joml.Vector3f;

public class ChangeLightColorHistoryCommand implements HistoryCommand {
    private final LightComponent lightComponent;
    private final Vector3f newColor;
    private final Vector3f oldColor;

    public ChangeLightColorHistoryCommand(LightComponent lightComponent, Vector3f newColor) {
        this.lightComponent = lightComponent;
        this.newColor = newColor;
        this.oldColor = lightComponent.getColor();
    }

    @Override
    public void execute() {
        LevelEditor.setUnsaved(true);
        this.lightComponent.setColor(newColor);
    }

    @Override
    public void undo() {
        LevelEditor.setUnsaved(true);
        this.lightComponent.setColor(oldColor);
    }
}
