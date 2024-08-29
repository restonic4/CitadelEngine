package me.restonic4.citadel.registries.types;

import me.restonic4.citadel.registries.RegistryObject;
import me.restonic4.citadel.util.Time;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProfilerStat extends RegistryObject {
    //          Time , Stat
    private Map<Float, Float> buffer;

    public ProfilerStat() {
        this.buffer = new HashMap<>();
    }

    public void register(float value) {
        buffer.put((float) Time.getRunningTime(), value);
    }

    public void clearBefore(float threshold) {
        buffer.entrySet().removeIf(entry -> entry.getKey() < threshold);
    }

    public Map<Float, Float> getBuffer() {
        return this.buffer;
    }

    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();

        for (Map.Entry<Float, Float> entry : buffer.entrySet()) {
            String key = entry.getKey().toString();
            Float value = entry.getValue();

            jsonObject.put(key, value);
        }

        return jsonObject;
    }
}
