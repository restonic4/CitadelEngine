package me.restonic4.citadel.registries;

import me.restonic4.citadel.events.EventResult;
import me.restonic4.citadel.events.types.RegistriesEvents;
import me.restonic4.citadel.util.debug.diagnosis.Logger;
import me.restonic4.game.core.world.sounds.Sounds;

import java.util.ArrayList;
import java.util.List;

public class RegistryManager {
    private static List<AbstractRegistryInitializer> builtInRegistries = new ArrayList<>();
    private static List<AbstractRegistryInitializer> customRegistries = new ArrayList<>();

    public static void registerBuiltIn() {
        Logger.log("Starting all the built-in registries");

        for (AbstractRegistryInitializer abstractRegistryInitializer : builtInRegistries) {
            abstractRegistryInitializer.register();
        }
    }

    public static void registerCustom() {
        Logger.log("Starting all the custom registries");

        for (AbstractRegistryInitializer abstractRegistryInitializer : customRegistries) {
            abstractRegistryInitializer.register();
        }
    }

    public static void registerBuiltInRegistrySet(AbstractRegistryInitializer abstractRegistryInitializer) {
        EventResult eventResult = RegistriesEvents.REGISTERING_SET.invoker().onRegisteringSet(abstractRegistryInitializer, true);
        if (eventResult == EventResult.CANCELED) {
            return;
        }

        builtInRegistries.add(abstractRegistryInitializer);

        RegistriesEvents.SET_REGISTERED.invoker().onSetRegistered(abstractRegistryInitializer, true);
    }

    public static void registerRegistrySet(AbstractRegistryInitializer abstractRegistryInitializer) {
        EventResult eventResult = RegistriesEvents.REGISTERING_SET.invoker().onRegisteringSet(abstractRegistryInitializer, false);
        if (eventResult == EventResult.CANCELED) {
            return;
        }

        customRegistries.add(abstractRegistryInitializer);

        RegistriesEvents.SET_REGISTERED.invoker().onSetRegistered(abstractRegistryInitializer, false);
    }
}
