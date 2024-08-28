package me.restonic4.citadel.localization;

import me.restonic4.citadel.files.FileManager;
import me.restonic4.citadel.util.debug.diagnosis.Logger;
import org.json.JSONObject;

import java.util.Locale;

public enum Locales {
    EN_US,
    ES_ES;

    private JSONObject data;

    public void setData(JSONObject data) {
        this.data = data;
    }

    public JSONObject getData() {
        if (this.data == null) {
            this.populate();
        }

        return this.data;
    }

    public void populate() {
        String path = FileManager.toResources("data/language/" + this + ".json");
        String data = FileManager.readFile(path);

        this.setData(new JSONObject(data));

    }

    @Override
    public String toString() {
        return name().toLowerCase();
    }

    public static Locales fromJavaLocale(Locale locale) {
        String langCountry = (locale.getLanguage() + "_" + locale.getCountry()).toUpperCase();

        for (Locales local : values()) {
            if (local.name().equalsIgnoreCase(langCountry)) {
                return local;
            }
        }

        Logger.log("Locale not registered on the engine: " + locale);

        return Locales.EN_US;
    }
}
