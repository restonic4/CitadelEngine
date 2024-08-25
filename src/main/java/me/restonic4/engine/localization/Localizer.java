package me.restonic4.engine.localization;

import me.restonic4.engine.platform.PlatformManager;

public class Localizer {
    public static String localizeKey(String key, Locale locale) {
        return key + "." + locale;
    }

    public static String localizeKey(String key) {
        return localizeKey(key, getCurrentLocale());
    }

    public static Locale getCurrentLocale() {
        // TODO: Config system to read from
        return PlatformManager.getInstance().getOperatingSystem().get().getSystemLocale();
    }
}
