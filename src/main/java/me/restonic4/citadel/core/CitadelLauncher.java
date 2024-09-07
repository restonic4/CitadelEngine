package me.restonic4.citadel.core;

import me.restonic4.citadel.events.types.CitadelLifecycleEvents;
import me.restonic4.citadel.localization.Localizer;
import me.restonic4.citadel.platform.PlatformManager;
import me.restonic4.citadel.platform.operating_systems.OperatingSystem;
import me.restonic4.citadel.registries.RegistryManager;
import me.restonic4.citadel.registries.built_in.managers.*;
import me.restonic4.citadel.sound.SoundManager;
import me.restonic4.citadel.util.GradleUtil;
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
        if (CitadelLauncher.instance == null) {
            throw new IllegalStateException("There no engine instanced");
        }

        return CitadelLauncher.instance;
    }

    public void launch() {
        CitadelLifecycleEvents.CITADEL_STARTING.invoker().onCitadelStarting(this);

        OperatingSystem operatingSystem = PlatformManager.getOperatingSystem().get();

        Logger.log("Starting Citadel engine");
        Logger.log("Platform: " + operatingSystem);
        Logger.log("Java locale: " + operatingSystem.getSystemLocale());

        GradleUtil.logInfo();

        SoundManager.getInstance().init();

        RegistryManager.registerBuiltInRegistrySet(new Sounds());
        RegistryManager.registerBuiltInRegistrySet(new ProfilerStats());
        RegistryManager.registerBuiltInRegistrySet(new Locales());
        RegistryManager.registerBuiltInRegistrySet(new KeyBinds());
        RegistryManager.registerBuiltInRegistrySet(new ImGuiScreens());
        RegistryManager.registerBuiltIn();

        Logger.log("Locale: " + Localizer.fromJavaLocale(operatingSystem.getSystemLocale()).getAssetLocation().getPath());

        Window.getInstance().run(this.citadelSettings.getiGameLogic());

        CitadelLifecycleEvents.CITADEL_STOPPED.invoker().onCitadelStopped(CitadelLauncher.getInstance());
    }

    public String getAppName() {
        return this.citadelSettings.getAppName();
    }
}
