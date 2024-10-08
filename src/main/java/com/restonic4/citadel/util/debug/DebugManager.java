package com.restonic4.citadel.util.debug;

import com.restonic4.citadel.events.EventResult;
import com.restonic4.citadel.events.types.DebugEvents;
import com.restonic4.citadel.localization.Localizer;
import com.restonic4.citadel.registries.built_in.managers.Sounds;
import com.restonic4.citadel.sound.SoundSource;
import com.restonic4.citadel.util.debug.diagnosis.DiagnosticManager;
import com.restonic4.citadel.util.debug.diagnosis.Logger;
import com.restonic4.citadel.util.CitadelConstants;
import com.restonic4.citadel.files.FileManager;
import com.restonic4.citadel.util.debug.diagnosis.OpenGLDebugOutput;
import com.restonic4.citadel.util.math.RandomUtil;

import org.joml.Vector3f;

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

    private static Vector3f[] cascadeShadowFrustumVertices;

    /**
     * Enables/Disables the debug mode. This mode will log extra messages and also display where the log came from, such as the class package and line of code.
     * @param value true/false
     */
    public static void setDebugMode(boolean value) {
        EventResult eventResult = DebugEvents.DEBUG_MODE_CHANGED.invoker().onDebugModeChanged(DEBUG_MODE, value);
        if (eventResult == EventResult.CANCELED) {
            /*

            If you are a modder/game dev, do not remove this log print. (Logger.log())
            Because maybe there is something canceling the debug mode toggle that isn't supposed to.
            If you really need to remove it for some reason, contact us, create an issue on our repo at {@link https://github.com/restonic4/CitadelEngine/issues}

            */
            Logger.log("The debug mode setter was canceled by event");
            return;
        }

        DEBUG_MODE = value;

        // PekeUranga suggested this, it's beautiful.
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

    /**
     * Displays a crash dialog.
     * @param e The error.
     */
    public static void displayCrashDialog(Throwable e) {
        String messageKey = "citadel:system.message.crash." + RandomUtil.random(1, CitadelConstants.CRASH_MESSAGES_AMOUNT);
        String message = Localizer.localizeKey(messageKey);

        SoundSource soundSource = Sounds.CRASH.createSource(false, true);
        soundSource.setPosition(new Vector3f(0, 0, 0));
        soundSource.play();

        Logger.log("///////////////////////////");
        Logger.log(Localizer.localizeKey("citadel:system.message.crash") + " -> " + message);
        Logger.log("///////////////////////////");

        StringBuilder crashMessage = new StringBuilder();
        crashMessage.append(message + "\n\n")
                .append("Error: ").append(e.toString()).append("\n\n")
                .append(Localizer.localizeKey("citadel:system.message.crash.details"));

        Object[] options = {
            Localizer.localizeKey("citadel:system.ui.crash.button.1"),
            Localizer.localizeKey("citadel:system.ui.crash.button.2"),
            Localizer.localizeKey("citadel:system.ui.crash.button.3"),
            Localizer.localizeKey("citadel:system.ui.crash.button.4")
        };

        int choice = JOptionPane.showOptionDialog(
                null,
                crashMessage.toString(),
                Localizer.localizeKey("citadel:system.message.crash.title"),
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

    /**
     * Enables/Disables wireframe mode.
     */
    public static void setWireFrameMode(boolean value) {
        WIREFRAME_MODE = value;
    }

    /**
     * Enables/Disables wireframe mode.
     */
    public static void toggleWireFrameMode() {
        setWireFrameMode(!WIREFRAME_MODE);
    }

    public static boolean isVerticesMode() {
        return VERTICES_MODE;
    }

    /**
     * Enables/Disables visible vertices points.
     */
    public static void setVerticesMode(boolean value) {
        VERTICES_MODE = value;
    }

    /**
     * Enables/Disables visible vertices points.
     */
    public static void toggleVerticesMode() {
        setVerticesMode(!VERTICES_MODE);
    }

    public static boolean isBatchRenderingDisabled() {
        return STOP_BATCH_RENDER;
    }

    /**
     * Enables/Disables model rendering.
     */
    public static void setBatchRenderingDisabled(boolean value) {
        STOP_BATCH_RENDER = value;
    }

    /**
     * Enables/Disables model rendering.
     */
    public static void toggleBatchRenderingDisabled() {
        setVerticesMode(!STOP_BATCH_RENDER);
    }

    /**
     * Enables/Disables advanced OpenGL output/debug info.
     */
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

    public static Vector3f[] getCascadeShadowFrustumVertices() {
        return cascadeShadowFrustumVertices;
    }

    public static void setCascadeShadowFrustumVertices(Vector3f[] cascadeShadowFrustumVertices) {
        DebugManager.cascadeShadowFrustumVertices = cascadeShadowFrustumVertices;
    }
}
