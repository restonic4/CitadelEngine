package com.restonic4.citadel.util.debug.diagnosis;

import com.restonic4.citadel.util.StringBuilderHelper;
import com.restonic4.citadel.util.debug.DebugManager;

// TODO: Some methods display wrong stacktrace info
public abstract class Logger {
    private static final PersistentLogger persistentLogger = new PersistentLogger();

    public static void log(Object message) {
        message = convertMessage(message);

        if (DebugManager.isDebugMode()) {
            message = getDebugInfo() + message;
        }

        System.out.println(message);

        persistentLogger.log(message.toString());
    }

    public static void logExtra(Object message) {
        if (DebugManager.isDebugMode()) {
            log(message);
        }
    }

    public static void logError(Object message) {
        message = convertMessage(message);

        log(StringBuilderHelper.concatenate("[ERROR]: ", message));
    }

    public static void logError(Exception exception) {
        log(StringBuilderHelper.concatenate("[ERROR]: ", exception));
        exception.printStackTrace();
    }

    public static void throwError(Exception exception) {
        if (!DebugManager.isDevEnvironment()) {
            logError(exception);
        }

        throw new RuntimeException(exception);
    }

    public static String convertMessage(Object message) {
        if (message == null) {
           return "[NULL_OBJECT]";
        }

        return message.toString();
    }

    private static String getDebugInfo() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StackTraceElement caller = stackTrace[3];

        return String.format("[DEBUG] %s.%s() [Line %d]: ",
                caller.getClassName(),
                caller.getMethodName(),
                caller.getLineNumber());
    }

    public static void stop() {
        if (!DebugManager.isDevEnvironment()) {
            persistentLogger.stop();
        }
    }

    public static PersistentLogger getPersistentLogger() {
        return persistentLogger;
    }
}
