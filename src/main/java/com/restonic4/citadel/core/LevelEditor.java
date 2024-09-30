package com.restonic4.citadel.core;

import com.restonic4.citadel.registries.built_in.managers.ImGuiScreens;
import com.restonic4.citadel.world.object.GameObject;
import imgui.ImGui;
import imgui.type.ImBoolean;

import static imgui.flag.ImGuiWindowFlags.*;
import static imgui.flag.ImGuiWindowFlags.NoNavFocus;

public abstract class LevelEditor {
    private static Window window;

    private static GameObject selectedObject;

    private static boolean isSceneViewVisible, isStatisticsVisible, isInspectorVisible;

    public static void init() {
        window = Window.getInstance();

        window.setCursorLocked(false);

        isSceneViewVisible = true;
        isStatisticsVisible = true;
        isInspectorVisible = true;

        ImGuiScreens.GAME_VIEWPORT.show();
        ImGuiScreens.RENDER_STATISTICS.show();
        ImGuiScreens.EDITOR_INSPECTOR.show();
    }

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

                ImGui.separator();

                if (ImGui.beginMenu("Overlay")) {
                    if (ImGui.menuItem("Show Grid")) {
                        // Acción para "Show Grid"
                    }
                    if (ImGui.menuItem("Show Axis")) {
                        // Acción para "Show Axis"
                    }
                    ImGui.endMenu();
                }

                ImGui.endMenu();
            }

            // Menú "Help"
            if (ImGui.beginMenu("Help")) {
                if (ImGui.menuItem("About")) {

                }

                if (ImGui.menuItem("Documentation")) {

                }

                if (ImGui.menuItem("Version")) {

                }

                ImGui.endMenu();
            }

            ImGui.endMainMenuBar();
        }
    }

    public static GameObject getSelectedObject() {
        return selectedObject;
    }

    public static void setSelectedObject(GameObject gameObject) {
        selectedObject = gameObject;
    }
}
