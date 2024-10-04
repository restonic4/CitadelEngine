package com.restonic4.citadel.core;

import com.restonic4.citadel.input.KeyListener;
import com.restonic4.citadel.registries.built_in.managers.ImGuiScreens;
import com.restonic4.citadel.render.gui.ImGuiHelper;
import com.restonic4.citadel.util.debug.diagnosis.Logger;
import com.restonic4.citadel.world.object.GameObject;
import imgui.ImGui;
import imgui.type.ImBoolean;
import imgui.type.ImString;
import org.lwjgl.glfw.GLFW;

import static imgui.flag.ImGuiWindowFlags.*;
import static imgui.flag.ImGuiWindowFlags.NoNavFocus;

// TODO: Make the UIs responsive
// TODO: Make an options/preferences window
// TODO: Make a keybindings UI to configure the keybindings such as F2, ESC and ENTER
public abstract class LevelEditor {
    private static Window window;

    private static GameObject selectedObject;

    private static boolean isSceneViewVisible, isStatisticsVisible, isInspectorVisible, isPropertiesVisible, isAssetsVisible;
    private static boolean isRenamingEnabled = false;

    public static void init() {
        window = Window.getInstance();

        window.setCursorLocked(false);

        isSceneViewVisible = true;
        isStatisticsVisible = false;
        isInspectorVisible = true;
        isPropertiesVisible = true;
        isAssetsVisible = true;

        ImGuiScreens.GAME_VIEWPORT.show();
        ImGuiScreens.EDITOR_INSPECTOR.show();
        ImGuiScreens.EDITOR_PROPERTIES.show();
        ImGuiScreens.EDITOR_ASSETS.show();
    }

    // TODO: Weird behaviours in some screens, it should ignore the navbar height
    public static void render() {
        int windowFlags = MenuBar | NoDocking | NoTitleBar | NoCollapse | NoResize | NoMove | NoBringToFrontOnFocus | NoNavFocus;

        ImGui.begin("Dockspace Demo", new ImBoolean(true), windowFlags);
        ImGui.popStyleVar(2);

        if (ImGui.beginMainMenuBar()) {
            if (ImGui.beginMenu("View")) {
                if (ImGui.menuItem("Scene view", isSceneViewVisible)) {
                    isSceneViewVisible = !isSceneViewVisible;

                    if (isSceneViewVisible) {
                        ImGuiScreens.GAME_VIEWPORT.show();
                    }
                    else {
                        ImGuiScreens.GAME_VIEWPORT.hide();
                    }
                }

                if (ImGui.menuItem("Statistics", isStatisticsVisible)) {
                    isStatisticsVisible = !isStatisticsVisible;

                    if (isStatisticsVisible) {
                        ImGuiScreens.RENDER_STATISTICS.show();
                    }
                    else {
                        ImGuiScreens.RENDER_STATISTICS.hide();
                    }
                }

                if (ImGui.menuItem("Inspector", isInspectorVisible)) {
                    isInspectorVisible = !isInspectorVisible;

                    if (isInspectorVisible) {
                        ImGuiScreens.EDITOR_INSPECTOR.show();
                    }
                    else {
                        ImGuiScreens.EDITOR_INSPECTOR.hide();
                    }
                }

                if (ImGui.menuItem("Properties", isPropertiesVisible)) {
                    isPropertiesVisible = !isPropertiesVisible;

                    if (isPropertiesVisible) {
                        ImGuiScreens.EDITOR_PROPERTIES.show();
                    }
                    else {
                        ImGuiScreens.EDITOR_PROPERTIES.hide();
                    }
                }

                if (ImGui.menuItem("Assets", isAssetsVisible)) {
                    isAssetsVisible = !isAssetsVisible;

                    if (isAssetsVisible) {
                        ImGuiScreens.EDITOR_ASSETS.show();
                    }
                    else {
                        ImGuiScreens.EDITOR_ASSETS.hide();
                    }
                }

                ImGui.separator();

                if (ImGui.beginMenu("Overlay")) {
                    if (ImGui.menuItem("Show Grid")) {
                        //TODO: Rendering system
                    }
                    if (ImGui.menuItem("Show Axis")) {
                        //TODO: Rendering system
                    }
                    ImGui.endMenu();
                }

                ImGui.endMenu();
            }

            // Menú "Help"
            if (ImGui.beginMenu("Help")) {
                if (ImGui.menuItem("About")) {
                    //TODO: UI
                }

                if (ImGui.menuItem("Documentation")) {
                    //TODO: UI
                }

                if (ImGui.menuItem("Version")) {
                    //TODO: UI
                }

                ImGui.endMenu();
            }

            ImGui.endMainMenuBar();
        }

        handleRenaming();
    }

    private static void handleRenaming() {
        if (KeyListener.isKeyPressedOnce(GLFW.GLFW_KEY_F2) && selectedObject != null) {
            isRenamingEnabled = true;
        }

        if (isRenamingEnabled) {
            ImGuiHelper.renameBox(getSelectedObject().getName());

            if (KeyListener.isKeyPressedOnce(GLFW.GLFW_KEY_ESCAPE)) {
                isRenamingEnabled = false;
                ImGuiHelper.resetRenameBox();
            }
            else if (KeyListener.isKeyPressedOnce(GLFW.GLFW_KEY_ENTER)) {
                String newName = ImGuiHelper.getRenameBoxResult();

                isRenamingEnabled = false;
                ImGuiHelper.resetRenameBox();

                getSelectedObject().setName(newName);
            }
        }
    }

    public static GameObject getSelectedObject() {
        return selectedObject;
    }

    public static void setSelectedObject(GameObject gameObject) {
        selectedObject = gameObject;
    }
}
