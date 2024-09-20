package com.restonic4.citadel.util.debug.diagnosis;

import com.restonic4.citadel.files.FileManager;
import com.restonic4.citadel.registries.built_in.types.ProfilerStat;
import com.restonic4.citadel.util.CitadelConstants;
import com.restonic4.citadel.util.Time;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProfilerManager {
    private static boolean isEnabled;
    private static float timeThreshold = 0;

    private static List<ProfilerStat> profilerStatsRegistered = new ArrayList<>();

    public static void setEnabled(boolean value) {
        isEnabled = value;
    }

    public static boolean isEnabled() {
        return isEnabled;
    }

    public static void registerStat(ProfilerStat profilerStat, float value) {
        if (!isEnabled) {
            return;
        }

        profilerStat.register(value);

        for (ProfilerStat profilerStatFound : profilerStatsRegistered) {
            if (profilerStatFound.getAssetLocation() == profilerStat.getAssetLocation()) {
                return;
            }
        }

        profilerStatsRegistered.add(profilerStat);
    }

    public static void update() {
        if (!isEnabled) {
            return;
        }

        timeThreshold = (float) (Time.getRunningTime() - CitadelConstants.PROFILER_TIME_CAPACITY);

        for (ProfilerStat profilerStat : profilerStatsRegistered) {
            profilerStat.clearBefore(timeThreshold);
        }
    }

    public static void export() {
        if (!isEnabled) {
            return;
        }

        for (ProfilerStat profilerStat : profilerStatsRegistered) {
            JSONObject jsonObject = profilerStat.toJSON();

            String fileName = (profilerStat.getAssetLocation().toString() + "_" + Time.getRunningTime()).replace(":", "_") + ".json";

            Logger.log(fileName);

            FileManager.exportFile(fileName, jsonObject.toString());
        }
    }
}
