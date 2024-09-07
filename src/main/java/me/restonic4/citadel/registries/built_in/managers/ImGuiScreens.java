package me.restonic4.citadel.registries.built_in.managers;

import me.restonic4.citadel.registries.AbstractRegistryInitializer;
import me.restonic4.citadel.registries.AssetLocation;
import me.restonic4.citadel.registries.Registries;
import me.restonic4.citadel.registries.Registry;
import me.restonic4.citadel.registries.built_in.types.ImGuiScreen;
import me.restonic4.citadel.render.gui.guis.CameraSettingsImGui;
import me.restonic4.citadel.render.gui.guis.StatisticsImGui;
import me.restonic4.citadel.util.CitadelConstants;

public class ImGuiScreens extends AbstractRegistryInitializer {
    public static ImGuiScreen RENDER_STATISTICS;
    public static ImGuiScreen CAMERA_SETTINGS;

    @Override
    public void register() {
        RENDER_STATISTICS = Registry.register(Registries.IM_GUI_SCREEN, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "render_statistics"), new StatisticsImGui());
        CAMERA_SETTINGS = Registry.register(Registries.IM_GUI_SCREEN, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "camera_settings"), new CameraSettingsImGui());
    }
}
