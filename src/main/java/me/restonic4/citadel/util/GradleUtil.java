package me.restonic4.citadel.util;

import me.restonic4.citadel.util.debug.diagnosis.Logger;

public class GradleUtil {
    // These variables are generated with a gradle task
    public static final String VERSION = "0.3.6";
    public static final String APP_NAME = "Citadel";

    public static void logInfo() {
        Logger.log("/###### | COMPILED GRADLE DATA | ######/");
        Logger.log(".                                      .");
        Logger.log("Version: " + VERSION);
        Logger.log("AppName: " + APP_NAME);
        Logger.log(".                                      .");
        Logger.log("/###### | #################### | ######/");
    }
}
