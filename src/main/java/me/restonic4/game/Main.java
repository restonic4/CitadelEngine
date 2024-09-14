package me.restonic4.game;

import me.restonic4.citadel.core.CitadelLauncher;
import me.restonic4.citadel.core.CitadelSettings;
import me.restonic4.citadel.networking.PacketData;
import me.restonic4.citadel.registries.built_in.managers.Locales;
import me.restonic4.citadel.util.debug.diagnosis.Logger;

public class Main {
    public static void main(String[] args) {
        CitadelSettings citadelSettings = new CitadelSettings(new ClientGameLogic(), new ServerGameLogic(), new SharedGameLogic(), "Citadel", args);
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
