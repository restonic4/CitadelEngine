package me.restonic4.engine.util.debug;

import me.restonic4.engine.localization.Localizer;
import me.restonic4.engine.util.debug.diagnosis.DiagnosticManager;
import me.restonic4.engine.util.debug.diagnosis.Logger;
import me.restonic4.shared.SharedConstants;
import me.restonic4.engine.files.FileManager;
import me.restonic4.engine.util.math.RandomUtil;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class DebugManager {
    private static boolean DEBUG_MODE = false;
    private static boolean WIREFRAME_MODE = false;
    private static boolean VERTICES_MODE = false;

    public static void setDebugMode(boolean value) {
        DEBUG_MODE = value;

        if (DEBUG_MODE) {
            Logger.log("Modo sigma activado");
        }
    }

    public static boolean isDebugMode() {
        return DEBUG_MODE;
    }

    public static boolean isDevEnvironment() {
        return FileManager.isFileInSystem("build.gradle");
    }

    public static void displayCrashDialog(Throwable e) {
        String messageKey = "system.message.crash." + RandomUtil.random(1, SharedConstants.CRASH_MESSAGES);
        String message = Localizer.localizeKey(messageKey);

        Logger.log("///////////////////////////");
        Logger.log("CRASHED -> " + message);
        Logger.log("///////////////////////////");

        StringBuilder crashMessage = new StringBuilder();
        crashMessage.append(message + "\n\n")
                .append("Error: ").append(e.toString()).append("\n\n")
                .append("A detailed log has been saved to the logs folder.");

        Object[] options = {"View Log", "Open Log Folder", "Report the issue", "Close"};

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

        File logFile = new File(FileManager.getMainDirectory() + "/" + SharedConstants.LOG_DIRECTORY + "/" + SharedConstants.LOG_FILE);
        File logDir = new File(FileManager.getMainDirectory() + "/" + SharedConstants.LOG_DIRECTORY);

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
        } else if (choice == 2) {
            DiagnosticManager.send();
        }
    }

    public static boolean isWireframeMode() {
        return WIREFRAME_MODE;
    }

    public static void setWireFrameMode(boolean value) {
        WIREFRAME_MODE = value;
    }

    public static void toggleWireFrameMode() {
        setWireFrameMode(!WIREFRAME_MODE);
    }

    public static boolean isVerticesMode() {
        return VERTICES_MODE;
    }

    public static void setVerticesMode(boolean value) {
        VERTICES_MODE = value;
    }

    public static void toggleVerticesMode() {
        setVerticesMode(!VERTICES_MODE);
    }
}
