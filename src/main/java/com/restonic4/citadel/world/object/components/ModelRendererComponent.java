package com.restonic4.citadel.world.object.components;

import com.restonic4.citadel.render.gui.ImGuiHelper;
import com.restonic4.citadel.util.StringBuilderHelper;
import com.restonic4.citadel.world.object.*;
import imgui.ImGui;

public class ModelRendererComponent extends Component {
    private Mesh mesh;
    private Material material;

    public ModelRendererComponent(Mesh mesh) {
        this.mesh = mesh;
        this.material = new Material();
    }

    public ModelRendererComponent(Mesh mesh, Material material) {
        this.mesh = mesh;
        this.material = material;
    }

    @Override
    public void update() {

    }

    public Mesh getMesh() {
        return this.mesh;
    }

    public Material getMaterial() {
        return material;
    }
}
