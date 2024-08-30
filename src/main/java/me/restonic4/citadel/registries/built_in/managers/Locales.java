package me.restonic4.citadel.registries.built_in.managers;

import me.restonic4.citadel.registries.AbstractRegistryInitializer;
import me.restonic4.citadel.registries.AssetLocation;
import me.restonic4.citadel.registries.Registries;
import me.restonic4.citadel.registries.Registry;
import me.restonic4.citadel.registries.built_in.types.Locale;
import me.restonic4.citadel.util.CitadelConstants;

public class Locales extends AbstractRegistryInitializer {
    public static Locale EN_US;
    public static Locale ES_ES;

    @Override
    public void register() {
        EN_US = Registry.register(Registries.LOCALE, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "en_us"), new Locale());
        ES_ES = Registry.register(Registries.LOCALE, new AssetLocation(CitadelConstants.REGISTRY_NAMESPACE, "es_es"), new Locale());
    }
}
