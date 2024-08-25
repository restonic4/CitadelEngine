package me.restonic4.engine.util.diagnosis;

import me.restonic4.engine.util.debug.Logger;
import me.restonic4.engine.util.debug.PersistentLogger;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class DiagnosticManager {
    public static void send() {
        Logger.log("Sending diagnostic");

        PersistentLogger persistentLogger = Logger.getPersistentLogger();
        List<String> logBuffer = persistentLogger.getLogBuffer();

        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                URI uri = new URI("https://github.com/restonic4/EpicAmazingCoolGameTestIGues/issues/new");

                Desktop.getDesktop().browse(uri);
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }
}
