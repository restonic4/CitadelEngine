package com.restonic4.citadel.render.gui.guis.editor;

import com.restonic4.citadel.core.editor.LevelEditor;
import com.restonic4.citadel.render.Texture;
import com.restonic4.citadel.render.gui.ImGuiHelper;
import com.restonic4.citadel.render.gui.guis.ToggleableImGuiScreen;
import com.restonic4.citadel.util.CitadelConstants;
import com.restonic4.citadel.util.StringBuilderHelper;
import com.restonic4.citadel.world.Scene;
import com.restonic4.citadel.world.SceneManager;
import com.restonic4.citadel.world.object.GameObject;
import imgui.ImGui;
import imgui.flag.ImGuiTreeNodeFlags;

public class EditorInspectorImGui extends ToggleableImGuiScreen {
    int sceneIconID, objectIconID;

    @Override
    public void start() {
        sceneIconID = new Texture(true, "assets/textures/icons/files/scene/56.png").getTextureID();
        objectIconID = new Texture(true, "assets/textures/icons/game_object/56.png").getTextureID();
    }

    @Override
    public void render() {
        if (!isVisible()) {
            return;
        }

        Scene scene = SceneManager.getCurrentScene();

        ImGui.begin("Inspector");

        if (scene == null) {
            ImGui.text("The scene could not be loaded!");
            ImGui.end();
            return;
        }

        if (ImGui.collapsingHeader(StringBuilderHelper.concatenate("Scene: ", scene.getClass().getSimpleName()), ImGuiTreeNodeFlags.DefaultOpen)) {
            ImGui.indent(CitadelConstants.IM_GUI_INDENT);

            for (int i = 0; i < scene.getGameObjects().size(); i++) {
                GameObject gameObject = scene.getGameObjects().get(i);

                boolean isSelected = false;
                if (LevelEditor.getSelectedObject() != null) {
                    isSelected = LevelEditor.getSelectedObject().equals(gameObject);
                }

                ImGui.image(objectIconID, 16, 16);
                ImGui.sameLine();

                if (ImGuiHelper.selectableTruncated(StringBuilderHelper.concatenate(gameObject.getName(), "##", gameObject.getId()), isSelected)) {
                    LevelEditor.setSelectedObject(gameObject);
                }
            }

            ImGui.unindent(CitadelConstants.IM_GUI_INDENT);
        }

        ImGui.end();
    }
}
