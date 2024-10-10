package com.restonic4.citadel.render.gui.guis.editor;

import com.restonic4.citadel.core.CitadelLauncher;
import com.restonic4.citadel.core.editor.LevelEditor;
import com.restonic4.citadel.registries.built_in.managers.Icons;
import com.restonic4.citadel.registries.built_in.managers.KeyBinds;
import com.restonic4.citadel.registries.built_in.types.Icon;
import com.restonic4.citadel.registries.built_in.types.subtypes.IconSize;
import com.restonic4.citadel.render.Texture;
import com.restonic4.citadel.render.gui.ImGuiHelper;
import com.restonic4.citadel.render.gui.guis.ToggleableImGuiScreen;
import com.restonic4.citadel.util.CitadelConstants;
import com.restonic4.citadel.util.StringBuilderHelper;
import com.restonic4.citadel.world.Scene;
import com.restonic4.citadel.world.SceneManager;
import com.restonic4.citadel.world.object.GameObject;
import imgui.ImGui;
import imgui.flag.ImGuiMouseButton;
import imgui.flag.ImGuiTreeNodeFlags;

import java.nio.file.Path;

public class EditorInspectorImGui extends ToggleableImGuiScreen {
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

                Icons.GAME_OBJECT.renderImGui(IconSize.SIZE_16);
                ImGui.sameLine();

                if (ImGuiHelper.selectableTruncated(StringBuilderHelper.concatenate(gameObject.getName(), "##", gameObject.getId()), isSelected)) {
                    LevelEditor.setSelectedObject(gameObject);
                }

                // If right click select the object
                if (ImGui.isItemClicked(ImGuiMouseButton.Right)) {
                    LevelEditor.setSelectedObject(gameObject);
                }
            }

            ImGui.unindent(CitadelConstants.IM_GUI_INDENT);
        }

        if (ImGui.isWindowHovered() && ImGui.isMouseClicked(ImGuiMouseButton.Right)) {
            ImGui.openPopup("InspectorRightClickMenu");
        }

        if (ImGui.beginPopup("InspectorRightClickMenu")) {
            if (ImGui.menuItem("Rename")) {
                LevelEditor.handleRenaming();
            }

            if (ImGui.menuItem("Copy")) {
                CitadelLauncher.getInstance().handleError("This method is not available!");
            }

            if (ImGui.beginMenu("Paste")) {
                if (ImGui.menuItem("Paste into")) {
                    CitadelLauncher.getInstance().handleError("This method is not available!");
                }

                if (ImGui.menuItem("Replace")) {
                    CitadelLauncher.getInstance().handleError("This method is not available!");
                }

                ImGui.endMenu();
            }

            if (ImGui.menuItem("Duplicate")) {
                CitadelLauncher.getInstance().handleError("This method is not available!");
            }

            if (ImGui.menuItem("Delete")) {
                LevelEditor.handleRemoval();
            }

            ImGui.endPopup();
        }

        ImGui.end();
    }
}
