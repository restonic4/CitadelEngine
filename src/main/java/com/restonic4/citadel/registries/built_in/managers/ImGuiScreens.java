package com.restonic4.citadel.registries.built_in.managers;

import com.restonic4.citadel.registries.AbstractRegistryInitializer;
import com.restonic4.citadel.registries.AssetLocation;
import com.restonic4.citadel.registries.Registries;
import com.restonic4.citadel.registries.Registry;
import com.restonic4.citadel.render.gui.guis.*;
import com.restonic4.citadel.render.gui.guis.editor.*;
import com.restonic4.citadel.util.CitadelConstants;

public class ImGuiScreens extends AbstractRegistryInitializer {
    public static ToggleableImGuiScreen RENDER_STATISTICS;
    public static ToggleableImGuiScreen CAMERA_SETTINGS;
    public static ToggleableImGuiScreen SERVER_CONSOLE;
    public static ToggleableImGuiScreen GAME_VIEWPORT;
    public static ToggleableImGuiScreen EDITOR_INSPECTOR;
    public static ToggleableImGuiScreen EDITOR_PROPERTIES;
    public static EditorAssetsImGui EDITOR_ASSETS;
    public static ToggleableImGuiScreen EDITOR_ABOUT;
    public static EditorRenameImGui EDITOR_RENAME;
    public static EditorMessageImGui EDITOR_MESSAGE;

    @Override
    public void register() {
        RENDER_STATISTICS = (ToggleableImGuiScreen) Registry.register(Registries.IM_GUI_SCREEN, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "render_statistics"), new StatisticsImGui());
        CAMERA_SETTINGS = (ToggleableImGuiScreen) Registry.register(Registries.IM_GUI_SCREEN, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "camera_settings"), new CameraSettingsImGui());
        SERVER_CONSOLE = (ToggleableImGuiScreen) Registry.register(Registries.IM_GUI_SCREEN, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "server_console"), new ServerConsoleImGui());
        GAME_VIEWPORT = (ToggleableImGuiScreen) Registry.register(Registries.IM_GUI_SCREEN, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "game_viewport"), new GameViewportImGui());
        EDITOR_INSPECTOR = (ToggleableImGuiScreen) Registry.register(Registries.IM_GUI_SCREEN, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "editor_inspector"), new EditorInspectorImGui());
        EDITOR_PROPERTIES = (ToggleableImGuiScreen) Registry.register(Registries.IM_GUI_SCREEN, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "editor_properties"), new EditorPropertiesImGui());
        EDITOR_ASSETS = (EditorAssetsImGui) Registry.register(Registries.IM_GUI_SCREEN, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "editor_assets"), new EditorAssetsImGui());
        EDITOR_ABOUT = (ToggleableImGuiScreen) Registry.register(Registries.IM_GUI_SCREEN, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "editor_about"), new EditorAboutImGui());
        EDITOR_RENAME = (EditorRenameImGui) Registry.register(Registries.IM_GUI_SCREEN, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "editor_rename"), new EditorRenameImGui());
        EDITOR_MESSAGE = (EditorMessageImGui) Registry.register(Registries.IM_GUI_SCREEN, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "editor_message"), new EditorMessageImGui());
    }
}
