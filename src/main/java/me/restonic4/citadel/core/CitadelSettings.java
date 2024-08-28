package me.restonic4.citadel.core;

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

    public CitadelSettings setVsyncFPSCap(int value) {
        CitadelConstants.FPS_CAP = value;
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
