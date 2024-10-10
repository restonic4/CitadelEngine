package com.restonic4.citadel.render.gui.guis.editor;

import com.restonic4.ClientSide;
import com.restonic4.citadel.registries.built_in.managers.Icons;
import com.restonic4.citadel.registries.built_in.types.subtypes.IconSize;
import com.restonic4.citadel.render.gui.ImGuiHelper;
import com.restonic4.citadel.render.gui.guis.ToggleableImGuiScreen;
import com.restonic4.citadel.util.GitHubHelper;
import com.restonic4.citadel.util.GradleUtil;
import com.restonic4.citadel.util.StringBuilderHelper;
import imgui.ImGui;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiWindowFlags;

@ClientSide
public class EditorMessageImGui extends ToggleableImGuiScreen {
    String message;

    @Override
    public void render() {
        if (!isVisible()) {
            return;
        }

        ImGui.begin("Message", ImGuiWindowFlags.NoDocking);
        ImGui.setNextWindowSize(100, 300, ImGuiCond.Appearing);

        ImGui.textWrapped(message);

        ImGui.newLine();

        ImGuiHelper.setCentered(100);
        if (ImGui.button("Close", 100, 30)) {
            hide();
        }

        ImGui.end();
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void show(String message) {
        setMessage(message);
        this.show();
    }
}