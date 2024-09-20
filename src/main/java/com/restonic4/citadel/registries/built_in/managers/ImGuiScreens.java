package com.restonic4.citadel.registries.built_in.managers;

import com.restonic4.citadel.registries.AbstractRegistryInitializer;
import com.restonic4.citadel.registries.AssetLocation;
import com.restonic4.citadel.registries.Registries;
import com.restonic4.citadel.registries.Registry;
import com.restonic4.citadel.render.gui.guis.CameraSettingsImGui;
import com.restonic4.citadel.render.gui.guis.ServerConsoleImGui;
import com.restonic4.citadel.render.gui.guis.StatisticsImGui;
import com.restonic4.citadel.render.gui.guis.ToggleableImGuiScreen;
import com.restonic4.citadel.util.CitadelConstants;

public class ImGuiScreens extends AbstractRegistryInitializer {
    public static ToggleableImGuiScreen RENDER_STATISTICS;
    public static ToggleableImGuiScreen CAMERA_SETTINGS;
    public static ToggleableImGuiScreen SERVER_CONSOLE;

    @Override
    public void register() {
        RENDER_STATISTICS = (ToggleableImGuiScreen) Registry.register(Registries.IM_GUI_SCREEN, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "render_statistics"), new StatisticsImGui());
        CAMERA_SETTINGS = (ToggleableImGuiScreen) Registry.register(Registries.IM_GUI_SCREEN, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "camera_settings"), new CameraSettingsImGui());
        SERVER_CONSOLE = (ToggleableImGuiScreen) Registry.register(Registries.IM_GUI_SCREEN, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "server_console"), new ServerConsoleImGui());
    }
}
