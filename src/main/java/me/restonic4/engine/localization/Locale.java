package me.restonic4.engine.localization;

import me.restonic4.engine.util.debug.Logger;

public enum Locale {
    EN_US,
    ES_ES;

    @Override
    public String toString() {
        return name().toLowerCase();
    }

    public static Locale fromJavaLocale(java.util.Locale locale) {
        String langCountry = (locale.getLanguage() + "_" + locale.getCountry()).toUpperCase();

        for (Locale local : values()) {
            if (local.name().equalsIgnoreCase(langCountry)) {
                return local;
            }
        }

        Logger.log("Locale not registered on the engine: " + locale);

        return Locale.EN_US;
    }
}
