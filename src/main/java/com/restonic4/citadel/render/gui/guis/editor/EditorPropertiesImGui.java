package com.restonic4.citadel.render.gui.guis.editor;

import com.restonic4.citadel.core.editor.LevelEditor;
import com.restonic4.citadel.render.gui.ImGuiHelper;
import com.restonic4.citadel.render.gui.guis.ToggleableImGuiScreen;
import com.restonic4.citadel.util.CitadelConstants;
import com.restonic4.citadel.util.helpers.StringBuilderHelper;
import com.restonic4.citadel.util.history.commands.*;
import com.restonic4.citadel.world.object.Component;
import com.restonic4.citadel.world.object.GameObject;
import imgui.ImGui;
import imgui.flag.ImGuiInputTextFlags;
import imgui.flag.ImGuiTreeNodeFlags;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImString;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.List;

public class EditorPropertiesImGui extends ToggleableImGuiScreen {
    @Override
    public void render() {
        if (!isVisible()) {
            return;
        }

        int windowFlags = ImGuiWindowFlags.HorizontalScrollbar;

        if (LevelEditor.isUnsaved()) {
            windowFlags |= ImGuiWindowFlags.UnsavedDocument;
        }

        ImGui.begin("Properties", windowFlags);

        GameObject selectedGameobject = LevelEditor.getSelectedObject();

        if (selectedGameobject == null) {
            ImGuiHelper.textTruncated("Select a GameObject to modify it's properties!");
            ImGui.end();
            return;
        }

        if (ImGui.collapsingHeader("Internal", ImGuiTreeNodeFlags.DefaultOpen)) {
            ImGui.indent(CitadelConstants.IM_GUI_INDENT);

            ImGui.text(StringBuilderHelper.concatenate("ID: ", selectedGameobject.getId()));
            ImGui.text(StringBuilderHelper.concatenate("On frustum: ", selectedGameobject.isInsideFrustum()));

            ImGui.unindent(CitadelConstants.IM_GUI_INDENT);
        }

        if (ImGui.collapsingHeader("General", ImGuiTreeNodeFlags.DefaultOpen)) {
            ImGui.indent(CitadelConstants.IM_GUI_INDENT);

            ImString nameBuffer = new ImString(selectedGameobject.getName(), 256);

            ImGui.text("Name:");
            ImGui.sameLine();
            if (ImGui.inputText("##name", nameBuffer, ImGuiInputTextFlags.EnterReturnsTrue)) {
                LevelEditor.getHistoryCommandManager().executeCommand(new RenameGameObjectHistoryCommand(selectedGameobject, nameBuffer.get()));
            }

            boolean isStatic = selectedGameobject.isStatic();

            if (ImGui.checkbox("Static", isStatic)) {
                LevelEditor.getHistoryCommandManager().executeCommand(new StaticToggleGameObjectHistoryCommand(selectedGameobject, !isStatic));
            }

            ImGui.unindent(CitadelConstants.IM_GUI_INDENT);
        }

        if (ImGui.collapsingHeader("Transform", ImGuiTreeNodeFlags.DefaultOpen)) {
            ImGui.indent(CitadelConstants.IM_GUI_INDENT);

            ImGuiHelper.renderPropertyRow("Position", () -> {
                float[] posArray = {
                        selectedGameobject.transform.getPosition().x,
                        selectedGameobject.transform.getPosition().y,
                        selectedGameobject.transform.getPosition().z
                };
                if (ImGui.dragFloat3("##PositionDrag", posArray, 0.1f, -1000.0f, 1000.0f, "%.3f")) {
                    LevelEditor.getHistoryCommandManager().executeCommand(new MoveGameObjectHistoryCommand(selectedGameobject, new Vector3f(posArray[0], posArray[1], posArray[2])));
                }
            });

            ImGuiHelper.renderPropertyRow("Rotation", () -> {
                Vector3f euler = new Vector3f();
                selectedGameobject.transform.getRotation().getEulerAnglesXYZ(euler);

                float[] rotArray = { euler.x, euler.y, euler.z };
                if (ImGui.dragFloat3("##RotationDrag", rotArray, 0.1f, -1000.0f, 1000.0f, "%.3f")) {
                    Quaternionf newRotation = new Quaternionf().rotationXYZ(rotArray[0], rotArray[1], rotArray[2]);

                    LevelEditor.getHistoryCommandManager().executeCommand(new RotateGameObjectHistoryCommand(selectedGameobject, newRotation));
                }
            });

            ImGuiHelper.renderPropertyRow("Scale", () -> {
                float[] scaleArray = {
                        selectedGameobject.transform.getScale().x,
                        selectedGameobject.transform.getScale().y,
                        selectedGameobject.transform.getScale().z
                };
                if (ImGui.dragFloat3("##ScaleDrag", scaleArray, 0.1f, -1000.0f, 1000.0f, "%.3f")) {
                    LevelEditor.getHistoryCommandManager().executeCommand(new ScaleGameObjectHistoryCommand(selectedGameobject, new Vector3f(scaleArray[0], scaleArray[1], scaleArray[2])));
                }
            });

            ImGui.unindent(CitadelConstants.IM_GUI_INDENT);
        }

        if (ImGui.collapsingHeader("Components", ImGuiTreeNodeFlags.DefaultOpen)) {
            ImGui.indent(CitadelConstants.IM_GUI_INDENT);

            List<Component> components = selectedGameobject.getComponents();

            for (int i = 0; i < components.size(); i++) {
                Component component = components.get(i);

                if (ImGui.collapsingHeader(StringBuilderHelper.concatenate(component.getClass().getSimpleName(), "##", component.getId()))) {
                    ImGui.indent(CitadelConstants.IM_GUI_INDENT);

                    component.renderEditorUI();

                    ImGui.unindent(CitadelConstants.IM_GUI_INDENT);
                }
            }

            ImGui.unindent(CitadelConstants.IM_GUI_INDENT);
        }

        ImGui.end();
    }
}
