package com.restonic4.citadel.core;

import com.restonic4.NotRecommended;
import com.restonic4.citadel.registries.built_in.types.Locale;
import com.restonic4.citadel.util.CitadelConstants;
import com.restonic4.citadel.util.GradleUtil;
import org.lwjgl.opengl.GLCapabilities;

public class CitadelSettings {
    private GameLogic clientGameLogic, serverGameLogic, sharedGameLogic;
    private String appName;
    private String[] args;

    private boolean isServer;
    private int serverPort;

    private boolean thirdPartyNamespaceRegistrationAllowed;
    private String[] allowedNamespaces;
    private boolean frameBuffersPreGenerationDisabled;
    private boolean isEditorMode;

    private GLCapabilities glCapabilities;

    public CitadelSettings(GameLogic clientGameLogic, GameLogic serverGameLogic, GameLogic sharedGameLogic, String appName, String[] args) {
        this.clientGameLogic = clientGameLogic;
        this.serverGameLogic = serverGameLogic;
        this.sharedGameLogic = sharedGameLogic;
        this.appName = appName;
        this.args = args;

        this.isServer = GradleUtil.SERVER_BUILD;
        this.serverPort = 8080;

        this.thirdPartyNamespaceRegistrationAllowed = true;
        this.frameBuffersPreGenerationDisabled = false;
    }

    // Configuration

    /**
     * Sets the amount of crash messages available. This is used at the Crash screen, the random crash message.
     * @param amount Amount of crash messages.
     */
    public CitadelSettings setCrashMessagesAmount(int amount) {
        CitadelConstants.CRASH_MESSAGES_AMOUNT = amount;
        return this;
    }

    /**
     * Enables/Disables VSYNC.
     */
    public CitadelSettings setVsync(boolean value) {
        CitadelConstants.VSYNC = value;
        return this;
    }

    /**
     * Sets the max FPS allowed.
     */
    public CitadelSettings setFPSCap(int value) {
        CitadelConstants.FPS_CAP = value;
        return this;
    }

    /**
     * Sets how many times the window title can change in seconds.
     */
    public CitadelSettings setWindowTitleChangeFrequency(int value) {
        CitadelConstants.WINDOW_TITLE_CHANGE_FREQUENCY = value;
        return this;
    }

    /**
     * Sets the default language.
     */
    public CitadelSettings setDefaultLocale(Locale locale) {
        CitadelConstants.DEFAULT_LOCALE = locale;
        return this;
    }

    /**
     * Sets if the current environment is the server.
     */
    public CitadelSettings setServerSide(boolean value) {
        this.isServer = value;
        return this;
    }

    /**
     * Enables/Disables modding.
     */
    @NotRecommended("It is not recommended to leave this method as false, as it will make it much more difficult for your community to create and use mods in your game.")
    public CitadelSettings setThirdPartyNamespaceRegistrationAllowed(boolean value) {
        this.thirdPartyNamespaceRegistrationAllowed = value;
        return this;
    }

    /**
     * Enables/Disables modding.
     */
    @NotRecommended("It is not recommended to leave this method as false, as it will make it much more difficult for your community to create and use mods in your game.")
    public CitadelSettings setThirdPartyNamespaceRegistrationAllowed(boolean value, String[] allowedNamespaces) {
        this.thirdPartyNamespaceRegistrationAllowed = value;
        this.allowedNamespaces = allowedNamespaces;
        return this;
    }

    /**
     * Enables/Disables the Frame buffers pre-generation phase.
     */
    public CitadelSettings disableFrameBuffersPreGeneration(boolean value) {
        this.frameBuffersPreGenerationDisabled = value;
        return this;
    }

    /**
     * Sets the server port.
     */
    public CitadelSettings setServerPort(int port) {
        this.serverPort = port;
        return this;
    }

    /**
     * Sets the server port.
     */
    public CitadelSettings setEditorMode(boolean value) {
        this.isEditorMode = value;
        return this;
    }

    public CitadelSettings setGLCapabilities(GLCapabilities glCapabilities) {
        this.glCapabilities = glCapabilities;
        return this;
    }

    // Getters

    public GameLogic getClientGameLogic() {
        return this.clientGameLogic;
    }

    public GameLogic getServerGameLogic() {
        return this.serverGameLogic;
    }

    public GameLogic getSharedGameLogic() {
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

    public int getServerPort() {
        return this.serverPort;
    }

    public boolean isThirdPartyNamespaceRegistrationAllowed() {
        return this.thirdPartyNamespaceRegistrationAllowed;
    }

    public String[] getAllowedNamespaces() {
        return this.allowedNamespaces;
    }

    public boolean isFrameBuffersPreGenerationDisabled() {
        return this.frameBuffersPreGenerationDisabled;
    }

    public boolean isEditorMode() {
        return this.isEditorMode;
    }

    public boolean shouldGenerateBindlessTextures() {
        if (glCapabilities.GL_ARB_bindless_texture /*&& glCapabilities.GL_NV_gpu_shader5*/) {
            return true;
        }

        // If we are launching the game as normal, we need bindless textures to render the scene.
        // In the level editor we can display an error on the game viewport.
        if (!isEditorMode) {
            return true;
        }

        return false;
    }
}
