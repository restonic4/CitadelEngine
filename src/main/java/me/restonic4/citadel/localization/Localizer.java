package me.restonic4.citadel.localization;

import me.restonic4.citadel.platform.PlatformManager;
import me.restonic4.citadel.util.debug.diagnosis.Logger;

public class Localizer {
    private static Locales currentLocal;

    public static String localizeKey(String key, Locales locale) {
        if (!locale.getData().has(key)) {
            Logger.logExtra("Could not find the translation key for " + key);

            return key;
        }

        return locale.getData().getString(key);
    }

    public static String localizeKey(String key) {
        return localizeKey(key, getCurrentLocale());
    }

    public static Locales getCurrentLocale() {
        if (currentLocal == null) {
            currentLocal = PlatformManager.getOperatingSystem().get().getSystemLocale();
            currentLocal.populate();
        }

        return currentLocal;
    }
}
