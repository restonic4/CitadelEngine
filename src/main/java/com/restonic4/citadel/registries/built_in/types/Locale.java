package com.restonic4.citadel.registries.built_in.types;

import com.restonic4.citadel.files.FileManager;
import com.restonic4.citadel.registries.RegistryObject;
import com.restonic4.citadel.util.debug.diagnosis.Logger;
import org.json.JSONObject;

public class Locale extends RegistryObject {
    private JSONObject data;

    public void setData(JSONObject data) {
        this.data = data;
    }

    public JSONObject getData() {
        return this.data;
    }

    @Override
    public void onPopulate() {
        super.onPopulate();

        Logger.logExtra("Preloading the asset: " + getAssetLocation());

        String path = FileManager.toResources("data/language/" + this.getAssetLocation().getPath() + ".json");
        String data = FileManager.readFile(path);

        this.setData(new JSONObject(data));
    }
}
