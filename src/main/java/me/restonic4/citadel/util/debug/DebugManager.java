package me.restonic4.citadel.util.debug;

import me.restonic4.citadel.localization.Localizer;
import me.restonic4.citadel.util.debug.diagnosis.DiagnosticManager;
import me.restonic4.citadel.util.debug.diagnosis.Logger;
import me.restonic4.citadel.util.CitadelConstants;
import me.restonic4.citadel.files.FileManager;
import me.restonic4.citadel.util.debug.diagnosis.OpenGLDebugOutput;
import me.restonic4.citadel.util.math.RandomUtil;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL43.GL_DEBUG_OUTPUT;
import static org.lwjgl.opengl.GL43.GL_DEBUG_OUTPUT_SYNCHRONOUS;

public class DebugManager {
    private static boolean DEBUG_MODE = false;
    private static boolean WIREFRAME_MODE = false;
    private static boolean VERTICES_MODE = false;
    private static boolean STOP_BATCH_RENDER = false;

    private static OpenGLDebugOutput openGLDebugOutput = new OpenGLDebugOutput();
    private static boolean openGLDebugOutputStarted = false;

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
        String messageKey = "system.message.crash." + RandomUtil.random(1, CitadelConstants.CRASH_MESSAGES_AMOUNT);
        String message = Localizer.localizeKey(messageKey);

        Logger.log("///////////////////////////");
        Logger.log("CRASHED -> " + message);
        Logger.log("///////////////////////////");

        StringBuilder crashMessage = new StringBuilder();
        crashMessage.append(message + "\n\n")
                .append("Error: ").append(e.toString()).append("\n\n")
                .append(Localizer.localizeKey("system.message.crash.details"));

        Object[] options = {
            Localizer.localizeKey("system.ui.crash.button.1"),
            Localizer.localizeKey("system.ui.crash.button.2"),
            Localizer.localizeKey("system.ui.crash.button.3"),
            Localizer.localizeKey("system.ui.crash.button.4")
        };

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

        File logFile = new File(FileManager.getMainDirectory() + "/" + CitadelConstants.LOG_DIRECTORY + "/" + CitadelConstants.LOG_FILE);
        File logDir = new File(FileManager.getMainDirectory() + "/" + CitadelConstants.LOG_DIRECTORY);

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

    public static boolean isBatchRenderingDisabled() {
        return STOP_BATCH_RENDER;
    }

    public static void setBatchRenderingDisabled(boolean value) {
        STOP_BATCH_RENDER = value;
    }

    public static void toggleBatchRenderingDisabled() {
        setVerticesMode(!STOP_BATCH_RENDER);
    }

    public static void enableOpenGLAdvancedLogOutput(boolean value) {
        if (value) {
            glEnable(GL_DEBUG_OUTPUT);
            glEnable(GL_DEBUG_OUTPUT_SYNCHRONOUS);

            if (!openGLDebugOutputStarted) {
                openGLDebugOutputStarted = true;
                openGLDebugOutput.setupDebugMessageCallback();
            }
        }
        else {
            glDisable(GL_DEBUG_OUTPUT);
            glDisable(GL_DEBUG_OUTPUT_SYNCHRONOUS);
        }
    }
}
