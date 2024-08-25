package me.restonic4.engine.platform.operating_systems;

import me.restonic4.engine.exceptions.UnknownPlatformException;
import me.restonic4.engine.localization.Locale;

public class OperatingSystem {
    public Locale getSystemLocale() {
        java.util.Locale locale = java.util.Locale.getDefault();
        return Locale.fromJavaLocale(locale);
    }
}
