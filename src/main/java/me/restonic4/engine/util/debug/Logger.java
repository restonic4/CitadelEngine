package me.restonic4.engine.util.debug;

public abstract class Logger {
    private static final PersistentLogger persistentLogger = new PersistentLogger();

    public static void log(Object message) {
        if (DebugManager.isDebugMode()) {
            message = getDebugInfo() + message;
        }

        System.out.println(message);

        if (!DebugManager.isDevEnvironment()) {
            persistentLogger.log(message.toString());
        }
    }

    public static void logExtra(Object message) {
        if (DebugManager.isDebugMode()) {
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

    public static void stop() {
        if (!DebugManager.isDevEnvironment()) {
            persistentLogger.stop();
        }
    }
}
