package me.restonic4.citadel.platform.operating_systems;

import me.restonic4.citadel.localization.Locales;

import java.util.Locale;

public class OperatingSystem {
    public Locales getSystemLocale() {
        Locale locale = Locale.getDefault();
        return Locales.fromJavaLocale(locale);
    }
}
