package me.restonic4.citadel.util;

public class CitadelConstants {
    public static final String REGISTRY_NAMESPACE = "citadel";
    public static final String DEFAULT_WINDOW_TITLE = "Citadel engine";

    public static final String REPOSITORY_URL = "https://github.com/restonic4/CitadelEngine";

    public static final String VALID_NAMESPACE_CHARS = "_-abcdefghijklmnopqrstuvwxyz0123456789.";
    public static final String VALID_PATH_CHARS = VALID_NAMESPACE_CHARS + "/";

    public static final long LOG_INTERVAL_MS = 60000;
    public static final String LOG_DIRECTORY = "logs";
    public static final String LOG_FILE = "CurrentLog.txt";
    public static final String LOG_DATE_FORMAT = "yyyy-MM-dd_HH-mm-ss";

    public static final int MAX_STATIC_BATCH_VERTEX_SIZE = 2048;
    public static final int MAX_DYNAMIC_BATCH_VERTEX_SIZE = 1024;

    public static final double NANOSECOND_CONVERSION_FACTOR = 1E-9;

    public static final int MATRIX4F_CAPACITY = 16;
    public static final int MATRIX3F_CAPACITY = 9;

    public static final float PROFILER_TIME_CAPACITY = 60;

    // Can be modified

    public static int CRASH_MESSAGES_AMOUNT = 7;

    public static boolean VSYNC = true;
    public static int FPS_CAP = 60;
}
