package com.restonic4.citadel.util;

import com.restonic4.citadel.platform.PlatformManager;
import com.restonic4.citadel.util.debug.diagnosis.Logger;

public class GradleUtil {
    // These variables are generated with a gradle task
    public static final String VERSION = "0.8-DEV-2";
    public static final String APP_NAME = "Citadel";
    public static final Boolean CONSOLE_ENABLED = false;
    public static final Boolean SERVER_BUILD = false;

    public static void logInfo() {
        Logger.log(StringBuilderHelper.concatenate(
                "//###### | COMPILED GRADLE DATA | ######\\\\", PlatformManager.getEndOfLine(),
                "||                                      ||", PlatformManager.getEndOfLine(),
                "||  -  VERSION: ", VERSION, "                   ||", PlatformManager.getEndOfLine(),
                "||  -  APP_NAME: ", APP_NAME, "                ||", PlatformManager.getEndOfLine(),
                "||  -  CONSOLE_ENABLED: ", CONSOLE_ENABLED, "           ||", PlatformManager.getEndOfLine(),
                "||  -  SERVER_BUILD: ", SERVER_BUILD, "              ||", PlatformManager.getEndOfLine(),
                "||                                      ||", PlatformManager.getEndOfLine(),
                "\\\\###### | #################### | ######//", PlatformManager.getEndOfLine()
        ));
    }
}
