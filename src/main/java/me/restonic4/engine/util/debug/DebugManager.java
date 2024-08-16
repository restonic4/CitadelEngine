package me.restonic4.engine.util.debug;

import me.restonic4.engine.util.FileManager;

import java.io.File;

public class DebugManager {
    private static boolean DEBUG_MODE = false;

    public static void setDebugMode(boolean value) {
        DEBUG_MODE = value;
    }

    public static boolean isDebugMode() {
        return DEBUG_MODE;
    }

    public static boolean isDevEnvironment() {
        return FileManager.isFileInSystem("build.gradle");
    }
}
