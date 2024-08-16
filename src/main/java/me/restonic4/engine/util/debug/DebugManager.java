package me.restonic4.engine.util.debug;

import me.restonic4.engine.util.Constants;
import me.restonic4.engine.util.FileManager;
import me.restonic4.engine.util.math.RandomUtil;

import javax.swing.*;
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

    public static void displayCrashDialog(Throwable e) {
        StringBuilder crashMessage = new StringBuilder();
        crashMessage.append(RandomUtil.getRandom(Constants.CRASH_MESSAGES) + "\n\n")
                .append("Error: ").append(e.toString()).append("\n\n")
                .append("A detailed log has been saved to the logs folder.");

        JOptionPane.showMessageDialog(
                null,
                crashMessage.toString(),
                "Game Crash",
                JOptionPane.ERROR_MESSAGE
        );
    }
}
