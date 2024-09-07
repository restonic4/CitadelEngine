package me.restonic4.citadel.registries.built_in.types;

import imgui.ImGui;
import me.restonic4.citadel.registries.RegistryObject;

public class ImGuiScreen extends RegistryObject {
    public void render() {
        ImGui.begin(this.getAssetLocation().toString());
        ImGui.text("This is empty...");
        ImGui.end();
    };
}
