package me.restonic4.engine.util.debug;

import me.restonic4.shared.SharedConstants;
import me.restonic4.engine.util.FileManager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PersistentLogger {
    private final List<String> logBuffer = new ArrayList<>();
    private final File logFile;
    private boolean running = true;

    public PersistentLogger() {
        if (DebugManager.isDevEnvironment()) {
            logFile = new File(SharedConstants.LOG_FILE);
            return;
        }

        File logDir = new File(FileManager.createDirectory(SharedConstants.LOG_DIRECTORY));
        logFile = new File(logDir, SharedConstants.LOG_FILE);

        archiveOldLog();
        clearLogFile();

        Thread logWriterThread = new Thread(this::logWriter);
        logWriterThread.setDaemon(true); // Closes the thread when the app dies
        logWriterThread.start();

        Runtime.getRuntime().addShutdownHook(new Thread(this::onShutdown));

        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            logCrashInfo(t, e);
            DebugManager.displayCrashDialog(e);
            onShutdown();
        });

        writeLogsToFile();
    }

    public synchronized void log(String message) {
        logBuffer.add(message);
    }

    private void logWriter() {
        while (running) {
            try {
                Thread.sleep(SharedConstants.LOG_INTERVAL_MS);
                writeLogsToFile();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void archiveOldLog() {
        if (logFile.exists()) {
            String timestamp = new SimpleDateFormat(SharedConstants.LOG_DATE_FORMAT).format(new Date(logFile.lastModified()));
            File archivedFile = new File(logFile.getParent(), "Log_" + timestamp + ".txt");
            boolean renamed = logFile.renameTo(archivedFile);
            if (!renamed) {
                System.err.println("Failed to archive the old log file.");
            }
        }
    }

    private void clearLogFile() {
        try {
            new FileWriter(logFile, false).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private synchronized void writeLogsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true))) {
            for (String log : logBuffer) {
                writer.write(log);
                writer.newLine();
            }
            logBuffer.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void logCrashInfo(Thread t, Throwable e) {
        StringBuilder crashInfo = new StringBuilder();
        crashInfo.append("Uncaught exception in thread '").append(t.getName()).append("': ")
                .append(e.toString()).append("\n");

        for (StackTraceElement element : e.getStackTrace()) {
            crashInfo.append("\tat ").append(element.toString()).append("\n");
        }

        Throwable cause = e.getCause();
        while (cause != null) {
            crashInfo.append("Caused by: ").append(cause.toString()).append("\n");
            for (StackTraceElement element : cause.getStackTrace()) {
                crashInfo.append("\tat ").append(element.toString()).append("\n");
            }
            cause = cause.getCause();
        }

        log(crashInfo.toString());

        writeLogsToFile();
    }

    private void onShutdown() {
        writeLogsToFile();
        stop();
    }

    public void stop() {
        running = false;
        writeLogsToFile();
    }
}
