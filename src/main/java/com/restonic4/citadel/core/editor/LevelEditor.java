package com.restonic4.citadel.core.editor;

import com.restonic4.citadel.core.CitadelLauncher;
import com.restonic4.citadel.core.Window;
import com.restonic4.citadel.registries.AssetLocation;
import com.restonic4.citadel.registries.Registries;
import com.restonic4.citadel.registries.Registry;
import com.restonic4.citadel.registries.built_in.managers.ImGuiScreens;
import com.restonic4.citadel.registries.built_in.managers.KeyBinds;
import com.restonic4.citadel.registries.built_in.types.LevelEditorAddTemplate;
import com.restonic4.citadel.render.Texture;
import com.restonic4.citadel.render.gui.ImGuiHelper;
import com.restonic4.citadel.util.debug.diagnosis.Logger;
import com.restonic4.citadel.util.history.HistoryCommandManager;
import com.restonic4.citadel.util.history.commands.DeleteFileHistoryCommand;
import com.restonic4.citadel.util.history.commands.RenameGameObjectHistoryCommand;
import com.restonic4.citadel.world.Scene;
import com.restonic4.citadel.world.SceneManager;
import com.restonic4.citadel.world.object.GameObject;
import com.restonic4.citadel.world.object.Transform;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.ImVec4;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiStyleVar;
import imgui.type.ImBoolean;

import java.nio.file.Files;
import java.util.Map;

import static imgui.flag.ImGuiWindowFlags.*;
import static imgui.flag.ImGuiWindowFlags.NoNavFocus;

// TODO: Make the UIs responsive
// TODO: Make an options/preferences window
// TODO: Make a keybindings UI to configure the keybindings such as F2, ESC and ENTER
public abstract class LevelEditor {
    private static Window window;
    private static HistoryCommandManager historyCommandManager;
    private static HardcodedCamera hardcodedCamera;

    private static GameObject selectedObject;
    private static boolean isPlaying = false;
    private static boolean isPaused = false;

    private static boolean isUnsaved = false;

    static int playButtonTextureId, stopButtonTextureId, pauseButtonTextureId;

    public static void init() {
        window = Window.getInstance();
        window.setCursorLocked(false);

        historyCommandManager = new HistoryCommandManager();

        hardcodedCamera = new HardcodedCamera(new Transform());

        ImGuiScreens.GAME_VIEWPORT.show();
        ImGuiScreens.SCENE_VIEWPORT.show();
        ImGuiScreens.EDITOR_INSPECTOR.show();
        ImGuiScreens.EDITOR_PROPERTIES.show();
        ImGuiScreens.EDITOR_ASSETS.show();

        playButtonTextureId = new Texture(true, "assets/textures/icons/play/56.png").getTextureID();
        stopButtonTextureId = new Texture(true, "assets/textures/icons/stop/56.png").getTextureID();
        pauseButtonTextureId = new Texture(true, "assets/textures/icons/pause/56.png").getTextureID();
    }

    // TODO: Weird behaviours in some screens, it should ignore the navbar height
    public static void render() {
        int windowFlags = MenuBar | NoDocking | NoTitleBar | NoCollapse | NoResize | NoMove | NoBringToFrontOnFocus | NoNavFocus;

        float menuBarHeight = ImGui.getFrameHeight();
        float menuBarSize = (menuBarHeight / 2) + ImGui.getStyle().getFramePadding().y;

        ImGui.setNextWindowPos(0.0f, menuBarHeight, ImGuiCond.Always);
        ImGui.setNextWindowSize(window.getWidth(), window.getHeight() - menuBarHeight);

        ImGui.begin("Dockspace Demo", new ImBoolean(true), windowFlags);
        ImGui.popStyleVar(3);

        ImGui.pushStyleVar(ImGuiStyleVar.FramePadding, menuBarSize, menuBarSize);

        if (ImGui.beginMainMenuBar()) {
            if (ImGui.beginMenu("Edit")) {
                if (ImGui.menuItem("Save")) {
                    saveScene();
                }

                ImGui.separator();

                if (ImGui.menuItem("Undo")) {
                   historyCommandManager.undo();
                }
                ImGui.sameLine();
                ImGuiHelper.coloredText(KeyBinds.UNDO.getKeyString(), ImGuiHelper.getTooltipColor());

                if (ImGui.menuItem("Redo")) {
                    historyCommandManager.redo();
                }
                ImGui.sameLine();
                ImGuiHelper.coloredText(KeyBinds.REDO.getKeyString(), ImGuiHelper.getTooltipColor());

                ImGui.separator();

                if (ImGui.menuItem("Rename")) {
                    handleRenaming();
                }
                ImGui.sameLine();
                ImGuiHelper.coloredText(KeyBinds.RENAME.getKeyString(), ImGuiHelper.getTooltipColor());

                if (ImGui.menuItem("Copy")) {
                    CitadelLauncher.getInstance().handleError("This method is not available!");
                }
                ImGui.sameLine();
                ImGuiHelper.coloredText(KeyBinds.COPY.getKeyString(), ImGuiHelper.getTooltipColor());

                if (ImGui.menuItem("Paste")) {
                    CitadelLauncher.getInstance().handleError("This method is not available!");
                }
                ImGui.sameLine();
                ImGuiHelper.coloredText(KeyBinds.PASTE.getKeyString(), ImGuiHelper.getTooltipColor());

                if (ImGui.menuItem("Duplicate")) {
                    CitadelLauncher.getInstance().handleError("This method is not available!");
                }
                ImGui.sameLine();
                ImGuiHelper.coloredText(KeyBinds.DUPLICATE.getKeyString(), ImGuiHelper.getTooltipColor());

                if (ImGui.menuItem("Delete")) {
                    handleRemoval();
                }
                ImGui.sameLine();
                ImGuiHelper.coloredText(KeyBinds.DELETE.getKeyString(), ImGuiHelper.getTooltipColor());

                ImGui.separator();

                if (ImGui.beginMenu("Add")) {
                    Map<AssetLocation, LevelEditorAddTemplate> guis = Registry.getRegistry(Registries.LEVEL_EDITOR_ADD_TEMPLATE);
                    for (Map.Entry<AssetLocation, LevelEditorAddTemplate> entry : guis.entrySet()) {
                        LevelEditorAddTemplate template = entry.getValue();

                        if (ImGui.menuItem(template.getName())) {
                            template.add();
                        }
                    }

                    ImGui.endMenu();
                }

                ImGui.endMenu();
            }

            if (ImGui.beginMenu("View")) {
                if (ImGui.menuItem("Scene view", ImGuiScreens.SCENE_VIEWPORT.isVisible())) {
                    ImGuiScreens.SCENE_VIEWPORT.toggle();

                    if (ImGuiScreens.SCENE_VIEWPORT.isVisible()) {
                        ImGuiScreens.SCENE_VIEWPORT.show();
                    }
                    else {
                        ImGuiScreens.SCENE_VIEWPORT.hide();
                    }
                }

                if (ImGui.menuItem("Game view", ImGuiScreens.GAME_VIEWPORT.isVisible())) {
                    ImGuiScreens.GAME_VIEWPORT.toggle();

                    if (ImGuiScreens.GAME_VIEWPORT.isVisible()) {
                        ImGuiScreens.GAME_VIEWPORT.show();
                    }
                    else {
                        ImGuiScreens.GAME_VIEWPORT.hide();
                    }
                }

                ImGui.separator();

                if (ImGui.menuItem("Statistics", ImGuiScreens.RENDER_STATISTICS.isVisible())) {
                    ImGuiScreens.RENDER_STATISTICS.toggle();

                    if (ImGuiScreens.RENDER_STATISTICS.isVisible()) {
                        ImGuiScreens.RENDER_STATISTICS.show();
                    }
                    else {
                        ImGuiScreens.RENDER_STATISTICS.hide();
                    }
                }

                if (ImGui.menuItem("Inspector", ImGuiScreens.EDITOR_INSPECTOR.isVisible())) {
                    ImGuiScreens.EDITOR_INSPECTOR.toggle();

                    if (ImGuiScreens.EDITOR_INSPECTOR.isVisible()) {
                        ImGuiScreens.EDITOR_INSPECTOR.show();
                    }
                    else {
                        ImGuiScreens.EDITOR_INSPECTOR.hide();
                    }
                }

                if (ImGui.menuItem("Properties", ImGuiScreens.EDITOR_PROPERTIES.isVisible())) {
                    ImGuiScreens.EDITOR_PROPERTIES.toggle();

                    if (ImGuiScreens.EDITOR_PROPERTIES.isVisible()) {
                        ImGuiScreens.EDITOR_PROPERTIES.show();
                    }
                    else {
                        ImGuiScreens.EDITOR_PROPERTIES.hide();
                    }
                }

                if (ImGui.menuItem("Assets", ImGuiScreens.EDITOR_ASSETS.isVisible())) {
                    ImGuiScreens.EDITOR_ASSETS.toggle();

                    if (ImGuiScreens.EDITOR_ASSETS.isVisible()) {
                        ImGuiScreens.EDITOR_ASSETS.show();
                    }
                    else {
                        ImGuiScreens.EDITOR_ASSETS.hide();
                    }
                }

                ImGui.separator();

                if (ImGui.beginMenu("Overlay")) {
                    if (ImGui.menuItem("Show Grid")) {
                        CitadelLauncher.getInstance().handleError("This rendering system is not available!");
                    }
                    if (ImGui.menuItem("Show Axis")) {
                        CitadelLauncher.getInstance().handleError("This rendering system is not available!");
                    }
                    ImGui.endMenu();
                }

                ImGui.endMenu();
            }

            if (ImGui.beginMenu("Help")) {
                if (ImGui.menuItem("About")) {
                    ImGuiScreens.EDITOR_ABOUT.show();
                }

                if (ImGui.menuItem("Documentation")) {
                    CitadelLauncher.getInstance().handleError("This UI window is not available!");
                }

                ImGui.endMenu();
            }

            ImGui.beginGroup();

            ImVec2 buttonSize = new ImVec2(16, 16);

            float availableWidth = ImGui.getContentRegionAvail().x;
            ImGui.setCursorPosX((availableWidth - buttonSize.x) / 2);

            if (!isIsPlaying()) {
                ImGui.pushStyleColor(ImGuiCol.Button, new ImVec4(0, 0, 0, 0));
                if (ImGui.imageButton(playButtonTextureId, buttonSize.x, buttonSize.y)) {
                    play();
                }
                ImGui.popStyleColor();
            } else {
                ImGui.pushStyleColor(ImGuiCol.Button, new ImVec4(0, 0, 0, 0));
                if (ImGui.imageButton(stopButtonTextureId, buttonSize.x, buttonSize.y)) {
                    stop();
                }
                ImGui.popStyleColor();

                if (isIsPaused()) {
                    ImGui.pushStyleColor(ImGuiCol.Button, new ImVec4(0.5f, 0.5f, 0.5f, 1));
                } else {
                    ImGui.pushStyleColor(ImGuiCol.Button, new ImVec4(0, 0, 0, 0));
                }

                if (ImGui.imageButton(pauseButtonTextureId, buttonSize.x, buttonSize.y)) {
                    pause();
                }

                ImGui.popStyleColor();
            }

            ImGui.endGroup();

            ImGui.endMainMenuBar();
        }

        ImGui.popStyleVar();

        if (ImGuiScreens.SCENE_VIEWPORT.isWindowFocused()) {
            hardcodedCamera.update();
        }

        handleKeyInputs();

        handleHistory();
    }

    private static void handleKeyInputs() {
        if (KeyBinds.RENAME.isPressedOnce()) {
            handleRenaming();
        } else if (KeyBinds.DELETE.isPressedOnce()) {
            handleRemoval();
        } else if (KeyBinds.COPY.isPressedOnce()) {
            CitadelLauncher.getInstance().handleError("This method is not available!");
        } else if (KeyBinds.PASTE.isPressedOnce()) {
            CitadelLauncher.getInstance().handleError("This method is not available!");
        } else if (KeyBinds.DUPLICATE.isPressedOnce()) {
            CitadelLauncher.getInstance().handleError("This method is not available!");
        }
    }

    public static void handleRenaming() {
        if (ImGuiScreens.EDITOR_ASSETS.getHoveringPath() != null) { // asset
            ImGuiScreens.EDITOR_ASSETS.handleAction(
                    false,
                    !Files.isDirectory(ImGuiScreens.EDITOR_ASSETS.getHoveringPath()),
                    ImGuiScreens.EDITOR_ASSETS.getHoveringPath()
            );
        } else if (ImGuiScreens.EDITOR_ASSETS.getLastClickedPath() != null) { // asset
            ImGuiScreens.EDITOR_ASSETS.handleAction(
                    false,
                    !Files.isDirectory(ImGuiScreens.EDITOR_ASSETS.getLastClickedPath()),
                    ImGuiScreens.EDITOR_ASSETS.getLastClickedPath()
            );
        } else if (selectedObject != null) { // object
            renameAction(getSelectedObject().getName(), () -> {
                String newName = ImGuiScreens.EDITOR_RENAME.getResult();
                historyCommandManager.executeCommand(new RenameGameObjectHistoryCommand(getSelectedObject(), newName));
            });
        }
    }

    public static void handleRemoval() {
        if (ImGuiScreens.EDITOR_ASSETS.getHoveringPath() != null) { // asset
            LevelEditor.getHistoryCommandManager().executeCommand(
                    new DeleteFileHistoryCommand(ImGuiScreens.EDITOR_ASSETS.getHoveringPath().toString())
            );
        } else if (ImGuiScreens.EDITOR_ASSETS.getLastClickedPath() != null) { // asset
            LevelEditor.getHistoryCommandManager().executeCommand(
                    new DeleteFileHistoryCommand(ImGuiScreens.EDITOR_ASSETS.getLastClickedPath().toString())
            );
        } else if (selectedObject != null) { // object
            // TODO: Object deletion system needed
            CitadelLauncher.getInstance().handleError("GameObject deletion system needed!");
        }
    }

    public static void handleAdding() {

    }

    private static void handleHistory() {
        if (isIsPlaying()) {
            return;
        }

        if (KeyBinds.UNDO.isPressedOnce()) {
            historyCommandManager.undo();
        } else if (KeyBinds.REDO.isPressedOnce()) {
            historyCommandManager.redo();
        } else if (KeyBinds.REDO_ALT.isPressedOnce()) {
            historyCommandManager.redo();
        }
    }

    public static void renameAction(String defaultText, Runnable runnable) {
        ImGuiScreens.EDITOR_RENAME.setDefaultText(defaultText);
        ImGuiScreens.EDITOR_RENAME.setRunnable(runnable);
        ImGuiScreens.EDITOR_RENAME.show();
    }

    public static void saveScene() {
        Scene scene = SceneManager.getCurrentScene();

        if (scene.hasBeenDeserialized()) {
            SceneSerializer sceneSerializer = new SceneSerializer();
            sceneSerializer.saveScene(scene, scene.getScenePath().toString());
            setUnsaved(false);
        } else {
            CitadelLauncher.getInstance().handleError("This scene is hardcoded. It can't be saved this way!");
        }
    }

    public static void play() {
        if (!LevelEditor.isPlaying) {
            saveScene();
        }

        SceneManager.reloadScene();
        setIsPlaying(true);
        setIsPaused(false);
    }

    public static void stop() {
        setIsPlaying(false);

        if (SceneManager.getCurrentScene().hasBeenDeserialized()) {
            SceneSerializer sceneSerializer = new SceneSerializer();
            SceneManager.loadScene(sceneSerializer.loadScene(SceneManager.getCurrentScene().getScenePath()));
        } else {
            SceneManager.reloadScene();
        }
    }

    public static void pause() {
        setIsPaused(!isIsPaused());
    }

    public static GameObject getSelectedObject() {
        return selectedObject;
    }

    public static void setSelectedObject(GameObject gameObject) {
        selectedObject = gameObject;
    }

    public static HistoryCommandManager getHistoryCommandManager() {
        return historyCommandManager;
    }

    public static boolean isIsPlaying() {
        return isPlaying;
    }

    public static void setIsPlaying(boolean isPlaying) {
        LevelEditor.isPlaying = isPlaying;
        setSelectedObject(null);
    }

    public static boolean isIsPaused() {
        return isPaused;
    }

    public static void setIsPaused(boolean isPaused) {
        LevelEditor.isPaused = isPaused;
    }

    public static boolean isUnsaved() {
        return isUnsaved;
    }

    public static void setUnsaved(boolean value) {
        LevelEditor.isUnsaved = value;
    }

    public static HardcodedCamera getHardcodedCamera() {
        return hardcodedCamera;
    }
}
