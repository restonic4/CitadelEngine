package com.restonic4.citadel.render.gui.guis.editor;

import com.restonic4.citadel.core.LevelEditor;
import com.restonic4.citadel.exceptions.FileException;
import com.restonic4.citadel.files.FileManager;
import com.restonic4.citadel.render.gui.guis.ToggleableImGuiScreen;
import com.restonic4.citadel.util.CitadelConstants;
import com.restonic4.citadel.util.StringBuilderHelper;
import com.restonic4.citadel.util.debug.diagnosis.Logger;
import com.restonic4.citadel.world.object.Component;
import com.restonic4.citadel.world.object.GameObject;
import imgui.ImGui;
import imgui.flag.ImGuiButtonFlags;
import imgui.flag.ImGuiSelectableFlags;
import imgui.flag.ImGuiTreeNodeFlags;
import imgui.type.ImString;
import org.joml.Vector3f;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

public class EditorAssetsImGui extends ToggleableImGuiScreen {
    private List<Path> paths;
    private String currentDir = "resources";
    private boolean needsToRefresh;

    @Override
    public void render() {
        if (!isVisible()) {
            return;
        }

        ImGui.begin("Assets");

        if (paths == null || needsToRefresh) {
            needsToRefresh = false;
            paths = FileManager.getFilesInDirectory(currentDir);
        }

        if (!Objects.equals(currentDir, "resources")) {
            if (ImGui.selectable("..", false, ImGuiSelectableFlags.AllowDoubleClick)) {
                if (ImGui.isItemHovered()) {
                    if (ImGui.isMouseDoubleClicked(0)) {
                        currentDir = Paths.get(currentDir).getParent().toString();
                        needsToRefresh = true;
                    }
                }
            }
        }

        for (int i = 0; i < paths.size(); i++) {
            Path path = paths.get(i);
            if (ImGui.selectable(path.getFileName().toString(), false, ImGuiSelectableFlags.AllowDoubleClick)) {
                if (ImGui.isItemHovered()) {
                    if (ImGui.isMouseDoubleClicked(0)) {
                        if (Files.isRegularFile(path)) {
                            if (Desktop.isDesktopSupported()) {
                                try {
                                    Desktop.getDesktop().open(path.toFile());
                                }
                                catch (IOException exception) {
                                    throw new FileException(exception.getMessage());
                                }
                            }
                            else {
                                Logger.log("Java desktop is not supported here");
                            }
                        }
                        else {
                            currentDir = currentDir + "/" + path.getFileName().toString();
                            needsToRefresh = true;
                        }
                    }
                }
            }
        }

        ImGui.end();
    }
}
