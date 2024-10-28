package com.restonic4.citadel.world.object;

import com.restonic4.citadel.files.Serializable;
import com.restonic4.citadel.registries.RegistryObject;
import com.restonic4.citadel.util.StringBuilderHelper;
import com.restonic4.citadel.util.UniqueIdentifierManager;
import imgui.ImGui;

public abstract class Component extends RegistryObject implements Serializable {
    private final int id;
    private String serializerID;
    public GameObject gameObject = null;

    public Component() {
        this.id = UniqueIdentifierManager.generateUID();
        this.serializerID = "none";
    }

    public Component(String serializerID) {
        this.id = UniqueIdentifierManager.generateUID();
        this.serializerID = serializerID;
    }

    public void start() {}

    public abstract void update();

    public void renderEditorUI() {
        ImGui.text(StringBuilderHelper.concatenate("ID: ", this.getId()));
    }

    public int getId() {
        return this.id;
    }

    public String getSerializerID() {
        return this.serializerID;
    }

    public Component setSerializerID(String serializerID) {
        this.serializerID = serializerID;
        return this;
    }
}
