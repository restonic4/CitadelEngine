package com.restonic4.citadel.render.gui.guis.editor;

import com.restonic4.ClientSide;
import com.restonic4.citadel.render.Texture;
import com.restonic4.citadel.render.gui.ImGuiHelper;
import com.restonic4.citadel.render.gui.guis.ToggleableImGuiScreen;
import com.restonic4.citadel.util.GitHubHelper;
import com.restonic4.citadel.util.GradleUtil;
import com.restonic4.citadel.util.StringBuilderHelper;
import imgui.ImGui;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiWindowFlags;

@ClientSide
public class EditorAboutImGui extends ToggleableImGuiScreen {
    int iconId;
    int imageSize = 75;
    String latestVersion;

    String title = StringBuilderHelper.concatenate("Citadel engine ", GradleUtil.VERSION);

    @Override
    public void start() {
        iconId = new Texture(true, "assets/textures/icons/icon.png").getTextureID();

        try {
            latestVersion = GitHubHelper.getLatestRelease("restonic4", "CitadelEngine");
        } catch (Exception e) {
            latestVersion = "Error";
        }
    }

    @Override
    public void render() {
        if (!isVisible()) {
            return;
        }

        ImGui.begin("About", ImGuiWindowFlags.NoDocking);
        ImGui.setNextWindowSize(100, 300, ImGuiCond.Appearing);

        ImGui.newLine();

        ImGuiHelper.setCentered(imageSize);
        ImGui.image(iconId, imageSize, imageSize);

        ImGui.setWindowFontScale(1.5f);
        ImGuiHelper.setCenteredText(title);
        ImGui.text(title);
        ImGui.setWindowFontScale(1);

        ImGui.newLine();

        ImGui.text(StringBuilderHelper.concatenate("Version: ", GradleUtil.VERSION));
        ImGui.text(StringBuilderHelper.concatenate("Latest version: ", latestVersion));

        ImGui.newLine();

        ImGuiHelper.setCentered(100);
        if (ImGui.button("Close", 100, 30)) {
            hide();
        }

        ImGui.end();
    }
}