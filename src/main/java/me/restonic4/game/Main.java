package me.restonic4.game;

import me.restonic4.citadel.core.CitadelLauncher;
import me.restonic4.citadel.core.CitadelSettings;
import me.restonic4.citadel.util.debug.DebugManager;

public class Main {
    public static void main(String[] args) {
        DebugManager.setDebugMode(true);

        CitadelSettings citadelSettings = new CitadelSettings(new Game(), "Citadel");
        citadelSettings.setVsync(true);
        citadelSettings.setVsyncFPSCap(60);
        citadelSettings.setCrashMessagesAmount(7);

        CitadelLauncher citadelLauncher = CitadelLauncher.create(citadelSettings);
        citadelLauncher.launch();
    }
}
