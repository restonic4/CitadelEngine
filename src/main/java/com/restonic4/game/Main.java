package com.restonic4.game;

import com.restonic4.citadel.core.CitadelLauncher;
import com.restonic4.citadel.core.CitadelSettings;
import com.restonic4.citadel.registries.built_in.managers.Locales;

public class Main {
    public static void main(String[] args) {
        CitadelSettings citadelSettings = new CitadelSettings(new ClientGameLogic(), new ServerGameLogic(), new SharedGameLogic(), "Citadel", args);
        citadelSettings.setVsync(true);
        citadelSettings.setFPSCap(60);
        citadelSettings.setCrashMessagesAmount(7);
        citadelSettings.setWindowTitleChangeFrequency(3);
        citadelSettings.setDefaultLocale(Locales.EN_US);
        //citadelSettings.disableFrameBuffersPreGeneration(true);
        //citadelSettings.setServerSide(true);

        CitadelLauncher citadelLauncher = CitadelLauncher.create(citadelSettings);
        citadelLauncher.launch();
    }
}
