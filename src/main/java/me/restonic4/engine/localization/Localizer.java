package me.restonic4.engine.localization;

public class Localizer {
    // TODO: A way of getting your pc language

    public static String localizeKey(String key, Local local) {
        return key + "." + local;
    }

    public static String localizeKey(String key) {
        // TODO: Read the files on the data folder, the current language you use
        return localizeKey(key, Local.EN_US);
    }

    public static enum Local {
        EN_US,
        ES_ES;

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }
}
