package me.restonic4.engine.util.debug;

import me.restonic4.engine.util.Constants;
import me.restonic4.engine.util.FileManager;
import me.restonic4.engine.util.math.RandomUtil;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

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

        Object[] options = {"View Log", "Open Log Folder", "Close"};

        int choice = JOptionPane.showOptionDialog(
                null,
                crashMessage.toString(),
                "Game Crash",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.ERROR_MESSAGE,
                null,
                options,
                options[0]
        );

        File logFile = new File(FileManager.getMainDirectory() + "/" + Constants.LOG_DIRECTORY + "/" + Constants.LOG_FILE);
        File logDir = new File(FileManager.getMainDirectory() + "/" + Constants.LOG_DIRECTORY);

        if (choice == 0) {
            // View Log button clicked
            try {
                Desktop.getDesktop().open(logFile);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else if (choice == 1) {
            // Open Log Folder button clicked
            try {
                Desktop.getDesktop().open(logDir);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
