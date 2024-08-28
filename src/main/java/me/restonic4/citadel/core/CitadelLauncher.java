package me.restonic4.citadel.core;

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

        Window.getInstance().run(this.citadelSettings.getiGameLogic());
    }

    public String getAppName() {
        return this.citadelSettings.getAppName();
    }
}
