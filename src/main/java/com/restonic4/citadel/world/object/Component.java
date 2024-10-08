package com.restonic4.citadel.world.object;

import com.restonic4.citadel.util.StringBuilderHelper;
import com.restonic4.citadel.util.UniqueIdentifierManager;
import imgui.ImGui;

import java.io.Serial;

public abstract class Component extends Serializable {
    private final int id;
    public GameObject gameObject = null;

    public Component() {
        this.id = UniqueIdentifierManager.generateUID();
    }

    public void start() {

    }

    public abstract void update();

    public void renderEditorUI() {
        ImGui.text(StringBuilderHelper.concatenate("ID: ", this.getId()));
    }

    public int getId() {
        return this.id;
    }
}
