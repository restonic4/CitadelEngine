package me.restonic4.citadel.core;

import me.restonic4.citadel.registries.built_in.types.Locale;
import me.restonic4.citadel.util.CitadelConstants;

public class CitadelSettings {
    private IGameLogic iGameLogic;
    private String appName;

    public CitadelSettings(IGameLogic iGameLogic, String appName) {
        this.iGameLogic = iGameLogic;
        this.appName = appName;
    }

    // Configuration

    public CitadelSettings setCrashMessagesAmount(int amount) {
        CitadelConstants.CRASH_MESSAGES_AMOUNT = amount;
        return this;
    }

    public CitadelSettings setVsync(boolean value) {
        CitadelConstants.VSYNC = value;
        return this;
    }

    public CitadelSettings setFPSCap(int value) {
        CitadelConstants.FPS_CAP = value;
        return this;
    }

    public CitadelSettings setWindowTitleChangeFrequency(int value) {
        CitadelConstants.WINDOW_TITLE_CHANGE_FREQUENCY = value;
        return this;
    }

    public CitadelSettings setDefaultLocale(Locale locale) {
        CitadelConstants.DEFAULT_LOCALE = locale;
        return this;
    }

    // Getters

    public IGameLogic getiGameLogic() {
        return this.iGameLogic;
    }

    public String getAppName() {
        return this.appName;
    }
}
