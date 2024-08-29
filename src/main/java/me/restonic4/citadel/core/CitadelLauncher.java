package me.restonic4.citadel.core;

import me.restonic4.citadel.platform.PlatformManager;
import me.restonic4.citadel.registries.RegistryManager;
import me.restonic4.citadel.registries.built_in.ProfilerStats;
import me.restonic4.citadel.util.debug.diagnosis.Logger;

public class CitadelLauncher {
    private static CitadelLauncher instance;

    private CitadelSettings citadelSettings;

    private CitadelLauncher(CitadelSettings citadelSettings) {
        this.citadelSettings = citadelSettings;
    }

    public static CitadelLauncher create(CitadelSettings citadelSettings) {
        if (CitadelLauncher.instance == null) {
            CitadelLauncher.instance = new CitadelLauncher(citadelSettings);
        }
        return CitadelLauncher.instance;
    }

    public static CitadelLauncher getInstance() {
        return CitadelLauncher.instance;
    }

    public void launch() {
        Logger.log("Starting Citadel engine");
        Logger.log("Platform: " + PlatformManager.getOperatingSystem());

        RegistryManager.registerBuiltInRegistrySet(new ProfilerStats());
        RegistryManager.registerBuiltIn();

        Window.getInstance().run(this.citadelSettings.getiGameLogic());
    }

    public String getAppName() {
        return this.citadelSettings.getAppName();
    }
}
