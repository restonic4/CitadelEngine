package com.restonic4.citadel.util.history.commands;

import com.restonic4.citadel.core.editor.LevelEditor;
import com.restonic4.citadel.util.history.HistoryCommand;
import com.restonic4.citadel.world.object.GameObject;
import com.restonic4.citadel.world.object.components.LightComponent;
import org.joml.Vector3f;

public class ChangeLightTypeHistoryCommand implements HistoryCommand {
    private final LightComponent lightComponent;
    private final LightComponent.LightType newLightType;
    private final LightComponent.LightType oldLightType;

    public ChangeLightTypeHistoryCommand(LightComponent lightComponent, LightComponent.LightType newLightType) {
        this.lightComponent = lightComponent;
        this.newLightType = newLightType;
        this.oldLightType = lightComponent.getLightType();
    }

    @Override
    public void execute() {
        LevelEditor.setUnsaved(true);
        this.lightComponent.setLightType(newLightType);
    }

    @Override
    public void undo() {
        LevelEditor.setUnsaved(true);
        this.lightComponent.setLightType(oldLightType);
    }
}
