package me.restonic4.game;

import me.restonic4.citadel.core.CitadelLauncher;
import me.restonic4.citadel.core.CitadelSettings;
import me.restonic4.citadel.events.EventResult;
import me.restonic4.citadel.events.types.CitadelLifecycleEvents;
import me.restonic4.citadel.events.types.RegistriesEvents;
import me.restonic4.citadel.registries.built_in.managers.Locales;
import me.restonic4.citadel.util.debug.DebugManager;
import me.restonic4.citadel.util.debug.diagnosis.Logger;
import me.restonic4.citadel.util.debug.diagnosis.ProfilerManager;

public class Main {
    public static void main(String[] args) {
        CitadelSettings citadelSettings = new CitadelSettings(new Game(), "Citadel");
        citadelSettings.setVsync(true);
        citadelSettings.setFPSCap(60);
        citadelSettings.setCrashMessagesAmount(7);
        citadelSettings.setWindowTitleChangeFrequency(3);
        citadelSettings.setDefaultLocale(Locales.EN_US);
        //citadelSettings.setServerSide(true);

        CitadelLauncher citadelLauncher = CitadelLauncher.create(citadelSettings);
        citadelLauncher.launch();
    }
}
