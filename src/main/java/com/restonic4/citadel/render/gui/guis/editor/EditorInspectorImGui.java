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
import imgui.ImDrawList;
import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiMouseButton;
import imgui.flag.ImGuiTreeNodeFlags;
import imgui.flag.ImGuiWindowFlags;

import java.nio.file.Path;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class EditorInspectorImGui extends ToggleableImGuiScreen {
    @Override
    public void render() {
        if (!isVisible()) {
            return;
        }

        Scene scene = SceneManager.getCurrentScene();

        ImGui.begin("Inspector", ImGuiWindowFlags.HorizontalScrollbar);

        if (scene == null) {
            ImGui.text("The scene could not be loaded!");
            ImGui.end();
            return;
        }

        if (ImGui.collapsingHeader(StringBuilderHelper.concatenate("Scene: ", scene.getClass().getSimpleName()), ImGuiTreeNodeFlags.DefaultOpen)) {
            ImGui.indent(CitadelConstants.IM_GUI_INDENT);

            for (int i = 0; i < scene.getRootGameObjects().size(); i++) {
                GameObject gameObject = scene.getRootGameObjects().get(i);

                boolean isSelected = false;
                if (LevelEditor.getSelectedObject() != null) {
                    isSelected = LevelEditor.getSelectedObject().equals(gameObject);
                }

                renderObjectTree(gameObject, isSelected);
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

    int flags = ImGuiTreeNodeFlags.OpenOnArrow | ImGuiTreeNodeFlags.OpenOnDoubleClick | ImGuiTreeNodeFlags.SpanAvailWidth;
    private void renderObjectTree(GameObject gameObject, boolean isSelected) {
        if (!gameObject.transform.hasChildren()) {
            if (ImGuiHelper.selectableTruncated(StringBuilderHelper.concatenate(gameObject.getName(), "##", gameObject.getId()), isSelected)) {
                LevelEditor.setSelectedObject(gameObject);
            }

            if (ImGui.isItemClicked(ImGuiMouseButton.Right)) {
                LevelEditor.setSelectedObject(gameObject);
            }

            return;
        }


        if (isSelected) {
            drawRectangleUnderSelectable();
        }

        if (ImGui.treeNodeEx(StringBuilderHelper.concatenate(gameObject.getName(), "##", gameObject.getId()), flags)) {
            if (ImGui.isItemClicked()) {
                LevelEditor.setSelectedObject(gameObject);
            }
            if (ImGui.isItemClicked(ImGuiMouseButton.Right)) {
                LevelEditor.setSelectedObject(gameObject);
            }

            List<GameObject> children = gameObject.transform.getChildren();
            for (int i = 0; i < children.size(); i++) {
                boolean isSubSelected = false;
                if (LevelEditor.getSelectedObject() != null) {
                    isSubSelected = LevelEditor.getSelectedObject().equals(children.get(i));
                }

                renderObjectTree(children.get(i), isSubSelected);
            }

            ImGui.treePop();
        } else {
            if (ImGui.isItemClicked()) {
                LevelEditor.setSelectedObject(gameObject);
            }
            if (ImGui.isItemClicked(ImGuiMouseButton.Right)) {
                LevelEditor.setSelectedObject(gameObject);
            }
        }
    }

    private void drawRectangleUnderSelectable() {
        ImDrawList drawList = ImGui.getWindowDrawList();

        float x = ImGui.getCursorScreenPos().x;
        float y = ImGui.getCursorScreenPos().y;

        float width = ImGui.getContentRegionAvail().x;
        float height = ImGui.getItemRectSize().y;

        int color = ImGui.getColorU32(ImGuiCol.TextSelectedBg);

        drawList.addRectFilled(x, y, x + width, y + height, color);
    }
}
