package me.restonic4.citadel.util;

import me.restonic4.citadel.util.debug.diagnosis.Logger;

public class GradleUtil {
    // These variables are generated with a gradle task
    public static final String VERSION = "0.6";
    public static final String APP_NAME = "Citadel";
    public static final Boolean CONSOLE_ENABLED = false;
    public static final Boolean SERVER_BUILD = false;

    public static void logInfo() {
        Logger.log("/###### | COMPILED GRADLE DATA | ######/");
        Logger.log(".                                      .");
        Logger.log("VERSION: " + VERSION);
        Logger.log("APP_NAME: " + APP_NAME);
        Logger.log("CONSOLE_ENABLED: " + CONSOLE_ENABLED);
        Logger.log("SERVER_BUILD: " + SERVER_BUILD);
        Logger.log(".                                      .");
        Logger.log("/###### | #################### | ######/");
    }
}
