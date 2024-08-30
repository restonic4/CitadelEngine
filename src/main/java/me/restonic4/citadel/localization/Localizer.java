package me.restonic4.citadel.localization;

import me.restonic4.citadel.platform.PlatformManager;
import me.restonic4.citadel.registries.AssetLocation;
import me.restonic4.citadel.registries.Registries;
import me.restonic4.citadel.registries.Registry;
import me.restonic4.citadel.registries.built_in.managers.Locales;
import me.restonic4.citadel.registries.built_in.types.Locale;
import me.restonic4.citadel.util.debug.diagnosis.Logger;

import java.util.Map;
import java.util.Objects;

public class Localizer {
    private static Locale currentLocale;

    public static String localizeKey(String key, Locale locale) {
        if (!locale.getData().has(key)) {
            Logger.logExtra("Could not find the translation key for " + key);

            return key;
        }

        return locale.getData().getString(key);
    }

    public static String localizeKey(String key) {
        return localizeKey(key, getCurrentLocale());
    }

    public static Locale getCurrentLocale() {
        if (currentLocale == null) {
            currentLocale = fromJavaLocale(PlatformManager.getOperatingSystem().get().getSystemLocale());
        }

        return currentLocale;
    }

    public static Locale fromJavaLocale(java.util.Locale locale) {
        String langCountry = (locale.getLanguage() + "_" + locale.getCountry()).toLowerCase();

        Map<AssetLocation, Locale> locales = Registry.getRegistry(Registries.LOCALE);

        return locales.values().stream()
                .filter(localeFound -> localeFound.getAssetLocation().getPath().equals(langCountry))
                .findFirst()
                .orElse(Locales.EN_US);
    }
}
