package me.restonic4.engine.util.debug;

public abstract class Logger {
    private static boolean DEBUG_MODE = false;

    public static void setDebugMode(boolean value) {
        DEBUG_MODE = value;
    }

    public static boolean isDebugMode() {
        return DEBUG_MODE;
    }

    public static void log(Object message) {
        if (isDebugMode()) {
            message = getDebugInfo() + message;
        }

        System.out.println(message);
    }

    public static void logExtra(Object message) {
        if (isDebugMode()) {
            log(message);
        }
    }

    private static String getDebugInfo() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StackTraceElement caller = stackTrace[3];

        return String.format("[DEBUG] %s.%s() [Line %d]: ",
                caller.getClassName(),
                caller.getMethodName(),
                caller.getLineNumber());
    }
}
