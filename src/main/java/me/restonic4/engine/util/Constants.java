package me.restonic4.engine.util;

public class Constants {
    public static final String WINDOW_TITLE = "EpicAmazingCoolGameTestIGues";
    public static final String APP_NAME = "YourGame";

    public static final int SHADOW_MAP_WIDTH = 4096;
    public static final int SHADOW_MAP_HEIGHT = SHADOW_MAP_WIDTH;

    public static final String VANILLA_NAMESPACE = "game";
    public static final String ASSETS_PATH = "resources";

    public static final String VALID_NAMESPACE_CHARS = "_-abcdefghijklmnopqrstuvwxyz0123456789.";
    public static final String VALID_PATH_CHARS = VALID_NAMESPACE_CHARS + "/";

    public static final String DEFAULT_TEXTURE = "models/default/default_texture.png";

    public static final long LOG_INTERVAL_MS = 60000;
    public static final String LOG_DIRECTORY = "logs";
    public static final String LOG_FILE = "CurrentLog.txt";
    public static final String LOG_DATE_FORMAT = "yyyy-MM-dd_HH-mm-ss";

    public static final String[] CRASH_MESSAGES = {
            "The game crashed.",
            "RIP.",
            "This game is so broken...",
            "Crashed... cool i guess?",
            "Pls, don't kill the devs...",
            "This crash message is random.",
            "This crash message is random. And you probably do not care at all."
    };
}
