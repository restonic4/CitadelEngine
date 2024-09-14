package me.restonic4.citadel.core;

import me.restonic4.NotRecommended;
import me.restonic4.citadel.registries.built_in.types.Locale;
import me.restonic4.citadel.util.CitadelConstants;
import me.restonic4.citadel.util.GradleUtil;

public class CitadelSettings {
    private IGameLogic clientGameLogic, serverGameLogic, sharedGameLogic;
    private String appName;
    private String[] args;

    private boolean isServer;
    private boolean thirdPartyNamespaceRegistrationAllowed;
    private String[] allowedNamespaces;

    public CitadelSettings(IGameLogic clientGameLogic, IGameLogic serverGameLogic, IGameLogic sharedGameLogic, String appName, String[] args) {
        this.clientGameLogic = clientGameLogic;
        this.serverGameLogic = serverGameLogic;
        this.sharedGameLogic = sharedGameLogic;
        this.appName = appName;
        this.args = args;

        isServer = GradleUtil.SERVER_BUILD;
        thirdPartyNamespaceRegistrationAllowed = true;
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

    public CitadelSettings setServerSide(boolean value) {
        this.isServer = value;
        return this;
    }

    @NotRecommended("It is not recommended to leave this method as false, as it will make it much more difficult for your community to create and use mods in your game.")
    public CitadelSettings setThirdPartyNamespaceRegistrationAllowed(boolean value) {
        this.thirdPartyNamespaceRegistrationAllowed = value;
        return this;
    }

    @NotRecommended("It is not recommended to leave this method as false, as it will make it much more difficult for your community to create and use mods in your game.")
    public CitadelSettings setThirdPartyNamespaceRegistrationAllowed(boolean value, String[] allowedNamespaces) {
        this.thirdPartyNamespaceRegistrationAllowed = value;
        this.allowedNamespaces = allowedNamespaces;
        return this;
    }

    // Getters

    public IGameLogic getClientGameLogic() {
        return this.clientGameLogic;
    }

    public IGameLogic getServerGameLogic() {
        return this.serverGameLogic;
    }

    public IGameLogic getSharedGameLogic() {
        return this.sharedGameLogic;
    }

    public String getAppName() {
        return this.appName;
    }

    public String[] getArgs() {
        return this.args;
    }

    public boolean isServerSide() {
        return this.isServer;
    }

    public boolean isThirdPartyNamespaceRegistrationAllowed() {
        return this.thirdPartyNamespaceRegistrationAllowed;
    }

    public String[] getAllowedNamespaces() {
        return this.allowedNamespaces;
    }
}
