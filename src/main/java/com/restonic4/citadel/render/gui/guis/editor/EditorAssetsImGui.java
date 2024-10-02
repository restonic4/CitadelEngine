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

        refreshPathsIfNeeded();

        if (!isRootDirectory()) {
            if (isBackNavigationSelected()) {
                navigateToParentDirectory();
            }
        }

        renderDirectoryContent();

        ImGui.end();
    }

    private void refreshPathsIfNeeded() {
        if (paths == null || needsToRefresh) {
            needsToRefresh = false;
            paths = FileManager.getFilesInDirectory(currentDir);
        }
    }

    private boolean isRootDirectory() {
        return Objects.equals(currentDir, "resources");
    }

    private boolean isBackNavigationSelected() {
        return ImGui.selectable("..", false, ImGuiSelectableFlags.AllowDoubleClick) &&
                ImGui.isItemHovered() &&
                ImGui.isMouseDoubleClicked(0);
    }

    private void navigateToParentDirectory() {
        currentDir = Paths.get(currentDir).getParent().toString();
        needsToRefresh = true;
    }

    private void renderDirectoryContent() {
        for (Path path : paths) {
            if (isPathSelected(path)) {
                handlePathSelection(path);
            }
        }
    }

    private boolean isPathSelected(Path path) {
        return ImGui.selectable(path.getFileName().toString(), false, ImGuiSelectableFlags.AllowDoubleClick) &&
                ImGui.isItemHovered() &&
                ImGui.isMouseDoubleClicked(0);
    }

    private void handlePathSelection(Path path) {
        if (Files.isRegularFile(path)) {
            openFile(path);
        } else {
            navigateToDirectory(path);
        }
    }

    private void openFile(Path path) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().open(path.toFile());
            } catch (IOException exception) {
                throw new FileException(exception.getMessage());
            }
        } else {
            Logger.log("Java desktop is not supported here");
        }
    }

    private void navigateToDirectory(Path path) {
        currentDir = currentDir + "/" + path.getFileName().toString();
        needsToRefresh = true;
    }
}
