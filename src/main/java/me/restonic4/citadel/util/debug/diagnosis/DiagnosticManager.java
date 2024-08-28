package me.restonic4.citadel.util.debug.diagnosis;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class DiagnosticManager {
    public static void send() {
        Logger.log("Sending diagnostic");

        PersistentLogger persistentLogger = Logger.getPersistentLogger();
        List<String> logBuffer = persistentLogger.getCompleteLogBuffer();

        StringBuilder issueBody = new StringBuilder();
        for (String log : logBuffer) {
            issueBody.append(log).append("\n");
        }

        String issueTitle = "Automatic Diagnostic Report";
        String issueDescription = issueBody.toString();

        String encodedTitle = encodeURIComponent(issueTitle);
        String encodedDescription = encodeURIComponent(issueDescription);

        String urlString = String.format(
                "https://github.com/restonic4/EpicAmazingCoolGameTestIGues/issues/new?title=%s&body=%s",
                encodedTitle, encodedDescription
        );

        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                URI uri = new URI(urlString);
                Desktop.getDesktop().browse(uri);
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    private static String encodeURIComponent(String value) {
        try {
            return java.net.URLEncoder.encode(value, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
