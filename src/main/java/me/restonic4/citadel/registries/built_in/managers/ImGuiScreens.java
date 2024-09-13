package me.restonic4.citadel.registries.built_in.managers;

import me.restonic4.citadel.registries.AbstractRegistryInitializer;
import me.restonic4.citadel.registries.AssetLocation;
import me.restonic4.citadel.registries.Registries;
import me.restonic4.citadel.registries.Registry;
import me.restonic4.citadel.registries.built_in.types.ImGuiScreen;
import me.restonic4.citadel.render.gui.guis.CameraSettingsImGui;
import me.restonic4.citadel.render.gui.guis.ServerConsoleImGui;
import me.restonic4.citadel.render.gui.guis.StatisticsImGui;
import me.restonic4.citadel.render.gui.guis.ToggleableImGuiScreen;
import me.restonic4.citadel.util.CitadelConstants;

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
