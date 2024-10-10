package com.restonic4.citadel.render.gui.guis.editor;

import com.restonic4.citadel.core.editor.LevelEditor;
import com.restonic4.citadel.exceptions.FileException;
import com.restonic4.citadel.files.FileManager;
import com.restonic4.citadel.registries.built_in.managers.Icons;
import com.restonic4.citadel.registries.built_in.managers.ImGuiScreens;
import com.restonic4.citadel.registries.built_in.types.subtypes.IconSize;
import com.restonic4.citadel.render.gui.guis.ToggleableImGuiScreen;
import com.restonic4.citadel.util.debug.diagnosis.Logger;
import com.restonic4.citadel.util.history.commands.CreateFileHistoryCommand;
import com.restonic4.citadel.util.history.commands.DeleteFileHistoryCommand;
import com.restonic4.citadel.util.history.commands.RenameFileHistoryCommand;
import imgui.ImGui;
import imgui.flag.ImGuiMouseButton;
import imgui.flag.ImGuiSelectableFlags;

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

    private boolean isCreating = false;
    private boolean isFile = false;

    private Path rightClickedPath = null;
    private Path hoveringPath = null;

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

        if (ImGui.isWindowHovered() && ImGui.isMouseClicked(ImGuiMouseButton.Right)) {
            rightClickedPath = null;
            ImGui.openPopup("AssetsRightClickMenu");
        }

        hoveringPath = null;

        renderDirectoryContent();

        if (ImGui.beginPopup("AssetsRightClickMenu")) {
            if (rightClickedPath == null) {
                if (ImGui.menuItem("New Folder")) {
                    handleAction(true, false);
                }
                if (ImGui.menuItem("New File")) {
                    handleAction(true, true);
                }
            } else {
                if (ImGui.menuItem("Rename")) {
                    handleAction(false, !Files.isDirectory(rightClickedPath));
                }
                if (ImGui.menuItem("Delete")) {
                    LevelEditor.getHistoryCommandManager().executeCommand(new DeleteFileHistoryCommand(rightClickedPath.toString()));
                }
            }
            ImGui.endPopup();
        }

        ImGui.end();
    }

    public void handleAction(boolean creating, boolean file) {
        handleAction(creating, file, rightClickedPath);
    }

    public void handleAction(boolean creating, boolean file, Path path) {
        isCreating = creating;
        isFile = file;

        if (creating) {
            handleCreating();
        } else {
            handleRenaming(path);
        }
    }

    private void handleRenaming(Path path) {
        LevelEditor.renameAction(path.getFileName().toString(), () -> {
            String name = ImGuiScreens.EDITOR_RENAME.getResult();
            LevelEditor.getHistoryCommandManager().executeCommand(new RenameFileHistoryCommand(path, name));
            reload();
        });
    }

    private void handleCreating() {
        if (isCreating) {
            String defaultText = (isFile) ? "New file.txt" : "New directory";

            LevelEditor.renameAction(defaultText, () -> {
                String name = ImGuiScreens.EDITOR_RENAME.getResult();
                LevelEditor.getHistoryCommandManager().executeCommand(new CreateFileHistoryCommand(currentDir, name, isFile));
                reload();
            });
        }
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
            boolean[] pathActions = getPathActions(path);

            if (pathActions[0]) {
                handlePathSelection(path);
            }
            else if (pathActions[1]) {
                rightClickedPath = path;
            }

            if (pathActions[2]) {
                hoveringPath = path;
            }
        }
    }

    private boolean[] getPathActions(Path path) {
        drawDesiredIcon(path);
        boolean clicked = ImGui.selectable(path.getFileName().toString(), false, ImGuiSelectableFlags.AllowDoubleClick);

        return new boolean[] {
                (clicked && ImGui.isItemHovered() && ImGui.isMouseDoubleClicked(0)), // Double clicked
                (ImGui.isItemHovered() && ImGui.isMouseClicked(ImGuiMouseButton.Right)), // Right clicked
                (ImGui.isItemHovered()) // Hover
        };
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

    private void drawDesiredIcon(Path path) {
        if (Files.isDirectory(path)) {
            ImGui.image(getIconForFolder(path, IconSize.SIZE_56.getFlag()), 16, 16);
        } else {
            ImGui.image(getIconForFile(path, IconSize.SIZE_56.getFlag()), 16, 16);
        }

        ImGui.sameLine();
    }

    private boolean isReservedFolder(Path path) {
        String normalizedPath = path.toString().replace("\\", "/");

        return normalizedPath.startsWith("resources") &&
                (normalizedPath.equals("resources") ||
                        normalizedPath.startsWith("resources/assets") ||
                        normalizedPath.startsWith("resources/data") ||
                        normalizedPath.startsWith("resources/docs")
                );
    }

    private int getIconForFolder(Path path, int sizeFlag) {
        return (isReservedFolder(path)) ? Icons.RESERVED_FOLDER.getTextureID(sizeFlag) : Icons.FOLDER.getTextureID(sizeFlag);
    }

    private int getIconForFile(Path path, int sizeFlag) {
        String fileName = path.getFileName().toString();
        if (!fileName.contains(".")) {
            return Icons.GENERIC_FILE.getTextureID(sizeFlag);
        }

        String fileExtension = getFileExtension(fileName);

        return switch (fileExtension) {
            case "png", "ico" -> Icons.IMAGE_FILE.getTextureID(sizeFlag);
            case "obj", "fbx" -> Icons.OBJECT_FILE.getTextureID(sizeFlag);
            case "json" -> Icons.JSON_FILE.getTextureID(sizeFlag);
            case "txt", "md" -> Icons.TEXT_FILE.getTextureID(sizeFlag);
            case "ogg" -> Icons.AUDIO_FILE.getTextureID(sizeFlag);
            case "glsl" -> Icons.SHADER_FILE.getTextureID(sizeFlag);
            case "citScene" -> Icons.SCENE_FILE.getTextureID(sizeFlag);
            default -> Icons.GENERIC_FILE.getTextureID(sizeFlag);
        };
    }

    private String getFileExtension(String fileName) {
        String[] parts = fileName.split("\\.");
        return parts[parts.length - 1];
    }

    public void reload() {
        this.needsToRefresh = true;
    }

    public Path getHoveringPath() {
        return hoveringPath;
    }

    public Path getRightClickedPath() {
        return rightClickedPath;
    }
}
