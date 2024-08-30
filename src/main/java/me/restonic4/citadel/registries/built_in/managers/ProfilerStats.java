package me.restonic4.citadel.registries.built_in.managers;

import me.restonic4.citadel.registries.AbstractRegistryInitializer;
import me.restonic4.citadel.registries.AssetLocation;
import me.restonic4.citadel.registries.Registries;
import me.restonic4.citadel.registries.Registry;
import me.restonic4.citadel.registries.built_in.types.ProfilerStat;
import me.restonic4.citadel.util.CitadelConstants;
import me.restonic4.citadel.util.debug.diagnosis.Logger;
import me.restonic4.game.util.Constants;

public class ProfilerStats extends AbstractRegistryInitializer {
    public static ProfilerStat DELTA_TIME;
    public static ProfilerStat FPS;

    @Override
    public void register() {
        Logger.log("Registering built-in profiler stats");

        DELTA_TIME = Registry.register(Registries.PROFILER_STAT, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "delta_time"), new ProfilerStat());
        FPS = Registry.register(Registries.PROFILER_STAT, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "fps"), new ProfilerStat());
    }
}
