package com.restonic4.citadel.render.gui.guis.editor;

import com.restonic4.citadel.core.CitadelLauncher;
import com.restonic4.citadel.core.editor.LevelEditor;
import com.restonic4.citadel.exceptions.FileException;
import com.restonic4.citadel.files.FileManager;
import com.restonic4.citadel.input.KeyListener;
import com.restonic4.citadel.render.Texture;
import com.restonic4.citadel.render.gui.ImGuiHelper;
import com.restonic4.citadel.render.gui.guis.ToggleableImGuiScreen;
import com.restonic4.citadel.util.debug.diagnosis.Logger;
import com.restonic4.citadel.util.history.commands.CreateFileHistoryCommand;
import com.restonic4.citadel.util.history.commands.DeleteFileHistoryCommand;
import com.restonic4.citadel.util.history.commands.RenameFileHistoryCommand;
import com.restonic4.citadel.util.history.commands.RenameGameObjectHistoryCommand;
import imgui.ImGui;
import imgui.flag.ImGuiMouseButton;
import imgui.flag.ImGuiSelectableFlags;
import org.lwjgl.glfw.GLFW;

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
    private boolean isRenaming = false;
    private boolean isFile = false;
    private Path rightClickedPath = null;

    int folderIconID, reservedFolderIconID, genericFileIconID, imageFileIconID, objectFileIconID, jsonFileIconID,
            textFileIconID, audioFileIconID, shaderFileIconID, sceneFileIconID;

    @Override
    public void start() {
        folderIconID = new Texture(true, "assets/textures/icons/files/folder/56.png").getTextureID();
        reservedFolderIconID = new Texture(true, "assets/textures/icons/files/reserved_folder/56.png").getTextureID();

        genericFileIconID = new Texture(true, "assets/textures/icons/files/generic/56.png").getTextureID();
        imageFileIconID = new Texture(true, "assets/textures/icons/files/image/56.png").getTextureID();
        objectFileIconID = new Texture(true, "assets/textures/icons/files/object/56.png").getTextureID();
        jsonFileIconID = new Texture(true, "assets/textures/icons/files/json/56.png").getTextureID();
        textFileIconID = new Texture(true, "assets/textures/icons/files/text/56.png").getTextureID();
        audioFileIconID = new Texture(true, "assets/textures/icons/files/audio/56.png").getTextureID();
        shaderFileIconID = new Texture(true, "assets/textures/icons/files/shader/56.png").getTextureID();
        sceneFileIconID = new Texture(true, "assets/textures/icons/files/scene/56.png").getTextureID();
    }

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
            ImGui.openPopup("RightClickMenu");
        }

        renderDirectoryContent();

        if (ImGui.beginPopup("RightClickMenu")) {
            if (rightClickedPath == null) {
                if (ImGui.menuItem("New Folder")) {
                    isCreating = true;
                    isFile = false;
                }
                if (ImGui.menuItem("New File")) {
                    isCreating = true;
                    isFile = true;
                }
            } else {
                if (ImGui.menuItem("Rename")) {
                    isRenaming = true;
                    isFile = !Files.isDirectory(rightClickedPath);
                }
                if (ImGui.menuItem("Delete")) {
                    LevelEditor.getHistoryCommandManager().executeCommand(new DeleteFileHistoryCommand(rightClickedPath.toString()));
                }
            }
            ImGui.endPopup();
        }

        handleCreateAction();
        handleRenaming();

        ImGui.end();
    }

    private void handleCreateAction() {
        if (isCreating) {
            if (isFile) {
                ImGuiHelper.renameBox("New file.txt");
            } else {
                ImGuiHelper.renameBox("New directory");
            }

            if (KeyListener.isKeyPressedOnce(GLFW.GLFW_KEY_ESCAPE)) {
                isCreating = false;
                ImGuiHelper.resetRenameBox();
            } else if (KeyListener.isKeyPressedOnce(GLFW.GLFW_KEY_ENTER)) {
                String name = ImGuiHelper.getRenameBoxResult();

                LevelEditor.getHistoryCommandManager().executeCommand(new CreateFileHistoryCommand(currentDir, name, isFile));

                isCreating = false;
                ImGuiHelper.resetRenameBox();
            }
        }
    }

    private void handleRenaming() {
        if (isRenaming) {
            ImGuiHelper.renameBox(rightClickedPath.getFileName().toString());

            if (KeyListener.isKeyPressedOnce(GLFW.GLFW_KEY_ESCAPE)) {
                isRenaming = false;
                ImGuiHelper.resetRenameBox();
            }
            else if (KeyListener.isKeyPressedOnce(GLFW.GLFW_KEY_ENTER)) {
                String newName = ImGuiHelper.getRenameBoxResult();

                isRenaming = false;
                ImGuiHelper.resetRenameBox();

                LevelEditor.getHistoryCommandManager().executeCommand(new RenameFileHistoryCommand(rightClickedPath, newName));

                reload();
            }
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
        }
    }

    private boolean[] getPathActions(Path path) {
        drawDesiredIcon(path);
        boolean clicked = ImGui.selectable(path.getFileName().toString(), false, ImGuiSelectableFlags.AllowDoubleClick);

        return new boolean[] {
                (clicked && ImGui.isItemHovered() && ImGui.isMouseDoubleClicked(0)), // Double clicked
                (ImGui.isItemHovered() && ImGui.isMouseClicked(ImGuiMouseButton.Right)) // Right clicked
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
            ImGui.image(getIconForFolder(path), 16, 16);
        } else {
            ImGui.image(getIconForFile(path), 16, 16);
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

    private int getIconForFolder(Path path) {
        return (isReservedFolder(path)) ? reservedFolderIconID : folderIconID;
    }

    private int getIconForFile(Path path) {
        String fileName = path.getFileName().toString();
        if (!fileName.contains(".")) {
            return genericFileIconID;
        }

        String fileExtension = getFileExtension(fileName);

        return switch (fileExtension) {
            case "png", "ico" -> imageFileIconID;
            case "obj", "fbx" -> objectFileIconID;
            case "json" -> jsonFileIconID;
            case "txt", "md" -> textFileIconID;
            case "ogg" -> audioFileIconID;
            case "glsl" -> shaderFileIconID;
            case "citScene" -> sceneFileIconID;
            default -> genericFileIconID;
        };
    }

    private String getFileExtension(String fileName) {
        String[] parts = fileName.split("\\.");
        return parts[parts.length - 1];
    }

    public void reload() {
        this.needsToRefresh = true;
    }
}
