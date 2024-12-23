package com.restonic4.citadel.util;

import com.restonic4.citadel.registries.built_in.managers.Locales;
import com.restonic4.citadel.registries.built_in.types.Locale;

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

    public static final String NODEX_DIRECTORY = "nodex";

    public static final int MAX_STATIC_BATCH_VERTEX_SIZE = 2048; // 2048
    public static final int MAX_DYNAMIC_BATCH_VERTEX_SIZE = 1024; // 1024

    public static final int MATRIX4F_CAPACITY = 16;
    public static final int MATRIX3F_CAPACITY = 9;

    public static final float PROFILER_TIME_CAPACITY = 60;

    public static final float FRUSTUM_BOUNDING_SPHERE_RADIUS = 2;

    public static final int IM_GUI_INDENT = 20;
    public static final float IM_GUI_INFO_COLUMN_WIDTH = 75;
    public static final float IM_GUI_INFO_COLUMN_PADDING = 5;

    public static final int HEX_COLOR_BLUE = 0x7290fc;
    public static final int HEX_COLOR_GREEN = 0x91e562;
    public static final int HEX_COLOR_RED = 0xff64ad;

    public static final int MAX_LIGHTS = 4;
    public static final int SHADOW_RESOLUTION = 4096;

    public static final String MOD_PROPERTIES_FILE_NAME = "mod.properties.json";
    public static final String MOD_PROPERTIES_MAIN_CLASS = "main_class";

    public static final String CLIENT_SIDE_THREAD_NAME = "Networking client side";
    public static final String CLIENT_SIDE_RENDER_THREAD_NAME = "Render client side";
    public static final String SERVER_SIDE_THREAD_NAME = "Networking server side";
    public static final String DISCORD_THREAD_NAME = "Discord Rich Presence";

    // Can be modified

    public static float CAMERA_NEAR_PLANE = 0.1f;
    public static float CAMERA_FAR_PLANE = 1000f;
    public static float CAMERA_FOV = (float) Math.toRadians(60);

    public static int CRASH_MESSAGES_AMOUNT = 7;

    public static boolean VSYNC = true;
    public static int FPS_CAP = 60;

    public static int WINDOW_TITLE_CHANGE_FREQUENCY = 3;

    public static Locale DEFAULT_LOCALE = Locales.EN_US;

    public static String MODS_DIRECTORY = "mods";

    public static long DISCORD_APP_ID = 1287694211906539520L;
}
